package com.kekcraft.api.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.SerializationUtils;

import com.kekcraft.ModPacket;
import com.kekcraft.SerializableEntity;

import cofh.api.transport.IItemDuct;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class MachineTileEntity extends TileEntity implements ISidedInventory, SerializableEntity, IItemDuct {
	private boolean enableAutomaticUpdates = true;
	private boolean changeMeta;
	protected ItemStack[] slots;
	protected int[] itemSlots;
	protected int[] outputSlots;
	protected int cookTime;
	protected int currentCookTime;
	protected int tickUpdateRate;
	protected String inventoryName;
	protected long cookTicks = 0;
	protected ArrayList<IMachineRecipe> validRecipes = new ArrayList<IMachineRecipe>();
	protected HashMap<ForgeDirection, FaceType> faces = new HashMap<ForgeDirection, FaceType>();
	protected HashMap<MachineUpgrade, Integer> upgrades = new HashMap<MachineUpgrade, Integer>();
	protected MachineUI ui;

	public MachineTileEntity(int slots, int tickUpdateRate) {
		this.slots = new ItemStack[slots];
		this.tickUpdateRate = tickUpdateRate;

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			faces.put(dir, FaceType.NONE);
		}
		for (MachineUpgrade upgrade : MachineUpgrade.values()) {
			upgrades.put(upgrade, 0);
		}
	}

	public void setChangeMeta(boolean meta) {
		this.changeMeta = meta;
	}

	protected void onSmeltingStopped() {
	}

	protected void onSmeltingFinished() {
		if (changeMeta) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
					worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7, 2);
		}
	}

	protected void onItemConsumeStart() {
		if (changeMeta) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
					worldObj.getBlockMetadata(xCoord, yCoord, zCoord) | 8, 2);
		}
	}

	protected void onItemSmelted(IMachineRecipe item) {
	}

	protected void reset() {
		currentCookTime = 0;
		cookTime = 0;
		targetSlot = -1;
		currentRecipe = null;
		cookTicks = 0;
	}

	protected void smeltItemWhenDone() {
		smeltNextItem();
		onItemSmelted(currentRecipe);
		onSmeltingStopped();
		reset();
		if (enablesAutomaticUpdates()) {
			ModPacket.sendTileEntityUpdate(this);
		}
	}

	public Runnable onUISet;

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

	public IMachineRecipe[] getValidRecipes() {
		return (IMachineRecipe[]) validRecipes.toArray(new IMachineRecipe[validRecipes.size()]);
	}

	public MachineTileEntity addRecipe(IMachineRecipe recipe) {
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

	protected IMachineRecipe getNextRecipe() {
		for (IMachineRecipe recipe : validRecipes) {
			if (recipe.satifies(slots)) {
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

	protected IMachineRecipe currentRecipe;
	protected int targetSlot;

	public IMachineRecipe getCurrentRecipe() {
		return currentRecipe;
	}

	public boolean beginSmeltNextItem() {
		IMachineRecipe recipe = getNextRecipe();

		int slot = getAvailableOutputSlot(recipe.getOutput());
		if (slot != -1) {
			currentRecipe = recipe;
			cookTime = recipe.getCookTime();
			currentCookTime = recipe.getCookTime();
			targetSlot = slot;
		}
		return slot != -1;

	}

	public void onInputSlotExhausted(int slot) {
	}

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
			System.out.println("NullPointerException thrown when finishing smelt!");
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
			for (IMachineRecipe recipe : validRecipes) {
				if (recipe.isValidElementOfRecipe(stack, slot)) {
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

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tabCompound1 = tagList.getCompoundTagAt(i);
			byte byte0 = tabCompound1.getByte("Slot");

			if (byte0 >= 0 && byte0 < slots.length) {
				slots[byte0] = ItemStack.loadItemStackFromNBT(tabCompound1);
			}
		}

		NBTTagList faces = tagCompound.getTagList("Faces", 10);
		for (int i = 0; i < faces.tagCount(); i++) {
			NBTTagCompound tag = faces.getCompoundTagAt(i);
			this.faces.put(ForgeDirection.valueOf(tag.getString("Direction")), FaceType.valueOf(tag.getString("Face")));
		}

		NBTTagList upgrades = tagCompound.getTagList("Upgrades", 10);
		for (int i = 0; i < upgrades.tagCount(); i++) {
			NBTTagCompound tag = upgrades.getCompoundTagAt(i);
			this.upgrades.put(MachineUpgrade.valueOf(tag.getString("Upgrade")), tag.getInteger("Amount"));
		}

		cookTime = tagCompound.getInteger("CookTime");
		currentCookTime = tagCompound.getInteger("CurrentCookTime");

		if (!tagCompound.getBoolean("IsCooking")) {
			currentCookTime = 0;
		} else {
			try {
				ByteArrayInputStream bin = new ByteArrayInputStream(tagCompound.getByteArray("CurrentRecipe"));
				ObjectInputStream oin = new ObjectInputStream(bin);
				currentRecipe = (IMachineRecipe) oin.readObject();
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

		NBTTagList faceList = new NBTTagList();
		for (Entry<ForgeDirection, FaceType> entry : faces.entrySet()) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Direction", entry.getKey().toString());
			tag.setString("Face", entry.getValue().toString());
			faceList.appendTag(tag);
		}
		tagCompound.setTag("Faces", faceList);

		NBTTagList upgradeList = new NBTTagList();
		for (Entry<MachineUpgrade, Integer> entry : upgrades.entrySet()) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Upgrade", entry.getKey().toString());
			tag.setInteger("Amount", entry.getValue());
			upgradeList.appendTag(tag);
		}
		tagCompound.setTag("Upgrades", upgradeList);

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

	@Override
	public ItemStack insertItem(ForgeDirection from, ItemStack item) {
		ItemStack copy = item.copy();
		if (faces.get(from) == FaceType.ITEM) {
			for (int slot : itemSlots) {
				if (isItemValidForSlot(slot, copy)) {
					int stackSize = slots[slot].stackSize;
					slots[slot].stackSize = Math.min(64, stackSize + copy.stackSize);
					copy.stackSize = Math.max(0, stackSize + copy.stackSize - 64);

					if (copy.stackSize == 0) {
						return null;
					}
				}
			}
		}
		return copy;
	}

	@Override
	public void read(ByteBufInputStream in) throws IOException {
		currentCookTime = in.readInt();
		cookTime = in.readInt();

		for (int i = 0; i < faces.size(); i++) {
			faces.put(ForgeDirection.values()[in.readInt()], FaceType.values()[in.readInt()]);
		}
		for (int i = 0; i < upgrades.size(); i++) {
			upgrades.put(MachineUpgrade.values()[in.readInt()], in.readInt());
		}
	}

	@Override
	public void write(ByteBufOutputStream out) throws IOException {
		out.writeInt(Minecraft.getMinecraft().theWorld.provider.dimensionId);
		out.writeInt(xCoord);
		out.writeInt(yCoord);
		out.writeInt(zCoord);

		out.writeInt(getCurrentCookTime());
		out.writeInt(getCookTime());

		for (Entry<ForgeDirection, FaceType> entry : faces.entrySet()) {
			out.writeInt(entry.getKey().ordinal());
			out.writeInt(entry.getValue().ordinal());
		}
		for (Entry<MachineUpgrade, Integer> entry : upgrades.entrySet()) {
			out.writeInt(entry.getKey().ordinal());
			out.writeInt(entry.getValue());
		}
	}
}