package kekcraft.api.ui.energy;

import kekcraft.api.ui.GeneratorTileEntity;
import kekcraft.api.ui.Machine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

public abstract class ElectricMachine extends Machine {
	public ElectricMachine(Material material, Object modInstance, int guid, ResourceLocation background) {
		super(material, modInstance, guid, background);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);

		TileEntity entity = world.getTileEntity(tileX, tileY, tileZ);
		if (entity != null) {
			if (entity instanceof GeneratorTileEntity) {
				ElectricMachineTileEntity e = (ElectricMachineTileEntity) world.getTileEntity(x, y, z);
				if (!e.generators.contains((GeneratorTileEntity) entity)) {
					e.generators.add((GeneratorTileEntity) entity);
				}
			}
		}
	}
}
