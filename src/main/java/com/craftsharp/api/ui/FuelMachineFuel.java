package com.craftsharp.api.ui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FuelMachineFuel implements IMachineFuel {
	private static final long serialVersionUID = 4206149843272748120L;

	private int burnTime;
	private int fuelSlot;
	private transient ItemStack input;

	public FuelMachineFuel(ItemStack input, int burn, int slot) {
		this.input = input;
		this.burnTime = burn;
		this.fuelSlot = slot;
	}

	@Override
	public ItemStack getInput() {
		return input;
	}

	@Override
	public int getBurnTime() {
		return burnTime;
	}

	@Override
	public int getFuelSlot() {
		return fuelSlot;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound sub = new NBTTagCompound();
		input.writeToNBT(sub);
		nbt.setTag("Input", sub);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		input = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Input"));
	}
}
