package com.craftsharp;

import static com.craftsharp.CraftSharp.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.craftsharp.api.ui.Machine;
import com.craftsharp.api.ui.UIHandler;
import com.craftsharp.blocks.KekCraftBlock;
import com.craftsharp.blocks.KekCraftBlockOre;
import com.craftsharp.blocks.OreGenerationHandler;
import com.craftsharp.blocks.machines.BlockCircuitFabricator;
import com.craftsharp.blocks.machines.BlockElectricFurnace;
import com.craftsharp.blocks.machines.BlockGeneratorCrankEngine;
import com.craftsharp.blocks.machines.BlockHeatTurbine;
import com.craftsharp.blocks.machines.BlockHighTemperatureAlloyFurnace;
import com.craftsharp.blocks.machines.BlockManualCrusher;
import com.craftsharp.blocks.machines.BlockOxidizer;
import com.craftsharp.blocks.machines.BlockUncrafter;
import com.craftsharp.cmd.DoxCommand;
import com.craftsharp.items.KekCraftItem;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CommonProxy {
	private static void instantiate(String pkg, String... ignore) throws ReflectiveOperationException, IOException {
		instantiate(pkg, Arrays.asList(ignore));
	}

	private static void instantiate(String pkg, List<String> i) throws ReflectiveOperationException, IOException {
		for (ClassInfo info : ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClasses(pkg)) {
			Class<?> clazz = Class.forName(info.getName());

			boolean valid = true;
			for (String ignore : i) {
				if (clazz.getSimpleName().endsWith(ignore)) {
					valid = false;
				}
			}
			if (valid) {
				clazz.newInstance();
			}
		}
	}

	public void preInit(FMLPreInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(CraftSharp.modInstance, new UIHandler());

		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(networkChannelName);
		channel.register(new ServerPacketHandler());

		Tabs.initialize(factory);
		try {
			instantiate("com.kekcraft.items", "KekCraftItem", "KekCraftItemUpgrade");
			instantiate("com.kekcraft.blocks", "KekCraftBlockOre", "ParticleFX", "KekCraftBlock",
					"OreGenerationHandler");
			for (Item item : KekCraftItem.ITEMS) {
				GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
			}
			for (Block block : KekCraftBlock.BLOCKS) {
				GameRegistry.registerBlock(block, "kekcraft_" + block.getUnlocalizedName().substring(5));
			}
			for (Block block : KekCraftBlockOre.BLOCKS) {
				GameRegistry.registerBlock(block, "kekcraft_" + block.getUnlocalizedName().substring(5));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("There was an error instantiating KekCraft items/blocks.");
			FMLCommonHandler.instance().exitJava(1, true);
		}
		registerMachine(new BlockElectricFurnace(factory));
		registerMachine(new BlockGeneratorCrankEngine(factory));
		registerMachine(new BlockOxidizer(factory));
		registerMachine(new BlockManualCrusher(factory));
		registerMachine(new BlockHighTemperatureAlloyFurnace(factory));
		registerMachine(new BlockCircuitFabricator(factory));
		registerMachine(new BlockHeatTurbine(factory));
		registerMachine(new BlockUncrafter(factory));

		DictionaryHandler.initialize(factory);
		RecipeHandler.initialize(factory);
		FuelHandler.initialize();
	}

	public void registerMachine(Machine machine) {
		GameRegistry.registerBlock(machine, machine.getUnlocalizedName().substring(5));
		GameRegistry.registerTileEntity(machine.getTileEntityClass(), machine.getUnlocalizedName());
	}

	public void init(FMLInitializationEvent e) {
		GameRegistry.registerWorldGenerator(new OreGenerationHandler(), 0);
	}

	public void postInit(FMLPostInitializationEvent e) {
		try {
			Class<?> registry = Class.forName("exnihilo.registries.SieveRegistry");
			Block source = GameRegistry.findBlock("exnihilo", "dust");
			Item output = factory.getItem("DustMagnesium");
			int meta = 0;
			int rarity = 20;
			registry.getDeclaredMethod("register", Block.class, Item.class, int.class, int.class).invoke(null, source,
					output, meta, rarity);
		} catch (ClassNotFoundException ex) {
			System.out.println("ExNihilo is not present, will not register magnesium dust in sieve.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new DoxCommand());
	}
}
