package com.kekcraft.api.ui;

import net.minecraft.block.material.Material;

public abstract class ElectricMachine extends Machine {
	public ElectricMachine(Material material, Object modInstance, int guid, String background) {
		super(material, modInstance, guid, background);
	}
}
