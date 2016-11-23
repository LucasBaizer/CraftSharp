package kekcraft.api.ui.energy;

import java.util.Arrays;
import java.util.List;

import kekcraft.api.ui.IElectricMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ElectricMachineRecipe implements IElectricMachineRecipe {
	private static final long serialVersionUID = -9178875502583203568L;

	private transient ItemStack in;
	private transient ItemStack out;
	private int cookTime;
	private int energyCost;
	private int inputSlot;

	public ElectricMachineRecipe(ItemStack in, ItemStack out, int cookTime, int energyCost, int inputSlot) {
		this.in = in;
		this.out = out;
		this.cookTime = cookTime;
		this.energyCost = energyCost;
		this.inputSlot = inputSlot;
	}

	@Override
	public int getEnergyCost() {
		return energyCost;
	}

	@Override
	public boolean satifies(ItemStack[] slots, int[] indicies) {
		return slots[inputSlot] != null && slots[inputSlot].getItem() == in.getItem()
				&& slots[inputSlot].stackSize >= in.stackSize;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return index == inputSlot && element.getItem() == in.getItem();
	}

	@Override
	public ItemStack getInput(int slot) {
		return in;
	}

	@Override
	public ItemStack getOutput() {
		return out;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(inputSlot);
	}

	@Override
	public int getCookTime() {
		return cookTime;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound inTag = new NBTTagCompound();
		in.writeToNBT(inTag);
		nbt.setTag("Input", inTag);

		NBTTagCompound outTag = new NBTTagCompound();
		out.writeToNBT(outTag);
		nbt.setTag("Output", outTag);

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		in = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Input"));
		out = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Output"));
	}

	public boolean isInstant() {
		return cookTime == -1;
	}
}
