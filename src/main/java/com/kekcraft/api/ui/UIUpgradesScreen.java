package com.kekcraft.api.ui;

import com.kekcraft.api.ui.MachineContainer.InventorySlot;
import com.kekcraft.api.ui.MachineContainer.UpgradeSlot;

public class UIUpgradesScreen extends UIScreen {
	public UIUpgradesScreen(MachineUI ui) {
		super(ui, "Upgrades");

		addScreenSwitch(0, 0, 23, 23, "MainScreen");
		addScreenSwitch(22, 0, 23, 23, "Options");
	}
	
	@Override
	public void load(MachineUI e) {
		super.load(e);
		
		e.allow.add(UpgradeSlot.class);
		e.allow.add(InventorySlot.class);
	}

	@Override
	public void render(MachineTileEntity e) {
	}
}
