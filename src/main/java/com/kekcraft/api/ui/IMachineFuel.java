package com.kekcraft.api.ui;

import java.io.Serializable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IMachineFuel extends Serializable {
	public ItemStack getInput();

	public int getBurnTime();
	
	public int getFuelSlot();

	public void writeToNBT(NBTTagCompound nbt);

	public void readFromNBT(NBTTagCompound nbt);
}
