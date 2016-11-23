package kekcraft.api.ui;

import java.io.Serializable;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IElectricMachineRecipe extends Serializable {
	public boolean satifies(ItemStack[] slots, int[] indicies);

	public boolean isValidElementOfRecipe(ItemStack element, int index);
	
	public boolean isInstant();

	public ItemStack getInput(int slot);

	public ItemStack getOutput();

	public List<Integer> getRecipeSlots();

	public int getCookTime();

	public int getEnergyCost();
	
	public void writeToNBT(NBTTagCompound nbt);
	
	public void readFromNBT(NBTTagCompound nbt);
}
