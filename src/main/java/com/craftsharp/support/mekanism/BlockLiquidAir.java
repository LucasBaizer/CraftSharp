package com.craftsharp.support.mekanism;

import com.craftsharp.CraftSharp;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockLiquidAir extends BlockFluidClassic {
	public static IIcon icon;
	public static IIcon flowing;

	public BlockLiquidAir() {
		super(FluidRegistry.getFluid("liquid_air"), Material.water);

		setBlockName("LiquidAir");
		CraftSharp.factory.addBlock("LiquidAir", this);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 0 ? icon : flowing;
	}
}
