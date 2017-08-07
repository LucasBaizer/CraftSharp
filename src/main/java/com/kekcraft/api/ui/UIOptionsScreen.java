package com.kekcraft.api.ui;

import static net.minecraftforge.common.util.ForgeDirection.*;

import com.kekcraft.ModPacket;

import net.minecraftforge.common.util.ForgeDirection;

public class UIOptionsScreen extends UIScreen {
	private MachineTileEntity entity;
	private FaceType[] types;

	public UIOptionsScreen(MachineUI ui, FaceType... availableTypes) {
		super(ui, "Options");

		addScreenSwitch(0, 0, 23, 23, "MainScreen");
		addClickListener(80, 36, 16, 16, createRunnable(UP));
		addClickListener(80, 74, 16, 16, createRunnable(DOWN));
		addClickListener(61, 55, 16, 16, createRunnable(WEST));
		addClickListener(99, 55, 16, 16, createRunnable(EAST));
		addClickListener(61, 74, 16, 16, createRunnable(NORTH));

		this.types = availableTypes;
	}

	@Override
	public void render(MachineTileEntity e, Object... args) {
		this.entity = e;

		drawUV(ui.left + 80, ui.top + 36, 176, getY(UP), 16, 16);
		drawUV(ui.left + 80, ui.top + 74, 176, getY(DOWN), 16, 16);
		drawUV(ui.left + 61, ui.top + 55, 176, getY(WEST), 16, 16);
		drawUV(ui.left + 99, ui.top + 55, 176, getY(EAST), 16, 16);
		drawUV(ui.left + 61, ui.top + 74, 176, getY(NORTH), 16, 16);
	}

	private Runnable createRunnable(final ForgeDirection dir) {
		return new Runnable() {
			@Override
			public void run() {
				FaceType currentType = entity.faces.get(dir);
				for (int i = 0; i < types.length; i++) {
					if (types[i] == currentType) {
						if (i + 1 == types.length) {
							i = -1;
						}
						entity.faces.put(dir, types[i + 1]);
						ModPacket.sendTileEntityUpdateToServer(entity);
						return;
					}
				}
			}
		};
	}

	private int getY(ForgeDirection dir) {
		FaceType type = entity.faces.get(dir);
		for (int i = 0; i < types.length; i++) {
			if (types[i] == type) {
				return 23 + (16 * i);
			}
		}
		return -1;
	}
}
