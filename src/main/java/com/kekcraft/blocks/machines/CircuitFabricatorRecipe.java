package com.kekcraft.blocks.machines;

import java.util.Arrays;
import java.util.List;

import com.kekcraft.KekCraft;
import com.kekcraft.api.ui.IMachineRecipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CircuitFabricatorRecipe implements IMachineRecipe {
	private static final long serialVersionUID = 428852275461702654L;

	private transient ItemStack input;
	private transient ItemStack output;

	public CircuitFabricatorRecipe(ItemStack input, Item circuit) {
		this.input = input;
		this.output = new ItemStack(circuit);
	}

	@Override
	public boolean satifies(ItemStack[] slots) {
		return has(slots[0], KekCraft.factory.getItem("RefinedSilicon")) && slots[1] != null
				&& slots[1].getItem() == input.getItem() && has(slots[2], KekCraft.factory.getItem("RefinedSilicon"));
	}

	private boolean has(ItemStack itemStack, Item item) {
		return itemStack != null && itemStack.getItem() == item && itemStack.stackSize >= 1;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return ((index == 0 || index == 2) && element.getItem() == KekCraft.factory.getItem("RefinedSilicon"))
				|| (index == 1 && input.getItem() == element.getItem());
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public ItemStack getInput(int slot) {
		return (slot == 0 || slot == 2) ? new ItemStack(KekCraft.factory.getItem("RefinedSilicon")) : input;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(0, 1, 2);
	}

	@Override
	public int getCookTime() {
		return 400;
	}

	@Override
	public int getFuelCost() {
		return 3200;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		write(nbt, input, "Input");
		write(nbt, output, "Output");
	}

	private void write(NBTTagCompound nbt, ItemStack stack, String name) {
		NBTTagCompound inTag = new NBTTagCompound();
		stack.writeToNBT(inTag);
		nbt.setTag(name, inTag);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		input = read(nbt, "Input");
		output = read(nbt, "Output");
	}

	private ItemStack read(NBTTagCompound nbt, String name) {
		return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(name));
	}
}
