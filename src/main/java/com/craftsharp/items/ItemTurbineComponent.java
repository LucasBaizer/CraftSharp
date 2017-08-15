package com.craftsharp.items;

import com.craftsharp.RecipeHandler;

import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemTurbineComponent extends CraftSharpItem {
	public ItemTurbineComponent() {
		super("TurbineComponent");
		RecipeHandler.RECIPES.add(new ShapedOreRecipe(this, " A ", "ABA", " A ", 'A', "gearSteel", 'B', "rodSteel"));
	}
}
