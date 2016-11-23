package kekcraft.api.ui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class MachineContainer extends Container {
	private MachineTileEntity tileEntity;
	private InventoryPlayer inventory;
	private int normalizer;

	public MachineContainer(InventoryPlayer inventory, Machine block, MachineTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		this.inventory = inventory;

		block.drawTiles(this);
	}

	public Slot createSlot(final int index, int x, int y) {
		return new Slot(tileEntity, index, x, normalize(y)) {
			@Override
			public boolean isItemValid(ItemStack item) {
				return tileEntity.canInsertItem(index, item, -1);
			}
		};
	}

	public int normalize(int x) {
		return x - normalizer;
	}

	public Slot createOutputSlot(int index, int x, int y) {
		return new SlotFurnace(inventory.player, tileEntity, index, x, normalize(y));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileEntity.isUseableByPlayer(player);
	}

	public MachineTileEntity getTileEntity() {
		return tileEntity;
	}

	public void setTileEntity(MachineTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public Slot addSlotToContainer(Slot slot) {
		return super.addSlotToContainer(slot);
	}

	public void drawMinecraftInventory(int x, int rawY) {
		int y = normalize(rawY);
		int tileWidth = 18;
		int tileHeight = 18;
		int perRow = 9;
		int mainHeight = 3;

		for (int i = 0; i < mainHeight; i++) {
			for (int j = 0; j < perRow; j++) {
				this.addSlotToContainer(
						new Slot(inventory, j + (i * perRow) + perRow, x + (j * tileWidth), y + (i * tileHeight)));
			}
		}
		for (int i = 0; i < perRow; i++) {
			this.addSlotToContainer(
					new Slot(inventory, i, x + (i * tileWidth), y + (tileHeight * mainHeight) + (mainHeight + 1)));
		}
	}

	private void cleanup(Slot slot, ItemStack slotStack, EntityPlayer player) {
		if (slotStack.stackSize == 0) {
			slot.putStack((ItemStack) null);
		} else {
			slot.onSlotChanged();
		}
		slot.onPickupFromSlot(player, slotStack);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		Slot slot = (Slot) this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack original = slot.getStack().copy();
			ItemStack slotStack = slot.getStack();

			if (index <= 35) {
				for (IElectricMachineRecipe recipe : tileEntity.getValidRecipes()) {
					for (int inUI = 0; inUI < tileEntity.getSizeInventory(); inUI++) {
						if (recipe.isValidElementOfRecipe(slotStack, inUI)) {
							if (mergeItemStack(slotStack, 36 + inUI, 37 + inUI, false)) {
								cleanup(slot, slotStack, player);
								return original;
							}
						}
					}
				}
			} else {
				if (mergeItemStack(slotStack, 0, 36, true)) {
					cleanup(slot, slotStack, player);
					return original;
				}
			}
		}
		return null;
	}

	public int getNormalizer() {
		return normalizer;
	}

	public void setNormalizer(int normalizer) {
		this.normalizer = normalizer;
	}
}