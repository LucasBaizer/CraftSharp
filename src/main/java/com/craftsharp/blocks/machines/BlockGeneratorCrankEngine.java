package com.craftsharp.blocks.machines;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

import java.awt.Dimension;

import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockGeneratorCrankEngine extends Generator {
	private IIcon topIcon;
	private IIcon sideIcon;

	public BlockGeneratorCrankEngine(GameFactory factory) {
		super(Material.rock, CraftSharp.modInstance, 1, "CrankEngine");

		factory.initializeBlock(this, "Crank Engine", "CrankEngine", Tabs.DEFAULT, "CrankEngine");
		setWindowDimensions(new Dimension(-1, 111));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("CrankEngine")),
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
			tile.energy.modifyEnergyStored(25);
		}
		return true;
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockGeneratorCrankEngineTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);
	}

	public static class BlockGeneratorCrankEngineTileEntity extends GeneratorTileEntity {
		public BlockGeneratorCrankEngineTileEntity() {
			super(0, -1);

			setItemSlots(new int[0]);
			setOutputSlots(new int[0]);

			energy.setCapacity(10000);
			energy.setMaxTransfer(128);
			energy.setEnergyStored(0);

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIMainScreen(ui) {
						@Override
						public void render(MachineTileEntity m) {
							BlockGeneratorCrankEngineTileEntity e = (BlockGeneratorCrankEngineTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight - (e.energy.getMaxEnergyStored() - e.energy.getEnergyStored())
									* barHeight / (int) e.energy.getMaxEnergyStored());
							drawUV(ui.left + 85, ui.top + 29 + (barHeight - targetHeight), 176,
									23 + barHeight - targetHeight, barWidth, targetHeight);
							drawTooltip(ui.left + 85, ui.top + 29, barWidth, barHeight,
									e.energy.getEnergyStored() + " RF");
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
