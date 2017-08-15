package com.craftsharp.api.ui;

import java.awt.Rectangle;

import com.craftsharp.CraftSharp;

import net.minecraft.util.ResourceLocation;

public abstract class UIScreen {
	private String name;
	protected MachineUI ui;
	private ResourceLocation tex;

	public UIScreen(MachineUI ui, String name) {
		this.ui = ui;
		this.name = name;
	}

	public void drawTooltip(int x, int y, int width, int height, String text) {
		ui.drawTooltip(x, y, width, height, text);
	}

	public void drawUV(int destX, int destY, int srcX, int srcY, int width, int height) {
		ui.drawTexturedModalRect(destX, destY, srcX, srcY, width, height);
	}

	public UIScreen addClickListener(int x, int y, int w, int h, Runnable r) {
		ui.addClickListener(this, new Rectangle(x, y, w, h), r);
		return this;
	}

	public UIScreen addScreenSwitch(int x, int y, int w, int h, final String screen) {
		this.addClickListener(x, y, w, h, new Runnable() {
			@Override
			public void run() {
				ui.setCurrentUIScreen(screen);
			}
		});
		return this;
	}

	public String getName() {
		return name;
	}

	public void render(MachineTileEntity e) {
		
	}
	
	public void load(MachineUI e) {}

	public ResourceLocation getBackgroundTexture(Machine block) {
		return tex == null
				? (tex = new ResourceLocation(CraftSharp.MODID, "textures/ui/" + block.background + "_" + name + ".png"))
				: tex;
	}
}
