package com.craftsharp.blocks;

import com.craftsharp.DictionaryHandler;
import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockSilicon extends KekCraftBlock {
	public BlockSilicon() {
		super(Material.glass, "BlockSilicon");
		DictionaryHandler.register("blockSilicon", this);
		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("BlockSilicon")),
						"AAA", "AAA", "AAA", 'A', "ingotSilicon"));
			}
		});
		RecipeHandler.RECIPES.add(
				new ShapelessOreRecipe(new ItemStack(CraftSharp.factory.getItem("RefinedSilicon"), 9), "blockSilicon"));

		setHardness(2f);
		setResistance(0f);
		setHarvestLevel("pickaxe", KekCraftBlock.STONE);
	}
}
