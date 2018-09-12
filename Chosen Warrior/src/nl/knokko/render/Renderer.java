package nl.knokko.render;

import java.util.ArrayList;
import java.util.HashMap;

import nl.knokko.entities.EntityPhysical;
import nl.knokko.render.model.RawModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.render.task.RenderTask;
import nl.knokko.shaders.StaticShader;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Maths;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Renderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	public Renderer(StaticShader shader){
		createProjectionMatrix();
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0, 0, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void render(HashMap<RawModel, HashMap<ModelTexture, ArrayList<RenderTask>>> entities){
		for(RawModel model : entities.keySet()){
			prepareModel(model);
			HashMap<ModelTexture, ArrayList<RenderTask>> map = entities.get(model);
			for(ModelTexture texture : map.keySet()){
				prepareTexture(texture);
				ArrayList<RenderTask> batch = map.get(texture);
				for(RenderTask task : batch){
					if(task.renderNow()){
						prepareTask(task);
						GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
					}
				}
			}
			unbindModel();
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void prepareModel(RawModel model){
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
	}
	
	private void prepareTexture(ModelTexture texture){
		shader.loadShine(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindModel(){
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
	}
	
	private void prepareTask(RenderTask task){
		shader.loadTransformationMatrix(task.getMatrix());
		Vector3f effectColor = task.getEffectColor();
		if(effectColor == null)
			shader.loadEffectColor(new Vector3f(-1, -1, -1));
		else
			shader.loadEffectColor(effectColor);
	}
	
	public void renderSingle(TexturedModel model, final Matrix4f matrix){
		prepareModel(model.getModel());
		prepareTexture(model.getTexture());
		prepareTask(new RenderTask(){

			@Override
			public Matrix4f getMatrix() {
				return matrix;
			}

			@Override
			public Vector3f getEffectColor() {
				return null;
			}
			
		});
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		unbindModel();
	}
	
	@Deprecated
	public void render(EntityPhysical entity){
		TexturedModel tModel = entity.getModel();
		RawModel model = tModel.getModel();
		ModelTexture texture = tModel.getTexture();
		shader.loadShine(texture.getShineDamper(), texture.getReflectivity());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tModel.getTexture().getID());
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		renderModel(entity, shader, model);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void renderModel(EntityPhysical entity, StaticShader shader, RawModel model){
		renderModel(shader, model, entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getSize());
	}
	
	public final Vector3f rot(Vector3f vec1, Vector3f v)
	{
		float w = 1;
	  float  k0 = w*w - 0.5f;
	  float  k1;
	  float  rx,ry,rz;

	  // k1 = Q.V
	  k1    = v.x*vec1.x;
	  k1   += v.y*vec1.y;
	  k1   += v.z*vec1.z;

	  // (qq-1/2)V+(Q.V)Q
	  rx  = v.x*k0 + vec1.x*k1;
	  ry  = v.y*k0 + vec1.y*k1;
	  rz  = v.z*k0 + vec1.z*k1;

	  // (Q.V)Q+(qq-1/2)V+q(QxV)
	  rx += w*(vec1.y*v.z-vec1.z*v.y);
	  ry += w*(vec1.z*v.x-vec1.x*v.z);
	  rz += w*(vec1.x*v.y-vec1.y*v.x);

	  //  2((Q.V)Q+(qq-1/2)V+q(QxV))
	  rx += rx;
	  ry += ry;
	  rz += rz;

	  return new Vector3f(rx,ry,rz);
	}
	
	private void renderModel(StaticShader shader, RawModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float size){
		Matrix4f matrix = Maths.createTransformationMatrix(position, rotationX, rotationY, rotationZ, size);
		shader.loadTransformationMatrix(matrix);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
