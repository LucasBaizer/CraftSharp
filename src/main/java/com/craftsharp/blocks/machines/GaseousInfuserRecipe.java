package com.craftsharp.blocks.machines;

import java.util.Arrays;
import java.util.List;

import com.craftsharp.api.ui.IMachineRecipe;
import com.craftsharp.api.ui.MachineTileEntity;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GaseousInfuserRecipe implements IMachineRecipe {
	private static final long serialVersionUID = 2285493231630666638L;

	@Override
	public boolean satifies(MachineTileEntity e, ItemStack[] slots) {
		return slots[0] != null && slots[0].getItem() == Items.diamond;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return index == 0 ? element.getItem() == Items.diamond : false;
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(0);
	}

	@Override
	public ItemStack getInput(int slot) {
		return slot == 0 ? new ItemStack(Items.diamond) : null;
	}

	@Override
	public int getCookTime() {
		return 1000;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	}

	public int getFuelCost() {
		return 16000;
	}
}
