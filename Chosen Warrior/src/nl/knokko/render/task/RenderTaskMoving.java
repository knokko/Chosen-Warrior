package nl.knokko.render.task;

import java.awt.Color;

import nl.knokko.entities.Entity;
import nl.knokko.util.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class RenderTaskMoving extends RenderTask {

	public RenderTaskMoving(Entity entity) {
		super(entity);
	}

	@Override
	public Matrix4f getMatrix() {
		return Maths.createTransformationMatrix(Vector3f.add(entity.getPosition(), entity.getBaseModelPosition(), null), entity.getRotationX(), -entity.getRotationY(), entity.getRotationZ(), entity.getSize());
	}

	@Override
	public Vector3f getEffectColor() {
		Color ec = entity.getEffectColor();
		if(ec != null)
			return new Vector3f(ec.getRed() / 255f, ec.getGreen() / 255f, ec.getBlue() / 255f);
		return null;
	}

}
