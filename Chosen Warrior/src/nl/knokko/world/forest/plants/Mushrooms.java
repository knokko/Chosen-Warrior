package nl.knokko.world.forest.plants;

import java.awt.Color;

import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;

public class Mushrooms {

	public static final MushroomSettings DEFAULT = new MushroomSettings(0.15f, 0.075f, 0.05f, 0.2f, (byte) -128);
	
	public static final ModelTexture RED_MUSHROOM = Resources.getMushroomTexture(new Color(150, 150, 150), Color.RED, Color.WHITE, 5, 5, 0.1f, 39842908698L, (byte) -128);
	public static final ModelTexture POISON_MUSHROOM = Resources.getMushroomTexture(new Color(50, 50, 50), new Color(100, 0, 100), new Color(0, 100, 0), 5, 5, 0.1f, 3984229840909283498L, (byte) -127);
	
	public static final MushroomSettings settingsFromID(byte id){
		if(id == -128)
			return DEFAULT;
		throw new IllegalArgumentException("Unknown MushroomSettings ID: " + id);
	}

	public static final ModelTexture textureFromID(byte id){
		if(id == -128)
			return RED_MUSHROOM;
		if(id == -127)
			return POISON_MUSHROOM;
		throw new IllegalArgumentException("Unknown Mushroom Texture ID: " + id);
	}
}
