package com.craftsharp.support.mekanism;

import mekanism.api.gas.Gas;

public class GasCompressedAir extends Gas {
	public GasCompressedAir() {
		super("compressedAir");

		registerFluid();
	}
}
