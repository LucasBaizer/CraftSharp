package com.craftsharp.blocks;

import java.util.ArrayList;
import java.util.List;

import com.craftsharp.CraftSharp;

import net.minecraft.block.BlockOre;

public class CraftSharpBlockOre extends BlockOre {
	public static final List<CraftSharpBlockOre> BLOCKS = new ArrayList<CraftSharpBlockOre>();
	
	private int veinSize;
	private int density;
	private int maxY;
	private int minY;

	public CraftSharpBlockOre(String name, int veinSize, int density, int minY, int maxY, int miningLevel) {
		this.setBlockName("craftsharp_" + name);
		this.setBlockTextureName("craftsharp:" + name);
		this.setVeinSize(veinSize);
		this.setDensity(density);
		this.setMaxY(maxY);
		this.setMinY(minY);
		this.setHarvestLevel("pickaxe", miningLevel);

		CraftSharp.factory.addBlock(name, this);
		BLOCKS.add(this);
	}

	public int getVeinSize() {
		return veinSize;
	}

	public void setVeinSize(int veinSize) {
		this.veinSize = veinSize;
	}

	public int getDensity() {
		return density;
	}

	public void setDensity(int density) {
		this.density = density;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}
}
