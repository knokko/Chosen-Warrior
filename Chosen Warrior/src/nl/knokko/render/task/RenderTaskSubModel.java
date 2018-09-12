package nl.knokko.render.task;

import java.awt.Color;

import nl.knokko.entities.Entity;
import nl.knokko.entities.util.SubModel;
import nl.knokko.util.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class RenderTaskSubModel extends RenderTask {
	
	protected SubModel subModel;
	protected RenderTask parent;

	public RenderTaskSubModel(Entity entity, SubModel model, RenderTask parent) {
		super(entity);
		this.subModel = model;
		this.model = model.getModel();
		this.parent = parent;
	}

	@Override
	public Matrix4f getMatrix() {
		Matrix4f matOwn = Maths.createTransformationMatrix(subModel.getRelativePosition(), subModel.rotationX, subModel.rotationY, subModel.rotationZ, 1);
		return Matrix4f.mul(parent.getMatrix(), matOwn, matOwn);
	}

	@Override
	public Vector3f getEffectColor() {
		Color ec = entity.getEffectColor();
		if(ec != null)
			return new Vector3f(ec.getRed() / 255f, ec.getGreen() / 255f, ec.getBlue() / 255f);
		return null;
	}
	
	public SubModel getSubModel(){
		return subModel;
	}
}
