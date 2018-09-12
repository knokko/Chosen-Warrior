package nl.knokko.entities.moves;

import java.util.ArrayList;

import nl.knokko.collision.Collider;
import nl.knokko.collision.ColliderBox;
import nl.knokko.entities.Entity;
import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.util.SubModel;

import org.lwjgl.util.vector.Vector3f;

public class Move360Kick extends Move {
	
	private static final int ROTATE_SPEED = 12;
	
	protected SubModel[] legs;
	
	protected int state = -restoreTime();

	public Move360Kick(EntityLiving user) {
		super(user);
		legs = entity.getLegs();
	}

	@Override
	public void update() {
		if(state < 0){
			boolean flipper = false;
			for(SubModel leg : legs){
				leg.rotationX = 90 - (-state / (float)restoreTime() * 90);
				if(flipper)
					leg.rotationX = 0;
				flipper = !flipper;
			}
			state++;
		}
		else if(state < rotateDuration()){
			entity.increaseRotation(0, ROTATE_SPEED, 0);
			state++;
			Collider ec = entity.getCollider();
			float l = entity.getLegLength();
			Collider col = new ColliderBox(ec.relMinX() - l, ec.relMinY(), ec.relMinZ() - l, ec.relMaxX() + l, ec.relMaxY(), ec.relMaxZ() + l);
			ArrayList<Entity> colliders = entity.getWorld().getPossibleColliders(entity, col);
			for(Entity collidingEntity : colliders){
				Vector3f direction = new Vector3f();
				Vector3f.sub(collidingEntity.getPosition(), entity.getPosition(), direction);
				direction.y = 0;
				direction.normalise();
				if(collidingEntity instanceof EntityPhysical)
					((EntityPhysical) collidingEntity).addForce(direction, entity.getKickForce());
				if(collidingEntity instanceof EntityLiving)
					((EntityLiving) collidingEntity).attack(1);
			}
		}
		else if(state < rotateDuration() + restoreTime()){
			float s = state - rotateDuration();
			boolean flipper = false;
			for(SubModel leg : legs){
				leg.rotationX = 90 - (s / restoreTime() * 90);
				if(flipper)
					leg.rotationX = 0;
				flipper = !flipper;
			}
			++state;
		}
		else
			finish();
	}

	@Override
	public int duration() {
		return rotateDuration() + 2 * restoreTime();
	}
	
	@Override
	public void start(){
		super.start();
		for(SubModel leg : legs){
			for(SubModel under : leg.childModels){
				for(SubModel foot : under.childModels){
					foot.rotationX = 0;
				}
				under.rotationX = 0;
			}
			leg.rotationX = 0;
		}
	}
	
	@Override
	public void interupt(){
		super.interupt();for(SubModel leg : legs){
			for(SubModel under : leg.childModels){
				for(SubModel foot : under.childModels){
					foot.rotationX = 0;
				}
				under.rotationX = 0;
			}
			leg.rotationX = 0;
		}
	}
	
	public int rotateDuration(){
		return 360 / ROTATE_SPEED;
	}
	
	/**
	 * @return The amount of ticks required to restore the rotation of the legs.
	 */
	protected int restoreTime(){
		return 10;
	}
}
