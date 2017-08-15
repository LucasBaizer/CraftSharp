package com.craftsharp;

import com.craftsharp.items.KekCraftItem;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
	public static void initialize() {
		GameRegistry.registerFuelHandler(new FuelHandler());
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		Item item = fuel.getItem();
		if (item instanceof KekCraftItem) {
			KekCraftItem f = (KekCraftItem) item;
			if (f.isFuel()) {
				return f.getBurnTime();
			}
		}
		return 0;
	}
}
