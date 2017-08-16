package com.craftsharp.api.ui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class FluidStorage {
	private Fluid fluid;
	private int capacity;
	private float stored;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
		setFluidStored(stored); // re-clamp
	}

	public float getFluidStored() {
		return stored;
	}

	public void setFluidStored(float stored) {
		this.stored = MathHelper.clamp_float(stored, 0, capacity);
	}

	public Fluid getFluid() {
		return fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public FluidStack getFluidStack() {
		return new FluidStack(fluid, (int) stored);
	}

	public FluidTankInfo getFluidTankInfo() {
		return new FluidTankInfo(getFluidStack(), capacity);
	}

	public int fill(FluidStack resource, boolean doFill) {
		float oldStored = stored;
		float newStored = Math.min(capacity, oldStored + resource.amount);
		float accepted = newStored - oldStored;
		if (doFill) {
			stored = newStored;
		}
		return (int) accepted;
	}

	public int fill(int amount, boolean doFill) {
		float oldStored = stored;
		float newStored = Math.min(capacity, oldStored + amount);
		float accepted = newStored - oldStored;
		if (doFill) {
			stored = newStored;
		}
		return (int) accepted;
	}

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return drain(resource.amount, doDrain);
	}

	public FluidStack drain(float maxDrain, boolean doDrain) {
		float drained = maxDrain;
		float stored = this.stored;
		stored -= maxDrain;
		if (stored < 0) {
			drained -= Math.abs(stored);
			stored = 0;
		}
		if(doDrain) {
			this.stored = stored;
		}
		return new FluidStack(fluid, (int) drained);
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("FluidID", fluid.getID());
		tag.setInteger("Capacity", capacity);
		tag.setFloat("Stored", stored);
	}

	public static FluidStorage readFromNBT(NBTTagCompound tag) {
		FluidStorage storage = new FluidStorage();
		storage.fluid = FluidRegistry.getFluid(tag.getInteger("FluidID"));
		storage.capacity = tag.getInteger("Capacity");
		storage.stored = tag.getFloat("Stored");
		return storage;
	}
}
