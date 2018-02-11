package com.craftsharp.blocks.machines;

import static net.minecraft.init.Items.*;

import java.awt.Dimension;

import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
import com.craftsharp.api.ParticleColor;
import com.craftsharp.api.ui.FaceType;
import com.craftsharp.api.ui.FuelMachine;
import com.craftsharp.api.ui.FuelMachineFuel;
import com.craftsharp.api.ui.FuelMachineTileEntity;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockHighTemperatureAlloyFurnace extends FuelMachine {
	public BlockHighTemperatureAlloyFurnace(GameFactory factory) {
		super(Material.rock, CraftSharp.modInstance, 4, "HighTemperatureAlloyFurnace");

		factory.initializeBlock(this, "High-Temperature Alloy Furnace", "HighTemperatureAlloyFurnace", Tabs.DEFAULT,
				"HighTemperatureAlloyFurnace");
		setWindowDimensions(new Dimension(-1, 189));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(
						new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("HighTemperatureAlloyFurnace")),
								"AAA", "BCB", "DED", 'A', lava_bucket, 'B', "gearIron", 'C',
								CraftSharp.factory.getItem("MachineCore"), 'D', "ingotSteel", 'E', "blockSteel"));
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
				return item.getItem() == CraftSharp.factory.getItem("DustThermite");
			}
		};
		if (container.getWorld().isRemote)
			slot.setBackgroundIcon(CraftSharp.factory.getItem("DustThermite").getIconFromDamage(0));
		container.addSlotToContainer(slot);
		container.addSlotToContainer(container.createOutputSlot(3, 135, 58));
		container.addSlotToContainer(container.createUpgradeSlot(4, 96, 58));
	}

	public static class BlockHighTemperatureAlloyFurnaceTileEntity extends FuelMachineTileEntity {
		public BlockHighTemperatureAlloyFurnaceTileEntity() {
			super(5, 5);

			setItemSlots(new int[] { 0, 1, 4 });
			setFuelSlots(new int[] { 2 });
			setOutputSlots(new int[] { 3 });
			setUpgradeSlots(new int[] { 4 });

			addFuel(new FuelMachineFuel(new ItemStack(CraftSharp.factory.getItem("DustThermite")), 200, 2));
			addRecipe(new HighTemperatureAlloyFurnaceRecipe(new ItemStack(Blocks.sand),
					new ItemStack(CraftSharp.factory.getItem("DustMagnesium")),
					new ItemStack(CraftSharp.factory.getItem("Silicon"))));
			addRecipe(new HighTemperatureAlloyFurnaceRecipe(new ItemStack(CraftSharp.factory.getItem("Silicon")),
					new ItemStack(CraftSharp.factory.getItem("Silicon")),
					new ItemStack(CraftSharp.factory.getItem("RefinedSilicon"), 2)));
			addRecipe(new HighTemperatureAlloyFurnaceRecipe(new ItemStack(CraftSharp.factory.getItem("Thermite"), 8),
					new ItemStack(Items.coal, 64), new ItemStack(Items.diamond)) {
				private static final long serialVersionUID = -4148558903737591963L;

				@Override
				public int getCookTime() {
					return 800;
				}
			});

			setChangeMeta(true);
			setValidUpgrades(new MachineUpgrade[] { MachineUpgrade.SPEED });

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIMainScreen(ui) {
						@Override
						public void render(MachineTileEntity m) {
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
								drawTooltip(ui.left + 80, ui.top + 57, arrowWidth, arrowHeight, 100
										- ((int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * 100)) + "%");
							} else {
								drawTooltip(ui.left + 80, ui.top + 57, arrowWidth, arrowHeight, "0%");
							}
						}
					});
					ui.addScreen(new UIOptionsScreen(ui, FaceType.NONE, FaceType.ITEM));
					ui.addScreen(new UIUpgradesScreen(ui));
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
