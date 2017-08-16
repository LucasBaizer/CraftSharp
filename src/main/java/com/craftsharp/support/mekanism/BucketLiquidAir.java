package com.craftsharp.support.mekanism;

import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class BucketLiquidAir extends ItemBucket {
	public BucketLiquidAir(GameFactory factory) {
		super(factory.getBlock("LiquidAir"));
		factory.initializeItem(this, "Bucket of Liquid Air", "BucketLiquidAir", Tabs.DEFAULT, "BucketLiquidAir");

		setContainerItem(Items.bucket);
		setMaxStackSize(1);
		setCreativeTab(Tabs.DEFAULT);
	}
}
