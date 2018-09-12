package nl.knokko.entities.moves;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.living.EntityLiving;

public abstract class Move {
	
	protected EntityLiving entity;

	public Move(EntityLiving user) {
		entity = user;
	}
	
	public void start(){
		entity.disableControl(duration());
	}
	
	public void interupt(){
		entity.enableControl();
	}
	
	public void finish(){
		entity.completeMove();
	}
	
	public Vector3f getBasePosition(){
		return null;
	}
	
	public abstract void update();
	
	public abstract int duration();
}
