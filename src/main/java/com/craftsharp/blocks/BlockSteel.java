package com.craftsharp.blocks;

import com.craftsharp.CraftSharp;
import com.craftsharp.DictionaryHandler;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockSteel extends CraftSharpBlock {
	public BlockSteel() {
		super(Material.iron, "BlockSteel");
		DictionaryHandler.register("blockSteel", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("BlockSteel")),
						"AAA", "AAA", "AAA", 'A', "ingotSteel"));
			}
		});
		RecipeHandler.RECIPES
				.add(new ShapelessOreRecipe(new ItemStack(CraftSharp.factory.getItem("IngotSteel"), 9), "blockSteel"));

		setHardness(5f);
		setResistance(1000f);
		setHarvestLevel("pickaxe", CraftSharpBlock.IRON);
	}
}
