package com.kekcraft.api.ui;

import java.io.IOException;
import java.util.Map.Entry;

import com.kekcraft.ModPacket;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class ElectricMachineTileEntity extends MachineTileEntity
		implements IEnergyReceiver, IEnergyConnection {
	public ElectricMachineTileEntity(int slots, int update) {
		super(slots, update);
	}

	protected EnergyStorage energy = new EnergyStorage(0);

	public EnergyStorage getEnergy() {
		return energy;
	}

	private int getEnergyCostPerCook(IMachineRecipe recipe) {
		return recipe.getFuelCost() / recipe.getCookTime();
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote) {
			if (canSmelt()) {
				IMachineRecipe recipe = getNextRecipe();
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
				if (!currentRecipe.satifies(slots)) {
					reset();
					onSmeltingStopped();
					ModPacket.sendTileEntityUpdate(this);
				} else {
					currentCookTime--;

					energy.modifyEnergyStored(-getEnergyCostPerCook(currentRecipe));

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
		energy.setMaxTransfer(tagCompound.getInteger("TransferRate"));
		energy.setCapacity(tagCompound.getInteger("MaxEnergy"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.defaultWriteToNBT(tagCompound);

		tagCompound.setInteger("Energy", energy.getEnergyStored());
		tagCompound.setInteger("TransferRate", energy.getMaxExtract());
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
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return getEnergy().receiveEnergy(maxReceive, simulate);
	}

	@Override
	public final void read(ByteBufInputStream in) throws IOException {
		getEnergy().setEnergyStored(in.readInt());
		setCurrentCookTime(in.readInt());
		setCookTime(in.readInt());

		for (int i = 0; i < faces.size(); i++) {
			ForgeDirection dir = ForgeDirection.values()[in.readInt()];
			FaceType face = FaceType.values()[in.readInt()];
			faces.put(dir, face);
		}
	}

	@Override
	public final void write(ByteBufOutputStream out) throws IOException {
		out.writeInt(Minecraft.getMinecraft().theWorld.provider.dimensionId);
		out.writeInt(xCoord);
		out.writeInt(yCoord);
		out.writeInt(zCoord);

		out.writeInt(getEnergy().getEnergyStored());
		out.writeInt(getCurrentCookTime());
		out.writeInt(getCookTime());

		for (Entry<ForgeDirection, FaceType> entry : faces.entrySet()) {
			out.writeInt(entry.getKey().ordinal());
			out.writeInt(entry.getValue().ordinal());
		}
	}

	@Override
	public final boolean canConnectEnergy(ForgeDirection from) {
		return faces.get(from) == FaceType.ENERGY;
	}
}