package com.craftsharp;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mekanism.api.gas.GasRegistry;
import net.minecraftforge.client.event.TextureStitchEvent;

public class CraftSharpRenderer {
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 0) {
			GasRegistry.getGas("refrigerant").setIcon(event.map.registerIcon(CraftSharp.MODID + ":fluids/Refrigerant"));
			GasRegistry.getGas("compressedAir").setIcon(event.map.registerIcon(CraftSharp.MODID + ":fluids/CompressedAir"));
		}
	}
}
