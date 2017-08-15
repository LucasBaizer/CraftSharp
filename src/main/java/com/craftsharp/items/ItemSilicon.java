package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;

public class ItemSilicon extends KekCraftItem {
	public ItemSilicon() {
		super("Silicon");
		DictionaryHandler.register("itemSilicon", this);
	}
}
