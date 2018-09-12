package nl.knokko.weapons;

public final class KnifeForm {
	
	public final float knifeLength;
	public final float knifeWidth;
	
	public final float baseKnifeHeight;
	public final float maxExtraKnifeHeight;
	public final float maxHeightPoint;
	
	public final float holdLength;
	public final float holdWidth;
	public final float holdHeight;

	public KnifeForm(float holdLength, float holdWidth, float holdHeight, float knifeLength, float knifeWidth, float baseKnifeHeight, float maxExtraKnifeHeight, float maxHeightPoint) {
		this.holdLength = holdLength;
		this.holdWidth = holdWidth;
		this.holdHeight = holdHeight;
		this.knifeLength = knifeLength;
		this.knifeWidth = knifeWidth;
		this.baseKnifeHeight = baseKnifeHeight;
		this.maxExtraKnifeHeight = maxExtraKnifeHeight;
		this.maxHeightPoint = maxHeightPoint;
	}
	
	@Override
	public boolean equals(Object other){
		if(other == this)
			return true;
		if(other instanceof KnifeForm){
			KnifeForm k = (KnifeForm) other;
			return k.knifeLength == knifeLength && k.knifeWidth == knifeWidth && k.baseKnifeHeight == baseKnifeHeight && k.maxExtraKnifeHeight == maxExtraKnifeHeight && k.maxHeightPoint == maxHeightPoint && k.holdLength == holdLength && k.holdWidth == holdWidth && k.holdHeight == holdHeight;
		}
		return false;
	}
}
