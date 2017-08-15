package com.kekcraft.api.ui;

import com.kekcraft.api.ui.MachineContainer.UpgradeSlot;

import net.minecraft.inventory.Slot;

public class UIMainScreen extends UIScreen {
	public UIMainScreen(MachineUI ui) {
		super(ui, "MainScreen");

		addScreenSwitch(22, 0, 23, 23, "Options");
		addScreenSwitch(45, 0, 23, 23, "Upgrades");
	}

	@Override
	public void load(MachineUI e) {
		super.load(e);

		e.allow.add(Slot.class);
		e.disallow.add(UpgradeSlot.class);
	}
}
