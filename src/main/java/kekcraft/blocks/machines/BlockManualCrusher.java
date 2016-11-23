package kekcraft.blocks.machines;

import java.awt.Dimension;

import kekcraft.KekCraft;
import kekcraft.Tabs;
import kekcraft.api.GameFactory;
import kekcraft.api.ui.MachineContainer;
import kekcraft.api.ui.MachineTileEntity;
import kekcraft.api.ui.MachineUI;
import kekcraft.api.ui.energy.ElectricMachine;
import kekcraft.api.ui.energy.ElectricMachineRecipe;
import kekcraft.api.ui.energy.ElectricMachineTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockManualCrusher extends ElectricMachine {
	public BlockManualCrusher(GameFactory factory) {
		super(Material.glass, KekCraft.modInstance, 3,
				new ResourceLocation(KekCraft.MODID, "textures/ui/ManualCrusher.png"));
		factory.initializeBlock(this, "Manual Crusher", "ManualCrusher", Tabs.MACHINES, "ManualCrusher");
		setWindowDimensions(new Dimension(-1, 189));
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

		if (player.isSneaking()) {
			BlockManualCrusherTileEntity tile = (BlockManualCrusherTileEntity) world.getTileEntity(x, y, z);
			if (tile.getStackInSlot(0) != null) {
				tile.getEnergy().modifyEnergyStored(5);
			}
		}
		return true;
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockManualCrusherTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);

		container.drawMinecraftInventory(8, 107);
		container.addSlotToContainer(container.createSlot(0, 50, 58));
		container.addSlotToContainer(container.createOutputSlot(1, 110, 58));
	}

	@Override
	public void drawToUI(MachineUI ui, MachineTileEntity entity) {
	}

	public static class BlockManualCrusherTileEntity extends ElectricMachineTileEntity {
		public BlockManualCrusherTileEntity() {
			super(2, 100 / 10);

			getEnergy().setCapacity(100);
			getEnergy().setMaxTransfer(0);
			getEnergy().setEnergyStored(0);

			setItemSlots(new int[] { 0 });
			setOutputSlots(new int[] { 1 });

			addRecipe(new ElectricMachineRecipe(new ItemStack(Items.iron_ingot),
					new ItemStack(KekCraft.factory.getItem("DustIron")), -1, 100, 0));
			addRecipe(new ElectricMachineRecipe(new ItemStack(KekCraft.factory.getItem("IngotAluminum")),
					new ItemStack(KekCraft.factory.getItem("DustAluminum")), -1, 100, 0));
			addRecipe(new ElectricMachineRecipe(new ItemStack(KekCraft.factory.getItem("IngotSteel")),
					new ItemStack(KekCraft.factory.getItem("DustSteel")), -1, 100, 0));
		}

		@Override
		public void onInputSlotExhausted(int slot) {
			getEnergy().setEnergyStored(0);
		}

		@Override
		public boolean canConnectEnergy(ForgeDirection from) {
			return false;
		}
	}
}
