package nl.knokko.guis.buttons;

import java.awt.Color;
import java.lang.reflect.Method;

import nl.knokko.guis.IGui;
import nl.knokko.guis.render.GuiTexture;
import nl.knokko.main.Game;
import nl.knokko.util.Options;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

public class GuiKeyBindButton extends GuiButton {
	
	protected Method getter;
	protected Method setter;
	
	protected String text;
	protected Color textColor;

	public GuiKeyBindButton(Vector2f position, Vector2f scale, Color backGround, Color textColor, String text, String keyName) {
		super(position, scale, backGround, textColor, text);
		this.text = text;
		this.textColor = textColor;
		Options options = Game.getOptions();
		try {
			getter = options.getClass().getMethod("get" + keyName + "Key");
		} catch (Exception ex) {
			throw new RuntimeException("Can't find the getter of " + options.getClass() + " named get" + keyName + "Key", ex);
		}
		try {
			setter = options.getClass().getMethod("set" + keyName + "Key", int.class);
		} catch(Exception ex){
			throw new RuntimeException("Can't find the setter of " + options.getClass() + " named get" + keyName + "Key", ex);
		}
	}

	@Override
	public void click(int x, int y, int button, IGui gui) {
		if(gui.getCurrentButton() == this){
			try {
				setter.invoke(Game.getOptions(), -button - 1);
			} catch (Exception ex) {
				throw new RuntimeException("Can't use the setter " + setter + " on " + Game.getOptions() + " with value " + (-button - 1), ex);
			} 
		}
		else
			gui.setCurrentButton(this);
	}
	
	@Override
	public void keyPressed(int key, char name, IGui gui){
		if(key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_NONE){
			try {
				setter.invoke(Game.getOptions(), key);
			} catch (Exception ex) {
				throw new RuntimeException("Can't use the setter " + setter + " on " + Game.getOptions() + " with value " + key, ex);
			} 
		}
		else if(key == Keyboard.KEY_ESCAPE){
			gui.setCurrentButton(null);
		}
	}
	
	@Override
	public GuiTexture[] getTextures(){
		try {
			int key = (int)getter.invoke(Game.getOptions());
			String name = Options.getKeyName(key);
			texture.setText(text + ": " + name, textColor);
		} catch(Exception ex){
			throw new RuntimeException("Can't use the getter " + getter + " on " + Game.getOptions(), ex);
		}
		return super.getTextures();
	}
}
