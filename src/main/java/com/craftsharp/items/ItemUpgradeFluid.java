package com.craftsharp.items;

import com.craftsharp.api.ui.MachineUpgrade;

import net.minecraft.init.Items;

public class ItemUpgradeFluid extends CraftSharpItemUpgrade {
	public ItemUpgradeFluid() {
		super("UpgradeFluid", Items.water_bucket);
	}

	@Override
	public MachineUpgrade getUpgrade() {
		return MachineUpgrade.FLUID_EFFICIENCY;
	}
}
