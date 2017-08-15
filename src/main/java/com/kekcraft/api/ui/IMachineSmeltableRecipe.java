package com.kekcraft.api.ui;

import net.minecraft.item.ItemStack;

public interface IMachineSmeltableRecipe extends IMachineRecipe {
	public ItemStack getOutput();
	
	public int getFuelCost();
}
