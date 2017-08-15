package com.craftsharp.blocks;

import java.util.Random;

import com.craftsharp.DictionaryHandler;
import com.craftsharp.CraftSharp;

import net.minecraft.item.Item;

public class BlockOreMagnesium extends KekCraftBlockOre {
	public BlockOreMagnesium() {
		super("OreMagnesium", 8, 16, 0, 64, KekCraftBlock.STONE);
		DictionaryHandler.register("oreMagnesium", this);

		setHardness(1.5f);
		setResistance(15f);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return CraftSharp.factory.getItem("DustMagnesium");
	}

	@Override
	public int damageDropped(int meta) {
		return 0;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 1;
	}
}
