package kekcraft.api.ui;

import kekcraft.api.ui.energy.ElectricMachineTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

public abstract class Generator extends Machine {
	public Generator(Material material, Object modInstance, int guid, ResourceLocation background) {
		super(material, modInstance, guid, background);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);

		TileEntity entity = world.getTileEntity(tileX, tileY, tileZ);
		if (entity != null) {
			if (entity instanceof ElectricMachineTileEntity) {
				ElectricMachineTileEntity e = (ElectricMachineTileEntity) entity;
				if (!e.generators.contains(this)) {
					e.generators.add((GeneratorTileEntity) world.getTileEntity(x, y, z));
				}
			}
		}
	}
}
