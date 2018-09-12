package nl.knokko.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.Camera;
import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.Light;
import nl.knokko.render.DisplayManager;
import nl.knokko.render.Loader;
import nl.knokko.render.Renderer;
import nl.knokko.render.model.RawModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.shaders.StaticShader;
import nl.knokko.util.Resources;
import nl.knokko.world.forest.animals.Dinosaurs;

public class ModelDesigner {
	
	public static Renderer renderer;
	public static StaticShader shader;
	public static Camera camera;
	
	private static BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	
	public static void createCube(){
		float[] vertices = {
				-1,-1,-1, -1,-1,1, 1,-1,1, 1,-1,-1,
				-1,1,-1, -1,1,1, 1,1,1, 1,1,-1,
				-1,1,-1, -1,-1,-1, 1,-1,-1, 1,1,-1,
				1,1,-1, 1,-1,-1, 1,-1,1, 1,1,1,
				-1,1,1, -1,-1,1, 1,-1,1, 1,1,1,
				-1,1,-1, -1,-1,-1, -1,-1,1, -1,1,1
				};
		float[] textureCoords = {
				0,0.5f, 0,1, 0.25f,1, 0.25f,0.5f,  
				0.25f,0.5f, 0.25f,1, 0.5f,1, 0.5f,0.5f,
				0,0, 0,0.5f, 0.25f,0.5f, 0.25f,0,
				0.25f,0, 0.25f,0.5f, 0.5f,0.5f, 0.5f, 0,
				0.5f,0, 0.5f,0.5f, 0.75f,0.5f, 0.75f, 0,
				0.75f,0, 0.75f,0.5f, 1,0.5f, 1,0
				};
		float[] normals = {
				0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0,
				0,1,0, 0,1,0, 0,1,0, 0,1,0,
				0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1,
				1,0,0, 1,0,0, 1,0,0, 1,0,0,
				0,0,1, 0,0,1, 0,0,1, 0,0,1,
				-1,0,0, -1,0,0, -1,0,0, -1,0,0
				};
		int[] indices = {
				0,1,2,2,3,0, 4,5,6,6,7,4, 8,9,10,10,11,8, 12,13,14,14,15,12, 16,17,18,18,19,16, 20,21,22,22,23,20};
		Loader.save(vertices, textureCoords, normals, indices, "resources/models/geometric/cube.model");
	}
	
	public static void createPyramid(){
		float x = 0.866025403784f;
		float y = 0.75f;
		float z = 0.5f;
		float[] vertices = {
				-x,-y,-z, 0,-y,z * 2, x,-y,-z,
				-x,-y,-z, 0,y,0, x,-y,-z,
				x,-y,-z, 0,y,0, 0,-y,z * 2,
				0,-y,z * 2, 0,y,0, -x,-y,-z
				};
		float[] textureCoords = {
				0,0, 0,1, 0.5f,1,
				0.5f,1, 0.5f,0, 0,0,
				0.5f,1, 0.5f,0, 1,0,
				1,0, 1,1, 0.5f,1
				};
		float[] normals = {
				0,-1,0, 0,-1,0, 0,-1,0,
				0,1,-1, 0,1,-1, 0,1,-1,
				1,1,1, 1,1,1, 1,1,1,
				-1,1,1, -1,1,1, -1,1,1
				};
		int[] indices = {
				0,1,2,
				3,4,5,
				6,7,8,
				9,10,11
				};
		Loader.save(vertices, textureCoords, normals, indices, "resources/models/geometric/pyramid.model");
	}
	
