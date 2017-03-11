package com.kekcraft.items;

import com.kekcraft.DictionaryHandler;

public class ItemSilicon extends KekCraftItem {
	public ItemSilicon() {
		super("Silicon");
		DictionaryHandler.register("itemSilicon", this);
	}
}
