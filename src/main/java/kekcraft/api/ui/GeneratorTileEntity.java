package kekcraft.api.ui;

import cofh.api.energy.IEnergyProvider;
import kekcraft.ModPacket;
import kekcraft.api.ui.energy.ElectricMachineTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class GeneratorTileEntity extends ElectricMachineTileEntity implements IEnergyProvider {
	public GeneratorTileEntity(int slots) {
		super(slots, -1);
		setEnableAutomaticUpdates(false);
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		int extracted = getEnergy().extractEnergy(maxExtract, simulate);
		if (extracted > 0) {
			ModPacket.sendTileEntityUpdate(this);
		}
		return extracted;
	}
}