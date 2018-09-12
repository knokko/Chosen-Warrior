package nl.knokko.entities.tasks;

import nl.knokko.entities.living.EntityLiving;

public abstract class Task {
	
	public final EntityLiving entity;
	
	protected int priority;
	/**
	 * This field marks how long this task is disabled.
	 * A value of 0 means this task is enabled.
	 * A negative value means this task is disabled until enable() is called.
	 * A positive value of x means this task is disabled for x more ticks.
	 */
	protected int disabled;
	
	private boolean destroyed;

	public Task(EntityLiving taskOwner, int priority) {
		entity = taskOwner;
		this.priority = priority;
		enable();
	}
	
	public final void update(){
		if(isEnabled())
			updateTask();
		else if(disabled > 0)
			--disabled;
	}
	
	public void enable(){
		disabled = 0;
	}
	
	public void disable(){
		disabled = -1;
	}
	
	public void disable(int ticks){
		if(ticks < 0)
			throw new IllegalArgumentException("Negative disable time: " + ticks);
		if(ticks > disabled)
			disabled = ticks;
	}
	
	public boolean isEnabled(){
		return disabled == 0;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public void destroy(){
		entity.removeTask(this);
		destroyed = true;
	}
	
	public boolean isDestroyed(){
		return destroyed;
	}
	
	public abstract void updateTask();
}
