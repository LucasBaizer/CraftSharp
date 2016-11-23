package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class DustAluminum extends KekCraftItem {
	public DustAluminum(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Aluminum Dust", "DustAluminum", Tabs.MATERIALS, "DustAluminum");
	}
}
