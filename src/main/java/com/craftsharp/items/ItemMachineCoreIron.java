package com.craftsharp.items;

import com.craftsharp.RecipeHandler;

import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemMachineCoreIron extends KekCraftItem {
	public ItemMachineCoreIron() {
		super("MachineCoreIron");
		RecipeHandler.RECIPES
				.add(new ShapedOreRecipe(this, " A ", "ABA", " A ", 'A', "ingotSteel", 'B', "circuitIron"));
	}
}
