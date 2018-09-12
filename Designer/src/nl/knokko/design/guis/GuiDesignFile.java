package nl.knokko.design.guis;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import nl.knokko.design.guis.buttons.GuiLinkButton;
import nl.knokko.design.main.MapDesigner;
import nl.knokko.guis.Gui;
import nl.knokko.guis.IGui;
import nl.knokko.guis.buttons.GuiButton;
import nl.knokko.guis.render.GuiRenderer;
import nl.knokko.guis.render.GuiTexture;
import static nl.knokko.design.guis.GuiDesignMenu.*;

public class GuiDesignFile extends Gui {
	
	private static GuiDesignFile instance;
	
	public static GuiDesignFile getInstance(){
		if(instance == null)
			instance = new GuiDesignFile();
		return instance;
	}
	
	private final GuiTexture mainTexture;

	private GuiDesignFile() {
		mainTexture = new GuiTexture(getTexture(), new Vector2f(-0.75f, 0.45f), new Vector2f(0.25f, 0.55f));
		addTexture(mainTexture);
		addButton(new GuiLinkButton(new Vector2f(-0.75f, 0.85f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Back", GuiDesignMenu.class));
		addButton(new GuiButton(new Vector2f(-0.75f, 0.6f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Save"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				save();
			}
		});
		addButton(new GuiButton(new Vector2f(-0.75f, 0.35f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.BLACK, "Save and Quit"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				save();
				quit();
			}
		});
		addButton(new GuiButton(new Vector2f(-0.75f, 0.1f), new Vector2f(0.2f, 0.1f), BUTTON_COLOR, Color.RED, "Quit"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				quit();
			}
		});
	}
	
	@Override
	public void render(GuiRenderer renderer){
		GuiDesignMenu.getInstance().render(renderer);
		super.render(renderer);
	}
	
	@Override
	public void update(){
		super.update();
		if(!mainTexture.isHit(Mouse.getX(), Mouse.getY(), this))
			MapDesigner.setCurrentGui(GuiDesignMenu.getInstance());
	}
	
	private void save(){
		MapDesigner.getWorld().save();
	}
	
	private void quit(){
		MapDesigner.stop();
	}
}
