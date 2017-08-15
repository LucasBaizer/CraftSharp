package com.kekcraft.blocks.machines;

import java.awt.Dimension;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.Machine;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockUncrafter extends Machine {
	public BlockUncrafter(GameFactory factory) {
		super(Material.wood, KekCraft.modInstance, 7, "Uncrafter");

		factory.initializeBlock(this, "Uncrafter", "Uncrafter", Tabs.DEFAULT, "Uncrafter");

		setWindowDimensions(new Dimension(-1, 166));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ItemStack(KekCraft.factory.getBlock("Uncrafter")), "AA", "AA", 'A',
						Blocks.log);
			}
		});

		initializeSpecialIcons();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockUncrafterTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.drawMinecraftInventory(8, 84);
		container.addSlotToContainer(container.createSlot(0, 30, 35));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				container.addSlotToContainer(container.createOutputSlot((i * 3) + j + 1, 92 + (i * 16), 17 + (i * 16)));
			}
		}
	}

	public class BlockUncrafterTileEntity extends MachineTileEntity {
		public BlockUncrafterTileEntity() {
			super(10, -1);

			for (Object obj : CraftingManager.getInstance().getRecipeList()) {
				if (obj instanceof ShapedOreRecipe) {
					ShapedOreRecipe recipe = (ShapedOreRecipe) obj;
					addRecipe(new UncrafterRecipe(recipe.getRecipeOutput(), recipe.getInput()));
				} else if (obj instanceof ShapelessOreRecipe) {
					ShapelessOreRecipe recipe = (ShapelessOreRecipe) obj;
					addRecipe(new UncrafterRecipe(recipe.getRecipeOutput(), recipe.getInput()));
				} else {
					System.out.println("Cannot uncraft: " + obj.getClass().getSimpleName());
				}
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound tagCompound) {
			super.defaultWriteToNBT(tagCompound);
		}
	}
}
