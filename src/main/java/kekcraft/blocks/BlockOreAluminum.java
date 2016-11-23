package kekcraft.blocks;

import kekcraft.KekCraft;
import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class BlockOreAluminum extends KekCraftBlockOre {
	public BlockOreAluminum(GameFactory factory) {
		super(8, 20, 0, 64, STONE);

		setHardness(2f);
		setResistance(15f);

		factory.initializeBlock(this, "Aluminum Ore", "OreAluminum", Tabs.BLOCKS, KekCraft.MODID + ":OreAluminum");
	}
}
