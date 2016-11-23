package kekcraft.items;

import kekcraft.Tabs;
import kekcraft.api.GameFactory;

public class MachineCore extends KekCraftItem {
	public MachineCore(GameFactory factory) {
		super(factory);

		factory.initializeItem(this, "Machine Core", "MachineCore", Tabs.MACHINES, "MachineCore");
	}
}
