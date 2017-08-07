package com.kekcraft.items;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class DustThermite extends KekCraftItem {
	public DustThermite() {
		super("DustThermite");
		setBurnTime(20000);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry
						.addRecipe(new ShapelessOreRecipe(new ItemStack(KekCraft.factory.getItem("DustThermite"), 4),
								"dustIronOxide", "dustIronOxide", "dustIronOxide", "dustAluminum", "dustMagnesium"));
			}
		});
	}
}
