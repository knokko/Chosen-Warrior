package nl.knokko.util.patterns;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import nl.knokko.util.TextureArea;

public class PatternPlanks implements ColorPattern {
	
	public static void paint(BufferedImage image, int minX, int minY, int maxX, int maxY, Color averagePlank, Color averageSplit, float maxDifference, Random random, int plankWidth, int plankHeight, int plankShift){
		PatternAverage.paint(image, minX, minY, maxX, maxY, averagePlank, maxDifference, random);
		int baseX = minX;
		for(int y = minY; y <= maxY; y += plankHeight){
			fillHorizontal(image, minX, maxX, y, averageSplit, maxDifference, random);
			for(int x = baseX; x <= maxX; x += plankWidth){
				if(x >= minX)
					fillVertical(image, y + 1, Math.min(y + plankHeight, maxY), x, averageSplit, maxDifference, random);
			}
			baseX -= plankShift;
		}
	}
	
	public static void fillHorizontal(BufferedImage image, int minX, int maxX, int y, Color color, float maxDifference, Random random){
		for(int x = minX; x <= maxX; x++)
			image.setRGB(x, y, PatternAverage.getDifColor(random, color, maxDifference).getRGB());
	}
	
	public static void fillVertical(BufferedImage image, int minY, int maxY, int x, Color color, float maxDifference, Random random){
		for(int y = minY; y <= maxY; y++)
			image.setRGB(x, y, PatternAverage.getDifColor(random, color, maxDifference).getRGB());
	}
	
	private final Color averagePlankColor;
	private final Color averageSplitColor;
	private final Random random;
	
	private final float maxDifference;
	private final int plankWidth;
	private final int plankHeight;
	private final int plankShift;

	public PatternPlanks(Color averagePlankColor, Color averageSplitColor, Random random, float maxDifference, int plankWidth, int plankHeight, int plankShift) {
		this.averagePlankColor = averagePlankColor;
		this.averageSplitColor = averageSplitColor;
		this.random = random;
		this.maxDifference = maxDifference;
		this.plankWidth = plankWidth;
		this.plankHeight = plankHeight;
		this.plankShift = plankShift;
	}

	@Override
	public void paint(BufferedImage image, int minX, int minY, int maxX, int maxY) {
		paint(image, minX, minY, maxX, maxY, averagePlankColor, averageSplitColor, maxDifference, random, plankWidth, plankHeight, plankShift);
	}

	@Override
	public void paint(BufferedImage image, TextureArea area) {
		paint(image, area.getMinX(), area.getMinY(), area.getMaxX(), area.getMaxY());
	}

}
