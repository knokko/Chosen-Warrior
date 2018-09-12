package nl.knokko.world.forest.plants;

public class MushroomSettings {
	
	public final float stemHeight;
	public final float stemRadius;
	
	public final float topHeight;
	public final float topRadius;
	
	public final byte id;

	public MushroomSettings(float stemHeight, float stemRadius, float topHeight, float topRadius, byte id) {
		this.stemHeight = stemHeight;
		this.stemRadius = stemRadius;
		this.topHeight = topHeight;
		this.topRadius = topRadius;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof MushroomSettings){
			MushroomSettings set = (MushroomSettings) other;
			return set.stemHeight == stemHeight && set.stemRadius == stemRadius && set.topHeight == topHeight && set.topRadius == topRadius;
		}
		return false;
	}
}
