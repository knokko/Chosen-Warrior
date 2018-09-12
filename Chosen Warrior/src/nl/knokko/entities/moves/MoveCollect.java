package nl.knokko.entities.moves;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.EntityCollectible;
import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.util.SubModel;
import nl.knokko.items.Item;
import nl.knokko.items.ItemEquipmentHand;

public class MoveCollect extends Move {
	
	private static final float BOW_SPEED = 1.5f;
	private static final float PICK_SPEED = 0.8f;
	
	private static final float MAX_BOW = 75;
	private static final float MAX_PICK = 30;
	
	private int state = (int) (-bowTime() - pickTime());
	
	private SubModel[] legs;
	private SubModel arm;
	private SubModel hand;
	
	private Item item;
	private EntityCollectible collectingEntity;

	public MoveCollect(EntityLiving user, SubModel arm, EntityCollectible collectingEntity){
		super(user);
		legs = user.getLegs();
		this.arm = arm;
		hand = arm.childModels.get(0).childModels.get(0);
		item = collectingEntity.getCollectingItem();
		this.collectingEntity = collectingEntity;
	}

	@Override
	public void update() {
		if(state < -pickTime()){
			for(SubModel leg : legs){
				leg.rotationX += BOW_SPEED;
				leg.childModels.get(0).rotationX -= BOW_SPEED * 2;
				leg.childModels.get(0).childModels.get(0).rotationX += BOW_SPEED;
			}
		}
		if(state >= -pickTime() && state < 0){
			arm.rotationX += PICK_SPEED;
			arm.childModels.get(0).rotationX += PICK_SPEED;
		}
		if(state == 0){
			entity.setHeldItem((ItemEquipmentHand) item, hand);
			if(collectingEntity.removeAfterCollect())
				collectingEntity.destroy();
		}
		if(state >= 0 && state < pickTime()){
			arm.rotationX -= PICK_SPEED;
			arm.childModels.get(0).rotationX -= PICK_SPEED;
		}
		if(state >= pickTime() && state < pickTime() + bowTime()){
			for(SubModel leg : legs){
				leg.rotationX -= BOW_SPEED;
				leg.childModels.get(0).rotationX += BOW_SPEED * 2;
				leg.childModels.get(0).childModels.get(0).rotationX -= BOW_SPEED;
			}
		}
		if(state >= pickTime() + bowTime()){
			finish();
		}
		++state;
	}

	@Override
	public int duration() {
		return (int) (2 * bowTime() + 2 * pickTime());
	}
	
	@Override
	public Vector3f getBasePosition(){
		float l = entity.getLegLength() * 0.6f;
		if(state < -pickTime()){
			float rot = -(state + pickTime()) / bowTime() * MAX_BOW;
			float extra = (float) (Math.sin(Math.toRadians(rot)) * l);
			return new Vector3f(0, extra - l, 0);
		}
		if(state >= pickTime()){
			float rot = (state - pickTime()) / bowTime() * MAX_BOW;
			float extra = (float) (Math.sin(Math.toRadians(rot)) * l);
			return new Vector3f(0, extra - l, 0);
		}
		return new Vector3f(0, (float) -Math.sin(Math.toRadians(MAX_BOW)) * l, 0);
	}
	
	public float bowTime(){
		return MAX_BOW / BOW_SPEED;
	}
	
	public float pickTime(){
		return MAX_PICK / PICK_SPEED;
	}
}
