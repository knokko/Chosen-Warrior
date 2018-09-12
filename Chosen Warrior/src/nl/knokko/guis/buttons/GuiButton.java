package nl.knokko.guis.buttons;

import java.awt.Color;

import nl.knokko.guis.IGui;
import nl.knokko.guis.IScrollGui;
import nl.knokko.guis.render.GuiDoubleTexture;
import nl.knokko.guis.render.GuiTexture;
import nl.knokko.render.DisplayManager;
import nl.knokko.util.Resources;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public abstract class GuiButton implements IButton {
	
	protected GuiDoubleTexture texture;
	
	public GuiButton(Vector2f position, Vector2f scale, Color backGround, Color textColor, String text){
		texture = new GuiDoubleTexture(Resources.getDinoTexture(backGround, Color.BLACK, 0.1f), Resources.getText(text, textColor), position, scale);
	}

	@Override
	public GuiTexture[] getTextures() {
		return new GuiTexture[]{texture};
	}

	@Override
	public boolean isHit(int x, int y, IGui gui) {
		if(gui instanceof IScrollGui){
			IScrollGui scroll = (IScrollGui) gui;
			y -= scroll.getCurrentScroll() * Display.getHeight();
		}
		return x >= texture.getMinX() && y >= texture.getMinY() && x <= texture.getMaxX() && y <= texture.getMaxY();
	}
	
	@Override
	public int clickCooldown(int x, int y, IGui gui){
		return DisplayManager.FPS_CAP / 2;
	}
	
	@Override
	public void keyPressed(int key, char character, IGui gui){}
	
	@Override
	public void keyReleased(int key, char character, IGui gui){}

	@Override
	public abstract void click(int x, int y, int button, IGui gui);
	
	@Override
	public void mouseDown(int x, int y, int button, IGui gui){}
}
