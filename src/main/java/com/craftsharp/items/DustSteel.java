package com.craftsharp.items;

import static net.minecraft.init.Items.coal;

import com.craftsharp.DictionaryHandler;
import com.craftsharp.CraftSharp;
import com.craftsharp.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class DustSteel extends CraftSharpItem {
	public DustSteel() {
		super("DustSteel");
		DictionaryHandler.register("dustSteel", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CraftSharp.factory.getItem("DustSteel")),
						coal, coal, coal, coal, "dustIron", "dustIron"));
			}
		});
	}
}
