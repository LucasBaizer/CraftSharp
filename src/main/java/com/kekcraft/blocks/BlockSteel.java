package com.kekcraft.blocks;

import com.kekcraft.DictionaryHandler;
import com.kekcraft.KekCraft;
import com.kekcraft.RecipeHandler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockSteel extends KekCraftBlock {
	public BlockSteel() {
		super(Material.iron, "BlockSteel");
		DictionaryHandler.register("blockSteel", this);

		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KekCraft.factory.getBlock("BlockSteel")),
						"AAA", "AAA", "AAA", 'A', "ingotSteel"));
			}
		});
		RecipeHandler.RECIPES
				.add(new ShapelessOreRecipe(new ItemStack(KekCraft.factory.getItem("IngotSteel"), 9), "blockSteel"));

		setHardness(5f);
		setResistance(1000f);
		setHarvestLevel("pickaxe", KekCraftBlock.IRON);
	}
}
