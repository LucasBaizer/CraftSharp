package com.kekcraft.blocks.machines;

import static net.minecraft.init.Items.*;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.ElectricMachine;
import com.kekcraft.api.ui.ElectricMachineTileEntity;
import com.kekcraft.api.ui.IMachineRecipe;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.UIScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockOxidizer extends ElectricMachine {
	public BlockOxidizer(GameFactory factory) {
		super(Material.glass, KekCraft.modInstance, 2, "Oxidizer");
		factory.initializeBlock(this, "Oxidizer", "Oxidizer", Tabs.DEFAULT, "Oxidizer");

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KekCraft.factory.getBlock("Oxidizer")), " A ",
						"BCB", "DED", 'A', water_bucket, 'B', iron_ingot, 'C', KekCraft.factory.getItem("MachineCore"),
						'D', "gearIron", 'E', "ingotAluminum"));
			}
		});
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockOxidizerTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.drawMinecraftInventory(8, 84);

		container.addSlotToContainer(container.createSlot(0, 54, 35));
		Slot slot = new Slot(container.getTileEntity(), 1, 32, 35) {
			@Override
			public boolean isItemValid(ItemStack item) {
				return item.getItem() == Items.water_bucket;
			}
		};
		slot.setBackgroundIcon(Items.water_bucket.getIconFromDamage(0));
		container.addSlotToContainer(slot);
		container.addSlotToContainer(container.createOutputSlot(2, 129, 35));
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
					new ItemStack(KekCraft.factory.getItem("DustIronOxide"), 2)));

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIScreen(ui, "MainScreen") {
						@Override
						public void render(MachineTileEntity m, Object... args) {
							BlockOxidizerTileEntity e = (BlockOxidizerTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight
									- ((int) e.getEnergy().getMaxEnergyStored() - (int) e.getEnergy().getEnergyStored())
											* barHeight / (int) e.getEnergy().getMaxEnergyStored());
							drawUV(ui.left + 8, ui.top + 5 + (barHeight - targetHeight), 176, barHeight - targetHeight,
									barWidth, targetHeight);
							drawTooltip(ui.left + 8, ui.top + 5, barWidth, barHeight,
									(int) e.getEnergy().getEnergyStored() + " DJ");

							int arrowWidth = 36;
							int arrowHeight = 16;
							if (e.getCurrentCookTime() > 0) {
								int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth
										/ (double) e.getCookTime()) + 1);
								if (e.getCurrentCookTime() == 0) {
									width = 0;
								}
								drawUV(ui.left + 78, ui.top + 34, 176, 74, width, arrowHeight);
								drawTooltip(ui.left + 78, ui.top + 34, arrowWidth, arrowHeight,
										(int) (Math.abs(e.getCurrentCookTime() - 200) / (double) e.getCookTime())
												+ "%");
							} else {
								drawTooltip(ui.left + 78, ui.top + 34, arrowWidth, arrowHeight, "0%");
							}
						}
					}.addScreenSwitch(22, 0, 23, 23, "Options"));
					ui.addScreen(new UIScreen(ui, "Options") {
						@Override
						public void render(MachineTileEntity e, Object... args) {
						}
					}.addScreenSwitch(0, 0, 23, 23, "MainScreen"));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}

		@Override
		protected void onItemSmelted(IMachineRecipe item) {
			slots[1] = new ItemStack(Items.bucket);
		}
	}
}
