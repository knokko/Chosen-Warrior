package nl.knokko.textures;

import java.awt.Color;

public class ModelDinoTexture extends ModelTexture {
	
	private Color skin;
	private Color nails;
	
	private float maxDifference;
	private long seed;

	public ModelDinoTexture(int textureID, Color skin, Color nails, float maxDifference, long seed) {
		super(textureID, Material.ORGANIC, null);
		this.skin = skin;
		this.nails = nails;
		this.maxDifference = maxDifference;
		this.seed = seed;
	}

	public Color getSkinColor() {
		return skin;
	}

	public Color getNailColor() {
		return nails;
	}

	public float getMaxDifference() {
		return maxDifference;
	}

	public long getSeed() {
		return seed;
	}

}
