package nl.knokko.guis;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import nl.knokko.guis.buttons.GuiButton;
import nl.knokko.guis.buttons.GuiOptionSliceButton;
import nl.knokko.main.Game;

public class GuiCharacterColors extends Gui {
	
	protected Color skinColor;
	protected Color nailColor;
	
	protected GuiButton nextButton;
	
	public static final Color getRandomColor(){
		Random random = new Random();
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	public GuiCharacterColors() {
		skinColor = getRandomColor();
		nailColor = getRandomColor();
		nextButton = new GuiButton(new Vector2f(0.7f, -0.8f), new Vector2f(0.3f, 0.1f), skinColor, nailColor, "start game"){

			@Override
			public void click(int x, int y, int button, IGui gui) {
				Game.spawnPlayer(skinColor, nailColor);
				Game.closeCurrentGUI();
			}
			
		};
		addButton(nextButton);
		try {
			addButton(new GuiOptionSliceButton(new Vector2f(-0.5f, 0.8f), new Vector2f(0.3f, 0.1f), Color.RED, Color.BLACK, "skin red", 0, 255, getClass().getMethod("setSkinRed", float.class), getClass().getMethod("getSkinRed"), this));
			addButton(new GuiOptionSliceButton(new Vector2f(-0.5f, 0.5f), new Vector2f(0.3f, 0.1f), Color.GREEN, Color.BLACK, "skin green", 0, 255, getClass().getMethod("setSkinGreen", float.class), getClass().getMethod("getSkinGreen"), this));
			addButton(new GuiOptionSliceButton(new Vector2f(-0.5f, 0.2f), new Vector2f(0.3f, 0.1f), Color.BLUE, Color.BLACK, "skin blue", 0, 255, getClass().getMethod("setSkinBlue", float.class), getClass().getMethod("getSkinBlue"), this));
			
			addButton(new GuiOptionSliceButton(new Vector2f(-0.5f, -0.2f), new Vector2f(0.3f, 0.1f), Color.RED, Color.BLACK, "nail red", 0, 255, getClass().getMethod("setNailRed", float.class), getClass().getMethod("getNailRed"), this));
			addButton(new GuiOptionSliceButton(new Vector2f(-0.5f, -0.5f), new Vector2f(0.3f, 0.1f), Color.GREEN, Color.BLACK, "nail green", 0, 255, getClass().getMethod("setNailGreen", float.class), getClass().getMethod("getNailGreen"), this));
			addButton(new GuiOptionSliceButton(new Vector2f(-0.5f, -0.8f), new Vector2f(0.3f, 0.1f), Color.BLUE, Color.BLACK, "nail blue", 0, 255, getClass().getMethod("setNailBlue", float.class), getClass().getMethod("getNailBlue"), this));
		} catch (Exception ex) {
			System.out.println("Failed to load the getters and setters for the character colors:");
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} 
	}
	
	@Override
	public boolean renderGameWorld(){
		return false;
	}
	
	@Override
	public boolean canClose(){
		return false;
	}
	
	protected void changeSkinColor(Color color){
		skinColor = color;
		nextButton.getTextures()[0].setBackGroundColor(skinColor);
	}
	
	protected void changeNailColor(Color color){
		nailColor = color;
		nextButton.getTextures()[0].setText("start game", nailColor);
	}
	
	public void setSkinRed(float red){
		changeSkinColor(new Color((int)red, skinColor.getGreen(), skinColor.getBlue()));
	}
	
	public void setSkinGreen(float green){
		changeSkinColor(new Color(skinColor.getRed(), (int)green, skinColor.getBlue()));
	}
	
	public void setSkinBlue(float blue){
		changeSkinColor(new Color(skinColor.getRed(), skinColor.getGreen(), (int)blue));
	}
	
	public int getSkinRed(){
		return skinColor.getRed();
	}
	
	public int getSkinGreen(){
		return skinColor.getGreen();
	}
	
	public int getSkinBlue(){
		return skinColor.getBlue();
	}
	
	public void setNailRed(float red){
		changeNailColor(new Color((int)red, nailColor.getGreen(), nailColor.getBlue()));
	}
	
	public void setNailGreen(float green){
		changeNailColor(new Color(nailColor.getRed(), (int)green, nailColor.getBlue()));
	}
	
	public void setNailBlue(float blue){
		changeNailColor(new Color(nailColor.getRed(), nailColor.getGreen(), (int)blue));
	}
	
	public int getNailRed(){
		return nailColor.getRed();
	}
	
	public int getNailGreen(){
		return nailColor.getGreen();
	}
	
	public int getNailBlue(){
		return nailColor.getBlue();
	}
}
