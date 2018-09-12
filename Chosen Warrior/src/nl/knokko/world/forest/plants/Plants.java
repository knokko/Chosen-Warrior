package nl.knokko.world.forest.plants;

import java.util.Random;

public class Plants {

	public static final PlantSettings THIN_PLANT = createThin(348593469L, -128);
	public static final PlantSettings BASIC_PLANT = createBasic(1287459L, -127);
	public static final PlantSettings THICK_PLANT = createThick(8730283775L, -126);
	
	public static final PlantSettings[] PLANTS = {THIN_PLANT, BASIC_PLANT, THICK_PLANT};
	
	public static PlantSettings settingsFromID(byte id){
		return PLANTS[id + 128];
	}
	
	public static PlantSettings createThin(long seed, int byteID){
		return new PlantSettings(1.0f, 0.2f, 15, 35, 10, seed, (byte) byteID);
	}
	
	public static PlantSettings createBasic(long seed, int byteID){
		return new PlantSettings(1.1f, 0.25f, 10, 40, 20, seed, (byte) byteID);
	}
	
	public static PlantSettings createThick(long seed, int byteID){
		return new PlantSettings(1.2f, 0.3f, 10, 50, 30, seed, (byte) byteID);
	}
	
	public static PlantSettings getRandom(Random random){
		return PLANTS[random.nextInt(PLANTS.length)];
	}
}
