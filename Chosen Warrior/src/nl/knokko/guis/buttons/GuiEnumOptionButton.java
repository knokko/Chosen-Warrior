package nl.knokko.guis.buttons;

import java.awt.Color;
import java.lang.reflect.Method;

import nl.knokko.guis.IGui;
import nl.knokko.guis.render.GuiTexture;
import nl.knokko.main.Game;

import org.lwjgl.util.vector.Vector2f;

public class GuiEnumOptionButton extends GuiButton {
	
	private static final Color MARKED_COLOR = new Color(0, 128, 255);
	
	protected Method getter;
	protected Method setter;
	
	protected Enum<?> value;
	protected Color defaultColor;

	public GuiEnumOptionButton(Vector2f position, Vector2f scale, Color backGround, Color textColor, String variable, Enum<?> value) {
		super(position, scale, backGround, textColor, value.toString());
		this.value = value;
		defaultColor = textColor;
		Class<?> options = Game.getOptions().getClass();
		try {
			getter = options.getMethod("get" + variable);
		} catch (Exception ex) {
			throw new RuntimeException("Can't find the getter for method " + variable + " for class " + options.getName(), ex);
		} 
		try {
			setter = options.getMethod("set" + variable, value.getClass());
		} catch (Exception ex) {
			throw new RuntimeException("Can't find the setter for method " + variable + " for class " + options.getName(), ex);
		} 
	}

	@Override
	public void click(int x, int y, int button, IGui gui) {
		try {
			setter.invoke(Game.getOptions(), value);
		} catch(Exception ex){
			throw new RuntimeException("Can't use setter " + setter + " on " + Game.getOptions() + " with value " + value);
		}
	}
	
	@Override
	public GuiTexture[] getTextures(){
		try {
			Object enumValue = getter.invoke(Game.getOptions());
			if(enumValue == value)
				texture.setText(value.toString(), MARKED_COLOR);
			else
				texture.setText(value.toString(), defaultColor);
		} catch(Exception ex){
			throw new RuntimeException("Can't use getter " + getter + " on " + Game.getOptions());
		}
		return super.getTextures();
	}
}
