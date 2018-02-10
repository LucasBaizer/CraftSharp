package com.craftsharp.support.mekanism;

import java.lang.reflect.Field;

import cpw.mods.fml.common.FMLCommonHandler;
import mekanism.api.gas.Gas;
import net.minecraftforge.fluids.FluidRegistry;

public class GasNitrogen extends Gas {
	public GasNitrogen() {
		super(FluidRegistry.getFluid("liquid_nitrogen"));

		setUnlocalizedName("nitrogen");
		Field name;
		try {
			name = Gas.class.getDeclaredField("name");
			name.setAccessible(true);
			name.set(this, "nitrogen");
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(1, true);
		}
	}
}
