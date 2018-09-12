package nl.knokko.util.patterns;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import nl.knokko.util.TextureArea;

public class PatternAverage implements ColorPattern {
	
	public static void paint(BufferedImage image, int minX, int minY, int maxX, int maxY, Color averageColor, float maxDifference, long seed){
		paint(image, minX, minY, maxX, maxY, averageColor, maxDifference, new Random(seed));
	}
	
	public static void paint(BufferedImage image, int minX, int minY, int maxX, int maxY, Color averageColor, float maxDifference, Random random){
		for(int x = minX; x <= maxX; x++)
			for(int y = minY; y <= maxY; y++)
				image.setRGB(x, y, getDifColor(random, averageColor, maxDifference).getRGB());
	}
	
	public static Color getDifColor(Random random, Color basic, float maxDifference){
    	float factor = 1 - maxDifference + random.nextFloat() * maxDifference * 2;
		return new Color(Math.max(Math.min((int)(basic.getRed() * factor), 255), 0), Math.max(Math.min((int)(basic.getGreen() * factor), 255), 0), Math.max(Math.min((int)(basic.getBlue() * factor), 255), 0));
    }
	
	private Color color;
	private float dif;
	private long seed;

	public PatternAverage(Color averageColor, float maxDifference, long seed){
		this.color = averageColor;
		this.dif = maxDifference;
		this.seed = seed;
	}

	@Override
	public void paint(BufferedImage image, int minX, int minY, int maxX, int maxY) {
		paint(image, minX, minY, maxX, maxY, color, dif, seed);
	}

	@Override
	public void paint(BufferedImage image, TextureArea area) {
		paint(image, area.getMinX(), area.getMinY(), area.getMaxX(), area.getMaxY());
	}

}
