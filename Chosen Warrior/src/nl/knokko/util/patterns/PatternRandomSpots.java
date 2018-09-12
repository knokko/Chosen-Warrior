package nl.knokko.util.patterns;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import nl.knokko.util.TextureArea;

public class PatternRandomSpots implements ColorPattern {
	
	public static void paint(BufferedImage image, int minX, int minY, int maxX, int maxY, Color color, float maxDifference, float radius, int amount, Random random){
		for(int i = 0; i < amount; i++){
			int centreX = minX + random.nextInt(maxX - minX + 1);
			int centreY = minY + random.nextInt(maxY - minY + 1);
			int minSX = max((int) (centreX - radius), 0);
			int minSY = max((int) (centreY - radius), image.getHeight() / 2);
			int maxSX = min((int) (centreX + radius + 1), image.getWidth() - 1);
			int maxSY = min((int) (centreY + radius + 1), image.getHeight() - 1);
			int ax = minSX;
			while(ax <= maxSX){
				int ay = minSY;
				while(ay <= maxSY){
					double distance = Point.distance(ax, ay, centreX, centreY);
					if(distance <= radius)
						image.setRGB(ax, ay, PatternAverage.getDifColor(random, color, maxDifference).getRGB());
					++ay;
				}
				++ax;
			}
		}
	}
	
	private final Color spotColor;
	
	private final float radius;
	private final float maxDifference;
	private final int amount;
	
	private final Random random;

	public PatternRandomSpots(Color spotColor, float radius, float maxDifference, int amount, Random random) {
		this.spotColor = spotColor;
		this.radius = radius;
		this.amount = amount;
		this.maxDifference = maxDifference;
		this.random = random;
	}

	@Override
	public void paint(BufferedImage image, int minX, int minY, int maxX, int maxY) {
		paint(image, minX, minY, maxX, maxY, spotColor, maxDifference, radius, amount, random);
	}
	
	@Override
	public void paint(BufferedImage image, TextureArea area) {
		paint(image, area.getMinX(), area.getMinY(), area.getMaxX(), area.getMaxY());
	}

}
