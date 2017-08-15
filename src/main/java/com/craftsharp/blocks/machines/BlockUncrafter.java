package com.craftsharp.blocks.machines;

import java.awt.Dimension;

import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
import com.craftsharp.api.ui.Machine;
import com.craftsharp.api.ui.MachineContainer;
import com.craftsharp.api.ui.MachineTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockUncrafter extends Machine {
	private IIcon xSide;
	private IIcon zSide;
	private IIcon top;
	private IIcon bottom;

	public BlockUncrafter(GameFactory factory) {
		super(Material.wood, CraftSharp.modInstance, 7, "Uncrafter");

		factory.initializeBlock(this, "Uncrafter", "Uncrafter", Tabs.DEFAULT, "Uncrafter");

		setWindowDimensions(new Dimension(-1, 166));

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 4; i++) {
					GameRegistry.addRecipe(new ItemStack(CraftSharp.factory.getBlock("Uncrafter")), "AA", "AA", 'A',
							new ItemStack(Blocks.log, 1, i));
				}
			}
		});
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		xSide = reg.registerIcon(this.textureName + "_front");
		zSide = reg.registerIcon(this.textureName + "_side");
		top = reg.registerIcon(this.textureName + "_top");
		bottom = reg.registerIcon(this.textureName + "_bottom");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		switch (side) {
			case 0:
				return bottom;
			case 1:
				return top;
			case 2:
			case 4:
				return xSide;
			case 3:
			case 5:
				return zSide;
		}
		throw new RuntimeException("Invalid side: " + side);
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
				container.addSlotToContainer(container.createOutputSlot((i * 3) + j + 1, 92 + (j * 18), 53 - (i * 18)));
			}
		}
	}

	public static class BlockUncrafterTileEntity extends MachineTileEntity {
		@SuppressWarnings("unchecked")
		public BlockUncrafterTileEntity() {
			super(10, -1);

			setItemSlots(new int[] { 0 });
			setOutputSlots(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });

			for (Object obj : CraftingManager.getInstance().getRecipeList()) {
				if (obj instanceof ShapedOreRecipe) {
					ShapedOreRecipe recipe = (ShapedOreRecipe) obj;
					addRecipe(new UncrafterRecipe(recipe.getRecipeOutput(), recipe.getInput()));
				} else if (obj instanceof ShapelessOreRecipe) {
					ShapelessOreRecipe recipe = (ShapelessOreRecipe) obj;
					addRecipe(new UncrafterRecipe(recipe.getRecipeOutput(), recipe.getInput()));
				} else if (obj instanceof ShapedRecipes) {
					ShapedRecipes recipe = (ShapedRecipes) obj;
					addRecipe(new UncrafterRecipe(recipe.getRecipeOutput(), recipe.recipeItems));
				} else if (obj instanceof ShapelessRecipes) {
					ShapelessRecipes recipe = (ShapelessRecipes) obj;
					addRecipe(new UncrafterRecipe(recipe.getRecipeOutput(), recipe.recipeItems));
				}
			}

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.allow.add(Slot.class);
				}
			};
		}

		@Override
		public void updateEntity() {
			super.updateEntity();

			if (!this.worldObj.isRemote) {
				if (isReady()) {
					UncrafterRecipe recipe = (UncrafterRecipe) getNextRecipe();
					if (recipe != null) {
						for (ItemStack output : recipe.getOutput()) {
							if (output != null) {
								output.stackSize = 1;
								boolean set = false;
								for (int i = 1; i < 10; i++) {
									if (slots[i] != null && slots[i].getItem() == output.getItem()) {
										slots[i].stackSize++;
										set = true;
										break;
									}
								}
								if (!set) {
									for (int i = 1; i < 10; i++) {
										if (slots[i] == null) {
											slots[i] = new ItemStack(output.getItem(), 1);
											break;
										}
									}
								}
							}
						}
						slots[0] = null;
					}
				}
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound tagCompound) {
			super.defaultWriteToNBT(tagCompound);
		}
	}
}
