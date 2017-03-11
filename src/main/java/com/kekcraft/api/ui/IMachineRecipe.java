package com.kekcraft.api.ui;

import java.io.Serializable;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IMachineRecipe extends Serializable {
	public boolean satifies(ItemStack[] slots);

	public boolean isValidElementOfRecipe(ItemStack element, int index);
	
	public boolean isInstant();

	public ItemStack getInput(int slot);

	public ItemStack getOutput();

	public List<Integer> getRecipeSlots();

	public int getCookTime();

	public int getFuelCost();
	
	public void writeToNBT(NBTTagCompound nbt);
	
	public void readFromNBT(NBTTagCompound nbt);
}
