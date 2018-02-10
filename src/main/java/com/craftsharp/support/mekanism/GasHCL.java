package com.craftsharp.support.mekanism;

import java.lang.reflect.Field;

import cpw.mods.fml.common.FMLCommonHandler;
import mekanism.api.gas.Gas;
import net.minecraftforge.fluids.FluidRegistry;

public class GasHCL extends Gas {
	public GasHCL() {
		super(FluidRegistry.getFluid("liquid_hcl_acid"));

		setUnlocalizedName("hcl_acid");
		Field name;
		try {
			name = Gas.class.getDeclaredField("name");
			name.setAccessible(true);
			name.set(this, "hcl_acid");
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(1, true);
		}
	}
}
