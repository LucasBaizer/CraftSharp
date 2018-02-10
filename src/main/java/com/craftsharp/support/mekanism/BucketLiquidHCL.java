package com.craftsharp.support.mekanism;

import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class BucketLiquidHCL extends ItemBucket {
	public BucketLiquidHCL(GameFactory factory) {
		super(factory.getBlock("LiquidHCL"));
		factory.initializeItem(this, "Bucket of Hydrochloric Acid", "BucketLiquidHCL", Tabs.DEFAULT, "BucketLiquidHCL");

		setContainerItem(Items.bucket);
		setMaxStackSize(1);
		setCreativeTab(Tabs.DEFAULT);
	}
}
