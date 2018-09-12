package nl.knokko.guis;

import java.util.ArrayList;

import nl.knokko.guis.buttons.IButton;
import nl.knokko.guis.render.GuiRenderer;
import nl.knokko.guis.render.GuiTexture;

import org.lwjgl.input.Cursor;

public interface IGui {
	
	ArrayList<GuiTexture> getGuiTextures();
	
	ArrayList<IButton> getButtons();
	
	Cursor getCursor();
	
	IButton getCurrentButton();
	
	void update();
	
	void render(GuiRenderer renderer);
	
	void setCurrentButton(IButton button);
	
	void keyPressed(int key, char character);
	
	void keyReleased(int key, char character);
	
	boolean canClose();
	
	boolean renderGameWorld();
}
