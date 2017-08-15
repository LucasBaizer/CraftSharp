package com.craftsharp.items;

import com.craftsharp.api.ui.MachineUpgrade;

import net.minecraft.init.Items;

public class ItemUpgradeEnergy extends CraftSharpItemUpgrade {
	public ItemUpgradeEnergy() {
		super("UpgradeEnergy", Items.glowstone_dust);
	}

	@Override
	public MachineUpgrade getUpgrade() {
		return MachineUpgrade.ENERGY_EFFICIENCY;
	}
}
