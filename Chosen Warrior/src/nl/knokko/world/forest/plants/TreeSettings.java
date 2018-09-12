package nl.knokko.world.forest.plants;

public class TreeSettings {
	
	public final long seed;
	
	public final float baseLength;
	public final float baseWidth;
	public final float baseTwigs;
	
	public final float lengthDivider;
	public final float widthDivider;
	public final float twigsDivider;
	
	public final float maxBow;
	public final float minTwigHeight;
	
	public final int twigParts;
	public final int imageSize;
	
	public final byte id;

	public TreeSettings(float baseLength, float baseWidth, float baseTwigs, float lengthDivider, float widthDivider, float twigsDivider, float maxBow, float minTwigHeight, int twigParts, int imageSize, long seed, byte id){
		this.baseLength = baseLength;
		this.baseWidth = baseWidth;
		this.baseTwigs = baseTwigs;
		this.lengthDivider = lengthDivider;
		this.widthDivider = widthDivider;
		this.twigsDivider = twigsDivider;
		this.twigParts = twigParts;
		this.maxBow = maxBow;
		this.minTwigHeight = minTwigHeight;
		this.imageSize = imageSize;
		this.seed = seed;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object other){
		if(other == this)
			return true;
		if(other instanceof TreeSettings){
			TreeSettings set = (TreeSettings) other;
			return set.seed == seed && set.baseLength == baseLength && set.baseWidth == baseWidth && set.baseTwigs == baseTwigs && set.lengthDivider == lengthDivider && set.widthDivider == widthDivider && set.twigsDivider == twigsDivider && set.maxBow == maxBow && set.minTwigHeight == minTwigHeight && set.twigParts == twigParts && set.imageSize == imageSize;
		}
		return false;
	}
}
