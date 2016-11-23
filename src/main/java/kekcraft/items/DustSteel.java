package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class DustSteel extends KekCraftItem {
	public DustSteel(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Steel Dust", "DustSteel", Tabs.MATERIALS, "DustSteel");
	}
}
