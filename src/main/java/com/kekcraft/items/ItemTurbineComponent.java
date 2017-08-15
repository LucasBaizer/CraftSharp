package com.kekcraft.items;

import com.kekcraft.RecipeHandler;

import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemTurbineComponent extends KekCraftItem {
	public ItemTurbineComponent() {
		super("TurbineComponent");
		RecipeHandler.RECIPES.add(new ShapedOreRecipe(this, " A ", "ABA", " A ", 'A', "gearSteel", 'B', "rodSteel"));
	}
}
