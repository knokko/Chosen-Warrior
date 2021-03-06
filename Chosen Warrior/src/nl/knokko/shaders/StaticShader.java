package nl.knokko.shaders;

import java.awt.Color;

import nl.knokko.entities.ICamera;
import nl.knokko.entities.Light;
import nl.knokko.util.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "nl/knokko/shaders/default.vshader";
	private static final String FRAGMENT_FILE = "nl/knokko/shaders/default.fshader";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightColour;
	private int locationLightPosition;
	private int locationReflectivity;
	private int locationShineDamper;
	private int locationEffectColor;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationLightColour = super.getUniformLocation("lightColour");
		locationLightPosition = super.getUniformLocation("lightPosition");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationEffectColor = super.getUniformLocation("effectColor");
	}
	
	public void loadShine(float damper, float reflectivity){
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadViewMatrix(ICamera camera){
		super.loadMatrix(locationViewMatrix, Maths.createViewMatrix(camera));
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(locationProjectionMatrix, matrix);
	}
	
	public void loadLight(Light light){
		super.loadVector(locationLightPosition, light.getPosition());
		super.loadVector(locationLightColour, light.getColour());
	}
	
	public void loadEffectColor(Color color){
		super.loadVector(locationEffectColor, new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getRed() / 255f));
	}
	
	public void loadEffectColor(Vector3f color){
		super.loadVector(locationEffectColor, color);
	}
}
