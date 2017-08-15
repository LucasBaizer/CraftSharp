package com.craftsharp.blocks;

import com.craftsharp.DictionaryHandler;

public class BlockOreAluminum extends CraftSharpBlockOre {
	public BlockOreAluminum() {
		super("OreAluminum", 8, 20, 0, 64, CraftSharpBlock.STONE);
		DictionaryHandler.register("oreAluminum", this);

		setHardness(2f);
		setResistance(15f);
	}
}
