package nl.knokko.util;

import static java.lang.Math.*;
import nl.knokko.entities.Entity;
import nl.knokko.entities.ICamera;
import nl.knokko.entities.util.SubModel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {
	
	public static final Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	public static final Matrix4f createTransformationMatrix(Vector3f position, float rx, float ry, float rz, float size){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(position, matrix, matrix);
		Matrix4f.rotate((float) toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float) toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.scale(new Vector3f(size, size, size), matrix, matrix);
		return matrix;
	}
	
	public static final Matrix4f createTransformationMatrix(Entity entity){
		return createTransformationMatrix(Vector3f.add(entity.getPosition(), entity.getBaseModelPosition(), null), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getSize());
	}
	
	public static final Matrix4f createTransformationMatrix(SubModel model){
		return createTransformationMatrix(model.getRelativePosition(), model.rotationX, model.rotationY, model.rotationZ, 1);
	}
	
	public static final Matrix4f createViewMatrix(ICamera camera){
		Matrix4f view = new Matrix4f();
		view.setIdentity();
		Matrix4f.rotate(camera.getRadPitch(), new Vector3f(1, 0, 0), view, view);
		Matrix4f.rotate(camera.getRadYaw(), new Vector3f(0, 1, 0), view, view);
		Matrix4f.rotate(camera.getRadRoll(), new Vector3f(0, 0, 1), view, view);
		Vector3f c = camera.getPosition();
		Vector3f invert = new Vector3f(-c.x, -c.y, -c.z);
		Matrix4f.translate(invert, view, view);
		return view;
	}
	
	public static final Vector3f getRotationVector(float pitch, float yaw, float roll){
		float r = (float) toRadians(roll);
		Matrix4f mat = createTransformationMatrix(new Vector3f(), pitch, yaw, 0, 1);
		Vector3f vector = new Vector3f(mat.m20, mat.m21, -mat.m22);
		return new Vector3f((float)(vector.x * cos(r) + vector.y * sin(r)), (float)(vector.y * cos(r) - vector.x * sin(r)), vector.z);
	}
	
	public static final double getDistance(Vector3f v1, Vector3f v2){
		return Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y) + (v1.z - v2.z) * (v1.z - v2.z));
	}
	
	public static final double getYaw(Vector3f vector){
		Vector3f vec = new Vector3f(vector);
		vec.y = 0;
		if(vec.length() == 0)
			return 0;
		vec.normalise();
		double angle = toDegrees(-acos(-vec.z));
		if(vec.x < 0)
			angle = -angle;
		if(angle < 0)
			angle += 360;
		if(angle > 360)
			angle -= 360;
		return angle;
	}

	public static final double getPitch(Vector3f normalized) {
		return toDegrees(-asin(normalized.y));
	}
	
	public static final float max(float... floats){
		float max = floats[0];
		for(float f : floats)
			if(f > max)
				max = f;
		return max;
	}
	
	public static final float min(float... floats){
		float min = floats[0];
		for(float f : floats)
			if(f < min)
				min = f;
		return min;
	}
}
