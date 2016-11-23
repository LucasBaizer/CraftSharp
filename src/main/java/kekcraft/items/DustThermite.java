package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class DustThermite extends ItemFuel {
	public DustThermite(GameFactory factory) {
		super(factory, 20000);

		factory.initializeItem(this, "Thermite", "DustThermite", Tabs.MATERIALS, "DustThermite");
		
	}
}
