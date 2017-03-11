package com.kekcraft.api.ui;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public abstract class Generator extends Machine {
	public Generator(Material material, Object modInstance, int guid, ResourceLocation background) {
		super(material, modInstance, guid, background);
	}
}
