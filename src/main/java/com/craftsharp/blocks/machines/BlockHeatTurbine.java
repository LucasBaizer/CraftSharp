package com.craftsharp.blocks.machines;

import java.awt.Dimension;

import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
import com.craftsharp.api.ParticleColor;
import com.craftsharp.api.ui.FaceType;
import com.craftsharp.api.ui.Generator;
import com.craftsharp.api.ui.GeneratorTileEntity;
import com.craftsharp.api.ui.MachineContainer;
import com.craftsharp.api.ui.MachineTileEntity;
import com.craftsharp.api.ui.UIMainScreen;
import com.craftsharp.api.ui.UIOptionsScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockHeatTurbine extends Generator {
	private IIcon topIcon;

	public BlockHeatTurbine(GameFactory factory) {
		super(Material.ground, CraftSharp.modInstance, 6, "HeatTurbine");

		factory.initializeBlock(this, "Heat Turbine", "HeatTurbine", Tabs.DEFAULT, "HeatTurbine");
		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("HeatTurbine")),
						"ABA", "DCD", "AAA", 'A', "ingotSteel", 'B', CraftSharp.factory.getItem("TurbineComponent"), 'C',
						CraftSharp.factory.getBlock("ElectricFurnace"), 'D', "circuitRedstone"));
			}
		});
		setWindowDimensions(new Dimension(-1, 189));
		initializeSpecialIcons();
		setParticleColor(new ParticleColor(200, 0, 0));
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
		return BlockHeatTurbineTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);
		container.drawMinecraftInventory(8, 107);
		container.addSlotToContainer(container.createSlot(0, 82, 58));
	}

	public static class BlockHeatTurbineTileEntity extends GeneratorTileEntity {
		public BlockHeatTurbineTileEntity() {
			super(1, 5);

			setItemSlots(new int[] { 0 });
			setOutputSlots(new int[0]);

			energy.setCapacity(25000);
			energy.setMaxTransfer(128);
			energy.setEnergyStored(0);

			addRecipe(new HeatTurbineRecipe(new ItemStack(Items.coal), 3200));
			addRecipe(new HeatTurbineRecipe(new ItemStack(Blocks.coal_block), 3600, 28800));
			addRecipe(new HeatTurbineRecipe(new ItemStack(CraftSharp.factory.getItem("DustThermite")), 12800));

			setChangeMeta(true);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIMainScreen(ui) {
						@Override
						public void render(MachineTileEntity m) {
							BlockHeatTurbineTileEntity e = (BlockHeatTurbineTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight - (e.energy.getMaxEnergyStored() - e.energy.getEnergyStored())
									* barHeight / (int) e.energy.getMaxEnergyStored());
							drawUV(ui.left + 69, ui.top + 29 + (barHeight - targetHeight), 176,
									23 + barHeight - targetHeight, barWidth, targetHeight);
							drawTooltip(ui.left + 69, ui.top + 29, barWidth, barHeight,
									e.energy.getEnergyStored() + " RF");

							int flameWidth = 13;
							int flameHeight = 13;
							if (e.getCurrentCookTime() > 0) {
								int height = (int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * flameHeight);

								drawUV(ui.left + 104, ui.top + 59 + (flameHeight - height), 176,
										97 + (flameHeight - height), flameWidth, height);
								drawTooltip(ui.left + 104, ui.top + 59, flameWidth, flameHeight,
										(int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * 100) + "%");
							}
						}
					});
					ui.addScreen(new UIOptionsScreen(ui, FaceType.NONE, FaceType.ENERGY)
							.setDirectionX(ForgeDirection.UP, 192));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}
	}
}
