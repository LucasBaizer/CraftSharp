package com.craftsharp.blocks;

import java.util.ArrayList;
import java.util.List;

import com.craftsharp.CraftSharp;
import com.craftsharp.Tabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class CraftSharpBlock extends Block {
	public static final List<CraftSharpBlock> BLOCKS = new ArrayList<CraftSharpBlock>();
	
	public static final int WOOD = 0;
	public static final int STONE = 1;
	public static final int IRON = 2;
	public static final int DIAMOND = 3;

	public CraftSharpBlock(Material material, String name) {
		super(material);

		setBlockName("craftsharp_" + name);
		setBlockTextureName("craftsharp:" + name);
		setCreativeTab(Tabs.DEFAULT);
		CraftSharp.factory.addBlock(name, this);
		BLOCKS.add(this);
	}
}
