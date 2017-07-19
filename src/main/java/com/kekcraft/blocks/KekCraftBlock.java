package com.kekcraft.blocks;

import java.util.ArrayList;
import java.util.List;

import com.kekcraft.KekCraft;
import com.kekcraft.Tabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class KekCraftBlock extends Block {
	public static final List<KekCraftBlock> BLOCKS = new ArrayList<KekCraftBlock>();

	public KekCraftBlock(Material material, String name) {
		super(material);

		setBlockName("kekcraft_" + name);
		setBlockTextureName("kekcraft:" + name);
		setCreativeTab(Tabs.DEFAULT);
		KekCraft.factory.addBlock(name, this);
		BLOCKS.add(this);
	}
}
