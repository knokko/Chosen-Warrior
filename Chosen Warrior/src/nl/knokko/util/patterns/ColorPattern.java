package nl.knokko.util.patterns;

import java.awt.image.BufferedImage;

import nl.knokko.util.TextureArea;

public interface ColorPattern {
	
	/**
	 * Paint this ColorPattern on the specified BufferedImage between the specified bounds.
	 * @param image The image to paint on
	 * @param minX The minimum x-coordinate to paint
	 * @param minY The minimum y-coordinate to paint
	 * @param maxX The maximum x-coordinate to paint
	 * @param maxY The maximum y-coordinate to paint
	 */
	void paint(BufferedImage image, int minX, int minY, int maxX, int maxY);
	
	void paint(BufferedImage image, TextureArea area);
}
