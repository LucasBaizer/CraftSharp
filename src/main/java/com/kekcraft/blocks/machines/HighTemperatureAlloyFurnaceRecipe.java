package com.kekcraft.blocks.machines;

import java.util.Arrays;
import java.util.List;

import com.kekcraft.KekCraft;
import com.kekcraft.api.ui.IMachineRecipe;
import com.kekcraft.api.ui.MachineTileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HighTemperatureAlloyFurnaceRecipe implements IMachineRecipe {
	private static final long serialVersionUID = -8206894167646765940L;

	private transient ItemStack a;
	private transient ItemStack b;
	private transient ItemStack out;
	private int cost = -1;

	public HighTemperatureAlloyFurnaceRecipe(ItemStack a, ItemStack b, ItemStack out) {
		this.a = a;
		this.b = b;
		this.out = out;
	}

	@Override
	public boolean satifies(MachineTileEntity entity, ItemStack[] slots) {
		return satifies0(a, slots[0], b, slots[1]) || satifies0(a, slots[1], b, slots[0]);
	}

	private boolean satifies0(ItemStack a, ItemStack b, ItemStack a1, ItemStack b1) {
		return b != null && b1 != null && a.getItem() == b.getItem() && b.stackSize >= a.stackSize
				&& a1.getItem() == b1.getItem() && b1.stackSize >= a1.stackSize;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return index == 2 ? element.getItem() == KekCraft.factory.getItem("DustThermite")
				: element.getItem() == a.getItem() || element.getItem() == b.getItem();
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public ItemStack getInput(int slot) {
		return slot == 0 ? a : b;
	}

	@Override
	public ItemStack getOutput() {
		return out;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(0, 1);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		write(nbt, a, "InputA");
		write(nbt, b, "InputB");
		write(nbt, out, "Output");
	}

	private void write(NBTTagCompound nbt, ItemStack stack, String name) {
		NBTTagCompound inTag = new NBTTagCompound();
		stack.writeToNBT(inTag);
		nbt.setTag(name, inTag);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		a = read(nbt, "InputA");
		b = read(nbt, "InputB");
		out = read(nbt, "Output");
	}

	private ItemStack read(NBTTagCompound nbt, String name) {
		return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(name));
	}

	@Override
	public int getCookTime() {
		return 200;
	}

	@Override
	public int getFuelCost() {
		return cost;
	}
}
