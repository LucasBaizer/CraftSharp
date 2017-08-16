package com.craftsharp.blocks.machines;

import java.awt.Dimension;
import java.io.IOException;

import com.craftsharp.CraftSharp;
import com.craftsharp.ModPacket;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
import com.craftsharp.api.ParticleColor;
import com.craftsharp.api.ui.ElectricMachine;
import com.craftsharp.api.ui.ElectricMachineTileEntity;
import com.craftsharp.api.ui.FaceType;
import com.craftsharp.api.ui.FluidStorage;
import com.craftsharp.api.ui.MachineContainer;
import com.craftsharp.api.ui.MachineTileEntity;
import com.craftsharp.api.ui.UIMainScreen;
import com.craftsharp.api.ui.UIOptionsScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.api.gas.IGasHandler;
import mekanism.api.gas.ITubeConnection;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockRefrigerantCompressor extends ElectricMachine {
	public BlockRefrigerantCompressor(GameFactory factory) {
		super(Material.rock, CraftSharp.modInstance, 10, "RefrigerantCompressor");

		factory.initializeBlock(this, "Refrigerant Compressor", "RefrigerantCompressor", Tabs.DEFAULT,
				"RefrigerantCompressor");
		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(CraftSharp.factory.getBlock("RefrigerantCompressor"), "ABA",
						"CDC", "ABA", 'A', "blockOsmium", 'B', CraftSharp.factory.getItem("TurbineComponent"), 'C',
						Blocks.piston, 'D', CraftSharp.factory.getItem("MachineCoreDiamond")));
			}
		});
		setWindowDimensions(new Dimension(-1, 189));
		initializeSpecialIcons();
		setParticleColor(new ParticleColor(200, 200, 200));
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockRefrigerantCompressorTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);
	}

	public static class BlockRefrigerantCompressorTileEntity extends ElectricMachineTileEntity
			implements ITubeConnection, IGasHandler, IFluidHandler {
		private GasTank refrigerant;
		private GasTank compressedAir;
		private FluidStorage liquidAir;
		private boolean generating;

		public BlockRefrigerantCompressorTileEntity() {
			super(0, -1);

			setItemSlots(new int[0]);
			setOutputSlots(new int[0]);

			energy.setCapacity(100000);
			energy.setMaxTransfer(128);
			energy.setEnergyStored(CraftSharp.ENERGY_MODE_DEV ? 100000 : 0);

			refrigerant = new GasTank(4000);
			compressedAir = new GasTank(4000);
			liquidAir = new FluidStorage();

			refrigerant.setGas(new GasStack(GasRegistry.getGas("refrigerant"), 0));
			compressedAir.setGas(new GasStack(GasRegistry.getGas("compressedAir"), 0));
			liquidAir.setFluid(FluidRegistry.getFluid("liquid_air"));
			liquidAir.setCapacity(4000);
			liquidAir.setFluidStored(0);

			setChangeMeta(true);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIMainScreen(ui) {
						@Override
						public void render(MachineTileEntity m) {
							BlockRefrigerantCompressorTileEntity e = (BlockRefrigerantCompressorTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight - (e.energy.getMaxEnergyStored() - e.energy.getEnergyStored())
									* barHeight / e.energy.getMaxEnergyStored());
							drawUV(ui.left + 7, ui.top + 28 + (barHeight - targetHeight), 176,
									23 + barHeight - targetHeight, barWidth, targetHeight);
							drawTooltip(ui.left + 7, ui.top + 27, barWidth, barHeight,
									e.energy.getEnergyStored() + " RF");

							if (generating) {
								drawUV(ui.left + 48, ui.top + 57, 176, 97, 80, 16);
							}

							drawBar(refrigerant, 183, 22);
							drawBar(compressedAir, 190, 33);

							int target = (int) (74 - (liquidAir.getCapacity() - liquidAir.getFluidStored()) * 74
									/ liquidAir.getCapacity());
							drawUV(ui.left + 135, ui.top + 28 + (74 - target), 197, 23 + 74 - target, 7, target);
							drawTooltip(ui.left + 135, ui.top + 28, 7, 74,
									"Liquid Air: " + (int) liquidAir.getFluidStored() + " ML");

						}

						private void drawBar(GasTank tank, int srcX, int destX) {
							int target = (int) (74 - (tank.getMaxGas() - tank.getStored()) * 74 / tank.getMaxGas());
							drawUV(ui.left + destX, ui.top + 28 + (74 - target), srcX, 23 + 74 - target, 7, target);
							drawTooltip(ui.left + destX, ui.top + 28, 7, 74,
									tank.getGasType().getLocalizedName() + ": " + tank.getStored() + " ML");
						}
					});
					ui.addScreen(new UIOptionsScreen(ui, FaceType.NONE, FaceType.ENERGY, FaceType.ITEM, FaceType.FLUID,
							FaceType.GAS));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}

		@Override
		public void updateEntity() {
			if (!this.worldObj.isRemote) {
				if (energy.getEnergyStored() >= 100 && refrigerant.getStored() >= 5 && compressedAir.getStored() >= 5
						&& liquidAir.getFluidStored() < liquidAir.getCapacity() - 1) {
					generating = true;
					refrigerant.draw(5, true);
					compressedAir.draw(5, true);
					liquidAir.fill(1, true);
					energy.extractEnergy(250, false);

					if (enablesAutomaticUpdates()) {
						ModPacket.sendTileEntityUpdate(this);
					}
				} else {
					generating = false;
				}
			}
		}

		@Override
		public void write(ByteBufOutputStream out) throws IOException {
			super.write(out);

			out.writeInt(refrigerant.getStored());
			out.writeInt(compressedAir.getStored());
			out.writeFloat(liquidAir.getFluidStored());
			out.writeBoolean(generating);
		}

		@Override
		public void read(ByteBufInputStream in) throws IOException {
			super.read(in);

			refrigerant.setGas(refrigerant.getGas().withAmount(in.readInt()));
			compressedAir.setGas(compressedAir.getGas().withAmount(in.readInt()));
			liquidAir.setFluidStored(in.readFloat());
			generating = in.readBoolean();
		}

		@Override
		public void writeToNBT(NBTTagCompound tag) {
			super.writeToNBT(tag);

			NBTTagCompound ref = new NBTTagCompound();
			refrigerant.write(ref);
			tag.setTag("Refrigerant", ref);

			NBTTagCompound cAir = new NBTTagCompound();
			compressedAir.write(cAir);
			tag.setTag("CompressedAir", cAir);

			NBTTagCompound air = new NBTTagCompound();
			liquidAir.writeToNBT(air);
			tag.setTag("LiquidAir", air);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			super.readFromNBT(tag);

			refrigerant = GasTank.readFromNBT(tag.getCompoundTag("Refrigerant"));
			compressedAir = GasTank.readFromNBT(tag.getCompoundTag("CompressedAir"));
			liquidAir = FluidStorage.readFromNBT(tag.getCompoundTag("LiquidAir"));
		}

		@Override
		public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer) {
			int received = 0;
			if (faces.get(side) == FaceType.GAS) {
				if (stack.getGas() == refrigerant.getGasType()) {
					received = refrigerant.receive(stack, doTransfer);
				} else if (stack.getGas() == compressedAir.getGasType()) {
					received = compressedAir.receive(stack, doTransfer);
				}
			}
			if (doTransfer && received > 0) {
				if (enablesAutomaticUpdates()) {
					ModPacket.sendTileEntityUpdate(this);
				}
			}
			return 0;
		}

		@Override
		public int receiveGas(ForgeDirection side, GasStack stack) {
			return receiveGas(side, stack, true);
		}

		@Override
		public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer) {
			return null;
		}

		@Override
		public GasStack drawGas(ForgeDirection side, int amount) {
			return null;
		}

		@Override
		public boolean canReceiveGas(ForgeDirection side, Gas type) {
			return faces.get(side) == FaceType.GAS
					? refrigerant.getGasType() == type || compressedAir.getGasType() == type : false;
		}

		@Override
		public boolean canDrawGas(ForgeDirection side, Gas type) {
			return canReceiveGas(side, type);
		}

		@Override
		public boolean canTubeConnect(ForgeDirection side) {
			return faces.get(side) == FaceType.GAS;
		}

		@Override
		public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
			int filled = faces.get(from) == FaceType.FLUID ? liquidAir.fill(resource, doFill) : 0;
			if (doFill && filled > 0) {
				if (enablesAutomaticUpdates()) {
					ModPacket.sendTileEntityUpdate(this);
				}
			}
			return filled;
		}

		@Override
		public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
			FluidStack drained = faces.get(from) == FaceType.FLUID ? liquidAir.drain(resource, doDrain)
					: new FluidStack(resource.getFluid(), 0);
			if (doDrain && drained != null && drained.amount > 0) {
				if (enablesAutomaticUpdates()) {
					ModPacket.sendTileEntityUpdate(this);
				}
			}
			return drained;
		}

		@Override
		public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
			return faces.get(from) == FaceType.FLUID ? liquidAir.drain(maxDrain, doDrain)
					: new FluidStack(liquidAir.getFluid(), 0);
		}

		@Override
		public boolean canFill(ForgeDirection from, Fluid fluid) {
			return faces.get(from) == FaceType.FLUID && liquidAir.getFluid() == fluid;
		}

		@Override
		public boolean canDrain(ForgeDirection from, Fluid fluid) {
			return canFill(from, fluid);
		}

		@Override
		public FluidTankInfo[] getTankInfo(ForgeDirection from) {
			return faces.get(from) == FaceType.FLUID ? new FluidTankInfo[] { liquidAir.getFluidTankInfo() } : null;
		}
	}
}