	public static void createModel(){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		addFourAngle(new Vector3f(-100, 0, -100), new Vector3f(-100, 0, 100), new Vector3f(100, 0, 100), new Vector3f(100, 0, -100), new Vector3f(0, 1, 0), vertices, intTextures, normals, indices);
		float[] verticeArray = new float[vertices.size()];
		float[] textureArray = new float[intTextures.size()];
		float[] normalArray = new float[normals.size()];
		int[] indiceArray = new int[indices.size()];
		double log = Math.log(image.getWidth()) / Math.log(2);
		if(log != (int)log)
			setImageSize((int) Math.pow(2, (int)log + 1), image.getHeight());
		log = Math.log(image.getHeight()) / Math.log(2);
		if(log != (int)log)
			setImageSize(image.getWidth(), (int) Math.pow(2, (int)log + 1));
		for(int i = 0; i < vertices.size(); i++)
            verticeArray[i] = vertices.get(i);
		for(int i = 0; i < intTextures.size(); i++){
			if(i / 2 == i / 2.0)
				textureArray[i] = (float)(intTextures.get(i) + 0.5f) / image.getWidth();
			else
				textureArray[i] = (float)(intTextures.get(i) + 0.5f) / image.getHeight();
		}
		for(int i = 0; i < normals.size(); i++)
            normalArray[i] = normals.get(i);
		for(int i = 0; i < indices.size(); i++)
            indiceArray[i] = indices.get(i);
		Loader.save(verticeArray, textureArray, normalArray, indiceArray, "resources/models/generated.model");
		try {
			ImageIO.write(image, "png", new File("resources/textures/generated.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTreeTexture(){
		int size = 3202;
		BufferedImage image = new BufferedImage(4096, 4096, BufferedImage.TYPE_INT_RGB);
		Random random = new Random();
		int x = 0;
		while(x < size){
			int z = 0;
			while(z < size){
				image.setRGB(x, z, new Color(0, random.nextInt(105) + 100, random.nextInt(50)).getRGB());
				++z;
			}
			++x;
		}
		try {
			ImageIO.write(image, "png", new File("generated.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void generateTreeTexture(){
		generateTreeTexture(new Color(130, 50, 0), new Color(20, 150, 50));
	}
	
	static void generateTreeTexture(Color wood, Color leaves){
		Random random = new Random();
		image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		int x = 0;
		while(x < image.getWidth()){
			int y = 0;
			while(y < image.getHeight()){
				float factor = 0.8f + random.nextFloat() * 0.4f;
				if(x < image.getWidth() / 2)
					image.setRGB(x, y, new Color((int)(wood.getRed() * factor), (int)(wood.getGreen() * factor), (int)(wood.getBlue() * factor)).getRGB());
				else
					image.setRGB(x, y, new Color((int)(leaves.getRed() * factor), (int)(leaves.getGreen() * factor), (int)(leaves.getBlue() * factor)).getRGB());
				++y;
			}
			++x;
		}
	}
	
	/*
	public static void generateTree(TreeSettings settings, float length, float width, float twigs){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		generateTreeTexture();
		addTwig(vertices, intTextures, normals, indices, new Vector3f(), new Vector3f(0, 1, 0), length, width, twigs, settings);
		float[] verticeArray = new float[vertices.size()];
		float[] textureArray = new float[intTextures.size()];
		float[] normalArray = new float[normals.size()];
		int[] indiceArray = new int[indices.size()];
		for(int i = 0; i < vertices.size(); i++)
            verticeArray[i] = vertices.get(i);
		for(int i = 0; i < intTextures.size(); i++){
			if(i / 2 == i / 2.0)
				textureArray[i] = (float)(intTextures.get(i) + 0.5f) / image.getWidth();
			else
				textureArray[i] = (float)(intTextures.get(i) + 0.5f) / image.getHeight();
		}
		for(int i = 0; i < normals.size(); i++)
            normalArray[i] = normals.get(i);
		for(int i = 0; i < indices.size(); i++)
            indiceArray[i] = indices.get(i);
		loader.save(verticeArray, textureArray, normalArray, indiceArray, "resources/models/tree1.model");
		try {
			ImageIO.write(image, "png", new File("resources/textures/tree1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	*/
	
		
	static void addTriAngle(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f normal, ArrayList<Float> vertices, ArrayList<Integer> tc, ArrayList<Float> normals, ArrayList<Integer> indices){
		int index = vertices.size() / 3;
		vertices.add(v1.x);
		vertices.add(v1.y);
		vertices.add(v1.z);
		vertices.add(v2.x);
		vertices.add(v2.y);
		vertices.add(v2.z);
		vertices.add(v3.x);
		vertices.add(v3.y);
		vertices.add(v3.z);
		for(int i = 0; i < 3; i++){
			normals.add(normal.x);
			normals.add(normal.y);
			normals.add(normal.z);
		}
		indices.add(index);
		indices.add(index + 1);
		indices.add(index + 2);
		int distanceY = (int) (10 * Math.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y) + (v2.z - v1.z) * (v2.z - v1.z)));
		int distanceX = (int) (10 * Math.sqrt((v2.x - v3.x) * (v2.x - v3.x) + (v2.y - v3.y) * (v2.y - v3.y) + (v2.z - v3.z) * (v2.z - v3.z)));
		int x1 = image.getWidth();
		tc.add(x1);
		tc.add(0);
		tc.add(x1);
		tc.add(distanceY);
		tc.add(x1 + distanceX);
		tc.add(distanceY);
		if(image.getHeight() <= distanceY)
			setImageSize(image.getWidth() + distanceX + 1, distanceY + 1);
		else
			setImageSize(image.getWidth() + distanceX + 1, image.getHeight());
		Graphics2D gr = image.createGraphics();
		gr.setColor(Color.BLACK);
		gr.drawLine(x1, 0, x1, distanceY);
		gr.drawLine(x1, distanceY, image.getWidth() - 1, distanceY);
		gr.drawLine(x1, 0, image.getWidth() - 1, distanceY);
	}
	
	static void addFourAngle(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f normal, ArrayList<Float> vertices, ArrayList<Integer> tc, ArrayList<Float> normals, ArrayList<Integer> indices){
		int index = vertices.size() / 3;
		vertices.add(v1.x);
		vertices.add(v1.y);
		vertices.add(v1.z);
		vertices.add(v2.x);
		vertices.add(v2.y);
		vertices.add(v2.z);
		vertices.add(v3.x);
		vertices.add(v3.y);
		vertices.add(v3.z);
		vertices.add(v4.x);
		vertices.add(v4.y);
		vertices.add(v4.z);
		for(int i = 0; i < 4; i++){
			normals.add(normal.x);
			normals.add(normal.y);
			normals.add(normal.z);
		}
		indices.add(index);
		indices.add(index + 1);
		indices.add(index + 2);
		indices.add(index);
		indices.add(index + 2);
		indices.add(index + 3);
		int distanceY = (int) (16 * Math.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y) + (v2.z - v1.z) * (v2.z - v1.z)));
		int distanceX = (int) (16 * Math.sqrt((v2.x - v3.x) * (v2.x - v3.x) + (v2.y - v3.y) * (v2.y - v3.y) + (v2.z - v3.z) * (v2.z - v3.z)));
		int x1 = image.getWidth();
		tc.add(x1);
		tc.add(0);
		tc.add(x1);
		tc.add(distanceY);
		tc.add(x1 + distanceX);
		tc.add(distanceY);
		tc.add(x1 + distanceX);
		tc.add(0);
		if(image.getHeight() <= distanceY)
			setImageSize(image.getWidth() + distanceX + 1, distanceY + 1);
		else
			setImageSize(image.getWidth() + distanceX + 1, image.getHeight());
		int x2 = image.getWidth() - 1;
		Graphics2D gr = image.createGraphics();
		gr.setColor(Color.BLACK);
		gr.drawLine(x1, 0, x1, distanceY);
		gr.drawLine(x2, 0, x2, distanceY);
		gr.drawLine(x1, distanceY, x2, distanceY);
		gr.drawLine(x1, 0, x2, 0);
	}
	
	static void addCube(Vector3f neg, Vector3f pos, ArrayList<Float> vertices, ArrayList<Integer> tc, ArrayList<Float> normals, ArrayList<Integer> indices){
		addFourAngle(neg, new Vector3f(neg.x, neg.y, pos.z), new Vector3f(pos.x, neg.y, pos.z), new Vector3f(pos.x, neg.y, neg.z), new Vector3f(0, -1, 0), vertices, tc, normals, indices);
		addFourAngle(new Vector3f(neg.x, pos.y, neg.z), new Vector3f(neg.x, pos.y, pos.z), pos, new Vector3f(pos.x, pos.y, neg.z), new Vector3f(0, 1, 0), vertices, tc, normals, indices);
		addFourAngle(new Vector3f(neg.x, pos.y, neg.z), neg, new Vector3f(pos.x, neg.y, neg.z), new Vector3f(pos.x, pos.y, neg.z), new Vector3f(0, 0, -1), vertices, tc, normals, indices);
		addFourAngle(new Vector3f(pos.x, pos.y, neg.z), new Vector3f(pos.x, neg.y, neg.z), new Vector3f(pos.x, neg.y, pos.z), pos, new Vector3f(1, 0, 0), vertices, tc, normals, indices);
		addFourAngle(new Vector3f(neg.x, pos.y, pos.z), new Vector3f(neg.x, neg.y, pos.z), new Vector3f(pos.x, neg.y, pos.z), pos, new Vector3f(0, 0, 1), vertices, tc, normals, indices);
		addFourAngle(new Vector3f(neg.x, pos.y, neg.z), neg, new Vector3f(neg.x, neg.y, pos.z), new Vector3f(neg.x, pos.y, pos.z), new Vector3f(-1, 0, 0), vertices, tc, normals, indices);
	}
	
	static void setImageSize(int width, int height){
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = newImage.createGraphics();
		gr.setColor(Color.WHITE);
		gr.fillRect(0, 0, width, height);
		gr.drawImage(image, 0, 0, null);
		image = newImage;
		gr.setColor(Color.BLACK);
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		shader = new StaticShader();
		renderer = new Renderer(shader);
		//camera = new Camera();
		RawModel model = Resources.getHumanoidModel(Dinosaurs.HUMANOID);
		TexturedModel texturedModel = new TexturedModel(model, Resources.getTreeTexture(new Color(130, 50, 0), new Color(20, 150, 50), 0.2f, (byte) 0));
		Light light = new Light(new Vector3f(10, 10, 10), new Vector3f(1, 1, 1));
		EntityPhysical entity = new EntityPhysical(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			camera.update();
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		shader.cleanUp();
		Loader.cleanUp();
		DisplayManager.closeDisplay();
		//https://www.youtube.com/watch?annotation_id=annotation_1142546655&feature=iv&src_vid=bcxX0R8nnDs&v=GZ_1xOm-3qU
	}

}
