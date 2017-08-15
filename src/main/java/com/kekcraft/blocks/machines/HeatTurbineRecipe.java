package com.kekcraft.blocks.machines;

import java.util.Arrays;
import java.util.List;

import com.kekcraft.api.ui.IGeneratorRecipe;
import com.kekcraft.api.ui.MachineTileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HeatTurbineRecipe implements IGeneratorRecipe {
	private static final long serialVersionUID = 541953037712744111L;

	private transient ItemStack fuel;
	private int generated;
	private int time;

	public HeatTurbineRecipe(ItemStack fuel, int generated) {
		this(fuel, 400, generated);
	}

	public HeatTurbineRecipe(ItemStack fuel, int time, int generated) {
		this.fuel = fuel;
		this.time = time;
		this.generated = generated;
	}

	@Override
	public boolean satifies(MachineTileEntity entity, ItemStack[] slots) {
		return slots[0] != null && slots[0].getItem() == fuel.getItem() && slots[0].stackSize >= fuel.stackSize;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return index == 0 ? element.getItem() == fuel.getItem() : false;
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public ItemStack getInput(int slot) {
		return slot == 0 ? fuel : null;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(0);
	}

	@Override
	public int getCookTime() {
		return time;
	}

	@Override
	public int getTotalEnergyGenerated() {
		return generated;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound inTag = new NBTTagCompound();
		fuel.writeToNBT(inTag);
		nbt.setTag("Fuel", inTag);
		nbt.setInteger("Generated", generated);
		nbt.setInteger("Time", time);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		fuel = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Fuel"));
		generated = nbt.getInteger("Generated");
		time = nbt.getInteger("Time");
	}
}
