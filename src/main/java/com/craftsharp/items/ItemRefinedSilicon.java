package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;

public class ItemRefinedSilicon extends CraftSharpItem {
	public ItemRefinedSilicon() {
		super("RefinedSilicon");
		DictionaryHandler.register("ingotSilicon", this);
	}
}
