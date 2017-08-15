package com.kekcraft;

import static com.kekcraft.KekCraft.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.kekcraft.api.ui.Machine;
import com.kekcraft.api.ui.UIHandler;
import com.kekcraft.blocks.KekCraftBlock;
import com.kekcraft.blocks.KekCraftBlockOre;
import com.kekcraft.blocks.OreGenerationHandler;
import com.kekcraft.blocks.machines.BlockCircuitFabricator;
import com.kekcraft.blocks.machines.BlockElectricFurnace;
import com.kekcraft.blocks.machines.BlockGeneratorCrankEngine;
import com.kekcraft.blocks.machines.BlockHeatTurbine;
import com.kekcraft.blocks.machines.BlockHighTemperatureAlloyFurnace;
import com.kekcraft.blocks.machines.BlockManualCrusher;
import com.kekcraft.blocks.machines.BlockOxidizer;
import com.kekcraft.cmd.DoxCommand;
import com.kekcraft.items.KekCraftItem;

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
		NetworkRegistry.INSTANCE.registerGuiHandler(KekCraft.modInstance, new UIHandler());

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
