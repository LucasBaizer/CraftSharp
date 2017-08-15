package com.craftsharp;

import com.craftsharp.api.ForgeMod;
import com.craftsharp.api.GameFactory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;

@Mod(modid = CraftSharp.MODID, name = CraftSharp.NAME, version = CraftSharp.VERSION, dependencies = "after:exnihilo;after:mekanism")
public class CraftSharp implements ForgeMod {
	public static final boolean ENERGY_MODE_DEV = false;
	public static final boolean FLUID_MODE_DEV = false;
	public static final String NAME = "CraftSharp";
	public static final String MODID = "craftsharp";
	public static final String VERSION = "1.0";

	@Instance
	public static CraftSharp modInstance;

	@SidedProxy(clientSide = "com.kekcraft.ClientProxy", serverSide = "com.kekcraft.CommonProxy")
	public static CommonProxy proxy;

	public static GameFactory factory;

	public static final String networkChannelName = MODID;
	public static FMLEventChannel channel;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		factory = new GameFactory(modInstance);
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		proxy.serverLoad(event);
	}

	@Override
	public String getModid() {
		return MODID;
	}
}
