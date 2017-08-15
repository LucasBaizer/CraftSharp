package com.craftsharp.blocks.machines;

import java.awt.Dimension;

import com.craftsharp.CraftSharp;
import com.craftsharp.ModPacket;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
import com.craftsharp.api.ParticleColor;
import com.craftsharp.api.ui.ElectricMachine;
import com.craftsharp.api.ui.ElectricMachineTileEntity;
import com.craftsharp.api.ui.FaceType;
import com.craftsharp.api.ui.MachineContainer;
import com.craftsharp.api.ui.MachineTileEntity;
import com.craftsharp.api.ui.UIMainScreen;
import com.craftsharp.api.ui.UIOptionsScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.api.gas.IGasHandler;
import mekanism.api.gas.ITubeConnection;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockAirCompressor extends ElectricMachine {
	private IIcon topIcon;

	public BlockAirCompressor(GameFactory factory) {
		super(Material.rock, CraftSharp.modInstance, 9, "AirCompressor");

		factory.initializeBlock(this, "Air Compressor", "AirCompressor", Tabs.DEFAULT, "AirCompressor");
		setWindowDimensions(new Dimension(-1, 111));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("AirCompressor")), "ABA", "CDC", "AAA",
						'A', Items.iron_ingot, 'B', CraftSharp.factory.getItem("TurbineComponent"), 'C', "circuitIron", 'D',
						CraftSharp.factory.getItem("MachineCoreIron")));
			}
		});

		initializeSpecialIcons();
		setParticleColor(ParticleColor.WHITE);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);

		this.topIcon = reg.registerIcon(this.textureName + "_top");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? topIcon : super.getIcon(side, meta);
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockAirCompressorTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);
	}

	public static class BlockAirCompressorTileEntity extends ElectricMachineTileEntity
			implements ITubeConnection, IGasHandler {
		private GasTank air;

		public BlockAirCompressorTileEntity() {
			super(0, -1);

			setEnableAutomaticUpdates(false);
			setItemSlots(new int[0]);
			setOutputSlots(new int[0]);

			energy.setCapacity(50000);
			energy.setMaxTransfer(128);
			energy.setEnergyStored(0);

			air = new GasTank(4000);
			air.setGas(new GasStack(GasRegistry.getGas("compressedAir"), 0));

			setChangeMeta(true);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIMainScreen(ui) {
						@Override
						public void render(MachineTileEntity m) {
							BlockAirCompressorTileEntity e = (BlockAirCompressorTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight - (e.energy.getMaxEnergyStored() - e.energy.getEnergyStored())
									* barHeight / (int) e.energy.getMaxEnergyStored());
							drawUV(ui.left + 78, ui.top + 29 + (barHeight - targetHeight), 176,
									23 + barHeight - targetHeight, barWidth, targetHeight);
							drawTooltip(ui.left + 78, ui.top + 29, barWidth, barHeight,
									e.energy.getEnergyStored() + " RF");

							int airHeight = (barHeight
									- (air.getMaxGas() - air.stored.amount) * barHeight / (int) air.getMaxGas());
							drawUV(ui.left + 92, ui.top + 29 + (barHeight - airHeight), 183, 23 + barHeight - airHeight,
									barWidth, airHeight);
							drawTooltip(ui.left + 92, ui.top + 29, barWidth, barHeight,
									"Compressed Air: " + air.stored.amount + " RF");
						}
					});
					ui.addScreen(new UIOptionsScreen(ui, FaceType.NONE, FaceType.ENERGY, FaceType.GAS)
							.setDirectionX(ForgeDirection.UP, 192).setFaceTypes(ForgeDirection.UP, new FaceType[0]));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}

		@Override
		public void updateEntity() {
			if (!this.worldObj.isRemote) {
				if (air.getNeeded() >= 10) {
					energy.extractEnergy(100, false);
					air.receive(new GasStack(air.getGasType(), 10), true);

					if (enablesAutomaticUpdates()) {
						ModPacket.sendTileEntityUpdate(this);
					}
				}
			}
		}

		@Override
		public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer) {
			return faces.get(side) == FaceType.GAS ? air.receive(stack, true) : 0;
		}

		@Override
		public int receiveGas(ForgeDirection side, GasStack stack) {
			return receiveGas(side, stack, true);
		}

		@Override
		public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer) {
			return faces.get(side) == FaceType.GAS ? air.draw(amount, true) : new GasStack(air.getGasType(), 0);
		}

		@Override
		public GasStack drawGas(ForgeDirection side, int amount) {
			return drawGas(side, amount);
		}

		@Override
		public boolean canReceiveGas(ForgeDirection side, Gas type) {
			return faces.get(side) == FaceType.GAS && type == air.getGasType();
		}

		@Override
		public boolean canDrawGas(ForgeDirection side, Gas type) {
			return canReceiveGas(side, type);
		}

		@Override
		public boolean canTubeConnect(ForgeDirection side) {
			return faces.get(side) == FaceType.GAS;
		}
	}
}
