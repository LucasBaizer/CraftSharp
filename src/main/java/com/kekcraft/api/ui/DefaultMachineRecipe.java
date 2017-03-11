package com.kekcraft.api.ui;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DefaultMachineRecipe implements IMachineRecipe {
	private static final long serialVersionUID = -9178875502583203568L;

	private transient ItemStack in;
	private transient ItemStack out;
	private int cookTime;
	private int fuelCost;
	private int inputSlot;

	public DefaultMachineRecipe(ItemStack in, ItemStack out, int cookTime, int fuelCost, int inputSlot) {
		this.in = in;
		this.out = out;
		this.cookTime = cookTime;
		this.fuelCost = fuelCost;
		this.inputSlot = inputSlot;
	}

	@Override
	public int getFuelCost() {
		return fuelCost;
	}

	@Override
	public boolean satifies(ItemStack[] slots) {
		return slots[inputSlot] != null && slots[inputSlot].getItem() == in.getItem()
				&& slots[inputSlot].stackSize >= in.stackSize;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return index == inputSlot && element.getItem() == in.getItem();
	}

	@Override
	public ItemStack getInput(int slot) {
		return in;
	}

	@Override
	public ItemStack getOutput() {
		return out;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(inputSlot);
	}

	@Override
	public int getCookTime() {
		return cookTime;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound inTag = new NBTTagCompound();
		in.writeToNBT(inTag);
		nbt.setTag("Input", inTag);

		NBTTagCompound outTag = new NBTTagCompound();
		out.writeToNBT(outTag);
		nbt.setTag("Output", outTag);

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		in = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Input"));
		out = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Output"));
	}

	public boolean isInstant() {
		return cookTime == -1;
	}
}
