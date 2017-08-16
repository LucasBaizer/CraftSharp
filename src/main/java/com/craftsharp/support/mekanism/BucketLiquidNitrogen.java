package com.craftsharp.support.mekanism;

import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class BucketLiquidNitrogen extends ItemBucket {
	public BucketLiquidNitrogen(GameFactory factory) {
		super(factory.getBlock("LiquidNitrogen"));
		factory.initializeItem(this, "Bucket of Liquid Nitrogen", "BucketLiquidNitrogen", Tabs.DEFAULT,
				"BucketLiquidNitrogen");

		setContainerItem(Items.bucket);
		setMaxStackSize(1);
		setCreativeTab(Tabs.DEFAULT);
	}
}
