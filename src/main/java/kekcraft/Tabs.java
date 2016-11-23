package kekcraft;

import kekcraft.api.GameFactory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Tabs {
	public static final CreativeTabs MATERIALS = CreativeTabs.tabMaterials;
	public static final CreativeTabs BLOCKS = CreativeTabs.tabBlock;
	public static CreativeTabs MACHINES;

	public static void initialize(final GameFactory factory) {
		MACHINES = new CreativeTabs("KekCraft") {
			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(factory.getBlock("ElectricFurnace"));
			}
		};
	}
}
