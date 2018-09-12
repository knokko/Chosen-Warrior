package nl.knokko.guis.render;

import java.awt.Color;

import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;

import org.lwjgl.util.vector.Vector2f;

public class GuiDoubleTexture extends GuiTexture {
	
	protected ModelTexture texture2;

	public GuiDoubleTexture(ModelTexture texture1, ModelTexture texture2, Vector2f position, Vector2f scale) {
		super(texture1, position, scale);
		this.texture2 = texture2;
	}
	
	@Override
	public ModelTexture[] getTexture(){
		return new ModelTexture[]{texture, texture2};
	}
	
	@Override
	public void setText(String text, Color color){
		texture2 = Resources.getText(text, color);
	}
}
