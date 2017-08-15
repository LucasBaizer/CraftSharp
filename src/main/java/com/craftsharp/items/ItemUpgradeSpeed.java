package com.craftsharp.items;

import com.craftsharp.api.ui.MachineUpgrade;

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
