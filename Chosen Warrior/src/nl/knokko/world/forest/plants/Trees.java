package nl.knokko.world.forest.plants;

import java.awt.Color;
import java.util.Random;

import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;

public final class Trees {
	
	private static final Random random = new Random();

	public static final TreeSettings OAK_MODEL_1 = createOakModel(934859236L, (byte) -128);
	public static final TreeSettings OAK_MODEL_2 = createOakModel(42969683498L, (byte) -127);
	public static final TreeSettings OAK_MODEL_3 = createOakModel(3925094650L, (byte) -126);
	
	public static final ModelTexture SPRUCE_TEXTURE_1 = createSpruceTexture(934859236L, (byte) -128);
	public static final ModelTexture SPRUCE_TEXTURE_2 = createSpruceTexture(42969683498L, (byte) -127);
	public static final ModelTexture SPRUCE_TEXTURE_3 = createSpruceTexture(3925094650L, (byte) -126);
	
	public static final TreeSettings[] OAK_MODELS = new TreeSettings[]{OAK_MODEL_1, OAK_MODEL_2, OAK_MODEL_3};
	public static final ModelTexture[] SPRUCE_TEXTURES = new ModelTexture[]{SPRUCE_TEXTURE_1, SPRUCE_TEXTURE_2, SPRUCE_TEXTURE_3};
	
	public static final TreeSettings settingsFromID(byte id){
		if(id == -128)
			return OAK_MODEL_1;
		if(id == -127)
			return OAK_MODEL_2;
		if(id == -126)
			return OAK_MODEL_3;
		throw new IllegalArgumentException("Unkown TreeSettings ID: " + id);
	}
	
	public static final ModelTexture textureFromID(byte id){
		if(id == -128)
			return SPRUCE_TEXTURE_1;
		if(id == -127)
			return SPRUCE_TEXTURE_2;
		if(id == -126)
			return SPRUCE_TEXTURE_3;
		throw new IllegalArgumentException("Unkown Tree Texture ID: " + id);
	}
	
	public static final TreeSettings getOakModel(){
		return OAK_MODELS[random.nextInt(OAK_MODELS.length)];
	}
	
	public static final ModelTexture getSpruceTexture(){
		return SPRUCE_TEXTURES[random.nextInt(SPRUCE_TEXTURES.length)];
	}
	
	public static final TreeSettings createOakModel(long seed, byte id){
		return new TreeSettings(30, 0.75f, 60, 4.5f, 5, 0.25f, 0.04f, 0.5f, 10, 256, seed, id);
	}
	
	public static final ModelTexture createSpruceTexture(long seed, byte id){
		return Resources.getTreeTexture(new Color(130, 50, 0), new Color(20, 75, 50), 0.5f, seed, id);
	}
}
