package com.craftsharp;

import com.craftsharp.support.mekanism.BlockLiquidAir;
import com.craftsharp.support.mekanism.BlockLiquidNitrogen;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mekanism.api.gas.GasRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidRegistry;

public class CraftSharpSubscriber {
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 0) {
			IIcon air = event.map.registerIcon(CraftSharp.MODID + ":fluids/Air");
			IIcon nitrogen = event.map.registerIcon(CraftSharp.MODID + ":fluids/Nitrogen");
			IIcon airFlowing = event.map.registerIcon(CraftSharp.MODID + ":fluids/Air_Flowing");
			IIcon nitrogenFlowing = event.map.registerIcon(CraftSharp.MODID + ":fluids/Nitrogen_Flowing");

			GasRegistry.getGas("refrigerant").setIcon(event.map.registerIcon(CraftSharp.MODID + ":fluids/Refrigerant"));
			GasRegistry.getGas("compressedAir").setIcon(air);
			GasRegistry.getGas("nitrogen").setIcon(nitrogen);

			FluidRegistry.getFluid("liquid_air").setIcons(air, airFlowing);
			FluidRegistry.getFluid("liquid_nitrogen").setIcons(nitrogen, nitrogenFlowing);
			BlockLiquidAir.icon = air;
			BlockLiquidAir.flowing = airFlowing;
			BlockLiquidNitrogen.icon = nitrogen;
			BlockLiquidNitrogen.flowing = nitrogenFlowing;
		}
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		ItemStack result = fillCustomBucket(event.world, event.target);

		if (result != null) {
			event.result = result;
			event.setResult(Result.ALLOW);
		}
	}

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {
		Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

		if (block instanceof BlockLiquidAir) {
			if (world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
				world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
				return new ItemStack(CraftSharp.factory.getItem("BucketLiquidAir"));
			}
		} else if (block instanceof BlockLiquidNitrogen) {
			if (world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
				world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
				return new ItemStack(CraftSharp.factory.getItem("BucketLiquidNitrogen"));
			}
		}
		return null;
	}
}
