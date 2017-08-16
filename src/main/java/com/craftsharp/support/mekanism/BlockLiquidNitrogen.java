package com.craftsharp.support.mekanism;

import com.craftsharp.CraftSharp;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockLiquidNitrogen extends BlockFluidClassic {
	public static IIcon icon;
	public static IIcon flowing;

	public BlockLiquidNitrogen() {
		super(FluidRegistry.getFluid("liquid_nitrogen"), Material.water);

		setBlockName("LiquidNitrogen");
		CraftSharp.factory.addBlock("LiquidNitrogen", this);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 0 ? icon : flowing;
	}
}
