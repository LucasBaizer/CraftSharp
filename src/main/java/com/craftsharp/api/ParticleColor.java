package com.craftsharp.api;

public class ParticleColor {
	public static final ParticleColor RED = new ParticleColor(1f, 0f, 0f);
	public static final ParticleColor GREEN = new ParticleColor(0f, 1f, 0f);
	public static final ParticleColor BLUE = new ParticleColor(0f, 0f, 1f);
	public static final ParticleColor CYAN = new ParticleColor(0f, 1f, 1f);
	public static final ParticleColor WHITE = new ParticleColor(1f, 1f, 1f);
	public static final ParticleColor BLACK = new ParticleColor(0f, 0f, 0f);

	private float r;
	private float g;
	private float b;

	public ParticleColor(int r, int g, int b) {
		this(r / 255f, g / 255f, b / 255f);
	}

	public ParticleColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public float getRed() {
		return r;
	}

	public void setRed(float r) {
		this.r = r;
	}

	public float getGreen() {
		return g;
	}

	public void setGreen(float g) {
		this.g = g;
	}

	public float getBlue() {
		return b;
	}

	public void setBlue(float b) {
		this.b = b;
	}
}
