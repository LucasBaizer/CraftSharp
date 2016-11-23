package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class IngotSteel extends KekCraftItem {
	public IngotSteel(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Steel Ingot", "IngotSteel", Tabs.MATERIALS, "IngotSteel");
	}
}
