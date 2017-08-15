package com.craftsharp.api.ui;

import java.util.HashMap;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class UIHandler implements IGuiHandler {
	static final HashMap<Integer, Machine> uiMap = new HashMap<Integer, Machine>();

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		for (int key : uiMap.keySet()) {
			if (id == key) {
				Machine block = uiMap.get(key);
				MachineTileEntity tileEntity = (MachineTileEntity) world.getTileEntity(x, y, z);
				return new MachineContainer(player.inventory, block, tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		for (int key : uiMap.keySet()) {
			if (id == key) {
				Machine block = uiMap.get(key);
				MachineTileEntity tileEntity = (MachineTileEntity) world.getTileEntity(x, y, z);
				return new MachineUI(player.inventory, block, tileEntity);
			}
		}
		return null;
	}
}
