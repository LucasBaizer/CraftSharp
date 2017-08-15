package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;

public class IngotSteel extends CraftSharpItem {
	public IngotSteel() {
		super("IngotSteel");
		DictionaryHandler.register("ingotSteel", this);
	}
}
