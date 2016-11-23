package kekcraft.blocks.machines;

import java.awt.Dimension;
import java.util.Map;
import java.util.Map.Entry;

import kekcraft.KekCraft;
import kekcraft.Tabs;
import kekcraft.api.GameFactory;
import kekcraft.api.ui.MachineContainer;
import kekcraft.api.ui.MachineTileEntity;
import kekcraft.api.ui.MachineUI;
import kekcraft.api.ui.energy.ElectricMachine;
import kekcraft.api.ui.energy.ElectricMachineRecipe;
import kekcraft.api.ui.energy.ElectricMachineTileEntity;
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
		factory.initializeBlock(this, "Electric Furnace", "ElectricFurnace", Tabs.MACHINES,
				KekCraft.MODID + ":ElectricFurnace");
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
			IIcon old = this.icons[meta];
			this.icons[meta] = currentIcon;
			IIcon icon = this.icons[side];
			this.icons[meta] = old;
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
		int targetHeight = (barHeight
				- ((int) e.getEnergy().getMaxEnergyStored() - (int) e.getEnergy().getEnergyStored()) * barHeight
						/ (int) e.getEnergy().getMaxEnergyStored());
		ui.drawUV(ui.left + 8, ui.top + 27 + (barHeight - targetHeight), 176, barHeight - targetHeight, barWidth,
				targetHeight);
		ui.drawTooltip(ui.left + 8, ui.top + 27, barWidth, barHeight, (int) e.getEnergy().getEnergyStored() + " RF");

		int arrowWidth = 24;
		int arrowHeight = 16;
		if (e.getCookTime() > 0) {
			int width = ((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth / e.getCookTime()) + 1;
			if (e.getCurrentCookTime() == 0) {
				width = 0;
			}
			ui.drawUV(ui.left + 79, ui.top + 57, 176, 97, width, arrowHeight);
			ui.drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight,
					e.getCurrentCookTime() / e.getCookTime() + "%");
		} else {
			ui.drawTooltip(ui.left + 79, ui.top + 57, arrowWidth, arrowHeight, "0%");
		}
	}

	public static class BlockElectricFurnaceTileEntity extends ElectricMachineTileEntity {
		public BlockElectricFurnaceTileEntity() {
			super(2, 100 / 24);

			getEnergy().setCapacity(100000);
			getEnergy().setMaxTransfer(128);
			getEnergy().setEnergyStored(50000);

			setItemSlots(new int[] { 0 });
			setOutputSlots(new int[] { 1 });

			@SuppressWarnings("unchecked")
			Map<ItemStack, ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();
			for (Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
				addRecipe(new ElectricMachineRecipe(entry.getKey(), entry.getValue(), 50, 800, 0));
			}
		}

		@Override
		public boolean canConnectEnergy(ForgeDirection from) {
			return true;
		}
	}
}
