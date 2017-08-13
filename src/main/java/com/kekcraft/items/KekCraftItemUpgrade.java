package com.kekcraft.items;

import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;
import com.kekcraft.api.ui.MachineUpgrade;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public abstract class KekCraftItemUpgrade extends KekCraftItem {
	public KekCraftItemUpgrade(final String name, final Item recipeItem) {
		super(name);

		setMaxStackSize(8);
		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KekCraft.factory.getItem(name)), " A ", "ABA",
						" A ", 'A', "ingotSteel", 'B', recipeItem));
			}
		});
	}

	public abstract MachineUpgrade getUpgrade();
}
