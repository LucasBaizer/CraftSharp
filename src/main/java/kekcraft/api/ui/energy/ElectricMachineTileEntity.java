package kekcraft.api.ui.energy;

import java.util.ArrayList;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import kekcraft.ModPacket;
import kekcraft.api.ui.GeneratorTileEntity;
import kekcraft.api.ui.IElectricMachineRecipe;
import kekcraft.api.ui.MachineTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class ElectricMachineTileEntity extends MachineTileEntity
		implements IEnergyReceiver, IEnergyConnection {
	public List<GeneratorTileEntity> generators = new ArrayList<GeneratorTileEntity>();

	public ElectricMachineTileEntity(int slots, int update) {
		super(slots, update);
	}

	protected EnergyStorage energy = new EnergyStorage(0);

	public EnergyStorage getEnergy() {
		return energy;
	}

	public void onItemConsumeStart() {
	}

	protected void onItemSmelted(IElectricMachineRecipe item) {
	}

	private long cookTicks = 0;

	private int getEnergyCostPerCook(IElectricMachineRecipe recipe) {
		return recipe.getEnergyCost() / recipe.getCookTime();
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote) {
			if (canSmelt()) {
				IElectricMachineRecipe recipe = (IElectricMachineRecipe) getNextRecipe();
				if (recipe != null) {
					if (energy.getEnergyStored() - recipe.getEnergyCost() >= 0) {
						beginSmeltNextItem();
						onItemConsumeStart();

						if (recipe.isInstant()) {
							energy.modifyEnergyStored(-recipe.getEnergyCost());
							smeltItemWhenDone();
						}
					}
				}
			}
			if (isBurningRecipe()) {
				if (!currentRecipe.satifies(slots, itemSlots)) {
					reset();
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
					}
				}
			}
		}
	}

	private void reset() {
		currentCookTime = 0;
		cookTime = 0;
		targetSlot = -1;
		currentRecipe = null;
		cookTicks = 0;
	}

	private void smeltItemWhenDone() {
		smeltNextItem();
		onItemSmelted(currentRecipe);
		reset();
		if (enablesAutomaticUpdates()) {
			ModPacket.sendTileEntityUpdate(this);
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
}