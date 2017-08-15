package com.craftsharp;

import java.util.HashMap;
import java.util.Map.Entry;

import com.craftsharp.api.GameFactory;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class DictionaryHandler {
	public static final HashMap<String, Item> ITEMS = new HashMap<String, Item>();
	public static final HashMap<String, Block> BLOCKS = new HashMap<String, Block>();

	public static void register(String name, Item item) {
		ITEMS.put(name, item);
	}

	public static void register(String name, Block item) {
		BLOCKS.put(name, item);
	}

	public static void initialize(GameFactory factory) {
		for (Entry<String, Item> e : ITEMS.entrySet()) {
			OreDictionary.registerOre(e.getKey(), e.getValue());
		}
		for (Entry<String, Block> e : BLOCKS.entrySet()) {
			OreDictionary.registerOre(e.getKey(), e.getValue());
		}
	}
}
