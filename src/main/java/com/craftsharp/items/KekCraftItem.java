package com.craftsharp.items;

import java.util.ArrayList;
import java.util.List;

import com.craftsharp.CraftSharp;
import com.craftsharp.Tabs;

import net.minecraft.item.Item;

public class KekCraftItem extends Item {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	private int burnTime = -1;

	public KekCraftItem(String name) {
		ITEMS.add(this);

		setUnlocalizedName("kekcraft_" + name);
		setTextureName("kekcraft:" + name);
		setCreativeTab(Tabs.DEFAULT);
		CraftSharp.factory.addItem(name, this);
	}

	public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	public boolean isFuel() {
		return burnTime != -1;
	}
}
