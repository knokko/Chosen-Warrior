package nl.knokko.world.forest.plants;

public class PlantSettings {
	
	public final long seed;
	
	public final int parts;
	
	public final float maxPitch;
	public final float minPitch;
	
	public final float width;
	public final float length;
	
	public final byte id;

	public PlantSettings(float length, float width, float minPitch, float maxPitch, int parts, long seed, byte id) {
		this.length = length;
		this.width = width;
		this.minPitch = minPitch;
		this.maxPitch = maxPitch;
		this.parts = parts;
		this.seed = seed;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof PlantSettings){
			PlantSettings set = (PlantSettings) other;
			return set.length == length && set.width == width && set.minPitch == minPitch && set.maxPitch == maxPitch && set.parts == parts && set.seed == seed;
		}
		return false;
	}
}
