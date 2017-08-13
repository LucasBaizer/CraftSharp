package com.kekcraft.blocks.machines;

import java.util.Arrays;
import java.util.List;

import com.kekcraft.api.ui.IFluidMachineRecipe;
import com.kekcraft.api.ui.MachineTileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class OxidizerRecipe implements IFluidMachineRecipe {
	private static final long serialVersionUID = -9181074539523858357L;

	private transient ItemStack in;
	private transient ItemStack out;

	public OxidizerRecipe(ItemStack input, ItemStack output) {
		this.in = input;
		this.out = output;
	}

	@Override
	public boolean satifies(MachineTileEntity entity, ItemStack[] slots) {
		return slots[0] != null && (slots[0].getItem() == in.getItem()) && slots[0].stackSize >= in.stackSize;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return element.getItem() == in.getItem();
	}

	@Override
	public ItemStack getInput(int slot) {
		return slot == 0 ? in : null;
	}

	@Override
	public ItemStack getOutput() {
		return out;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(0);
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

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public int getCookTime() {
		return 1000;
	}

	@Override
	public int getFuelCost() {
		return 2000;
	}

	@Override
	public int getFluidCost() {
		return 1000;
	}
}
