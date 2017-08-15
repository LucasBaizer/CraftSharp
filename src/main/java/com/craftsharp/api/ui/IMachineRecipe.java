package com.craftsharp.api.ui;

import java.io.Serializable;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IMachineRecipe extends Serializable {
	public boolean satifies(MachineTileEntity entity, ItemStack[] slots);

	public boolean isValidElementOfRecipe(ItemStack element, int index);
	
	public boolean isInstant();

	public ItemStack getInput(int slot);

	public List<Integer> getRecipeSlots();

	public int getCookTime();
	
	public void writeToNBT(NBTTagCompound nbt);
	
	public void readFromNBT(NBTTagCompound nbt);
}
