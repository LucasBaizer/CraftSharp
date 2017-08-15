package com.craftsharp.items;

import com.craftsharp.api.ui.MachineUpgrade;

import net.minecraft.init.Items;

public class ItemUpgradeGas extends CraftSharpItemUpgrade {
	public ItemUpgradeGas() {
		super("UpgradeGas", Items.gold_ingot);
	}

	@Override
	public MachineUpgrade getUpgrade() {
		return MachineUpgrade.GAS_EFFICIENCY;
	}
}
