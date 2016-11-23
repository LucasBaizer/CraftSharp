package kekcraft.blocks.machines;

import kekcraft.KekCraft;
import kekcraft.Tabs;
import kekcraft.api.GameFactory;
import kekcraft.api.ui.Generator;
import kekcraft.api.ui.GeneratorTileEntity;
import kekcraft.api.ui.MachineContainer;
import kekcraft.api.ui.MachineTileEntity;
import kekcraft.api.ui.MachineUI;
import kekcraft.api.ui.energy.ElectricMachineTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGeneratorCrankEngine extends Generator {
	public BlockGeneratorCrankEngine(GameFactory factory) {
		super(Material.glass, KekCraft.MODID, 1, new ResourceLocation(KekCraft.MODID, "textures/ui/CrankEngine.png"));

		factory.initializeBlock(this, "Crank Engine", "CrankEngine", Tabs.MACHINES, "cobblestone");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

		if (player.isSneaking()) {
			BlockGeneratorCrankEngineTileEntity tile = (BlockGeneratorCrankEngineTileEntity) world.getTileEntity(x, y, z);
			tile.getEnergy().modifyEnergyStored(25);
		}
		return true;
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockGeneratorCrankEngineTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
	}

	@Override
	public void drawToUI(MachineUI ui, MachineTileEntity entity) {
		ElectricMachineTileEntity e = (ElectricMachineTileEntity) entity;

		int barWidth = 7;
		int barHeight = 74;
		int targetHeight = (barHeight
				- ((int) e.getEnergy().getMaxEnergyStored() - (int) e.getEnergy().getEnergyStored()) * barHeight
						/ (int) e.getEnergy().getMaxEnergyStored());
		ui.drawUV(ui.left + 85, ui.top + 6 + (barHeight - targetHeight), 176, barHeight - targetHeight, barWidth,
				targetHeight);
		ui.drawTooltip(ui.left + 85, ui.top + 6, barWidth, barHeight, e.getEnergy().getEnergyStored() + " RF");
	}

	public static class BlockGeneratorCrankEngineTileEntity extends GeneratorTileEntity {
		public BlockGeneratorCrankEngineTileEntity() {
			super(0);

			setItemSlots(new int[0]);
			setOutputSlots(new int[0]);

			getEnergy().setCapacity(10000);
			getEnergy().setMaxTransfer(128);
			getEnergy().setEnergyStored(0);
		}

		@Override
		public void writeToNBT(NBTTagCompound tagCompound) {
			super.defaultWriteToNBT(tagCompound);
		}

		@Override
		public boolean canConnectEnergy(ForgeDirection from) {
			return true;
		}
	}
}
