package com.kekcraft.blocks.machines;

import java.awt.Dimension;

import com.kekcraft.KekCraft;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.FuelMachine;
import com.kekcraft.api.ui.FuelMachineFuel;
import com.kekcraft.api.ui.FuelMachineTileEntity;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.MachineUI;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class BlockHighTemperatureAlloyFurnace extends FuelMachine {
	public BlockHighTemperatureAlloyFurnace(GameFactory factory) {
		super(Material.rock, KekCraft.modInstance, 4,
				new ResourceLocation(KekCraft.MODID, "textures/ui/HighTemperatureAlloyFurnace.png"));

		factory.initializeBlock(this, "High-Temperature Alloy Furnace", "HighTemperatureAlloyFurnace", Tabs.DEFAULT,
				"HighTemperatureAlloyFurnace");
		setWindowDimensions(new Dimension(-1, 189));
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockHighTemperatureAlloyFurnaceTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);

		container.drawMinecraftInventory(8, 107);
		container.addSlotToContainer(container.createSlot(0, 56, 46));
		container.addSlotToContainer(container.createSlot(1, 56, 69));
		container.addSlotToContainer(new Slot(container.getTileEntity(), 2, 27, 58 - 12) {
			@Override
			public boolean isItemValid(ItemStack item) {
				return item.getItem() == KekCraft.factory.getItem("DustThermite");
			}
		});
		container.addSlotToContainer(container.createOutputSlot(3, 135, 58));
	}

	@Override
	public void drawToUI(MachineUI ui, MachineTileEntity entity) {
		BlockHighTemperatureAlloyFurnaceTileEntity e = (BlockHighTemperatureAlloyFurnaceTileEntity) entity;

		int flameWidth = 13;
		int flameHeight = 13;
		int arrowWidth = 41;
		int arrowHeight = 16;
		if (e.getCurrentBurnTime() > 0) {
			int height = (int) ((e.getCurrentBurnTime() / (double) e.getBurnTime()) * flameHeight);

			ui.drawUV(ui.left + 8, ui.top + 59 + (flameHeight - height), 176, 23 + (flameHeight - height), flameWidth,
					height);
			ui.drawTooltip(ui.left + 8, ui.top + 59, flameWidth, flameHeight,
					(int) ((e.getCurrentBurnTime() / (double) e.getBurnTime()) * 100) + "%");
		}
		if (e.getCurrentCookTime() > 0) {
			int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth / (double) e.getCookTime())
					+ 1);
			if (e.getCurrentCookTime() == 0) {
				width = 0;
			}
			ui.drawUV(ui.left + 80, ui.top + 57, 176, 37, width, arrowHeight);
			ui.drawTooltip(ui.left + 80, ui.top + 57, arrowWidth, arrowHeight,
					(int) ((Math.abs(e.getCurrentCookTime() - 200) / (double) e.getCookTime()) * 100) + "%");
		} else {
			ui.drawTooltip(ui.left + 80, ui.top + 57, arrowWidth, arrowHeight, "0%");
		}
	}

	public static class BlockHighTemperatureAlloyFurnaceTileEntity extends FuelMachineTileEntity {
		public BlockHighTemperatureAlloyFurnaceTileEntity() {
			super(4, 5);

			setItemSlots(new int[] { 0, 1 });
			setFuelSlots(new int[] { 2 });
			setOutputSlots(new int[] { 3 });

			addFuel(new FuelMachineFuel(new ItemStack(KekCraft.factory.getItem("DustThermite")), 200, 2));
			addRecipe(new DualSlotRecipe(new ItemStack(Blocks.sand),
					new ItemStack(KekCraft.factory.getItem("DustMagnesium")),
					new ItemStack(KekCraft.factory.getItem("Silicon")), -1));
			addRecipe(new DualSlotRecipe(new ItemStack(KekCraft.factory.getItem("Silicon")),
					new ItemStack(KekCraft.factory.getItem("Silicon")),
					new ItemStack(KekCraft.factory.getItem("RefinedSilicon")), -1));
		}

		@Override
		public void writeToNBT(NBTTagCompound tagCompound) {
			super.defaultWriteToNBT(tagCompound);
		}
	}
}
