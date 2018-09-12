package nl.knokko.world.forest.plants;

public class FlowerSettings {
	
	public final long seed;
	
	public final float stemHeight;
	public final float stemRadius;
	
	public final float flowerRadius;
	public final float outerRadius;
	
	public final byte id;

	public FlowerSettings(float stemHeight, float stemRadius, float flowerRadius, float outerRadius, long seed, byte id) {
		this.stemHeight = stemHeight;
		this.stemRadius = stemRadius;
		this.flowerRadius = flowerRadius;
		this.outerRadius = outerRadius;
		this.seed = seed;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof FlowerSettings){
			FlowerSettings set = (FlowerSettings) other;
			return set.seed == seed && set.stemHeight == stemHeight && set.stemRadius == stemRadius && set.outerRadius == outerRadius;
		}
		return false;
	}
}
