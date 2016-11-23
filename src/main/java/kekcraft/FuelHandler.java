package kekcraft;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import kekcraft.items.ItemFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
	public static void initialize() {
		GameRegistry.registerFuelHandler(new FuelHandler());
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		Item item = fuel.getItem();
		if (item instanceof ItemFuel) {
			return ((ItemFuel) item).getBurnTime();
		}
		return 0;
	}
}
