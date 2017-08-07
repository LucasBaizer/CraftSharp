package com.kekcraft.api.ui;

import net.minecraft.block.material.Material;

public abstract class FuelMachine extends Machine {
	public FuelMachine(Material material, Object modInstance, int guid, String background) {
		super(material, modInstance, guid, background);
	}
}
