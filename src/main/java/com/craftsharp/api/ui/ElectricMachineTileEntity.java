package com.craftsharp.api.ui;

import java.io.IOException;

import com.craftsharp.ModPacket;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class ElectricMachineTileEntity extends MachineTileEntity
		implements IEnergyReceiver, IEnergyConnection, IEnergyInfo {
	public EnergyStorage energy = new EnergyStorage(0);

	public ElectricMachineTileEntity(int slots, int update) {
		super(slots, update);
	}

	private int getEnergyCostPerCook(IMachineSmeltableRecipe recipe) {
		return (recipe.getFuelCost() * (getUpgrades(MachineUpgrade.SPEED) + 1)
				/ (getUpgrades(MachineUpgrade.ENERGY_EFFICIENCY) + 1))
				/ (recipe.getCookTime() / (getUpgrades(MachineUpgrade.SPEED) + 1));
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!this.worldObj.isRemote) {
			if (isReady()) {
				IMachineSmeltableRecipe recipe = (IMachineSmeltableRecipe) getNextRecipe();
				if (recipe != null) {
					if (energy.getEnergyStored() - recipe.getFuelCost() >= 0) {
						beginSmeltNextItem();
						onItemConsumeStart();

						if (enablesAutomaticUpdates()) {
							ModPacket.sendTileEntityUpdate(this);
						}

						if (recipe.isInstant()) {
							energy.modifyEnergyStored(-recipe.getFuelCost());
							smeltItemWhenDone();
						}
					}
				}
			}
			if (isBurningRecipe()) {
				if (!currentRecipe.satifies(this, slots)) {
					reset();
					onSmeltingStopped();
					if (getNextRecipe() == null) {
						onSmeltingFinished();
					}
					if (enablesAutomaticUpdates()) {
						ModPacket.sendTileEntityUpdate(this);
					}
				} else {
					currentCookTime--;

					energy.modifyEnergyStored(-getEnergyCostPerCook((IMachineSmeltableRecipe) currentRecipe));

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

		energy.setEnergyStored(tagCompound.getInteger("Energy"));
		energy.setMaxExtract(tagCompound.getInteger("Extract"));
		energy.setMaxReceive(tagCompound.getInteger("Receive"));
		energy.setCapacity(tagCompound.getInteger("MaxEnergy"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.defaultWriteToNBT(tagCompound);

		tagCompound.setInteger("Energy", energy.getEnergyStored());
		tagCompound.setInteger("Extract", energy.getMaxExtract());
		tagCompound.setInteger("Receive", energy.getMaxReceive());
		tagCompound.setInteger("MaxEnergy", energy.getMaxEnergyStored());
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energy.getMaxEnergyStored();
	}

	@Override
	public int getInfoEnergyPerTick() {
		return isBurningRecipe() ? getEnergyCostPerCook((IMachineSmeltableRecipe) currentRecipe) : 0;
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return getEnergyCostPerCook((IMachineSmeltableRecipe) validRecipes.get(0));
	}

	@Override
	public int getInfoEnergyStored() {
		return energy.getEnergyStored();
	}

	@Override
	public int getInfoMaxEnergyStored() {
		return energy.getMaxEnergyStored();
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return faces.get(from) == FaceType.ENERGY ? energy.receiveEnergy(maxReceive, simulate) : 0;
	}

	@Override
	public void read(ByteBufInputStream in) throws IOException {
		super.read(in);

		energy.setCapacity(in.readInt());
		energy.setEnergyStored(in.readInt());
		energy.setMaxExtract(in.readInt());
		energy.setMaxReceive(in.readInt());
	}

	@Override
	public void write(ByteBufOutputStream out) throws IOException {
		super.write(out);

		out.writeInt(energy.getMaxEnergyStored());
		out.writeInt(energy.getEnergyStored());
		out.writeInt(energy.getMaxExtract());
		out.writeInt(energy.getMaxReceive());
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return faces.get(from) == FaceType.ENERGY;
	}
}