package com.kekcraft;

import static net.minecraft.init.Blocks.cobblestone;
import static net.minecraft.init.Blocks.stone;
import static net.minecraft.init.Items.*;

import java.util.ArrayList;

import com.kekcraft.api.GameFactory;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeHandler {
	public static ArrayList<IRecipe> RECIPES = new ArrayList<IRecipe>();

	public static void initialize(GameFactory factory) {
		Item ironDust = factory.getItem("DustIron");
		Item aluminumDust = factory.getItem("DustAluminum");
		Item steelDust = factory.getItem("DustSteel");
		Item steelIngot = factory.getItem("IngotSteel");
		Item aluminumIngot = factory.getItem("IngotAluminum");
		Item machineCore = factory.getItem("MachineCore");
		Item thermite = factory.getItem("DustThermite");
		Block aluminumOre = factory.getBlock("OreAluminum");

		GameRegistry.addRecipe(
				new ShapelessOreRecipe(new ItemStack(steelDust), coal, coal, coal, coal, "dustIron", "dustIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(factory.getItem("GearIron"), " A ", "ABA", " A ", 'A', iron_ingot,
				'B', "ingotAluminum"));
		GameRegistry.addRecipe(new ItemStack(factory.getBlock("ElectricFurnace")), "ABA", "BCB", "ABA", 'A', iron_ingot,
				'B', stone, 'C', machineCore);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(factory.getBlock("Oxidizer")), " A ", "BCB", "DED",
				'A', water_bucket, 'B', iron_ingot, 'C', machineCore, 'D', "gearIron", 'E', "ingotAluminum"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(factory.getBlock("ManualCrusher")), " A ", "B B",
				"CCC", 'A', stick, 'B', "gearIron", 'C', iron_ingot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(factory.getBlock("CrankEngine")), " A ", "BCB", "BBB",
				'A', stick, 'B', cobblestone, 'C', "gearIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(factory.getBlock("HighTemperatureAlloyFurnace")),
				"AAA", "BCB", "DDD", 'A', lava_bucket, 'B', "gearIron", 'C', machineCore, 'D', iron_ingot));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(thermite, 4), "dustIronOxide", "dustIronOxide",
				"dustIronOxide", "dustAluminum", "dustMagnesium"));

		GameRegistry.addSmelting(steelDust, new ItemStack(steelIngot), 1);
		GameRegistry.addSmelting(ironDust, new ItemStack(iron_ingot), 1);
		GameRegistry.addSmelting(aluminumOre, new ItemStack(aluminumIngot), 1);
		GameRegistry.addSmelting(aluminumDust, new ItemStack(aluminumIngot), 1);

		for (IRecipe recipe : RECIPES) {
			GameRegistry.addRecipe(recipe);
		}
	}
}
