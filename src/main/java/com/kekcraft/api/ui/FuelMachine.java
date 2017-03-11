package com.kekcraft.api.ui;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public abstract class FuelMachine extends Machine {
	public FuelMachine(Material material, Object modInstance, int guid, ResourceLocation background) {
		super(material, modInstance, guid, background);
	}
}
