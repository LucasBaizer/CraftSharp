package com.craftsharp.items;

import static net.minecraft.init.Items.iron_ingot;

import com.craftsharp.DictionaryHandler;
import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GearIron extends CraftSharpItem {
	public GearIron() {
		super("GearIron");
		DictionaryHandler.register("gearIron", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(CraftSharp.factory.getItem("GearIron"), " A ", "ABA", " A ",
						'A', iron_ingot, 'B', "ingotAluminum"));
			}
		});
	}
}
