package com.kekcraft;

import com.kekcraft.api.ForgeMod;
import com.kekcraft.api.GameFactory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;

@Mod(modid = KekCraft.MODID, name = KekCraft.NAME, version = KekCraft.VERSION)
public class KekCraft implements ForgeMod {
	public static final boolean ENERGY_MODE_DEV = true;
	public static final String NAME = "KekCraft";
	public static final String MODID = "kekcraft";
	public static final String VERSION = "1.0";

	@Instance
	public static KekCraft modInstance;

	@SidedProxy(clientSide = "com.kekcraft.ClientProxy", serverSide = "com.kekcraft.api.CommonProxy")
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
