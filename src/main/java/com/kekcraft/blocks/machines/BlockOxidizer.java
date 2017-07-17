package com.kekcraft.blocks.machines;

import com.kekcraft.KekCraft;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.ElectricMachine;
import com.kekcraft.api.ui.ElectricMachineTileEntity;
import com.kekcraft.api.ui.IMachineRecipe;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.MachineUI;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockOxidizer extends ElectricMachine {
	public BlockOxidizer(GameFactory factory) {
		super(Material.glass, KekCraft.modInstance, 2,
				new ResourceLocation(KekCraft.MODID, "textures/ui/Oxidizer.png"));
		factory.initializeBlock(this, "Oxidizer", "Oxidizer", Tabs.DEFAULT, "Oxidizer");
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockOxidizerTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.drawMinecraftInventory(8, 84);

		container.addSlotToContainer(container.createSlot(0, 54, 35));
		container.addSlotToContainer(new Slot(container.getTileEntity(), 1, 32, 35) {
			@Override
			public boolean isItemValid(ItemStack item) {
				return item.getItem() == Items.water_bucket;
			}
		});
		container.addSlotToContainer(container.createOutputSlot(2, 129, 35));
	}

	@Override
	public void drawToUI(MachineUI ui, MachineTileEntity entity) {
		BlockOxidizerTileEntity e = (BlockOxidizerTileEntity) entity;

		int barWidth = 7;
		int barHeight = 74;
		int targetHeight = (barHeight
				- ((int) e.getEnergy().getMaxEnergyStored() - (int) e.getEnergy().getEnergyStored()) * barHeight
						/ (int) e.getEnergy().getMaxEnergyStored());
		ui.drawUV(ui.left + 8, ui.top + 5 + (barHeight - targetHeight), 176, barHeight - targetHeight, barWidth,
				targetHeight);
		ui.drawTooltip(ui.left + 8, ui.top + 5, barWidth, barHeight, (int) e.getEnergy().getEnergyStored() + " DJ");

		int arrowWidth = 36;
		int arrowHeight = 16;
		if (e.getCurrentCookTime() > 0) {
			int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth / (double) e.getCookTime())
					+ 1);
			if (e.getCurrentCookTime() == 0) {
				width = 0;
			}
			ui.drawUV(ui.left + 78, ui.top + 34, 176, 74, width, arrowHeight);
			ui.drawTooltip(ui.left + 78, ui.top + 34, arrowWidth, arrowHeight,
					Math.abs(e.getCurrentCookTime() - 200) / e.getCookTime() + "%");
		} else {
			ui.drawTooltip(ui.left + 78, ui.top + 34, arrowWidth, arrowHeight, "0%");
		}
	}

	public static class BlockOxidizerTileEntity extends ElectricMachineTileEntity {
		public BlockOxidizerTileEntity() {
			super(3, 100 / 24);

			getEnergy().setCapacity(100000);
			getEnergy().setMaxTransfer(128);
			getEnergy().setEnergyStored(KekCraft.ENERGY_MODE_DEV ? 100000 : 0);

			setItemSlots(new int[] { 0, 1 });
			setOutputSlots(new int[] { 2 });

			addRecipe(new OxidizerRecipe(new ItemStack(KekCraft.factory.getItem("DustIron")),
					new ItemStack(KekCraft.factory.getItem("DustIronOxide"))));
		}

		@Override
		protected void onItemSmelted(IMachineRecipe item) {
			slots[1] = new ItemStack(Items.bucket);
		}

		@Override
		public boolean canConnectEnergy(ForgeDirection from) {
			return true;
		}
	}
}
