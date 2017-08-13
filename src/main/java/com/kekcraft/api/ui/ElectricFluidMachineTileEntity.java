package com.kekcraft.api.ui;

import java.io.IOException;
import java.lang.reflect.Field;

import com.kekcraft.ModPacket;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class ElectricFluidMachineTileEntity extends ElectricMachineTileEntity implements IFluidHandler {
	private int[] fluidSlots;
	public FluidStorage fluid = new FluidStorage();

	public ElectricFluidMachineTileEntity(int slots, int update) {
		super(slots, update);
	}

	@Override
	public MachineTileEntity addRecipe(IMachineRecipe recipe) {
		if (!(recipe instanceof IFluidMachineRecipe)) {
			throw new RuntimeException("All fluid machine recipes must be an implementation of IFluidMachineRecipe; "
					+ recipe.getClass().getSimpleName() + " is not one");
		}
		return super.addRecipe(recipe);
	}

	@Override
	protected IMachineRecipe getNextRecipe() {
		for (IMachineRecipe recipe : validRecipes) {
			if (recipe.satifies(this, slots)
					&& fluid.getFluidStored() >= ((IFluidMachineRecipe) recipe).getFluidCost()) {
				return recipe;
			}
		}
		return null;
	}

	private int getEnergyCostPerCook(IMachineRecipe recipe) {
		return (recipe.getFuelCost() * (getUpgrades(MachineUpgrade.SPEED) + 1)
				/ (getUpgrades(MachineUpgrade.ENERGY_EFFICIENCY) + 1))
				/ (recipe.getCookTime() / (getUpgrades(MachineUpgrade.SPEED) + 1));
	}

	private float getFluidCostPerCook(IFluidMachineRecipe recipe) {
		return ((float) recipe.getFluidCost() / ((float) getUpgrades(MachineUpgrade.FLUID_EFFICIENCY) + 1))
				/ ((float) recipe.getCookTime() / ((float) getUpgrades(MachineUpgrade.SPEED) + 1));
	}

	@Override
	public void updateEntity() {
		// super.updateEntity();

		if (!worldObj.isRemote) {
			if (fluid.getCapacity() - fluid.getFluidStored() >= 1000) {
				for (int slot : fluidSlots) {
					if (slots[slot] != null && slots[slot].getItem() instanceof ItemBucket) {
						ItemBucket bucket = (ItemBucket) slots[slot].getItem();
						if (bucket != Items.bucket) {
							if (bucket == Items.water_bucket) {
								this.fluid.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
							} else if (bucket == Items.lava_bucket) {
								this.fluid.fill(new FluidStack(FluidRegistry.LAVA, 1000), true);
							} else {
								try {
									Field field = bucket.getClass().getDeclaredField("isFull");
									field.setAccessible(true);
									BlockFluidClassic liquid = (BlockFluidClassic) field.get(bucket);
									Fluid fluid = liquid.getFluid();
									this.fluid.fill(new FluidStack(fluid, 1000), true);
								} catch (ReflectiveOperationException e) {
									throw new RuntimeException(e);
								}
							}
							slots[slot] = new ItemStack(Items.bucket);

							if (enablesAutomaticUpdates()) {
								ModPacket.sendTileEntityUpdate(this);
							}
						}
					}
				}
			}
			if (canSmelt()) {
				IFluidMachineRecipe recipe = (IFluidMachineRecipe) getNextRecipe();
				if (recipe != null) {
					if (energy.getEnergyStored() - recipe.getFuelCost() >= 0
							&& fluid.getFluidStored() - recipe.getFluidCost() >= 0) {
						beginSmeltNextItem();
						onItemConsumeStart();

						if (enablesAutomaticUpdates()) {
							ModPacket.sendTileEntityUpdate(this);
						}

						if (recipe.isInstant()) {
							energy.modifyEnergyStored(-recipe.getFuelCost());
							fluid.drain(recipe.getFluidCost(), true);
							smeltItemWhenDone();
						}
					}
				}
			}
			if (isBurningRecipe()) {
				if (!currentRecipe.satifies(this, slots)) {
					reset();
					onSmeltingStopped();
					onSmeltingFinished();
					if (enablesAutomaticUpdates()) {
						ModPacket.sendTileEntityUpdate(this);
					}
				} else {
					currentCookTime--;

					energy.modifyEnergyStored(-getEnergyCostPerCook(currentRecipe));
					fluid.drain(getFluidCostPerCook((IFluidMachineRecipe) currentRecipe), true);

					if (enablesAutomaticUpdates()) {
						cookTicks++;
						if (cookTicks >= tickUpdateRate) {
							cookTicks = 0;
							ModPacket.sendTileEntityUpdate(this);
						}
					}
					if (currentCookTime == 0) {
						smeltItemWhenDone();
						if (getNextRecipe() == null) {
							onSmeltingFinished();
						}
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		fluid.setFluidStored(tagCompound.getFloat("Fluid"));
		fluid.setCapacity(tagCompound.getInteger("MaxFluid"));
		fluid.setFluid(FluidRegistry.getFluid(tagCompound.getInteger("FluidID")));
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setFloat("Fluid", fluid.getFluidStored());
		tagCompound.setInteger("MaxFluid", fluid.getCapacity());
		tagCompound.setInteger("FluidID", fluid.getFluid().getID());
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return faces.get(from) == FaceType.ENERGY ? energy.receiveEnergy(maxReceive, simulate) : 0;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return faces.get(from) == FaceType.FLUID ? fluid.fill(resource, doFill) : 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return faces.get(from) == FaceType.FLUID ? fluid.drain(resource, doDrain)
				: new FluidStack(resource.getFluid(), 0);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return faces.get(from) == FaceType.FLUID ? fluid.drain(maxDrain, doDrain) : new FluidStack(fluid.getFluid(), 0);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return faces.get(from) == FaceType.FLUID ? this.fluid.getFluid() == fluid : false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return this.canFill(from, fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if (faces.get(from) == FaceType.FLUID) {
			return new FluidTankInfo[] { fluid.getFluidTankInfo() };
		}
		return null;
	}

	@Override
	public final void read(ByteBufInputStream in) throws IOException {
		super.read(in);

		fluid.setFluidStored(in.readFloat());
	}

	@Override
	public final void write(ByteBufOutputStream out) throws IOException {
		super.write(out);

		out.writeFloat(fluid.getFluidStored());
	}

	public int[] getFluidSlots() {
		return fluidSlots;
	}

	public void setFluidSlots(int[] fluidSlots) {
		this.fluidSlots = fluidSlots;
	}
}