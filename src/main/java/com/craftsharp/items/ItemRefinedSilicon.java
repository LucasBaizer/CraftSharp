package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;

public class ItemRefinedSilicon extends KekCraftItem {
	public ItemRefinedSilicon() {
		super("RefinedSilicon");
		DictionaryHandler.register("ingotSilicon", this);
	}
}
