package com.kekcraft;

import static net.minecraft.init.Items.iron_ingot;

import java.util.ArrayList;

import com.kekcraft.api.GameFactory;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class RecipeHandler {
	public static final ArrayList<IRecipe> RECIPES = new ArrayList<IRecipe>();
	public static final ArrayList<Runnable> FUTURES = new ArrayList<Runnable>();

	public static void initialize(GameFactory factory) {
		Item ironDust = factory.getItem("DustIron");
		Item aluminumDust = factory.getItem("DustAluminum");
		Item steelDust = factory.getItem("DustSteel");
		Item steelIngot = factory.getItem("IngotSteel");
		Item aluminumIngot = factory.getItem("IngotAluminum");
		Block aluminumOre = factory.getBlock("OreAluminum");

		GameRegistry.addSmelting(steelDust, new ItemStack(steelIngot), 1);
		GameRegistry.addSmelting(ironDust, new ItemStack(iron_ingot), 1);
		GameRegistry.addSmelting(aluminumOre, new ItemStack(aluminumIngot), 1);
		GameRegistry.addSmelting(aluminumDust, new ItemStack(aluminumIngot), 1);

		for (IRecipe recipe : RECIPES) {
			GameRegistry.addRecipe(recipe);
		}
		for (Runnable future : FUTURES) {
			future.run();
		}
	}
}
