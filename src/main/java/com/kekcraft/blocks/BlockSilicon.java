package com.kekcraft.blocks;

import com.kekcraft.DictionaryHandler;
import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockSilicon extends KekCraftBlock {
	public BlockSilicon() {
		super(Material.glass, "BlockSilicon");
		DictionaryHandler.register("blockSilicon", this);
		RecipeHandler.RECIPES.add(new ShapedOreRecipe(this, "AAA", "AAA", "AAA", 'A', "ingotSilicon"));
		RecipeHandler.RECIPES.add(
				new ShapelessOreRecipe(new ItemStack(KekCraft.factory.getItem("ingotSilicon"), 9), "blockSilicon"));
	}
}
