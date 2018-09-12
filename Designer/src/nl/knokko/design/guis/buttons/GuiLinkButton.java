package nl.knokko.design.guis.buttons;

import java.awt.Color;
import java.lang.reflect.Method;

import nl.knokko.design.main.MapDesigner;
import nl.knokko.guis.IGui;
import nl.knokko.guis.buttons.GuiButton;

import org.lwjgl.util.vector.Vector2f;

public class GuiLinkButton extends GuiButton {
	
	protected Method instance;

	public GuiLinkButton(Vector2f position, Vector2f scale, Color backGround, Color textColor, String text, Class<?> target) {
		super(position, scale, backGround, textColor, text);
		try {
			instance = target.getMethod("getInstance");
		} catch (Exception ex) {
			throw new RuntimeException("Can't find getInstance() method of class " + target.getName(), ex);
		} 
	}

	@Override
	public void click(int x, int y, int button, IGui gui) {
		try {
			MapDesigner.setCurrentGui((IGui) instance.invoke(null));
		} catch (Exception ex) {
			throw new RuntimeException("Can't invoke method " + instance.getName(), ex);
		} 
	}
}
