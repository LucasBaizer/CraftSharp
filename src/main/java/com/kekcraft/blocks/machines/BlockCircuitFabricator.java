package com.kekcraft.blocks.machines;

import java.awt.Dimension;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.ElectricMachine;
import com.kekcraft.api.ui.ElectricMachineTileEntity;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.MachineUI;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockCircuitFabricator extends ElectricMachine {
	public BlockCircuitFabricator(GameFactory factory) {
		super(Material.glass, KekCraft.modInstance, 5,
				new ResourceLocation(KekCraft.MODID, "textures/ui/CircuitFabricator.png"));

		factory.initializeBlock(this, "Circuit Fabricator", "CircuitFabricator", Tabs.DEFAULT, "CircuitFabricator");
		RecipeHandler.RECIPES.add(new ShapedOreRecipe(new ItemStack(this), "ABA", "CDC", "AEA", 'A', "ingotAluminum",
				'B', "blockSilicon", 'C', "gearIron", 'D', KekCraft.factory.getItem("MachineCore"), 'E', "ingotGold"));
		setWindowDimensions(new Dimension(-1, 189));
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockCircuitFabricatorTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);

		container.drawMinecraftInventory(8, 107);
		container.addSlotToContainer(container.createSlot(0, 56, 34));
		container.addSlotToContainer(container.createSlot(1, 56, 57));
		container.addSlotToContainer(container.createSlot(2, 56, 80));
		container.addSlotToContainer(container.createOutputSlot(3, 135, 58));
	}

	@Override
	public void drawToUI(MachineUI ui, MachineTileEntity entity) {
		BlockCircuitFabricatorTileEntity e = (BlockCircuitFabricatorTileEntity) entity;

		int barWidth = 7;
		int barHeight = 74;
		int targetHeight = (barHeight - (e.getEnergy().getMaxEnergyStored() - e.getEnergy().getEnergyStored())
				* barHeight / e.getEnergy().getMaxEnergyStored());
		ui.drawUV(ui.left + 26, ui.top + 29 + (barHeight - targetHeight), 176, 23 + barHeight - targetHeight, barWidth,
				targetHeight);
		ui.drawTooltip(ui.left + 26, ui.top + 29, barWidth, barHeight, (int) e.getEnergy().getEnergyStored() + " RF");

		int arrowWidth = 43;
		int arrowHeight = 16;
		if (e.getCurrentCookTime() > 0) {
			int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth / (double) e.getCookTime())
					+ 1);
			if (e.getCurrentCookTime() == 0) {
				width = 0;
			}
			ui.drawUV(ui.left + 79, ui.top + 57, 176, 97, width, arrowHeight);
			ui.drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight,
					100 - (int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * 100) + "%");
		} else {
			ui.drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight, "0%");
		}
	}

	public static class BlockCircuitFabricatorTileEntity extends ElectricMachineTileEntity {
		public BlockCircuitFabricatorTileEntity() {
			super(4, 20);

			setItemSlots(new int[] { 0, 1, 2 });
			setOutputSlots(new int[] { 3 });

			getEnergy().setCapacity(100000);
			getEnergy().setMaxTransfer(128);
			getEnergy().setEnergyStored(KekCraft.ENERGY_MODE_DEV ? 100000 : 0);

			addRecipe(new CircuitFabricatorRecipe(new ItemStack(Items.redstone),
					new ItemStack(KekCraft.factory.getItem("RefinedSilicon")),
					KekCraft.factory.getItem("CircuitRedstone")));

			addRecipe(new CircuitFabricatorRecipe(new ItemStack(Items.iron_ingot),
					new ItemStack(KekCraft.factory.getItem("CircuitRedstone")),
					KekCraft.factory.getItem("CircuitIron")));

			addRecipe(new CircuitFabricatorRecipe(new ItemStack(Items.gold_ingot),
					new ItemStack(KekCraft.factory.getItem("CircuitIron")), KekCraft.factory.getItem("CircuitGold")));

			addRecipe(new CircuitFabricatorRecipe(new ItemStack(Items.diamond),
					new ItemStack(KekCraft.factory.getItem("CircuitGold")),
					KekCraft.factory.getItem("CircuitDiamond")));

			addRecipe(new CircuitFabricatorRecipe(new ItemStack(Items.emerald),
					new ItemStack(KekCraft.factory.getItem("CircuitDiamond")),
					KekCraft.factory.getItem("CircuitEmerald")));
		}

		@Override
		public boolean canConnectEnergy(ForgeDirection from) {
			return true;
		}
	}
}
