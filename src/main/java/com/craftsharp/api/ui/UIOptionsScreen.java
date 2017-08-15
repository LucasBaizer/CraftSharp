package com.craftsharp.api.ui;

import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.HashMap;

import com.craftsharp.ModPacket;
import com.craftsharp.api.ui.MachineContainer.InventorySlot;

import net.minecraftforge.common.util.ForgeDirection;

public class UIOptionsScreen extends UIScreen {
	private HashMap<ForgeDirection, Integer> directionXMap = new HashMap<ForgeDirection, Integer>();
	private MachineTileEntity entity;
	private FaceType[] types;

	public UIOptionsScreen(MachineUI ui, FaceType... availableTypes) {
		super(ui, "Options");

		addScreenSwitch(0, 0, 23, 23, "MainScreen");
		addScreenSwitch(45, 0, 23, 23, "Upgrades");
		addClickListener(80, 36, 16, 16, createRunnable(UP));
		addClickListener(80, 74, 16, 16, createRunnable(DOWN));
		addClickListener(61, 55, 16, 16, createRunnable(WEST));
		addClickListener(99, 55, 16, 16, createRunnable(EAST));
		addClickListener(61, 74, 16, 16, createRunnable(NORTH));

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			directionXMap.put(dir, 176);
		}

		this.types = availableTypes;
	}

	public UIOptionsScreen setDirectionX(ForgeDirection dir, int x) {
		directionXMap.put(dir, x);
		return this;
	}

	@Override
	public void load(MachineUI e) {
		super.load(e);

		e.allow.add(InventorySlot.class);
	}

	@Override
	public void render(MachineTileEntity e) {
		this.entity = e;

		drawUV(ui.left + 80, ui.top + 36, directionXMap.get(UP), getY(translateDirection(UP)), 16, 16);
		drawUV(ui.left + 80, ui.top + 74, directionXMap.get(DOWN), getY(translateDirection(DOWN)), 16, 16);
		drawUV(ui.left + 61, ui.top + 55, directionXMap.get(WEST), getY(translateDirection(WEST)), 16, 16);
		drawUV(ui.left + 99, ui.top + 55, directionXMap.get(EAST), getY(translateDirection(EAST)), 16, 16);
		drawUV(ui.left + 61, ui.top + 74, directionXMap.get(NORTH), getY(translateDirection(NORTH)), 16, 16);
	}

	private Runnable createRunnable(final ForgeDirection dir) {
		return new Runnable() {
			@Override
			public void run() {
				FaceType currentType = entity.faces.get(translateDirection(dir));
				for (int i = 0; i < types.length; i++) {
					if (types[i] == currentType) {
						if (i + 1 == types.length) {
							i = -1;
						}
						entity.faces.put(translateDirection(dir), types[i + 1]);
						ModPacket.sendTileEntityUpdateToServer(entity);
						return;
					}
				}
			}
		};
	}

	private ForgeDirection translateDirection(ForgeDirection dir) {
		int meta = entity.getWorldObj().getBlockMetadata(entity.xCoord, entity.yCoord, entity.zCoord);
		if ((meta & 8) == 8) {
			meta &= 7;
		}
		switch (meta) {
			case 2:
				// front is facing north
				if (dir != UP && dir != DOWN) {
					return dir.getOpposite();
				}
				return dir;
			case 3:
				// front is facing south
				return dir;
			case 4:
				// front is facing west
				switch (dir) {
					case WEST:
						return NORTH;
					case EAST:
						return SOUTH;
					case NORTH:
						return EAST;
					case SOUTH:
						return WEST;
					default:
						return dir;
				}
			case 5:
				// front is facing east
				switch (dir) {
					case WEST:
						return SOUTH;
					case EAST:
						return NORTH;
					case NORTH:
						return WEST;
					case SOUTH:
						return EAST;
					default:
						return dir;
				}
			default:
				System.out.println("Invalid metadata for block (" + meta + "), returning direction unchanged!");
				return dir;
		}
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
