package nl.knokko.entities.moves;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.collision.Collider;
import nl.knokko.collision.ColliderBox;
import nl.knokko.entities.Entity;
import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.util.SubModel;
import nl.knokko.items.ItemEquipmentHand;
import nl.knokko.render.DisplayManager;
import nl.knokko.util.Maths;

public class MoveStabUp extends Move {
	
	private static final float MAX_ROTATION = 50f;
	private static final int STAB_TIME = DisplayManager.FPS_CAP / 4;
	
	private static final float DELTA = MAX_ROTATION / STAB_TIME;
	
	private SubModel upArm;
	private SubModel lowArm;
	
	private ItemEquipmentHand weapon;
	
	private int state;

	public MoveStabUp(EntityLiving user, SubModel upperArm, ItemEquipmentHand heldItem) {
		super(user);
		upArm = upperArm;
		lowArm = upArm.childModels.get(0);
		weapon = heldItem;
	}

	@Override
	public void update() {
		if(state < STAB_TIME){
			upArm.rotationX += DELTA;
			lowArm.rotationX += DELTA;
		}
		if(state == STAB_TIME){
			Collider ec = entity.getCollider();
			Vector3f extra = Maths.getRotationVector(0, entity.getRotationY(), 0);
			float l = entity.getArmLength();
			extra.scale(l);
			Collider col = new ColliderBox(ec.relMinX() + (extra.x < 0 ? extra.x : 0), ec.relMinY(), ec.relMinZ() + (extra.z < 0 ? extra.z : 0), ec.relMaxX() + (extra.x > 0 ? extra.x : 0), ec.relMaxY(), ec.relMaxZ() + (extra.z > 0 ? extra.z : 0));
			ArrayList<Entity> colliders = entity.getWorld().getPossibleColliders(entity, col);
			for(Entity collidingEntity : colliders){
				Vector3f direction = new Vector3f();
				Vector3f.sub(collidingEntity.getPosition(), entity.getPosition(), direction);
				direction.y = 0;
				direction.normalise();
				if(collidingEntity instanceof EntityPhysical)
					((EntityPhysical) collidingEntity).addForce(direction, entity.getPunchForce() * 0.2f);
				if(collidingEntity instanceof EntityLiving)
					((EntityLiving) collidingEntity).attack(weapon != null ? weapon.getStabDamage() * 10 : 10);
			}
		}
		if(state > STAB_TIME){
			upArm.rotationX -= DELTA;
			lowArm.rotationX -= DELTA;
		}
		if(state > duration())
			finish();
		state++;
	}

	@Override
	public int duration() {
		return STAB_TIME * 2;
	}
	
	@Override
	public void start(){
		super.start();
		reset();
	}
	
	private void reset(){
		upArm.rotationX = 0;
		lowArm.rotationX = 0;
	}
}
