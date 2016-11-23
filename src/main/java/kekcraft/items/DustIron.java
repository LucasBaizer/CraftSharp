package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class DustIron extends KekCraftItem {
	public DustIron(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Iron Dust", "DustIron", Tabs.MATERIALS, "DustIron");
	}
}
