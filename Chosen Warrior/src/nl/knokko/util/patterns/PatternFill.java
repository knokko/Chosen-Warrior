package nl.knokko.util.patterns;

import java.awt.Color;
import java.awt.image.BufferedImage;

import nl.knokko.util.TextureArea;

public class PatternFill implements ColorPattern {
	
	public static void paint(BufferedImage image, int minX, int minY, int maxX, int maxY, int rgb){
		for(int x = minX; x <= maxX; x++)
			for(int y = minY; y <= maxY; y++)
				image.setRGB(x, y, rgb);
	}
	
	public static void paint(BufferedImage image, int minX, int minY, int maxX, int maxY, Color color){
		paint(image, minX, minY, maxX, maxY, color.getRGB());
	}
	
	private final int rgb;

	public PatternFill(Color fillColor) {
		rgb = fillColor.getRGB();
	}

	@Override
	public void paint(BufferedImage image, int minX, int minY, int maxX, int maxY) {
		paint(image, minX, minY, maxX, maxY, rgb);
	}

	@Override
	public void paint(BufferedImage image, TextureArea area) {
		paint(image, area.getMinX(), area.getMinY(), area.getMaxX(), area.getMaxY());
	}

}
