package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;

public class IngotSteel extends KekCraftItem {
	public IngotSteel() {
		super("IngotSteel");
		DictionaryHandler.register("ingotSteel", this);
	}
}
