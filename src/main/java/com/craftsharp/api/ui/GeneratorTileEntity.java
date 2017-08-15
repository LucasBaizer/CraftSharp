package com.craftsharp.api.ui;

import java.io.IOException;

import com.craftsharp.ModPacket;

import cofh.api.energy.IEnergyProvider;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class GeneratorTileEntity extends ElectricMachineTileEntity implements IEnergyProvider {
	private int cachedEnergy;
	protected boolean generating;
	protected int energyPerTick;

	public GeneratorTileEntity(int slots, int update) {
		super(slots, update);
	}

	protected boolean canAcceptFuel() {
		if (!isEmpty(itemSlots) && currentCookTime == 0) {
			for (IMachineRecipe recipe : validRecipes) {
				if (recipe.satifies(this, slots)) {
					return true;
				}
			}
		}
		return false;
	}

	protected void onGenerationStarted() {
	}

	protected void onGenerationEnded() {
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote) {
			if (generating) {
				currentCookTime--;
				energy.receiveEnergy(energyPerTick, false);

				cookTicks++;
				if (tickUpdateRate != -1 && cookTicks >= tickUpdateRate) {
					cookTicks = 0;
					if (enablesAutomaticUpdates()) {
						ModPacket.sendTileEntityUpdate(this);
					}
				}
				if (currentCookTime == 0) {
					if (!canAcceptFuel() || getNextRecipe() == null) {
						this.generating = false;
						this.currentRecipe = null;
						this.cookTime = 0;
						this.energyPerTick = 0;
						onGenerationEnded();

						if (enablesAutomaticUpdates()) {
							ModPacket.sendTileEntityUpdate(this);
						}
					}
				}
			}
			if (canAcceptFuel()) {
				IGeneratorRecipe fuel = (IGeneratorRecipe) getNextRecipe();
				if (fuel != null && energy.getEnergyStored() != energy.getMaxEnergyStored()) {
					for (int slot : fuel.getRecipeSlots()) {
						slots[slot].stackSize -= fuel.getInput(slot).stackSize;
						if (slots[slot].stackSize == 0) {
							slots[slot] = null;
						}
					}
					this.cookTime = fuel.getCookTime();
					this.currentCookTime = fuel.getCookTime();
					this.energyPerTick = fuel.getTotalEnergyGenerated() / fuel.getCookTime();
					this.generating = true;
					this.currentRecipe = fuel;

					if (enablesAutomaticUpdates()) {
						ModPacket.sendTileEntityUpdate(this);
					}

					onGenerationStarted();
				}
			}

			if (enablesAutomaticUpdates() && cachedEnergy != energy.getEnergyStored()) {
				cachedEnergy = energy.getEnergyStored();
				ModPacket.sendTileEntityUpdate(this);
			}
		}
	}

	@Override
	public int getInfoEnergyPerTick() {
		return generating ? energyPerTick : 0;
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		int maxEnergyPerTick = 0;
		for (IMachineRecipe r : validRecipes) {
			IGeneratorRecipe recipe = (IGeneratorRecipe) r;
			int energy = recipe.getTotalEnergyGenerated() / recipe.getCookTime();
			if (energy > maxEnergyPerTick) {
				maxEnergyPerTick = energy;
			}
		}
		return maxEnergyPerTick;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return energy.extractEnergy(maxExtract, simulate);
	}

	@Override
	public void write(ByteBufOutputStream out) throws IOException {
		super.write(out);

		out.writeInt(cachedEnergy);
		out.writeInt(energyPerTick);
		out.writeBoolean(generating);
	}

	@Override
	public void read(ByteBufInputStream in) throws IOException {
		super.read(in);

		cachedEnergy = in.readInt();
		energyPerTick = in.readInt();
		generating = in.readBoolean();
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("CachedEnergy", cachedEnergy);
		tagCompound.setInteger("EnergyPerTick", energyPerTick);
		tagCompound.setBoolean("Generating", generating);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		cachedEnergy = tagCompound.getInteger("CachedEnergy");
		energyPerTick = tagCompound.getInteger("EnergyPerTick");
		generating = tagCompound.getBoolean("Generating");
	}
}