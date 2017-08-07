package com.kekcraft.items;

import static net.minecraft.init.Items.iron_ingot;

import com.kekcraft.DictionaryHandler;
import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GearIron extends KekCraftItem {
	public GearIron() {
		super("GearIron");
		DictionaryHandler.register("gearIron", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(KekCraft.factory.getItem("GearIron"), " A ", "ABA", " A ",
						'A', iron_ingot, 'B', "ingotAluminum"));
			}
		});
	}
}
