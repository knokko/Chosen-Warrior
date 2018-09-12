package nl.knokko.world.forest.plants;

import java.awt.Color;
import java.util.Random;

import nl.knokko.textures.ModelTexture;

import static nl.knokko.util.Resources.getFlowerTexture;

public class Flowers {
	
	public static final FlowerSettings DEFAULT_1 = createDefaultFlower(984392639L, (byte) -128);
	
	public static final ModelTexture YELLOW_FLOWER = createFlowerTexture(Color.YELLOW, (byte) -128);
	public static final ModelTexture RED_FLOWER = createFlowerTexture(Color.RED, (byte) -127);
	public static final ModelTexture MAGENTA_FLOWER = createFlowerTexture(Color.MAGENTA, (byte) -126);
	public static final ModelTexture BLUE_FLOWER = createFlowerTexture(Color.BLUE, (byte) -125);
	public static final ModelTexture GREEN_FLOWER = createFlowerTexture(new Color(0, 255, 128), (byte) -124);
	public static final ModelTexture ORANGE_FLOWER = createFlowerTexture(Color.ORANGE, (byte) -123);
	
	public static final ModelTexture[] TEXTURES = {YELLOW_FLOWER, RED_FLOWER, MAGENTA_FLOWER, BLUE_FLOWER, GREEN_FLOWER, ORANGE_FLOWER};
	
	public static FlowerSettings settingsFromID(byte id){
		if(id == -128)
			return DEFAULT_1;
		throw new IllegalArgumentException("Unknown FlowerSettings ID: " + id);
	}
	
	public static ModelTexture textureFromID(byte id){
		return TEXTURES[id + 128];
	}

	public static final FlowerSettings createDefaultFlower(long seed, byte id){
		return new FlowerSettings(0.4f, 0.05f, 0.11f, 0.17f, seed, id);
	}
	
	public static ModelTexture randomTexture(Random random){
		return TEXTURES[random.nextInt(TEXTURES.length)];
	}
	
	public static ModelTexture createFlowerTexture(Color color, byte id){
		return getFlowerTexture(new Color(0, 100, 0), color, 0.1f, 39488758L, id);
	}
}
