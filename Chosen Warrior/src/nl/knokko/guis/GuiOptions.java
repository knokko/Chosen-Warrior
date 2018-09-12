package nl.knokko.guis;

import java.awt.Color;

import nl.knokko.guis.buttons.GuiLinkButton;

import org.lwjgl.util.vector.Vector2f;

public class GuiOptions extends Gui{
	
	public static final Color BUTTON_COLOR = new Color(50, 150, 50);
	public static final Color TEXT_COLOR = new Color(0, 0, 0);
	
	private static GuiOptions instance;
	
	public static GuiOptions getInstance(){
		if(instance == null)
			instance = new GuiOptions();
		return instance;
	}

	private GuiOptions() {
		addButton(new GuiLinkButton(new Vector2f(-0.5f, 0.8f), new Vector2f(0.25f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "controls", GuiControls.class));
		addButton(new GuiLinkButton(new Vector2f(0.65f, 0.8f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "back to menu", GuiMenu.class));
		addButton(GuiLinkButton.createCloseButton(new Vector2f(0.65f, 0.55f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR));
	}
}
