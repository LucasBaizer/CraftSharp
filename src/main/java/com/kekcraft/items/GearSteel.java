package com.kekcraft.items;

import com.kekcraft.DictionaryHandler;
import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GearSteel extends KekCraftItem {
	public GearSteel() {
		super("GearSteel");
		DictionaryHandler.register("gearSteel", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(KekCraft.factory.getItem("GearSteel"), " A ", "ABA", " A ",
						'A', "ingotSteel", 'B', "gearIron"));
			}
		});
	}
}
