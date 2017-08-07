package com.kekcraft.blocks.machines;

import static net.minecraft.init.Blocks.stone;
import static net.minecraft.init.Items.iron_ingot;

import java.awt.Dimension;
import java.util.Map;
import java.util.Map.Entry;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ParticleColor;
import com.kekcraft.api.ui.DefaultMachineRecipe;
import com.kekcraft.api.ui.ElectricMachine;
import com.kekcraft.api.ui.ElectricMachineTileEntity;
import com.kekcraft.api.ui.FaceType;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.UIOptionsScreen;
import com.kekcraft.api.ui.UIScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;

public class BlockElectricFurnace extends ElectricMachine {
	public BlockElectricFurnace(GameFactory factory) {
		super(Material.ground, KekCraft.modInstance, 0, "ElectricFurnace");
		factory.initializeBlock(this, "Electric Furnace", "ElectricFurnace", Tabs.DEFAULT, "ElectricFurnace");
		setWindowDimensions(new Dimension(-1, 189));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ItemStack(KekCraft.factory.getBlock("ElectricFurnace")), "ABA", "BCB", "ABA",
						'A', iron_ingot, 'B', stone, 'C', KekCraft.factory.getItem("MachineCore"));
			}
		});

		initializeSpecialIcons();
		setParticleColor(ParticleColor.BLACK);
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockElectricFurnaceTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);
		container.drawMinecraftInventory(8, 107);

		container.addSlotToContainer(container.createSlot(0, 56, 58));
		container.addSlotToContainer(container.createOutputSlot(1, 116, 58));
	}

	public static class BlockElectricFurnaceTileEntity extends ElectricMachineTileEntity {
		public BlockElectricFurnaceTileEntity() {
			super(2, 5);

			getEnergy().setCapacity(100000);
			getEnergy().setMaxTransfer(128);
			getEnergy().setEnergyStored(KekCraft.ENERGY_MODE_DEV ? 100000 : 0);

			setItemSlots(new int[] { 0 });
			setOutputSlots(new int[] { 1 });

			@SuppressWarnings("unchecked")
			Map<ItemStack, ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();
			for (Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
				addRecipe(new DefaultMachineRecipe(entry.getKey(), entry.getValue(), 50, 800, 0));
			}

			setChangeMeta(true);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIScreen(ui, "MainScreen") {
						@Override
						public void render(MachineTileEntity m, Object... args) {
							BlockElectricFurnaceTileEntity e = (BlockElectricFurnaceTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight
									- (e.getEnergy().getMaxEnergyStored() - e.getEnergy().getEnergyStored()) * barHeight
											/ e.getEnergy().getMaxEnergyStored());
							drawUV(ui.left + 8, ui.top + 28 + (barHeight - targetHeight), 176,
									23 + barHeight - targetHeight, barWidth, targetHeight);
							drawTooltip(ui.left + 8, ui.top + 27, barWidth, barHeight,
									(int) e.getEnergy().getEnergyStored() + " RF");

							int arrowWidth = 24;
							int arrowHeight = 16;
							if (e.getCurrentCookTime() > 0) {
								int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth
										/ (double) e.getCookTime()) + 1);
								if (e.getCurrentCookTime() == 0) {
									width = 0;
								}
								drawUV(ui.left + 79, ui.top + 57, 176, 97, width, arrowHeight);
								drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight, 100
										- ((int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * 100)) + "%");
							} else {
								drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight, "0%");
							}
						}
					}.addScreenSwitch(22, 0, 23, 23, "Options"));
					ui.addScreen(new UIOptionsScreen(ui, FaceType.NONE, FaceType.ENERGY, FaceType.ITEM));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}
	}
}
