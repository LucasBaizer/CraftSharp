package kekcraft.blocks;

import java.util.Random;

import kekcraft.KekCraft;
import kekcraft.Tabs;
import kekcraft.api.GameFactory;
import net.minecraft.item.Item;

public class BlockOreMagnesium extends KekCraftBlockOre {
	public BlockOreMagnesium(GameFactory factory) {
		super(8, 14, 0, 64, STONE);

		setHardness(1.5f);
		setResistance(15f);

		factory.initializeBlock(this, "Magnesium Ore", "OreMagnesium", Tabs.BLOCKS, KekCraft.MODID + ":OreMagnesium");
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return KekCraft.factory.getItem("DustMagnesium");
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
