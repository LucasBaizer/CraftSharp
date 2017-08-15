package com.craftsharp.blocks.machines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.craftsharp.api.ui.IMachineRecipe;
import com.craftsharp.api.ui.MachineTileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class UncrafterRecipe implements IMachineRecipe {
	private static final long serialVersionUID = -3125475335385139744L;

	private transient ItemStack input;
	private transient ItemStack[] output;

	public UncrafterRecipe(ItemStack input, Object[] output) {
		this.input = input;
		init(output);
	}

	public UncrafterRecipe(ItemStack input, List<Object> output) {
		this.input = input;
		init(output.toArray(new Object[output.size()]));
	}

	private void init(Object[] output) {
		this.output = new ItemStack[output.length];
		for (int i = 0; i < output.length; i++) {
			if (output[i] instanceof ItemStack) {
				this.output[i] = (ItemStack) output[i];
			} else if (output[i] instanceof ArrayList<?>) {
				this.output[i] = (ItemStack) ((ArrayList<?>) output[i]).get(0);
			}
		}
	}

	@Override
	public boolean satifies(MachineTileEntity entity, ItemStack[] slots) {
		return slots[0] != null && slots[0].getItem() == input.getItem() && slots[0].stackSize == input.stackSize;
	}

	@Override
	public boolean isValidElementOfRecipe(ItemStack element, int index) {
		return index == 0 ? element.getItem() == input.getItem() : false;
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public ItemStack getInput(int slot) {
		return slot == 0 ? input : null;
	}

	@Override
	public List<Integer> getRecipeSlots() {
		return Arrays.asList(0);
	}

	@Override
	public int getCookTime() {
		return -1;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound inTag = new NBTTagCompound();
		input.writeToNBT(inTag);
		nbt.setTag("Input", inTag);
		nbt.setInteger("Outputs", output.length);
		for (int i = 0; i < output.length; i++) {
			NBTTagCompound outTag = new NBTTagCompound();
			output[i].writeToNBT(outTag);
			nbt.setTag("Output-" + i, outTag);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		input = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Input"));

		output = new ItemStack[nbt.getInteger("Outputs")];
		for (int i = 0; i < output.length; i++) {
			output[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Output-" + i));
		}
	}

	public ItemStack[] getOutput() {
		return output;
	}
}
