package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;
import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RodSteel extends KekCraftItem {
	public RodSteel() {
		super("RodSteel");
		DictionaryHandler.register("rodSteel", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(
						new ShapedOreRecipe(CraftSharp.factory.getItem("RodSteel"), "A", "A", 'A', "ingotSteel"));
			}
		});
	}
}
