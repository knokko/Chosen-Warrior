package nl.knokko.world.forest.animals;

import java.awt.Color;

import nl.knokko.textures.ModelDinoTexture;
import nl.knokko.util.Resources;

public final class Dinosaurs {
	
	public static final DinoSettings HUMANOID = createDinoHumanoidModel(982452135986323L);
	public static final DinoSettings DWOORF = createDwoorfModel(38156389235689L);
	
	public static final ModelDinoTexture RED_SKIN_1 = createRedSkin(42962934593L);
	public static final ModelDinoTexture DARK_PURPLE_SKIN = createDarkPurpleSkin(347287254L);

	public static DinoSettings createDinoHumanoidModel(long seed){
		return new DinoSettings(0.2f, 0.25f, 0.4f, 0.1f, 0.45f, 0.125f, 0.55f, 0.1f, 0.15f, 0.05f, 0.1f, 0.5f, 5, 0, 0, 0, seed, 32);
	}
	
	public static DinoSettings createDwoorfModel(long seed){
		return new DinoSettings(4f, 7f, 12f, 2f, 9f, 2f, 9f, 2f, 3f, 1f, 0.5f, 8f, 8, 13f, 0.9f, 5, seed, 128);
	}
	
	public static ModelDinoTexture createRedSkin(long seed){
		return Resources.getDinoTexture(new Color(150, 25, 40), new Color(50, 50, 50), 0.3f, seed);
	}
	
	public static ModelDinoTexture createDarkPurpleSkin(long seed){
		return Resources.getDinoTexture(new Color(50, 0, 120), Color.BLACK, 0.2f, seed);
	}
}
