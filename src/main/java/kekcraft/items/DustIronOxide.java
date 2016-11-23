package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class DustIronOxide extends KekCraftItem {
	public DustIronOxide(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Iron Oxide", "DustIronOxide", Tabs.MATERIALS, "DustIronOxide");
	}
}
