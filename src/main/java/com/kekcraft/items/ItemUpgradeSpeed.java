package com.kekcraft.items;

import com.kekcraft.api.ui.MachineUpgrade;

import net.minecraft.init.Items;

public class ItemUpgradeSpeed extends KekCraftItemUpgrade {
	public ItemUpgradeSpeed() {
		super("UpgradeSpeed", Items.redstone);
	}

	@Override
	public MachineUpgrade getUpgrade() {
		return MachineUpgrade.SPEED;
	}
}
