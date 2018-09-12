package nl.knokko.guis.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import nl.knokko.guis.IGui;
import nl.knokko.guis.IScrollGui;
import nl.knokko.guis.buttons.IButton;
import nl.knokko.render.Loader;
import nl.knokko.render.model.RawModel;
import nl.knokko.shaders.GuiShader;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Maths;

public class GuiRenderer {
	
	private static final float[] QUADS = {-1,1, -1,-1, 1,1, 1,-1};
	
	private final RawModel quad;
	
	private GuiShader shader;

	public GuiRenderer() {
		quad = Loader.loadToVAO(QUADS);
		shader = new GuiShader();
	}
	
	public void start(){
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	public void stop(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void render(ArrayList<GuiTexture> guis, ArrayList<IButton> buttons, IGui igui){
		start();
		for(GuiTexture gui : guis)
			renderGuiTexture(gui, igui);
		for(IButton button : buttons)
			for(GuiTexture gui : button.getTextures())
				renderGuiTexture(gui, igui);
		stop();
	}
	
	public void renderGuiTexture(GuiTexture gui, IGui igui){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		Vector2f translation = new Vector2f(gui.getPosition());
		if(igui instanceof IScrollGui)
			translation.y += ((IScrollGui) igui).getCurrentScroll() * 2;
		renderTextures(translation, gui.getScale(), gui.getTexture());
	}
	
	public void renderTextures(Vector2f translation, Vector2f scale, ModelTexture[] textures){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		Matrix4f matrix = Maths.createTransformationMatrix(translation, scale);
		shader.loadTransformation(matrix);
		for(ModelTexture texture : textures){
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
