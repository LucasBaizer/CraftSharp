package kekcraft;

import kekcraft.api.GameFactory;
import net.minecraftforge.oredict.OreDictionary;

public class DictionaryHandler {
	public static void initialize(GameFactory factory) {
		OreDictionary.registerOre("dustIron", factory.getItem("DustIron"));
		OreDictionary.registerOre("dustSteel", factory.getItem("DustSteel"));
		OreDictionary.registerOre("dustAluminum", factory.getItem("DustAluminum"));
		OreDictionary.registerOre("dustMagnesiun", factory.getItem("DustMagnesium"));
		OreDictionary.registerOre("dustIronOxide", factory.getItem("DustIronOxide"));

		OreDictionary.registerOre("ingotSteel", factory.getItem("IngotSteel"));
		OreDictionary.registerOre("ingotAluminum", factory.getItem("IngotAluminum"));

		OreDictionary.registerOre("oreMagnesium", factory.getBlock("OreMagnesium"));
		OreDictionary.registerOre("oreAluminum", factory.getBlock("OreAluminum"));

		OreDictionary.registerOre("gearIron", factory.getItem("GearIron"));
	}
}
