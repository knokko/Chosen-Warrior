package nl.knokko.design.guis;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import nl.knokko.design.guis.buttons.GuiLinkButton;
import nl.knokko.guis.Gui;
import nl.knokko.guis.render.GuiTexture;
import nl.knokko.textures.Material;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;

public class GuiDesignMenu extends Gui {
	
	public static final Color TEXTURE_COLOR = new Color(0, 0, 100);
	public static final Color BUTTON_COLOR = new Color(0, 0, 150);
	
	private static GuiDesignMenu instance;
	
	private static ModelTexture texture;
	
	public static GuiDesignMenu getInstance(){
		if(instance == null)
			instance = new GuiDesignMenu();
		return instance;
	}
	
	public static ModelTexture getTexture(){
		if(texture == null)
			texture = Resources.getFilledTexture(TEXTURE_COLOR, Material.GUI);
		return texture;
	}

	private GuiDesignMenu() {
		addTexture(new GuiTexture(getTexture(), new Vector2f(0f, 0.9f), new Vector2f(1f, 0.1f)));
		addButton(new GuiLinkButton(new Vector2f(-0.75f, 0.9f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "File", GuiDesignFile.class));
		addButton(new GuiLinkButton(new Vector2f(-0.25f, 0.9f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Add", GuiDesignAdd.class));
	}

}
