package nl.knokko.design.main;

import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import nl.knokko.design.entity.FreeCamera;
import nl.knokko.design.entity.IEntityPlacer;
import nl.knokko.design.guis.DefaultGui;
import nl.knokko.design.guis.GuiDesignMenu;
import nl.knokko.design.world.WorldBuilder;
import nl.knokko.guis.IGui;
import nl.knokko.guis.render.GuiRenderer;
import nl.knokko.render.DisplayManager;
import nl.knokko.render.Loader;
import nl.knokko.render.Renderer;
import nl.knokko.shaders.StaticShader;
import nl.knokko.util.Maths;
import nl.knokko.util.Natives;
import nl.knokko.util.Options;

public class MapDesigner {
	
	public static Cursor INVISIBLE;
	public static Cursor DEFAULT;
	
	private static Renderer renderer;
	private static GuiRenderer guiRenderer;
	private static StaticShader shader;
	
	private static Options options;
	
	private static IGui gui;
	private static WorldBuilder world;
	private static FreeCamera camera;
	private static Random random;
	private static DefaultGui defaultGui;
	
	private static IEntityPlacer currentPlacer;
	
	private static boolean isStopping;

	public static void main(String[] args) throws LWJGLException {
		Natives.prepare();
		DisplayManager.createDisplay();
		init();
		Mouse.setNativeCursor(INVISIBLE);
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
	
	private static void init() throws LWJGLException {
		random = new Random();
		options = new Options();
		shader = new StaticShader();
		renderer = new Renderer(shader);
		world = new WorldBuilder();
		camera = new FreeCamera();
		guiRenderer = new GuiRenderer();
		defaultGui = new DefaultGui();
		DEFAULT = Mouse.getNativeCursor();
		INVISIBLE = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
	}
	
	private static void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			if(gui == null){
				while(Keyboard.next()){
					if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
						setCurrentGui(GuiDesignMenu.getInstance());
				}
			}
			else if(gui.canClose()){
				while(Keyboard.next()){
					if(Keyboard.getEventKeyState()){
						if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
							closeCurrentGui();
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
			camera.update(options);
			while(Mouse.next() && currentPlacer != null){
				if(Mouse.getEventButtonState()){
					Vector3f location = new Vector3f(camera.getPosition());
					Vector3f direction = Maths.getRotationVector(camera.getPitch(), camera.getYaw(), camera.getRoll());
					direction.scale(0.1f);
					if(direction.y < 0){
						float height = world.getGroundHeight(location.x, location.z);
						while(location.y > height){
							Vector3f.add(location, direction, location);
							height = world.getGroundHeight(location.x, location.z);
						}
						currentPlacer.click(world, location.x, height, location.z, random);
					}
				}
			}
			if(currentPlacer != null)
				currentPlacer.update(world, random);
		}
	}
	
	private static void render(){
		if(gui == null || gui.renderGameWorld()){
			renderer.prepare();
			shader.start();
			shader.loadLight(world.getLight());
			shader.loadViewMatrix(camera);
			world.render(renderer);
			if(currentPlacer != null)
				currentPlacer.render(world, renderer);
			shader.stop();
		}
		if(gui != null)
			gui.render(guiRenderer);
		else
			defaultGui.render(guiRenderer);
	}
	
	public static void stop(){
		isStopping = true;
	}
	
	public static boolean isStopping(){
		return isStopping;
	}
	
	public static Options getOptions(){
		return options;
	}
	
	public static void closeCurrentGui(){
		gui = null;
		try {
			Mouse.setNativeCursor(INVISIBLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setCurrentGui(IGui newGui){
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
	
	public static IGui getCurrentGui(){
		return gui;
	}
	
	public static FreeCamera getCamera(){
		return camera;
	}
	
	public static WorldBuilder getWorld(){
		return world;
	}
	
	public static void setEntityPlacer(IEntityPlacer placer){
		currentPlacer = placer;
	}
}
