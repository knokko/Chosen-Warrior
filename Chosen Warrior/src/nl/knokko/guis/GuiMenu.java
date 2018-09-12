package nl.knokko.guis;

import java.awt.Color;

import nl.knokko.guis.buttons.GuiLinkButton;

import org.lwjgl.util.vector.Vector2f;

public class GuiMenu extends Gui {
	
	public static final Color BUTTON_COLOR = new Color(50, 50, 150);
	public static final Color TEXT_COLOR = new Color(0, 0, 0);
	
	private static GuiMenu instance;
	
	public static GuiMenu getInstance(){
		if(instance == null)
			instance = new GuiMenu();
		return instance;
	}

	private GuiMenu() {
		addButton(GuiLinkButton.createCloseButton(new Vector2f(0.65f, 0.4f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR));
		addButton(GuiLinkButton.createSaveQuit(new Vector2f(0.65f, -0.4f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR));
		addButton(new GuiLinkButton(new Vector2f(-0.5f, 0.4f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "options", GuiOptions.class));
	}

}
