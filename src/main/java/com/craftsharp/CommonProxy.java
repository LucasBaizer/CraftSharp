package com.craftsharp;

import static com.craftsharp.CraftSharp.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.craftsharp.api.ui.Machine;
import com.craftsharp.api.ui.UIHandler;
import com.craftsharp.blocks.CraftSharpBlock;
import com.craftsharp.blocks.CraftSharpBlockOre;
import com.craftsharp.blocks.OreGenerationHandler;
import com.craftsharp.blocks.machines.BlockAirCompressor;
import com.craftsharp.blocks.machines.BlockCircuitFabricator;
import com.craftsharp.blocks.machines.BlockElectricFurnace;
import com.craftsharp.blocks.machines.BlockGaseousInfuser;
import com.craftsharp.blocks.machines.BlockGeneratorCrankEngine;
import com.craftsharp.blocks.machines.BlockHeatTurbine;
import com.craftsharp.blocks.machines.BlockHighTemperatureAlloyFurnace;
import com.craftsharp.blocks.machines.BlockManualCrusher;
import com.craftsharp.blocks.machines.BlockOxidizer;
import com.craftsharp.blocks.machines.BlockRefrigerantCompressor;
import com.craftsharp.blocks.machines.BlockUncrafter;
import com.craftsharp.cmd.DoxCommand;
import com.craftsharp.items.CraftSharpItem;
import com.craftsharp.support.mekanism.BlockLiquidAir;
import com.craftsharp.support.mekanism.BlockLiquidHCL;
import com.craftsharp.support.mekanism.BlockLiquidNitrogen;
import com.craftsharp.support.mekanism.BucketLiquidAir;
import com.craftsharp.support.mekanism.BucketLiquidHCL;
import com.craftsharp.support.mekanism.BucketLiquidNitrogen;
import com.craftsharp.support.mekanism.GasCompressedAir;
import com.craftsharp.support.mekanism.GasHCL;
import com.craftsharp.support.mekanism.GasNitrogen;
import com.craftsharp.support.mekanism.GasRefrigerant;
import com.craftsharp.support.mekanism.LiquidAir;
import com.craftsharp.support.mekanism.LiquidHCL;
import com.craftsharp.support.mekanism.LiquidNitrogen;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.gas.GasRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

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
			instantiate("com.craftsharp.items", "CraftSharpItem", "CraftSharpItemUpgrade");
			instantiate("com.craftsharp.blocks", "CraftSharpBlockOre", "ParticleFX", "CraftSharpBlock",
					"OreGenerationHandler");
			for (Item item : CraftSharpItem.ITEMS) {
				GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
			}
			for (Block block : CraftSharpBlock.BLOCKS) {
				GameRegistry.registerBlock(block, "craftsharp_" + block.getUnlocalizedName().substring(5));
			}
			for (Block block : CraftSharpBlockOre.BLOCKS) {
				GameRegistry.registerBlock(block, "craftsharp_" + block.getUnlocalizedName().substring(5));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("There was an error instantiating CraftSharp items/blocks.");
			FMLCommonHandler.instance().exitJava(1, true);
		}

		Fluid fluidAir = new LiquidAir();
		Fluid fluidNitrogen = new LiquidNitrogen();
		Fluid fluidHCL = new LiquidHCL();
		FluidRegistry.registerFluid(fluidAir);
		FluidRegistry.registerFluid(fluidNitrogen);
		FluidRegistry.registerFluid(fluidHCL);

		Block fluidBlock = new BlockLiquidAir();
		Block nitrogenBlock = new BlockLiquidNitrogen();
		Block hclBlock = new BlockLiquidHCL();
		GameRegistry.registerBlock(fluidBlock, MODID + "_" + fluidBlock.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(nitrogenBlock, MODID + "_" + nitrogenBlock.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(hclBlock, MODID + "_" + hclBlock.getUnlocalizedName().substring(5));

		ItemBucket bucket = new BucketLiquidAir(factory);
		ItemBucket nitroBucket = new BucketLiquidNitrogen(factory);
		ItemBucket hclBucket = new BucketLiquidHCL(factory);
		GameRegistry.registerItem(bucket, MODID + "_" + bucket.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(nitroBucket, MODID + "_" + nitroBucket.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(hclBucket, MODID + "_" + hclBucket.getUnlocalizedName().substring(5));

		FluidContainerRegistry.registerFluidContainer(fluidAir, new ItemStack(bucket), new ItemStack(Items.bucket));
		FluidContainerRegistry.registerFluidContainer(fluidNitrogen, new ItemStack(nitroBucket),
				new ItemStack(Items.bucket));
		FluidContainerRegistry.registerFluidContainer(fluidHCL, new ItemStack(hclBucket), new ItemStack(Items.bucket));

		GasRegistry.register(new GasRefrigerant());
		GasRegistry.register(new GasCompressedAir());
		GasRegistry.register(new GasNitrogen());
		GasRegistry.register(new GasHCL());

		registerMachine(new BlockElectricFurnace(factory));
		registerMachine(new BlockGeneratorCrankEngine(factory));
		registerMachine(new BlockOxidizer(factory));
		registerMachine(new BlockManualCrusher(factory));
		registerMachine(new BlockHighTemperatureAlloyFurnace(factory));
		registerMachine(new BlockCircuitFabricator(factory));
		registerMachine(new BlockHeatTurbine(factory));
		registerMachine(new BlockGaseousInfuser(factory));
		registerMachine(new BlockAirCompressor(factory));
		registerMachine(new BlockRefrigerantCompressor(factory));
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

		try {
			Class<?> registry = Class.forName("erogenousbeef.bigreactors.api.registry.BigReactors");

			registry.getDeclaredMethod("registerFluid", String.class, float.class, float.class, float.class,
					float.class).invoke(null, "liquid_nitrogen", 0.66f, 0.99f, 6.00f, 3f);
		} catch (ClassNotFoundException ex) {
			System.out.println("BigReactors is not present, will not register liquid nitrogen as a reactor fluid.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new DoxCommand());
	}
}
