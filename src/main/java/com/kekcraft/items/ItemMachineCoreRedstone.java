package com.kekcraft.items;

import com.kekcraft.RecipeHandler;

import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemMachineCoreRedstone extends KekCraftItem {
	public ItemMachineCoreRedstone() {
		super("MachineCoreRedstone");
		RecipeHandler.RECIPES
				.add(new ShapedOreRecipe(this, " A ", "ABA", " A ", 'A', "ingotSteel", 'B', "circuitRedstone"));
	}
}
