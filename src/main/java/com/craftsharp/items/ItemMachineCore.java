package com.craftsharp.items;

import com.craftsharp.RecipeHandler;

import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemMachineCore extends CraftSharpItem {
	public ItemMachineCore() {
		super("MachineCore");
		RecipeHandler.RECIPES.add(new ShapedOreRecipe(this, " A ", "A A", " A ", 'A', "ingotSteel"));
	}
}
