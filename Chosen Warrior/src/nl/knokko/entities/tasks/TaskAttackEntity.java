package nl.knokko.entities.tasks;

import java.util.Random;

import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.moves.MovePunch;
import nl.knokko.entities.util.SubModel;
import nl.knokko.items.ItemEquipmentHand;

public class TaskAttackEntity extends Task {
	
	protected EntityPhysical target;
	protected Random random;

	public TaskAttackEntity(EntityLiving taskOwner, EntityPhysical target, int priority) {
		super(taskOwner, priority);
		this.target = target;
		this.random = new Random();
	}

	@Override
	public void updateTask() {
		if(target.isDestroyed()){
			destroy();
			return;
		}
		if(entity.getDistance(target) < 1.3f){
			SubModel[] hands = entity.getHands();
			int index = random.nextInt(hands.length);
			SubModel hand = hands[index];
			ItemEquipmentHand weapon = hand.getHeldItem();
			if(weapon != null)
				weapon.onItemUse(entity, entity.getArms()[index], hand);
			else
				entity.setMove(new MovePunch(entity, entity.getArms()[index], weapon));
		}
	}

}
