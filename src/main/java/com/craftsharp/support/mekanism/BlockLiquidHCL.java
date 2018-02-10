package com.craftsharp.support.mekanism;

import com.craftsharp.CraftSharp;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockLiquidHCL extends BlockFluidClassic {
	public static IIcon icon;
	public static IIcon flowing;

	public BlockLiquidHCL() {
		super(FluidRegistry.getFluid("liquid_hcl_acid"), Material.water);

		setBlockName("LiquidHCL");
		CraftSharp.factory.addBlock("LiquidHCL", this);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 0 ? icon : flowing;
	}
}
