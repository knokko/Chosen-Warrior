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

public class MovePunch extends Move {
	
	public static final int BOW_TIME = DisplayManager.FPS_CAP / 10;
	public static final int STRECH_TIME = DisplayManager.FPS_CAP / 6;
	
	private static final int STATE1 = BOW_TIME;
	private static final int STATE2 = BOW_TIME + STRECH_TIME;
	private static final int STATE3 = BOW_TIME + STRECH_TIME * 2;
	private static final int STATE4 = BOW_TIME * 2 + STRECH_TIME * 2;
	
	private static final float DELTA_BOW = 90f / BOW_TIME;
	private static final float DELTA_STRECH = 90f / STRECH_TIME;
	
	private SubModel upArm;
	private SubModel lowArm;
	
	private ItemEquipmentHand weapon;
	
	private int state;

	public MovePunch(EntityLiving user, SubModel upperArm, ItemEquipmentHand heldItem) {
		super(user);
		upArm = upperArm;
		lowArm = upArm.childModels.get(0);
		weapon = heldItem;
	}

	@Override
	public void update() {
		if(state <= STATE1){
			lowArm.rotationX += DELTA_BOW;
		}
		else if(state <= STATE2){
			upArm.rotationX += DELTA_STRECH;
			lowArm.rotationX -= DELTA_STRECH;
		}
		else if(state <= STATE3){
			upArm.rotationX -= DELTA_STRECH;
			lowArm.rotationX += DELTA_STRECH;
		}
		else if(state <= STATE4){
			lowArm.rotationX -= DELTA_BOW;
		}
		else
			finish();
		state++;
		if(state > STATE2 && state <= STATE3){
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
					((EntityPhysical) collidingEntity).addForce(direction, entity.getPunchForce());
				if(collidingEntity instanceof EntityLiving)
					((EntityLiving) collidingEntity).attack(weapon != null ? weapon.getPunchDamage() : 1);
			}
		}
	}

	@Override
	public int duration() {
		return STATE4;
	}
	
	@Override
	public void interupt(){
		super.interupt();
		reset();
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
