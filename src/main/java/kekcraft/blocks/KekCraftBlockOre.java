package kekcraft.blocks;

import net.minecraft.block.BlockOre;

public class KekCraftBlockOre extends BlockOre {
	public static final int WOOD = 0;
	public static final int STONE = 1;
	public static final int IRON = 2;
	public static final int DIAMOND = 3;

	private int veinSize;
	private int density;
	private int maxY;
	private int minY;

	public KekCraftBlockOre(int veinSize, int density, int minY, int maxY, int miningLevel) {
		super();

		this.setVeinSize(veinSize);
		this.setDensity(density);
		this.setMaxY(maxY);
		this.setMinY(minY);
		this.setHarvestLevel("pickaxe", miningLevel);
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
