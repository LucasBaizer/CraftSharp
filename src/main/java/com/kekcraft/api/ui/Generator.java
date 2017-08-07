package com.kekcraft.api.ui;

import net.minecraft.block.material.Material;

public abstract class Generator extends Machine {
	public Generator(Material material, Object modInstance, int guid, String background) {
		super(material, modInstance, guid, background);
	}
}
