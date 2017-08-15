package com.craftsharp.items;

import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class DustThermite extends CraftSharpItem {
	public DustThermite() {
		super("DustThermite");
		setBurnTime(20000);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry
						.addRecipe(new ShapelessOreRecipe(new ItemStack(CraftSharp.factory.getItem("DustThermite"), 4),
								"dustIronOxide", "dustIronOxide", "dustIronOxide", "dustAluminum", "dustMagnesium"));
			}
		});
	}
}
