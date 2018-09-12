package nl.knokko.design.guis;

import org.lwjgl.util.vector.Vector2f;

import nl.knokko.design.main.MapDesigner;
import nl.knokko.guis.Gui;
import nl.knokko.guis.render.GuiRenderer;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;

public class DefaultGui extends Gui {
	
	private static final Vector2f SCALE = new Vector2f(1f, 0.1f);
	private static final Vector2f CROSS_SCALE = new Vector2f(0.05f, 0.05f);
	
	private static final Vector2f ORIGIN = new Vector2f();
	private static final Vector2f TRANSLATION_X = new Vector2f(0f, 0.8f);
	private static final Vector2f TRANSLATION_Y = new Vector2f(0f, 0.6f);
	private static final Vector2f TRANSLATION_Z = new Vector2f(0f, 0.4f);
	
	private static final ModelTexture[] CROSS = new ModelTexture[]{Resources.getCrossTexture()};
	
	private ModelTexture[] coordTextureX = new ModelTexture[1];
	private ModelTexture[] coordTextureY = new ModelTexture[1];
	private ModelTexture[] coordTextureZ = new ModelTexture[1];

	public DefaultGui() {}
	
	private void refresh(){
		coordTextureX[0] = Resources.getTextLine("X: " + (int) MapDesigner.getCamera().getPosition().getX());
		coordTextureY[0] = Resources.getTextLine("Y: " + (int) MapDesigner.getCamera().getPosition().getY());
		coordTextureZ[0] = Resources.getTextLine("Z: " + (int) MapDesigner.getCamera().getPosition().getZ());
	}
	
	@Override
	public void render(GuiRenderer renderer){
		refresh();
		renderer.start();
		renderer.renderTextures(TRANSLATION_X, SCALE, coordTextureX);
		renderer.renderTextures(TRANSLATION_Y, SCALE, coordTextureY);
		renderer.renderTextures(TRANSLATION_Z, SCALE, coordTextureZ);
		renderer.renderTextures(ORIGIN, CROSS_SCALE, CROSS);
		renderer.stop();
	}
}
