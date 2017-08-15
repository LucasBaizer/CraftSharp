package com.craftsharp.blocks;

import com.craftsharp.DictionaryHandler;
import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockAluminum extends CraftSharpBlock {
	public BlockAluminum() {
		super(Material.iron, "BlockAluminum");
		DictionaryHandler.register("blockAluminum", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CraftSharp.factory.getBlock("BlockAluminum")),
						"AAA", "AAA", "AAA", 'A', "ingotAluminum"));
			}
		});
		RecipeHandler.RECIPES
				.add(new ShapelessOreRecipe(new ItemStack(CraftSharp.factory.getItem("IngotSteel"), 9), "blockAluminum"));

		setHardness(2f);
		setResistance(15f);
		setHarvestLevel("pickaxe", CraftSharpBlock.STONE);
	}
}
