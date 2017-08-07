package com.kekcraft.items;

import static net.minecraft.init.Items.coal;

import com.kekcraft.DictionaryHandler;
import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class DustSteel extends KekCraftItem {
	public DustSteel() {
		super("DustSteel");
		DictionaryHandler.register("dustSteel", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(KekCraft.factory.getItem("DustSteel")),
						coal, coal, coal, coal, "dustIron", "dustIron"));
			}
		});
	}
}
