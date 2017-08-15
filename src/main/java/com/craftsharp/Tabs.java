package com.craftsharp;

import com.craftsharp.api.GameFactory;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Tabs {
	public static CreativeTabs DEFAULT;

	public static void initialize(final GameFactory factory) {
		DEFAULT = new CreativeTabs("KekCraft") {
			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(factory.getBlock("ElectricFurnace"));
			}
		};
	}
}
