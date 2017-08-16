package com.craftsharp.api;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class GameFactory {
	private HashMap<String, Object> objects = new HashMap<String, Object>();
	private ForgeMod instance;

	public GameFactory(ForgeMod instance) {
		this.instance = instance;
	}

	public Block getBlock(String name) {
		Block block = (Block) objects.get(name);
		if (block == null) {
			System.out.println("WARNING: Attempting to retreive a null block from the GameFactory!");
			printStackTrace(3);
		}
		return block;
	}

	public Item getItem(String name) {
		Item item = (Item) objects.get(name);
		if (item == null) {
			System.out
					.println("WARNING: Attempting to retreive a null item from the GameFactory: attempt name: " + name);
			printStackTrace(3);
		}
		return item;
	}

	private void printStackTrace(int frames) {
		System.out.println("Current stacktrace (to " + frames + " frames): ");
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (int i = 2; i < frames + 2; i++) {
			System.out.println(stack[i]);
		}
	}

	public Item initializeItem(Item item, String name, String unlocalized, CreativeTabs tab, String icon) {
		objects.put(unlocalized, item);
		return item.setTextureName(instance.getModid() + ":" + icon)
				.setUnlocalizedName(instance.getModid() + "_" + unlocalized).setCreativeTab(tab);
	}

	public Block initializeBlock(Block block, String name, String unlocalized, CreativeTabs tab, String icon) {
		objects.put(unlocalized, block);
		return block.setBlockTextureName(instance.getModid() + ":" + icon)
				.setBlockName(instance.getModid() + "_" + unlocalized).setCreativeTab(tab);
	}

	public void addItem(String name, Item item) {
		objects.put(name, item);
	}

	public void addBlock(String name, Block item) {
		objects.put(name, item);
	}
}
