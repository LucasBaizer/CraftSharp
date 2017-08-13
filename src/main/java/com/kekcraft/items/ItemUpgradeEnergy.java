package com.kekcraft.items;

import com.kekcraft.api.ui.MachineUpgrade;

import net.minecraft.init.Items;

public class ItemUpgradeEnergy extends KekCraftItemUpgrade {
	public ItemUpgradeEnergy() {
		super("UpgradeEnergy", Items.glowstone_dust);
	}

	@Override
	public MachineUpgrade getUpgrade() {
		return MachineUpgrade.ENERGY_EFFICIENCY;
	}
}
