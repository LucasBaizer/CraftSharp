package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class IngotAluminum extends KekCraftItem {
	public IngotAluminum(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Aluminum Ingot", "IngotAluminum", Tabs.MATERIALS, "IngotAluminum");
	}
}
