package nl.knokko.main;

import java.awt.Color;

import nl.knokko.entities.*;
import nl.knokko.entities.living.*;
import nl.knokko.guis.GuiCharacterColors;
import nl.knokko.guis.GuiMenu;
import nl.knokko.guis.IGui;
import nl.knokko.guis.render.GuiRenderer;
import nl.knokko.items.Items;
import nl.knokko.render.*;
import nl.knokko.shaders.StaticShader;
import nl.knokko.util.Natives;
import nl.knokko.util.Options;
import nl.knokko.world.World;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Game {
	
	public static Cursor INVISIBLE;
	public static Cursor DEFAULT;
	
	private static StaticShader shader;
	private static Camera camera;
	private static World world;
	private static Renderer renderer;
	private static GuiRenderer guiRenderer;
	private static IGui gui;
	private static Options options;
	private static EntityPlayer player;
	
	private static boolean isStopping;

	public static void main(String[] args) throws LWJGLException {
		Natives.prepare();
		DisplayManager.createDisplay();
		init();
		Mouse.setNativeCursor(INVISIBLE);
		setCurrentGUI(new GuiCharacterColors());
		while(!Display.isCloseRequested() && !isStopping){
			update();
			render();
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		shader.cleanUp();
		Loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	private static void init() throws LWJGLException{
		options = new Options();
		shader = new StaticShader();
		renderer = new Renderer(shader);
		world = new World();
		guiRenderer = new GuiRenderer();
		DEFAULT = Mouse.getNativeCursor();
		INVISIBLE = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
	}
	
	private static void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			if(gui == null){
				while(Keyboard.next()){
					if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
						setCurrentGUI(GuiMenu.getInstance());
				}
			}
			else if(gui.canClose()){
				while(Keyboard.next()){
					if(Keyboard.getEventKeyState()){
						if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
							setCurrentGUI(null);
						else
							gui.keyPressed(Keyboard.getEventKey(), Keyboard.getEventCharacter());
					}
					else
						gui.keyReleased(Keyboard.getEventKey(), Keyboard.getEventCharacter());
				}
			}
		}
		if(gui != null)
			gui.update();
		else {
			world.update();
			camera.update();
		}
	}
	
	private static void render(){
		if(gui == null || gui.renderGameWorld()){
			renderer.prepare();
			shader.start();
			shader.loadLight(world.getLight());
			shader.loadViewMatrix(camera);
			world.render(renderer);
			shader.stop();
		}
		if(gui != null)
			gui.render(guiRenderer);
	}

	public static StaticShader getShader() {
		return shader;
	}

	public static Camera getCamera() {
		return camera;
	}
	
	public static World getWorld(){
		return world;
	}
	
	public static Renderer getRenderer(){
		return renderer;
	}
	
	public static IGui getCurrentGUI(){
		return gui;
	}
	
	public static void setCurrentGUI(IGui newGui){
		gui = newGui;
		if(gui != null){
			try {
				Mouse.setNativeCursor(gui.getCursor());
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Mouse.setNativeCursor(INVISIBLE);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeCurrentGUI(){
		setCurrentGUI(null);
	}
	
	/**
	 * Request the game to stop after finishing the current tick.
	 */
	public static void stop(){
		isStopping = true;
	}
	
	public static boolean isStopping(){
		return isStopping;
	}
	
	public static Options getOptions(){
		return options;
	}
	
	public static EntityPlayer getPlayer(){
		return player;
	}
	
	public static void spawnPlayer(Color skinColor, Color nailColor){
		player = new EntityPlayer(new Vector3f(10, 10, 0), skinColor, nailColor, 0, 0, 0, 1);
		world.spawnEntity(player);
		player.setHeldItem(Items.KNIFE, player.getHands()[0]);
		player.setHeldItem(Items.KNIFE, player.getHands()[1]);
		camera = new Camera(player);
	}
}
