package nl.knokko.guis.render;

import java.awt.Color;

import nl.knokko.guis.IGui;
import nl.knokko.guis.IScrollGui;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
	
	protected ModelTexture texture;
	
	private Vector2f position;
	private Vector2f scale;
	
	public GuiTexture(ModelTexture texture, Vector2f position, Vector2f scale) {
		super();
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public ModelTexture[] getTexture(){
		return new ModelTexture[]{texture};
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public Vector2f getScale(){
		return scale;
	}
	
	public void setBackGroundColor(Color color){
		texture = Resources.getDinoTexture(color, null, 0.1f);
	}
	
	public void setText(String text, Color color) {}
	
	public int getMinX(){
		return Display.getWidth() / 2 + (int) (position.x * Display.getWidth() / 2 - scale.x * Display.getWidth() / 2);
	}
	
	public int getMinY(){
		return Display.getHeight() / 2 + (int) (position.y * Display.getHeight() / 2 - scale.y * Display.getHeight() / 2);
	}
	
	public int getMaxX(){
		return Display.getWidth() / 2 + (int)(position.x * Display.getWidth() / 2 + scale.x * Display.getWidth() / 2);
	}
	
	public int getMaxY(){
		return Display.getHeight() / 2 + (int)(position.y * Display.getHeight() / 2 + scale.y * Display.getHeight() / 2);
	}
	
	public boolean isHit(int x, int y, IGui gui) {
		if(gui instanceof IScrollGui){
			IScrollGui scroll = (IScrollGui) gui;
			y -= scroll.getCurrentScroll() * Display.getHeight();
		}
		return x >= getMinX() && y >= getMinY() && x <= getMaxX() && y <= getMaxY();
	}
}
