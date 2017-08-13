package com.kekcraft.api.ui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.player.InventoryPlayer;

public class MachineUI extends GuiContainerModified {
	public MachineTileEntity tileEntity;

	public int left;
	public int top;
	public int mouseX;
	public int mouseY;
	private Machine block;
	private UIScreen currentScreen;
	private HashMap<String, UIScreen> screens = new HashMap<String, UIScreen>();
	private ArrayList<ClickListener> clickListeners = new ArrayList<ClickListener>();

	public MachineUI(InventoryPlayer inventory, Machine block, MachineTileEntity tileEntity) {
		super(new MachineContainer(inventory, block, tileEntity));

		tileEntity.ui = this;
		if (tileEntity.onUISet != null)
			tileEntity.onUISet.run();

		this.tileEntity = tileEntity;
		this.block = block;
	}

	public void addScreen(UIScreen screen) {
		screens.put(screen.getName(), screen);
	}

	public void setCurrentUIScreen(String name) {
		if (screens.containsKey(name)) {
			currentScreen = screens.get(name);
			allow.clear();
			disallow.clear();
			currentScreen.load(this);
		}
	}
	
	public UIScreen getUIScreen(String name) {
		return screens.get(name);
	}

	public UIScreen getCurrentUIScreen() {
		return currentScreen;
	}

	void drawTooltip(int x, int y, int width, int height, String text) {
		if (mouseX > x && mouseX < x + width) {
			if (mouseY > y && mouseY < y + height) {
				this.func_146283_a(Arrays.asList(new String[] { text }), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1f, 1f, 1f, 1f);

		this.mc.getTextureManager().bindTexture(
				currentScreen == null ? block.getDefaultBackground() : currentScreen.getBackgroundTexture(block));
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
	public void drawScreen(final int x, final int y, final float par3) {
		this.mouseX = x;
		this.mouseY = y;

		MachineUI.super.drawScreen(x, y, par3);
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int par3) {
		MachineUI.super.mouseClicked(x, y, par3);

		for (ClickListener listener : clickListeners) {
			if (listener.screen == currentScreen) {
				if (listener.rect.contains(x - left, y - top)) {
					listener.callback.run();
				}
			}
		}
	}

	public void addClickListener(UIScreen uiScreen, Rectangle rectangle, Runnable r) {
		clickListeners.add(new ClickListener(uiScreen, rectangle, r));
	}

	private static class ClickListener {
		public UIScreen screen;
		public Rectangle rect;
		public Runnable callback;

		public ClickListener(UIScreen screen, Rectangle rect, Runnable callback) {
			this.screen = screen;
			this.rect = rect;
			this.callback = callback;
		}
	}
}
