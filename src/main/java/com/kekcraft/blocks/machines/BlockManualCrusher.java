package com.kekcraft.blocks.machines;

import static net.minecraft.init.Items.*;

import java.awt.Dimension;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.DefaultMachineRecipe;
import com.kekcraft.api.ui.ElectricMachine;
import com.kekcraft.api.ui.ElectricMachineTileEntity;
import com.kekcraft.api.ui.MachineContainer;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockManualCrusher extends ElectricMachine {
	private IIcon topIcon;

	public BlockManualCrusher(GameFactory factory) {
		super(Material.glass, KekCraft.modInstance, 3, "ManualCrusher");
		factory.initializeBlock(this, "Manual Crusher", "ManualCrusher", Tabs.DEFAULT, "ManualCrusher");
		setWindowDimensions(new Dimension(-1, 189));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KekCraft.factory.getBlock("ManualCrusher")),
						" A ", "B B", "CCC", 'A', stick, 'B', "gearIron", 'C', iron_ingot));
			}
		});
		initializeSpecialIcons();
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);

		this.topIcon = reg.registerIcon(this.textureName + "_top");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? topIcon : super.getIcon(side, meta);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

		if (player.isSneaking()) {
			BlockManualCrusherTileEntity tile = (BlockManualCrusherTileEntity) world.getTileEntity(x, y, z);
			if (tile.getStackInSlot(0) != null) {
				tile.energy.modifyEnergyStored(5);
			}
		}
		return true;
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockManualCrusherTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);

		container.drawMinecraftInventory(8, 107);
		container.addSlotToContainer(container.createSlot(0, 50, 58));
		container.addSlotToContainer(container.createOutputSlot(1, 110, 58));
	}

	public static class BlockManualCrusherTileEntity extends ElectricMachineTileEntity {
		public BlockManualCrusherTileEntity() {
			super(2, 100 / 10);

			energy.setCapacity(100);
			energy.setMaxTransfer(0);
			energy.setEnergyStored(0);

			setItemSlots(new int[] { 0 });
			setOutputSlots(new int[] { 1 });

			addRecipe(new DefaultMachineRecipe(new ItemStack(Items.iron_ingot),
					new ItemStack(KekCraft.factory.getItem("DustIron")), -1, 100, 0));
			addRecipe(new DefaultMachineRecipe(new ItemStack(KekCraft.factory.getItem("IngotAluminum")),
					new ItemStack(KekCraft.factory.getItem("DustAluminum")), -1, 100, 0));
			addRecipe(new DefaultMachineRecipe(new ItemStack(KekCraft.factory.getItem("IngotSteel")),
					new ItemStack(KekCraft.factory.getItem("DustSteel")), -1, 100, 0));
		}

		@Override
		public void onInputSlotExhausted(int slot) {
			energy.setEnergyStored(0);
		}
	}
}
