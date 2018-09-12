package nl.knokko.world.forest.animals;

public class DinoSettings {
	
	public final long seed;
	
	public final float headRadius;
	public final float bodyRadius;
	public final float armRadius;
	public final float legRadius;
	public final float tailRadius;
	public final float footRadius;
	public final float neckRadius;
	
	public final float bodyLength;
	public final float armLength;
	public final float legLength;
	public final float tailLength;
	public final float footLength;
	public final float footHeight;
	public final float neckLength;
	
	public final int imageSize;
	public final int neckParts;
	public final int tailParts;

	public DinoSettings(float headWidth, float bodyWidth, float bodyHeight, float armWidth, float armHeight, float legWidth, float legHeight, float footWidth, float footLength, float footHeight, float tailWidth, float tailHeight, int tailParts, float neckLength, float neckRadius, int neckParts, long seed, int imageSize) {
		headRadius = headWidth / 2;
		bodyRadius = bodyWidth / 2;
		armRadius = armWidth / 2;
		legRadius = legWidth / 2;
		tailRadius = tailWidth / 2;
		footRadius = footWidth / 2;
		bodyLength = bodyHeight;
		armLength = armHeight;
		legLength = legHeight;
		tailLength = tailHeight;
		this.neckLength = neckLength;
		this.neckRadius = neckRadius;
		this.footLength = footLength;
		this.footHeight = footHeight;
		this.tailParts = tailParts;
		this.neckParts = neckParts;
		this.seed = seed;
		this.imageSize = imageSize;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof DinoSettings){
			DinoSettings set = (DinoSettings) other;
			return set.seed == seed && set.imageSize == imageSize && set.bodyRadius == bodyRadius && set.armRadius == armRadius && set.legRadius == legRadius && set.tailRadius == tailRadius && set.bodyLength == bodyLength && set.armLength == armLength && set.legLength == legLength && set.tailLength == tailLength && set.neckLength == neckLength && set.neckRadius == neckRadius && set.neckParts == neckParts;
		}
		return false;
	}
}
