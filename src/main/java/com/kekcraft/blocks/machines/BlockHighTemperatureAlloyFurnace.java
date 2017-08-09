package com.kekcraft.blocks.machines;

import static net.minecraft.init.Items.lava_bucket;

import java.awt.Dimension;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ParticleColor;
import com.kekcraft.api.ui.FaceType;
import com.kekcraft.api.ui.FuelMachine;
import com.kekcraft.api.ui.FuelMachineFuel;
import com.kekcraft.api.ui.FuelMachineTileEntity;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.UIOptionsScreen;
import com.kekcraft.api.ui.UIScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockHighTemperatureAlloyFurnace extends FuelMachine {
	public BlockHighTemperatureAlloyFurnace(GameFactory factory) {
		super(Material.rock, KekCraft.modInstance, 4, "HighTemperatureAlloyFurnace");

		factory.initializeBlock(this, "High-Temperature Alloy Furnace", "HighTemperatureAlloyFurnace", Tabs.DEFAULT,
				"HighTemperatureAlloyFurnace");
		setWindowDimensions(new Dimension(-1, 189));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(
						new ItemStack(KekCraft.factory.getBlock("HighTemperatureAlloyFurnace")), "AAA", "BCB", "DED",
						'A', lava_bucket, 'B', "gearIron", 'C', KekCraft.factory.getItem("MachineCore"), 'D',
						KekCraft.factory.getItem("IngotSteel"), 'E', KekCraft.factory.getBlock("BlockSteel")));
			}
		});

		initializeSpecialIcons();
		setParticleColor(ParticleColor.RED);
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

		Slot slot = new Slot(container.getTileEntity(), 2, 27, 58 - 12) {
			@Override
			public boolean isItemValid(ItemStack item) {
				return item.getItem() == KekCraft.factory.getItem("DustThermite");
			}
		};
		slot.setBackgroundIcon(KekCraft.factory.getItem("DustThermite").getIconFromDamage(0));
		container.addSlotToContainer(slot);
		container.addSlotToContainer(container.createOutputSlot(3, 135, 58));
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
					new ItemStack(KekCraft.factory.getItem("RefinedSilicon"), 2), -1));

			setChangeMeta(true);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIScreen(ui, "MainScreen") {
						@Override
						public void render(MachineTileEntity m, Object... args) {
							int flameWidth = 13;
							int flameHeight = 13;
							int arrowWidth = 41;
							int arrowHeight = 16;

							BlockHighTemperatureAlloyFurnaceTileEntity e = (BlockHighTemperatureAlloyFurnaceTileEntity) m;
							if (e.getCurrentBurnTime() > 0) {
								int height = (int) ((e.getCurrentBurnTime() / (double) e.getBurnTime()) * flameHeight);

								drawUV(ui.left + 8, ui.top + 59 + (flameHeight - height), 176,
										23 + (flameHeight - height), flameWidth, height);
								drawTooltip(ui.left + 8, ui.top + 59, flameWidth, flameHeight,
										(int) ((e.getCurrentBurnTime() / (double) e.getBurnTime()) * 100) + "%");
							}
							if (e.getCurrentCookTime() > 0) {
								int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth
										/ (double) e.getCookTime()) + 1);
								if (e.getCurrentCookTime() == 0) {
									width = 0;
								}
								drawUV(ui.left + 80, ui.top + 57, 176, 37, width, arrowHeight);
								drawTooltip(ui.left + 80, ui.top + 57, arrowWidth, arrowHeight,
										(int) ((Math.abs(e.getCurrentCookTime() - 200) / (double) e.getCookTime())
												* 100) + "%");
							} else {
								drawTooltip(ui.left + 80, ui.top + 57, arrowWidth, arrowHeight, "0%");
							}
						}
					}.addScreenSwitch(22, 0, 23, 23, "Options"));
					ui.addScreen(new UIOptionsScreen(ui, FaceType.NONE, FaceType.ENERGY, FaceType.ITEM));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}

		@Override
		public void writeToNBT(NBTTagCompound tagCompound) {
			super.defaultWriteToNBT(tagCompound);
		}
	}
}
