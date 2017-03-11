package com.kekcraft.blocks.machines;

import java.awt.Dimension;
import java.util.Map;
import java.util.Map.Entry;

import com.kekcraft.KekCraft;
import com.kekcraft.Tabs;
import com.kekcraft.api.GameFactory;
import com.kekcraft.api.ui.ElectricMachine;
import com.kekcraft.api.ui.DefaultMachineRecipe;
import com.kekcraft.api.ui.ElectricMachineTileEntity;
import com.kekcraft.api.ui.MachineContainer;
import com.kekcraft.api.ui.MachineTileEntity;
import com.kekcraft.api.ui.MachineUI;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockElectricFurnace extends ElectricMachine {
	private IIcon currentIcon;
	private IIcon onIcon;
	private IIcon offIcon;
	private IIcon sideIcon;
	private IIcon[] icons = new IIcon[6];

	public BlockElectricFurnace(GameFactory factory) {
		super(Material.ground, KekCraft.modInstance, 0,
				new ResourceLocation(KekCraft.MODID, "textures/ui/ElectricFurnace.png"));
		factory.initializeBlock(this, "Electric Furnace", "ElectricFurnace", Tabs.DEFAULT, "ElectricFurnace");
		setWindowDimensions(new Dimension(-1, 189));
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		super.breakBlock(world, x, y, z, block, meta);

		currentIcon = offIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		onIcon = reg.registerIcon(this.textureName + "_front_on");
		currentIcon = offIcon = reg.registerIcon(this.textureName + "_front_off");

		this.sideIcon = reg.registerIcon(this.textureName + "_side");
		for (int i = 0; i < 6; i++) {
			this.icons[i] = this.sideIcon;
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta == 0) {
			if (side == 3) {
				return offIcon;
			}
			return sideIcon;
		} else {
			if (meta > 5) {
				meta -= 6;
				this.currentIcon = onIcon;
			}
			IIcon old = this.icons[meta];
			this.icons[meta] = currentIcon;
			IIcon icon = this.icons[side];
			this.icons[meta] = old;

			this.currentIcon = offIcon;

			return icon;
		}
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockElectricFurnaceTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);
		container.drawMinecraftInventory(8, 107);

		container.addSlotToContainer(container.createSlot(0, 56, 58));
		container.addSlotToContainer(container.createOutputSlot(1, 116, 58));
	}

	@Override
	public void drawToUI(MachineUI ui, MachineTileEntity entity) {
		BlockElectricFurnaceTileEntity e = (BlockElectricFurnaceTileEntity) entity;

		int barWidth = 7;
		int barHeight = 74;
		int targetHeight = (barHeight - (e.getEnergy().getMaxEnergyStored() - e.getEnergy().getEnergyStored())
				* barHeight / e.getEnergy().getMaxEnergyStored());
		ui.drawUV(ui.left + 8, ui.top + 28 + (barHeight - targetHeight), 176, 23 + barHeight - targetHeight, barWidth,
				targetHeight);
		ui.drawTooltip(ui.left + 8, ui.top + 27, barWidth, barHeight, (int) e.getEnergy().getEnergyStored() + " RF");

		int arrowWidth = 24;
		int arrowHeight = 16;
		if (e.getCurrentCookTime() > 0) {
			int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth / (double) e.getCookTime())
					+ 1);
			if (e.getCurrentCookTime() == 0) {
				width = 0;
			}
			ui.drawUV(ui.left + 79, ui.top + 57, 176, 97, width, arrowHeight);
			ui.drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight,
					(int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * 100) + "%");
		} else {
			ui.drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight, "0%");
		}
	}

	public static class BlockElectricFurnaceTileEntity extends ElectricMachineTileEntity {
		public BlockElectricFurnaceTileEntity() {
			super(2, 5);

			getEnergy().setCapacity(100000);
			getEnergy().setMaxTransfer(128);
			getEnergy().setEnergyStored(KekCraft.ENERGY_MODE_DEV ? 100000 : 0);

			setItemSlots(new int[] { 0 });
			setOutputSlots(new int[] { 1 });

			@SuppressWarnings("unchecked")
			Map<ItemStack, ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();
			for (Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
				addRecipe(new DefaultMachineRecipe(entry.getKey(), entry.getValue(), 50, 800, 0));
			}
		}

		private void modifyMeta(int x) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
					worldObj.getBlockMetadata(xCoord, yCoord, zCoord) + x, 2);
		}

		@Override
		protected void onItemConsumeStart() {
			modifyMeta(6);
		}

		@Override
		protected void onSmeltingStopped() {
			modifyMeta(-6);
		}

		@Override
		public boolean canConnectEnergy(ForgeDirection from) {
			return true;
		}
	}
}
