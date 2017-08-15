package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;
import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GearSteel extends CraftSharpItem {
	public GearSteel() {
		super("GearSteel");
		DictionaryHandler.register("gearSteel", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(CraftSharp.factory.getItem("GearSteel"), " A ", "ABA", " A ",
						'A', "ingotSteel", 'B', "gearIron"));
			}
		});
	}
}
