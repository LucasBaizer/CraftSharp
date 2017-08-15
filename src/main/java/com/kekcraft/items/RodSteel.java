package com.kekcraft.items;

import com.kekcraft.DictionaryHandler;
import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;

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
						new ShapedOreRecipe(KekCraft.factory.getItem("RodSteel"), "A", "A", 'A', "ingotSteel"));
			}
		});
	}
}
