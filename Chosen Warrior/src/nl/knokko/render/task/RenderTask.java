package nl.knokko.render.task;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.Entity;
import nl.knokko.entities.util.SubModel;
import nl.knokko.render.model.TexturedModel;

public abstract class RenderTask {
	
	public static final ArrayList<RenderTask> createTasks(Entity entity){
		ArrayList<RenderTask> list = new ArrayList<RenderTask>();
		if(entity.shouldBePermanent()){
			list.add(new RenderTaskPermanent(entity));
			return list;
		}
		RenderTaskMoving mainTask = new RenderTaskMoving(entity);
		list.add(mainTask);
		for(SubModel sub : entity.subModels){
			addSubTask(entity, sub, mainTask, list);
		}
		return list;
	}
	
	private static void addSubTask(Entity entity, SubModel sub, RenderTask parent, ArrayList<RenderTask> list){
		RenderTaskSubModel main = new RenderTaskSubModel(entity, sub, parent);
		list.add(main);
		if(sub.getArmorModel() != null)
			list.add(new RenderTaskArmor(main));
		if(sub.getHeldItemModel() != null)
			list.add(new RenderTaskWeapon(main));
		for(SubModel child : sub.childModels)
			addSubTask(entity, child, main, list);
	}
	
	protected Entity entity;
	protected TexturedModel model;

	public RenderTask(Entity entity) {
		this.entity = entity;
		this.model = entity.getModel();
	}
	
	public RenderTask(){}
	
	public Entity getEntity(){
		return entity;
	}
	
	public TexturedModel getModel(){
		return model;
	}
	
	public boolean renderNow(){
		return entity.shouldRenderNow();
	}
	
	public abstract Matrix4f getMatrix();
	
	public abstract Vector3f getEffectColor();
}
