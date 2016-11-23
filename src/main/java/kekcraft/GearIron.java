package kekcraft;

import kekcraft.api.GameFactory;
import kekcraft.items.KekCraftItem;

public class GearIron extends KekCraftItem {
	public GearIron(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Iron Gear", "GearIron", Tabs.MACHINES, "GearIron");
	}
}
