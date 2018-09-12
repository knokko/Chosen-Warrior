package nl.knokko.render.task;

import nl.knokko.entities.Entity;
import nl.knokko.entities.util.SubModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class RenderTaskEquipment extends RenderTask {
	
	protected RenderTaskSubModel ownerTask;

	public RenderTaskEquipment(Entity entity, RenderTaskSubModel owner) {
		super(entity);
		this.ownerTask = owner;
	}

	@Override
	public Matrix4f getMatrix() {
		SubModel equip = ownerTask.getSubModel().getHeldItemModel();
		Matrix4f matOwn = Maths.createTransformationMatrix(equip.getRelativePosition(), equip.rotationX, equip.rotationY, equip.rotationZ, 1);
		return Matrix4f.mul(ownerTask.getMatrix(), matOwn, matOwn);
	}

	@Override
	public Vector3f getEffectColor() {
		return null;
	}
	
	@Override
	public abstract TexturedModel getModel();
	
	public RenderTaskSubModel getOwnerTask(){
		return ownerTask;
	}
}
