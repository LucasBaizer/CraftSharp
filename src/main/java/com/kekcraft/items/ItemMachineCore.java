package com.kekcraft.items;

import com.kekcraft.RecipeHandler;

import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemMachineCore extends KekCraftItem {
	public ItemMachineCore() {
		super("MachineCore");
		RecipeHandler.RECIPES.add(new ShapedOreRecipe(this, " A ", "A A", " A ", 'A', "ingotSteel"));
	}
}
