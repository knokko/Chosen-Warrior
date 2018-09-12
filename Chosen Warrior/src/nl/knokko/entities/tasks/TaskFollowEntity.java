package nl.knokko.entities.tasks;

import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.living.EntityLiving;

public class TaskFollowEntity extends Task {
	
	protected EntityPhysical target;

	public TaskFollowEntity(EntityLiving taskOwner, EntityPhysical target, int priority) {
		super(taskOwner, priority);
		this.target = target;
	}

	@Override
	public void updateTask() {
		if(target.isDestroyed()){
			destroy();
			return;
		}
		if(!entity.getMover().isDestination(target.getPosition()))
			entity.getMover().setDestination(target.getPosition(), this);
	}
}
