package com.craftsharp.items;

import com.craftsharp.DictionaryHandler;

public class IngotAluminum extends CraftSharpItem {
	public IngotAluminum() {
		super("IngotAluminum");
		DictionaryHandler.register("ingotAluminum", this);
	}
}
