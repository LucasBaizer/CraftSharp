package com.kekcraft.blocks.machines;

import java.util.Arrays;
import java.util.List;

import com.kekcraft.api.ui.IMachineRecipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class OxidizerRecipe implements IMachineRecipe {
	private static final long serialVersionUID = -9181074539523858357L;

	private transient ItemStack in;
	private transient ItemStack out;

	public OxidizerRecipe(ItemStack input, ItemStack output) {
		this.in = input;
		this.out = output;
	}

	@Override
	public boolean satifies(ItemStack[] slots) {
		return slots[1] != null && slots[1].getItem() == Items.water_bucket && slots[0] != null
				&& slots[0].getItem() == in.getItem() && slots[0].stackSize >= in.stackSize;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return index == 1 ? (element.getItem() == Items.water_bucket) : (element.getItem() == in.getItem());
	}

	@Override
	public ItemStack getInput(int slot) {
		return slot == 0 ? in : new ItemStack(Items.water_bucket);
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
	public int getCookTime() {
		return 800;
	}

	@Override
	public int getFuelCost() {
		return 1600;
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
}
