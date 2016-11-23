package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class DustMagnesium extends KekCraftItem {
	public DustMagnesium(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Magnesium Dust", "DustMagnesium", Tabs.MATERIALS, "DustMagnesium");
	}
}
