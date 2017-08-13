package com.kekcraft.api.ui;

import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.Fluid;
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

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		float oldStored = stored;
		float newStored = Math.max(0, oldStored - resource.amount);
		float drained = newStored - oldStored;
		if (doDrain) {
			stored = newStored;
		}
		return new FluidStack(resource.getFluid(), (int) drained);
	}

	public FluidStack drain(float maxDrain, boolean doDrain) {
		float oldStored = stored;
		float newStored = Math.max(0, oldStored - maxDrain);
		float drained = newStored - oldStored;
		if (doDrain) {
			stored = newStored;
		}
		return new FluidStack(fluid, (int) drained);
	}
}
