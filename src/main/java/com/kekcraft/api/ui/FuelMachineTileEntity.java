package com.kekcraft.api.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.kekcraft.ModPacket;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class FuelMachineTileEntity extends MachineTileEntity {
	public FuelMachineTileEntity(int slots, int tickUpdateRate) {
		super(slots, tickUpdateRate);
	}

	public int[] fuelSlots;
	public ArrayList<IMachineFuel> fuels = new ArrayList<IMachineFuel>();

	public void setFuelSlots(int[] slots) {
		this.fuelSlots = slots;
	}

	public void addFuel(IMachineFuel fuel) {
		fuels.add(fuel);
	}

	private int burnTime;
	private int currentBurnTime;

	@Override
	protected boolean canSmelt() {
		return !isEmpty(itemSlots) && currentCookTime == 0 && currentBurnTime > 0;
	}

	protected boolean canAcceptFuel() {
		boolean bool = !isEmpty(fuelSlots) && currentBurnTime == 0;
		if (bool) {
			if (currentCookTime > 0) {
				return true;
			} else {
				for (IMachineRecipe recipe : validRecipes) {
					if (recipe.satifies(slots)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private IMachineFuel getNextFuel() {
		for (IMachineFuel fuel : fuels) {
			if (slots[fuel.getFuelSlot()] != null) {
				if (slots[fuel.getFuelSlot()].getItem() == fuel.getInput().getItem()
						&& slots[fuel.getFuelSlot()].stackSize >= fuel.getInput().stackSize) {
					return fuel;
				}
			}
		}
		return null;
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote) {
			if (canAcceptFuel()) {
				IMachineFuel fuel = getNextFuel();
				if (fuel != null) {
					slots[fuel.getFuelSlot()].stackSize -= fuel.getInput().stackSize;
					if (slots[fuel.getFuelSlot()].stackSize == 0) {
						slots[fuel.getFuelSlot()] = null;
					}
					burnTime = fuel.getBurnTime();
					currentBurnTime = fuel.getBurnTime();
				}
			}
			if (canSmelt()) {
				IMachineRecipe recipe = getNextRecipe();
				if (recipe != null) {
					beginSmeltNextItem();
					onItemConsumeStart();
				}
			}
			if (isBurningRecipe()) {
				if (currentBurnTime == 0 || !currentRecipe.satifies(slots)) {
					reset();
					onSmeltingStopped();
					ModPacket.sendTileEntityUpdate(this);
				} else {
					currentCookTime--;

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
			if (currentBurnTime > 0) {
				currentBurnTime--;
				if (enablesAutomaticUpdates()) {
					if (currentBurnTime % tickUpdateRate == 0) {
						ModPacket.sendTileEntityUpdate(this);
					}
				}
			}
		}

	}

	@Override
	public void defaultWriteToNBT(NBTTagCompound tagCompound) {
		super.defaultWriteToNBT(tagCompound);

		tagCompound.setInteger("BurnTime", burnTime);
		tagCompound.setInteger("CurrentBurnTime", currentBurnTime);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		burnTime = tagCompound.getInteger("BurnTime");
		currentBurnTime = tagCompound.getInteger("CurrentBurnTime");
	}

	public int getBurnTime() {
		return burnTime;
	}

	public int getCurrentBurnTime() {
		return currentBurnTime;
	}

	@Override
	public void read(ByteBufInputStream in) throws IOException {
		currentBurnTime = in.readInt();
		burnTime = in.readInt();
		currentCookTime = in.readInt();
		cookTime = in.readInt();

		for (int i = 0; i < faces.size(); i++) {
			ForgeDirection dir = ForgeDirection.values()[in.readInt()];
			FaceType face = FaceType.values()[in.readInt()];
			faces.put(dir, face);
		}
	}

	@Override
	public void write(ByteBufOutputStream out) throws IOException {
		out.writeInt(Minecraft.getMinecraft().theWorld.provider.dimensionId);
		out.writeInt(xCoord);
		out.writeInt(yCoord);
		out.writeInt(zCoord);

		out.writeInt(getCurrentBurnTime());
		out.writeInt(getBurnTime());
		out.writeInt(getCurrentCookTime());
		out.writeInt(getCookTime());

		for (Entry<ForgeDirection, FaceType> entry : faces.entrySet()) {
			out.writeInt(entry.getKey().ordinal());
			out.writeInt(entry.getValue().ordinal());
		}
	}
}
