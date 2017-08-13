package com.kekcraft.items;

import com.kekcraft.api.ui.MachineUpgrade;

import net.minecraft.init.Items;

public class ItemUpgradeFluid extends KekCraftItemUpgrade {
	public ItemUpgradeFluid() {
		super("UpgradeFluid", Items.water_bucket);
	}

	@Override
	public MachineUpgrade getUpgrade() {
		return MachineUpgrade.FLUID_EFFICIENCY;
	}
}
