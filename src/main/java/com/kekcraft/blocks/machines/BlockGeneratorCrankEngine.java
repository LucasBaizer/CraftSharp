package com.kekcraft.blocks.machines;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

import java.awt.Dimension;

import com.kekcraft.KekCraft;
import com.kekcraft.ModPacket;
import com.kekcraft.RecipeHandler;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.ElectricMachineTileEntity;
import com.kekcraft.api.ui.Generator;
import com.kekcraft.api.ui.GeneratorTileEntity;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.UIScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockGeneratorCrankEngine extends Generator {
	private IIcon topIcon;
	private IIcon sideIcon;

	public BlockGeneratorCrankEngine(GameFactory factory) {
		super(Material.glass, KekCraft.modInstance, 1, "CrankEngine");

		factory.initializeBlock(this, "Crank Engine", "CrankEngine", Tabs.DEFAULT, "CrankEngine");
		setWindowDimensions(new Dimension(-1, 88));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KekCraft.factory.getBlock("CrankEngine")),
						" A ", "BCB", "BBB", 'A', stick, 'B', cobblestone, 'C', "gearIron"));
			}
		});
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		this.topIcon = reg.registerIcon(this.textureName + "_top");
		this.sideIcon = reg.registerIcon(this.textureName + "_side");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? topIcon : sideIcon;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

		if (player.isSneaking()) {
			BlockGeneratorCrankEngineTileEntity tile = (BlockGeneratorCrankEngineTileEntity) world.getTileEntity(x, y,
					z);
			tile.getEnergy().modifyEnergyStored(25);
			ModPacket.sendTileEntityUpdate(tile);
		}
		return true;
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockGeneratorCrankEngineTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
	}

	public static class BlockGeneratorCrankEngineTileEntity extends GeneratorTileEntity {
		public BlockGeneratorCrankEngineTileEntity() {
			super(0);

			setItemSlots(new int[0]);
			setOutputSlots(new int[0]);

			getEnergy().setCapacity(10000);
			getEnergy().setMaxTransfer(128);
			getEnergy().setEnergyStored(0);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIScreen(ui, "MainScreen") {
						@Override
						public void render(MachineTileEntity m, Object... args) {
							ElectricMachineTileEntity e = (ElectricMachineTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight
									- ((int) e.getEnergy().getMaxEnergyStored() - (int) e.getEnergy().getEnergyStored())
											* barHeight / (int) e.getEnergy().getMaxEnergyStored());
							drawUV(ui.left + 85, ui.top + 6 + (barHeight - targetHeight), 176, barHeight - targetHeight,
									barWidth, targetHeight);
							drawTooltip(ui.left + 85, ui.top + 6, barWidth, barHeight,
									e.getEnergy().getEnergyStored() + " RF");
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
	}
}
