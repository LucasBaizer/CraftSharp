package com.craftsharp.blocks.machines;

import java.awt.Dimension;
import java.io.IOException;

import com.craftsharp.CraftSharp;
import com.craftsharp.ModPacket;
import com.craftsharp.RecipeHandler;
import com.craftsharp.Tabs;
import com.craftsharp.api.GameFactory;
import com.craftsharp.api.ParticleColor;
import com.craftsharp.api.ui.ElectricMachine;
import com.craftsharp.api.ui.ElectricMachineTileEntity;
import com.craftsharp.api.ui.FaceType;
import com.craftsharp.api.ui.MachineContainer;
import com.craftsharp.api.ui.MachineTileEntity;
import com.craftsharp.api.ui.MachineUpgrade;
import com.craftsharp.api.ui.UIMainScreen;
import com.craftsharp.api.ui.UIOptionsScreen;
import com.craftsharp.api.ui.UIUpgradesScreen;

import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.api.gas.IGasHandler;
import mekanism.api.gas.ITubeConnection;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockGaseousInfuser extends ElectricMachine {
	public BlockGaseousInfuser(GameFactory factory) {
		super(Material.glass, CraftSharp.modInstance, 8, "GaseousInfuser");

		factory.initializeBlock(this, "Gaseous Infuser", "GaseousInfuser", Tabs.DEFAULT, "GaseousInfuser");
		RecipeHandler.FUTURES.add(new Runnable() {
			@Override
			public void run() {
				GameRegistry.addRecipe(new ShapedOreRecipe(CraftSharp.factory.getBlock("GaseousInfuser"), "ABA", "CDC",
						"EEE", 'A', Items.bucket, 'B', CraftSharp.factory.getItem("TurbineComponent"), 'C',
						"circuitDiamond", 'D', CraftSharp.factory.getItem("MachineCoreDiamond"), 'E', "blockOsmium"));
			}
		});
		setWindowDimensions(new Dimension(-1, 189));
		initializeSpecialIcons();
		setParticleColor(ParticleColor.CYAN);
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return BlockGaseousInfuserTileEntity.class;
	}

	@Override
	public void drawTiles(MachineContainer container) {
		container.setNormalizer(12);

		container.drawMinecraftInventory(8, 107);
		Slot slot = new Slot(container.getTileEntity(), 0, 66, container.normalize(58)) {
			@Override
			public boolean isItemValid(ItemStack item) {
				return item.getItem() == Items.diamond;
			}
		};
		if (container.getWorld().isRemote)
			slot.setBackgroundIcon(Items.diamond.getIconFromDamage(0));
		container.addSlotToContainer(slot);

		container.addSlotToContainer(container.createUpgradeSlot(1, 96, 37));
		container.addSlotToContainer(container.createUpgradeSlot(2, 96, 58));
		container.addSlotToContainer(container.createUpgradeSlot(3, 96, 79));
	}

	public static class BlockGaseousInfuserTileEntity extends ElectricMachineTileEntity
			implements ITubeConnection, IGasHandler {
		private GasTank hydrogen;
		private GasTank chlorine;
		private GasTank refrigerant;

		public BlockGaseousInfuserTileEntity() {
			super(4, 10);

			setItemSlots(new int[] { 0, 1, 2, 3 });
			setOutputSlots(new int[0]);
			setUpgradeSlots(new int[] { 1, 2, 3 });

			setChangeMeta(true);
			setValidUpgrades(new MachineUpgrade[] { MachineUpgrade.ENERGY_EFFICIENCY, MachineUpgrade.SPEED,
					MachineUpgrade.GAS_EFFICIENCY });

			energy.setCapacity(100000);
			energy.setMaxTransfer(128);
			energy.setEnergyStored(CraftSharp.ENERGY_MODE_DEV ? 100000 : 0);

			hydrogen = new GasTank(4000);
			chlorine = new GasTank(4000);
			refrigerant = new GasTank(4000);

			hydrogen.setGas(new GasStack(GasRegistry.getGas("deuterium"), 0));
			chlorine.setGas(new GasStack(GasRegistry.getGas("chlorine"), 0));
			refrigerant.setGas(new GasStack(GasRegistry.getGas("refrigerant"), 0));

			// only one recipe for now, so this is hard-coded
			addRecipe(new GaseousInfuserRecipe());

			onUISet = new Runnable() {
				@Override
				public void run() {
					ui.addScreen(new UIMainScreen(ui) {
						@Override
						public void render(MachineTileEntity m) {
							BlockGaseousInfuserTileEntity e = (BlockGaseousInfuserTileEntity) m;

							int barWidth = 7;
							int barHeight = 74;
							int targetHeight = (barHeight - (e.energy.getMaxEnergyStored() - e.energy.getEnergyStored())
									* barHeight / e.energy.getMaxEnergyStored());
							drawUV(ui.left + 7, ui.top + 28 + (barHeight - targetHeight), 176,
									23 + barHeight - targetHeight, barWidth, targetHeight);
							drawTooltip(ui.left + 7, ui.top + 27, barWidth, barHeight,
									e.energy.getEnergyStored() + " RF");

							int arrowWidth = 36;
							int arrowHeight = 16;
							if (e.getCurrentCookTime() > 0) {
								int width = (int) (((e.getCookTime() - e.getCurrentCookTime()) * arrowWidth
										/ (double) e.getCookTime()) + 1);
								if (e.getCurrentCookTime() == 0) {
									width = 0;
								}
								drawUV(ui.left + 90, ui.top + 57, 176, 97, width, arrowHeight);
								drawTooltip(ui.left + 90, ui.top + 57, arrowWidth, arrowHeight, 100
										- ((int) ((e.getCurrentCookTime() / (double) e.getCookTime()) * 100)) + "%");
							} else {
								drawTooltip(ui.left + 90, ui.top + 57, arrowWidth, arrowHeight, "0%");
							}

							drawBar(hydrogen, 183, 22);
							drawBar(chlorine, 190, 33);
							drawBar(refrigerant, 197, 135);
						}

						private void drawBar(GasTank tank, int srcX, int destX) {
							int target = (int) (74 - (tank.getMaxGas() - tank.getStored()) * 74 / tank.getMaxGas());
							drawUV(ui.left + destX, ui.top + 28 + (74 - target), srcX, 23 + 74 - target, 7, target);
							drawTooltip(ui.left + destX, ui.top + 28, 7, 74,
									tank.getGasType().getLocalizedName() + ": " + tank.getStored() + " ML");
						}
					});
					ui.addScreen(new UIOptionsScreen(ui, FaceType.NONE, FaceType.ENERGY, FaceType.ITEM, FaceType.GAS));
					ui.addScreen(new UIUpgradesScreen(ui));
					ui.setCurrentUIScreen("MainScreen");
				}
			};
		}

		private int getEnergyCostPerCook(GaseousInfuserRecipe recipe) {
			return (recipe.getFuelCost() * (getUpgrades(MachineUpgrade.SPEED) + 1)
					/ (getUpgrades(MachineUpgrade.ENERGY_EFFICIENCY) + 1))
					/ (recipe.getCookTime() / (getUpgrades(MachineUpgrade.SPEED) + 1));
		}

		private int getGasCostPerCook(GaseousInfuserRecipe recipe) {
			return (1000 / (getUpgrades(MachineUpgrade.GAS_EFFICIENCY) + 1))
					/ (recipe.getCookTime() / (getUpgrades(MachineUpgrade.SPEED) + 1));
		}

		@Override
		public void updateEntity() {
			if (!this.worldObj.isRemote) {
				if (isReady()) {
					GaseousInfuserRecipe recipe = (GaseousInfuserRecipe) getNextRecipe();
					if (recipe != null) {
						if (energy.getEnergyStored() - recipe.getFuelCost() >= 0 && hydrogen.getStored() >= 1000
								&& chlorine.getStored() >= 1000 && refrigerant.getStored() - 1000 <= 3000) {
							int cook = recipe.getCookTime() / (getUpgrades(MachineUpgrade.SPEED) + 1);

							currentRecipe = recipe;
							cookTime = cook;
							currentCookTime = cook;
							slots[0].stackSize--;
							if (slots[0].stackSize == 0) {
								slots[0] = null;
							}

							onItemConsumeStart();

							if (enablesAutomaticUpdates()) {
								ModPacket.sendTileEntityUpdate(this);
							}
						}
					}
				}
				if (isBurningRecipe()) {
					currentCookTime--;

					int cost = getGasCostPerCook((GaseousInfuserRecipe) currentRecipe);
					energy.modifyEnergyStored(-getEnergyCostPerCook((GaseousInfuserRecipe) currentRecipe));
					hydrogen.draw(cost, true);
					chlorine.draw(cost, true);
					refrigerant.receive(new GasStack(GasRegistry.getGas("refrigerant"), cost), true);

					if (enablesAutomaticUpdates()) {
						cookTicks++;
						if (cookTicks >= tickUpdateRate) {
							cookTicks = 0;
							ModPacket.sendTileEntityUpdate(this);
						}
					}
					if (currentCookTime == 0) {
						smeltItemWhenDone();
						if (getNextRecipe() == null) {
							onSmeltingFinished();
						}
					}
				}
			}
		}

		@Override
		public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer) {
			int received = 0;
			if (faces.get(side) == FaceType.GAS) {
				if (stack.getGas() == hydrogen.getGasType()) {
					received = hydrogen.receive(stack, doTransfer);
				} else if (stack.getGas() == chlorine.getGasType()) {
					received = chlorine.receive(stack, doTransfer);
				}
			}
			if (doTransfer && received > 0) {
				if (enablesAutomaticUpdates()) {
					ModPacket.sendTileEntityUpdate(this);
				}
			}
			return 0;
		}

		@Override
		public int receiveGas(ForgeDirection side, GasStack stack) {
			return receiveGas(side, stack, true);
		}

		@Override
		public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer) {
			GasStack stack = faces.get(side) == FaceType.GAS ? refrigerant.draw(amount, doTransfer) : null;
			if (stack != null && stack.amount > 0) {
				if (enablesAutomaticUpdates()) {
					ModPacket.sendTileEntityUpdate(this);
				}
			}
			return stack;
		}

		@Override
		public GasStack drawGas(ForgeDirection side, int amount) {
			return drawGas(side, amount, true);
		}

		@Override
		public boolean canReceiveGas(ForgeDirection side, Gas type) {
			return faces.get(side) == FaceType.GAS ? hydrogen.getGasType() == type || chlorine.getGasType() == type
					: false;
		}

		@Override
		public boolean canDrawGas(ForgeDirection side, Gas type) {
			return faces.get(side) == FaceType.GAS ? hydrogen.getGasType() == type || chlorine.getGasType() == type
					: false;
		}

		@Override
		public boolean canTubeConnect(ForgeDirection side) {
			return faces.get(side) == FaceType.GAS;
		}

		@Override
		public void write(ByteBufOutputStream out) throws IOException {
			super.write(out);

			out.writeInt(hydrogen.getStored());
			out.writeInt(chlorine.getStored());
			out.writeInt(refrigerant.getStored());
		}

		@Override
		public void read(ByteBufInputStream in) throws IOException {
			super.read(in);

			hydrogen.setGas(hydrogen.getGas().withAmount(in.readInt()));
			chlorine.setGas(chlorine.getGas().withAmount(in.readInt()));
			refrigerant.setGas(refrigerant.getGas().withAmount(in.readInt()));
		}

		@Override
		public void writeToNBT(NBTTagCompound tag) {
			super.writeToNBT(tag);

			NBTTagCompound hyd = new NBTTagCompound();
			hydrogen.write(hyd);
			tag.setTag("Hydrogen", hyd);

			NBTTagCompound chl = new NBTTagCompound();
			chlorine.write(chl);
			tag.setTag("Chlorine", chl);

			NBTTagCompound ref = new NBTTagCompound();
			refrigerant.write(ref);
			tag.setTag("Refrigerant", ref);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			super.readFromNBT(tag);

			hydrogen = GasTank.readFromNBT(tag.getCompoundTag("Hydrogen"));
			chlorine = GasTank.readFromNBT(tag.getCompoundTag("Chlorine"));
			refrigerant = GasTank.readFromNBT(tag.getCompoundTag("Refrigerant"));
		}
	}
}
