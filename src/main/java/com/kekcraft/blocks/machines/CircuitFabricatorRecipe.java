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

	private transient ItemStack inputA;
	private transient ItemStack inputB;
	private transient ItemStack output;

	public CircuitFabricatorRecipe(ItemStack inputA, ItemStack inputB, Item circuit) {
		this.inputA = inputA;
		this.inputB = inputB;
		this.output = new ItemStack(circuit);
	}

	@Override
	public boolean satifies(ItemStack[] slots) {
		return has(slots[0], KekCraft.factory.getItem("RefinedSilicon")) && slots[1] != null
				&& slots[1].getItem() == inputA.getItem() && slots[2] != null && slots[2].getItem() == inputB.getItem();
	}

	private boolean has(ItemStack itemStack, Item item) {
		return itemStack != null && itemStack.getItem() == item && itemStack.stackSize >= 1;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return ((index == 0 || index == 2) && element.getItem() == KekCraft.factory.getItem("RefinedSilicon"))
				|| (index == 1 && inputA.getItem() == element.getItem())
				|| (index == 2 && inputB.getItem() == element.getItem());
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public ItemStack getInput(int slot) {
		return slot == 0 ? new ItemStack(KekCraft.factory.getItem("RefinedSilicon")) : (slot == 1 ? inputA : inputB);
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
		write(nbt, inputA, "InputA");
		write(nbt, inputB, "InputB");
		write(nbt, output, "Output");
	}

	private void write(NBTTagCompound nbt, ItemStack stack, String name) {
		NBTTagCompound inTag = new NBTTagCompound();
		stack.writeToNBT(inTag);
		nbt.setTag(name, inTag);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		inputA = read(nbt, "InputA");
		inputB = read(nbt, "InputB");
		output = read(nbt, "Output");
	}

	private ItemStack read(NBTTagCompound nbt, String name) {
		return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(name));
	}
}
