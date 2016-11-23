package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class GearIron extends KekCraftItem {
	public GearIron(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Iron Gear", "GearIron", Tabs.MACHINES, "GearIron");
	}
}
