package com.kekcraft.blocks;

import com.kekcraft.DictionaryHandler;

public class BlockOreAluminum extends KekCraftBlockOre {
	public BlockOreAluminum() {
		super("OreAluminum", 8, 20, 0, 64, KekCraftBlock.STONE);
		DictionaryHandler.register("oreAluminum", this);

		setHardness(2f);
		setResistance(15f);
	}
}
