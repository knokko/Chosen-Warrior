package nl.knokko.render.task;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.Entity;
import nl.knokko.util.Maths;

public class RenderTaskPermanent extends RenderTask {
	
	protected Matrix4f matrix;

	public RenderTaskPermanent(Entity entity) {
		super(entity);
		matrix = Maths.createTransformationMatrix(Vector3f.add(entity.getPosition(), entity.getBaseModelPosition(), null), entity.getRotationX(), -entity.getRotationY(), entity.getRotationZ(), entity.getSize());
	}

	@Override
	public Matrix4f getMatrix() {
		return matrix;
	}

	@Override
	public Vector3f getEffectColor() {
		return null;
	}

}
