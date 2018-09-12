package nl.knokko.entities.projectile;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.Entity;
import nl.knokko.entities.EntityPhysical;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Maths;

public abstract class EntityProjectile extends EntityPhysical implements IProjectile {
	
	/**
	 * This projectile will not collide until counter has a value of 0.
	 */
	private byte counter;

	public EntityProjectile(TexturedModel model, EntityPhysical shooter, Vector3f location, float startForce, float size) {
		super(model, location, shooter.getHeadPitch(), shooter.getRotationY(), shooter.getRotationZ(), size);
		addForce(rotationX, rotationY, rotationZ, startForce);
		counter = 5;
	}
	
	@Override
	public void update(){
		super.update();
		if(changeRotationByMotion()){
			Vector3f motion = new Vector3f(motionX, motionY, motionZ);
			motion.normalise();
			rotationX = (float) -Maths.getPitch(motion) + straightPitch();
			rotationY = (float) -Maths.getYaw(motion);
		}
		if(counter > 0)
			counter--;
	}
	
	@Override
	public boolean collideNow(){
		return counter == 0;
	}
	
	@Override
	public float getMass(){
		return 1;
	}
	
	public boolean changeRotationByMotion(){
		return true;
	}
	
	public float straightPitch(){
		return 0;
	}
	
	public abstract void onEntityHit(Entity hitEntity, Vector3f impactPosition);
	
	public abstract void onGroundHit(Vector3f impactPosition);
}
