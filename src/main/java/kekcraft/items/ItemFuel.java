package kekcraft.items;

import kekcraft.api.GameFactory;

public abstract class ItemFuel extends KekCraftItem {
	private int burnTime;

	public ItemFuel(GameFactory factory, int burnTime) {
		super(factory);
		this.setBurnTime(burnTime);
	}

	public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}
}
