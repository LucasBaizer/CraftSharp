package kekcraft;

import static net.minecraft.init.Blocks.cobblestone;
import static net.minecraft.init.Blocks.stone;
import static net.minecraft.init.Items.coal;
import static net.minecraft.init.Items.iron_ingot;
import static net.minecraft.init.Items.stick;

import cpw.mods.fml.common.registry.GameRegistry;
import kekcraft.api.GameFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeHandler {
	public static void initialize(GameFactory factory) {
		Item ironDust = factory.getItem("DustIron");
		Item aluminumDust = factory.getItem("DustAluminum");
		Item steelDust = factory.getItem("DustSteel");
		Item steelIngot = factory.getItem("IngotSteel");
		Item aluminumIngot = factory.getItem("IngotAluminum");
		Item machineCore = factory.getItem("MachineCore");
		Item thermite = factory.getItem("DustThermite");
		Item ironGear = factory.getItem("GearIron");
		Block aluminumOre = factory.getBlock("OreAluminum");

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ironGear), " A ", "ABA", " A ", 'A', iron_ingot, 'B',
				"ingotAluminum"));
		GameRegistry.addRecipe(
				new ShapelessOreRecipe(new ItemStack(steelDust, 1), coal, coal, coal, coal, "dustIron", "dustIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(machineCore), " A ", "A A", " A ", 'A', "ingotSteel"));
		GameRegistry.addRecipe(new ItemStack(factory.getBlock("ElectricFurnace")), "ABA", "BCB", "ABA", 'A', iron_ingot,
				'B', stone, 'C', machineCore);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(factory.getBlock("CrankEngine")), " A ", "BCB", "BBB",
				'A', stick, 'B', cobblestone, 'C', "gearIron"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(thermite, 4), "dustIronOxide", "dustIronOxide",
				"dustIronOxide", "dustAluminum", "dustMagnesium"));

		GameRegistry.addSmelting(steelDust, new ItemStack(steelIngot), 0);
		GameRegistry.addSmelting(ironDust, new ItemStack(iron_ingot), 0);
		GameRegistry.addSmelting(aluminumOre, new ItemStack(aluminumIngot), 0);
		GameRegistry.addSmelting(aluminumDust, new ItemStack(aluminumIngot), 0);
	}
}
