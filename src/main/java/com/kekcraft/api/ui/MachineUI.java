package com.kekcraft.api.ui;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class MachineUI extends GuiContainer {
	public MachineTileEntity tileEntity;

	public int left;
	public int top;
	public int mouseX;
	public int mouseY;
	private Machine block;

	public MachineUI(InventoryPlayer inventory, Machine block, MachineTileEntity tileEntity) {
		super(new MachineContainer(inventory, block, tileEntity));

		this.tileEntity = tileEntity;
		this.block = block;
	}

	public void drawTooltip(int x, int y, int width, int height, String text) {
		if (mouseX > x && mouseX < x + width) {
			if (mouseY > y && mouseY < y + height) {
				this.func_146283_a(Arrays.asList(new String[] { text }), mouseX, mouseY);
			}
		}
	}

	public void drawUV(int destX, int destY, int srcX, int srcY, int width, int height) {
		this.drawTexturedModalRect(destX, destY, srcX, srcY, width, height);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1f, 1f, 1f, 1f);

		this.mc.getTextureManager().bindTexture(block.background);
		if (block.hasAnySpecificDimensions()) {
			this.xSize = block.getWindowDimensions().width != -1 ? block.getWindowDimensions().width : this.xSize;
			this.ySize = block.getWindowDimensions().height != -1 ? block.getWindowDimensions().height : this.ySize;
		}
		left = (this.width - this.xSize) / 2;
		top = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);

		block.drawToUI(this, tileEntity);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.mouseX = par1;
		this.mouseY = par2;

		super.drawScreen(par1, par2, par3);
	}
}
