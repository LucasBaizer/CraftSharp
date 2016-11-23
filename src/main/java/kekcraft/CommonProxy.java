package kekcraft;

import static kekcraft.KekCraft.channel;
import static kekcraft.KekCraft.factory;
import static kekcraft.KekCraft.networkChannelName;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import kekcraft.api.ui.Machine;
import kekcraft.api.ui.UIHandler;
import kekcraft.blocks.BlockOreAluminum;
import kekcraft.blocks.BlockOreMagnesium;
import kekcraft.blocks.KekCraftBlockOre;
import kekcraft.blocks.OreGenerationHandler;
import kekcraft.blocks.machines.BlockGeneratorCrankEngine;
import kekcraft.blocks.machines.BlockElectricFurnace;
import kekcraft.blocks.machines.BlockManualCrusher;
import kekcraft.blocks.machines.BlockOxidizer;
import kekcraft.cmd.DoxCommand;
import kekcraft.items.DustAluminum;
import kekcraft.items.DustIron;
import kekcraft.items.DustIronOxide;
import kekcraft.items.DustMagnesium;
import kekcraft.items.DustSteel;
import kekcraft.items.DustThermite;
import kekcraft.items.GearIron;
import kekcraft.items.IngotAluminum;
import kekcraft.items.IngotSteel;
import kekcraft.items.MachineCore;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(KekCraft.modInstance, new UIHandler());

		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(networkChannelName);
		channel.register(new ServerPacketHandler());

		Tabs.initialize(factory);

		GameRegistry.registerItem(new DustIron(factory), "kekcraft_DustIron");
		GameRegistry.registerItem(new DustSteel(factory), "kekcraft_DustSteel");
		GameRegistry.registerItem(new DustIronOxide(factory), "kekcraft_DustIronOxide");
		GameRegistry.registerItem(new DustMagnesium(factory), "kekcraft_DustMagnesium");
		GameRegistry.registerItem(new DustAluminum(factory), "kekcraft_DustAluminum");
		GameRegistry.registerItem(new DustThermite(factory), "kekcraft_DustThermite");
		GameRegistry.registerItem(new IngotSteel(factory), "kekcraft_IngotSteel");
		GameRegistry.registerItem(new IngotAluminum(factory), "kekcraft_IngotAluminum");
		GameRegistry.registerItem(new MachineCore(factory), "kekcraft_MachineCore");
		GameRegistry.registerItem(new GearIron(factory), "kekcraft_GearIron");

		GameRegistry.registerBlock(new BlockOreMagnesium(factory), "kekcraft_OreMagnesium");
		GameRegistry.registerBlock(new BlockOreAluminum(factory), "kekcraft_OreAluminum");

		registerMachine(new BlockElectricFurnace(factory));
		registerMachine(new BlockGeneratorCrankEngine(factory));
		registerMachine(new BlockOxidizer(factory));
		registerMachine(new BlockManualCrusher(factory));

		DictionaryHandler.initialize(factory);
		RecipeHandler.initialize(factory);
		FuelHandler.initialize();
	}

	public void registerMachine(Machine machine) {
		GameRegistry.registerBlock(machine, machine.getUnlocalizedName().substring(5));
		GameRegistry.registerTileEntity(machine.getTileEntityClass(), machine.getUnlocalizedName());
	}

	public void init(FMLInitializationEvent e) {
		OreGenerationHandler.ORES.add((KekCraftBlockOre) factory.getBlock("OreMagnesium"));
		OreGenerationHandler.ORES.add((KekCraftBlockOre) factory.getBlock("OreAluminum"));

		GameRegistry.registerWorldGenerator(new OreGenerationHandler(), 0);
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new DoxCommand());
	}
}
