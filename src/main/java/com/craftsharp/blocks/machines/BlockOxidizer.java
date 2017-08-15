package com.craftsharp.blocks.machines;

import static net.minecraft.init.Items.*;

import java.awt.Dimension;

import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
import com.craftsharp.api.ParticleColor;
import com.craftsharp.api.ui.ElectricFluidMachineTileEntity;
import com.craftsharp.api.ui.ElectricMachine;
import com.craftsharp.api.ui.FaceType;
import com.craftsharp.api.ui.MachineContainer;
import com.craftsharp.api.ui.MachineTileEntity;
import com.craftsharp.api.ui.MachineUpgrade;
import com.craftsharp.api.ui.UIMainScreen;
import com.craftsharp.api.ui.UIOptionsScreen;
import com.craftsharp.api.ui.UIUpgradesScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockOxidizer extends ElectricMachine {
	public BlockOxidizer(GameFactory factory) {
		super(Material.glass, CraftSharp.modInstance, 2, "Oxidizer");
		factory.initializeBlock(this, "Oxidizer", "Oxidizer", Tabs.DEFAULT, "Oxidizer");

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("Oxidizer")), "EAE",
						"BCB", "DED", 'A', water_bucket, 'B', iron_ingot, 'C', CraftSharp.factory.getItem("MachineCore"),
						'D', "gearIron", 'E', "ingotAluminum"));
			}
		});

		setWindowDimensions(new Dimension(-1, 189));
		initializeSpecialIcons();
		setParticleColor(ParticleColor.BLUE);
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockOxidizerTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);

		container.drawMinecraftInventory(8, 107);
		container.addSlotToContainer(container.createSlot(0, 66, 58));
		Slot slot = new Slot(container.getTileEntity(), 1, 30, container.normalize(58)) {
			@Override
			public boolean isItemValid(ItemStack item) {
				return item.getItem() == Items.water_bucket;
			}
		};
		slot.setBackgroundIcon(Items.water_bucket.getIconFromDamage(0));
		container.addSlotToContainer(slot);
		container.addSlotToContainer(container.createOutputSlot(2, 141, 58));
		container.addSlotToContainer(container.createUpgradeSlot(3, 96, 37));
		container.addSlotToContainer(container.createUpgradeSlot(4, 96, 58));
		container.addSlotToContainer(container.createUpgradeSlot(5, 96, 79));
	}

	public static class BlockOxidizerTileEntity extends ElectricFluidMachineTileEntity {
		public BlockOxidizerTileEntity() {
			super(6, 100 / 24);

			energy.setCapacity(100000);
			energy.setMaxTransfer(128);
			energy.setEnergyStored(CraftSharp.ENERGY_MODE_DEV ? 100000 : 0);
			fluid.setFluid(FluidRegistry.WATER);
			fluid.setCapacity(4000);
			fluid.setFluidStored(CraftSharp.FLUID_MODE_DEV ? 4000 : 0);

			setItemSlots(new int[] { 0, 1, 3, 4, 5 });
			setFluidSlots(new int[] { 1 });
			setOutputSlots(new int[] { 2 });
			setUpgradeSlots(new int[] { 3, 4, 5 });
			setValidUpgrades(new MachineUpgrade[] { MachineUpgrade.ENERGY_EFFICIENCY, MachineUpgrade.SPEED,
					MachineUpgrade.FLUID_EFFICIENCY });

			addRecipe(new OxidizerRecipe(new ItemStack(CraftSharp.factory.getItem("DustIron")),
					new ItemStack(CraftSharp.factory.getItem("DustIronOxide"), 2)));
			addRecipe(new OxidizerRecipe(new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.mossy_cobblestone)));

			setChangeMeta(true);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIMainScreen(ui) {
						@Override
						public void render(MachineTileEntity m) {
							BlockOxidizerTileEntity e = (BlockOxidizerTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight - (e.energy.getMaxEnergyStored() - e.energy.getEnergyStored())
									* barHeight / e.energy.getMaxEnergyStored());
							drawUV(ui.left + 7, ui.top + 28 + (barHeight - targetHeight), 176,
									23 + barHeight - targetHeight, barWidth, targetHeight);
							drawTooltip(ui.left + 7, ui.top + 28, barWidth, barHeight,
									e.energy.getEnergyStored() + " RF");

							int liquidWidth = 7;
							int liquidHeight = 74;
							int targetLiquidHeight = (int) (liquidHeight
									- (e.fluid.getCapacity() - e.fluid.getFluidStored()) * liquidHeight
											/ e.fluid.getCapacity());
							drawUV(ui.left + 18, ui.top + 28 + (liquidHeight - targetLiquidHeight), 183,
									23 + liquidHeight - targetLiquidHeight, liquidWidth, targetLiquidHeight);
							drawTooltip(ui.left + 18, ui.top + 28, liquidWidth, liquidHeight,
									(int) e.fluid.getFluidStored() + " ML");

							int arrowWidth = 36;
							int arrowHeight = 16;
							if (e.getCurrentCookTime() > 0) {
								int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth
										/ (double) e.getCookTime()) + 1);
								if (e.getCurrentCookTime() == 0) {
									width = 0;
								}
								drawUV(ui.left + 90, ui.top + 57, 176, 97, width, arrowHeight);
								drawTooltip(ui.left + 90, ui.top + 57, arrowWidth, arrowHeight, 100
										- ((int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * 100)) + "%");
							} else {
								drawTooltip(ui.left + 90, ui.top + 57, arrowWidth, arrowHeight, "0%");
							}
						}
					});
					ui.addScreen(
							new UIOptionsScreen(ui, FaceType.NONE, FaceType.ENERGY, FaceType.ITEM, FaceType.FLUID));
					ui.addScreen(new UIUpgradesScreen(ui));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}
	}
}
