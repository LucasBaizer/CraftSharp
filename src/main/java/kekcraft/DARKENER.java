package kekcraft;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DARKENER {
	public static void main(String[] args) throws IOException {
		// File img = new File(
		// "C:\\Users\\Lucas\\Desktop\\Forge
		// Utilities\\build\\tmp\\decompile\\assets\\minecraft\\textures\\items\\iron_ingot.png");
		File img = new File(
				"C:/Users/Lucas/Desktop/Forge Utilities/src/main/resources/assets/kekcraft/textures/items/DustAluminum.png");
		File dest = new File(
				"C:/Users/Lucas/Desktop/Forge Utilities/src/main/resources/assets/kekcraft/textures/items/DustAluminum.png");

		int darken = 5;
		BufferedImage image = ImageIO.read(img);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color color = new Color(image.getRGB(x, y));
				if (color.getRGB() != -16777216) {
					image.setRGB(x, y, new Color(clamp(color.getRed() - darken), clamp(color.getGreen() - darken),
							clamp(color.getBlue() - darken), color.getAlpha()).getRGB());
				}
			}
		}
		ImageIO.write(image, "PNG", dest);
	}

	private static int clamp(int val) {
		return val < 0 ? 0 : (val > 255 ? 255 : val);
	}
}
