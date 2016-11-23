package kekcraft.api.ui;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class MachineTileEntity extends TileEntity implements ISidedInventory {
	private boolean enableAutomaticUpdates = true;
	protected ItemStack[] slots;
	protected int[] itemSlots;
	protected int[] outputSlots;
	protected int cookTime;
	protected int currentCookTime;
	protected int tickUpdateRate;
	protected String inventoryName;
	protected List<IElectricMachineRecipe> validRecipes = new ArrayList<IElectricMachineRecipe>();

	public MachineTileEntity(int slots, int tickUpdateRate) {
		this.slots = new ItemStack[slots];
		this.tickUpdateRate = tickUpdateRate;
	}

	public int[] getOutputSlots() {
		return outputSlots;
	}

	public MachineTileEntity setOutputSlots(int[] outputSlots) {
		this.outputSlots = outputSlots;
		return this;
	}

	public int[] getItemSlots() {
		return itemSlots;
	}

	public MachineTileEntity setItemSlots(int[] itemSlots) {
		this.itemSlots = itemSlots;
		return this;
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}
	
	public ItemStack[] getSlots() {
		return slots;
	}

	@Override
	public ItemStack decrStackSize(int slot, int par2) {
		if (this.slots[slot] != null) {
			ItemStack item;
			if (this.slots[slot].stackSize <= par2) {
				item = this.slots[slot];
				this.slots[slot] = null;
			} else {
				item = this.slots[slot].splitStack(par2);

				if (this.slots[slot].stackSize == 0) {
					this.slots[slot] = null;
				}
			}
			return item;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.slots[slot] != null) {
			ItemStack stack = this.slots[slot];
			this.slots[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.slots[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	public MachineTileEntity setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
		return this;
	}

	public IElectricMachineRecipe[] getValidRecipes() {
		return (IElectricMachineRecipe[]) validRecipes.toArray(new IElectricMachineRecipe[validRecipes.size()]);
	}

	public MachineTileEntity addRecipe(IElectricMachineRecipe recipe) {
		validRecipes.add(recipe);
		return this;
	}

	public int getCookTime() {
		return cookTime;
	}

	public int getCurrentCookTime() {
		return currentCookTime;
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? inventoryName : "";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return inventoryName != null && !inventoryName.isEmpty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
				: player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
						(double) this.zCoord + 0.5D) <= 64.0D;
	}

	protected IElectricMachineRecipe getNextRecipe() {
		for (IElectricMachineRecipe recipe : validRecipes) {
			if (recipe.satifies(slots, itemSlots)) {
				return recipe;
			}
		}
		return null;
	}

	public boolean isBurningRecipe() {
		return currentCookTime > 0;
	}

	protected boolean isEmpty(int[] slotRegion) {
		for (int slot : slotRegion) {
			if (slots[slot] != null) {
				return false;
			}
		}
		return true;
	}

	private int getAvailableOutputSlot(ItemStack stack) {
		for (int slot : outputSlots) {
			if (slots[slot] == null || (slots[slot].getItem().equals(stack.getItem())
					&& (slots[slot].stackSize + stack.stackSize <= getInventoryStackLimit()))) {
				return slot;
			}
		}
		return -1;
	}

	protected boolean canSmelt() {
		return !isEmpty(itemSlots) && currentCookTime <= 0;
	}

	protected IElectricMachineRecipe currentRecipe;
	protected int targetSlot;

	public IElectricMachineRecipe getCurrentRecipe() {
		return currentRecipe;
	}

	public boolean beginSmeltNextItem() {
		IElectricMachineRecipe recipe = getNextRecipe();

		int slot = getAvailableOutputSlot(recipe.getOutput());
		if (slot != -1) {
			currentRecipe = recipe;
			cookTime = recipe.getCookTime();
			currentCookTime = recipe.getCookTime();
			targetSlot = slot;
		}
		return slot != -1;

	}
	
	public void onInputSlotExhausted(int slot) {}

	public void smeltNextItem() {
		try {
			if (this.slots[targetSlot] == null) {
				this.slots[targetSlot] = currentRecipe.getOutput().copy();
			} else {
				this.slots[targetSlot].stackSize += currentRecipe.getOutput().stackSize;
			}

			for (int slot : currentRecipe.getRecipeSlots()) {
				slots[slot].stackSize -= currentRecipe.getInput(slot).stackSize;
				if (slots[slot].stackSize <= 0) {
					slots[slot] = null;
					onInputSlotExhausted(slot);
				}
			}
		} catch (NullPointerException e) {
			return;
		}
	}

	@Override
	public void openInventory() {
		// empty
	}

	@Override
	public void closeInventory() {
		// empty
	}

	private boolean contains(int[] slotReigon, int slot) {
		for (int i : slotReigon) {
			if (i == slot) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (contains(itemSlots, slot)) {
			for (IElectricMachineRecipe recipe : validRecipes) {
				if (recipe.getInput(slot).getItem() == stack.getItem()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		// TODO
		return null;
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack, int par3) {
		return isItemValidForSlot(par1, stack);
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack stack, int par3) {
		// return canInsertItem(par1, stack, par3);
		return false;
	}

	@Override
	public abstract void updateEntity();

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound tabCompound1 = tagList.getCompoundTagAt(i);
			byte byte0 = tabCompound1.getByte("Slot");

			if (byte0 >= 0 && byte0 < slots.length) {
				slots[byte0] = ItemStack.loadItemStackFromNBT(tabCompound1);
			}
		}

		cookTime = tagCompound.getInteger("CookTime");
		currentCookTime = tagCompound.getInteger("CurrentCookTime");

		if (!tagCompound.getBoolean("IsCooking")) {
			currentCookTime = 0;
		} else {
			try {
				ByteArrayInputStream bin = new ByteArrayInputStream(tagCompound.getByteArray("CurrentRecipe"));
				ObjectInputStream oin = new ObjectInputStream(bin);
				currentRecipe = (IElectricMachineRecipe) oin.readObject();
				currentRecipe.readFromNBT(tagCompound.getCompoundTag("CurrentRecipeNBT"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (tagCompound.hasKey("CustomName", 8)) {
			inventoryName = tagCompound.getString("CustomName");
		}
	}

	protected void defaultWriteToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("CookTime", cookTime);
		tagCompound.setInteger("CurrentCookTime", currentCookTime);
		tagCompound.setBoolean("IsCooking", currentRecipe != null);
		if (currentRecipe != null) {
			NBTTagCompound tag = new NBTTagCompound();
			currentRecipe.writeToNBT(tag);
			tagCompound.setByteArray("CurrentRecipe", SerializationUtils.serialize(currentRecipe));
			tagCompound.setTag("CurrentRecipeNBT", tag);
		}

		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < slots.length; ++i) {
			if (slots[i] != null) {
				NBTTagCompound tagCompound1 = new NBTTagCompound();
				tagCompound1.setByte("Slot", (byte) i);
				slots[i].writeToNBT(tagCompound1);
				tagList.appendTag(tagCompound1);
			}
		}

		tagCompound.setTag("Items", tagList);

		if (this.hasCustomInventoryName()) {
			tagCompound.setString("CustomName", getInventoryName());
		}
	}

	@Override
	public abstract void writeToNBT(NBTTagCompound tagCompound);

	public void setCurrentCookTime(int b) {
		this.currentCookTime = b;
	}

	public void setCookTime(int b) {
		this.cookTime = b;
	}

	public boolean enablesAutomaticUpdates() {
		return enableAutomaticUpdates;
	}

	public void setEnableAutomaticUpdates(boolean enableAutomaticUpdates) {
		this.enableAutomaticUpdates = enableAutomaticUpdates;
	}
}