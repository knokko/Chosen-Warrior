package nl.knokko.guis;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import nl.knokko.guis.buttons.GuiButton;
import nl.knokko.guis.render.GuiDoubleTexture;
import nl.knokko.main.Game;
import nl.knokko.util.Resources;

public class GuiGameOver extends Gui {
	
	private static GuiGameOver instance;
	
	public static GuiGameOver getInstance(){
		if(instance == null)
			instance = new GuiGameOver();
		return instance;
	}

	private GuiGameOver() {
		addTexture(new GuiDoubleTexture(Resources.getDinoTexture(Color.BLACK, Color.BLACK, 0.1f), Resources.getText("You have died!", Color.RED), new Vector2f(0f, 0.5f), new Vector2f(0.4f, 0.2f)));
		/*
		addButton(new GuiButton(new Vector2f(-0.5f, 0f), new Vector2f(0.4f, 0.2f), Color.RED, Color.GREEN, "Try Again"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				Game.getWorld().rebuild();
				Game.spawnBoss();
				EntityPlayer p = Game.getPlayer();
				Game.spawnPlayer(p.skinColor, p.nailColor);
				Game.closeCurrentGUI();
			}
			
		});
		*/
		//TODO reload the world completely
		addButton(new GuiButton(new Vector2f(0.5f, 0f), new Vector2f(0.4f, 0.2f), Color.RED, Color.BLACK, "Quit Game"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				Game.stop();
			}
			
		});
	}

}
