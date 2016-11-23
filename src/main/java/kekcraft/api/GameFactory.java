package kekcraft.api;

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
		return (Block) objects.get(name);
	}

	public Item getItem(String name) {
		return (Item) objects.get(name);
	}

	public Item initializeItem(Item item, String name, String unlocalized, CreativeTabs tab, String icon) {
		objects.put(unlocalized, item);
		return item.setTextureName(instance.getModid() + ":" + icon)
				.setUnlocalizedName(instance.getModid() + "_" + unlocalized).setCreativeTab(tab);
	}

	public Block initializeBlock(Block block, String name, String unlocalized, CreativeTabs tab, String icon) {
		objects.put(unlocalized, block);
		return block.setBlockTextureName(icon).setBlockName(instance.getModid() + "_" + unlocalized)
				.setCreativeTab(tab);
	}
}
