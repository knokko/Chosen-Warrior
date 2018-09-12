package nl.knokko.entities.util;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.moves.MoveWalking;
import nl.knokko.entities.tasks.Task;
import nl.knokko.util.Maths;

public class EntityMover {
	
	public final EntityLiving entity;
	
	protected Vector3f destination;
	protected Task task;
	
	protected int currentPriority;
	/**
	 * This field marks how long this task is disabled.
	 * A value of 0 means this task is enabled.
	 * A negative value means this task is disabled until enable() is called.
	 * A positive value of x means this task is disabled for x more ticks.
	 */
	protected int disabled;
	protected boolean flipper;
	
	private MoveWalking walker;
	
	private SubModel[] legs;
	private SubModel[] arms;
	
	private boolean[] legProgress;
	private boolean[] armProgress;

	public EntityMover(EntityLiving entityToMove) {
		entity = entityToMove;
		initLegs();
		initArms();
	}
	
	public boolean setDestination(Vector3f destination, Task commander, int priority){
		if(task == null || (task == commander && priority >= currentPriority) || priority > currentPriority){
			this.destination = destination;
			task = commander;
			currentPriority = priority;
			return true;
		}
		return false;
	}
	
	public boolean setDestination(Vector3f destination, Task commander){
		return setDestination(destination, commander, commander.getPriority());
	}
	
	public boolean hasDestination(){
		return destination != null;
	}
	
	public boolean isDestination(Vector3f goal){
		return hasDestination() && destination.equals(goal);
	}
	
	public int getPriority(){
		return currentPriority;
	}
	
	public void update(){
		if(disabled == 0){
			if(task != null && task.isDestroyed()){
				resetDestination();
				return;
			}
			if(destination != null && entity.currentNormal != null){
				Vector3f direction = Vector3f.sub(destination, entity.getPosition(), null);
				direction.y = 0;
				direction.normalise();
				if(!(entity.getCurrentMove() instanceof MoveWalking) && entity.isEnabled()){
					if(walker == null)
						walker = new MoveWalking(entity, true);
					entity.setMove(walker);
				}
				entity.setRotation(entity.getRotationX(), (float) -Maths.getYaw(direction) + entity.getDefaultYaw(), entity.getRotationZ());
			}
			else if(entity.getCurrentMove() instanceof MoveWalking)
				((MoveWalking) entity.getCurrentMove()).requestStop();
		}
		if(disabled > 0)
			--disabled;
	}
	
	/*
	public void setLegAnimation(float progress, float max){
		if(!isEnabled())
			return;
		resetLegRotation();
		float maxRotation = 45;
		float prog = progress / max;
		for(int i = 0; i < legs.length; i++){
			SubModel leg = legs[i];
			if(prog <= 0.25f)
				leg.rotationX = prog * 4 * maxRotation;
			else if(prog > 0.75f)
				leg.rotationX = -maxRotation + (prog - 0.75f) * maxRotation * 4;
			else 
				leg.rotationX = maxRotation - (prog - 0.25f) * maxRotation * 4;
			if(!legProgress[i])
				leg.rotationX = -leg.rotationX;
			SubModel under = leg.childModels.get(0);
			under.rotationX = -Math.abs(leg.rotationX);
			leg.rotationX += entity.defaultLegRotation();
		}
	}
	
	public void setArmAnimation(float progress, float max){
		if(!isEnabled())
			return;
		resetArmRotation();
		float maxRotation = 20;
		float prog = progress / max;
		for(int i = 0; i < arms.length; i++){
			SubModel arm = arms[i];
			if(prog <= 0.25f)
				arm.rotationX = prog * 4 * maxRotation;
			else if(prog > 0.75f)
				arm.rotationX = -maxRotation + (prog - 0.75f) * maxRotation * 4;
			else
				arm.rotationX = maxRotation - (prog - 0.25f) * maxRotation * 4;
			if(!armProgress[i])
				arm.rotationX = -arm.rotationX;
		}
	}
	*/
	
	public void disable(){
		disabled = -1;
	}
	
	public void disable(int ticks){
		if(ticks < 0)
			throw new IllegalArgumentException("Negative disable time: " + ticks);
		if(ticks > disabled)
			disabled = ticks;
	}
	
	public void enable(){
		disabled = 0;
	}
	
	public boolean isEnabled(){
		return disabled == 0;
	}
	
	public void resetDestination(){
		task = null;
		destination = null;
		currentPriority = 0;
	}
	
	protected void initLegs(){
		legs = entity.getLegs();
		legProgress = new boolean[legs.length];
		initLegFlipper();
	}
	
	protected void initArms(){
		arms = entity.getArms();
		armProgress = new boolean[arms.length];
		initArmFlipper();
	}
	
	protected void initLegFlipper(){
		boolean flipper = false;
		for(int i = 0; i < legProgress.length; i++){
			legProgress[i] = flipper;
			flipper = !flipper;
		}
	}
	
	protected void resetLegRotation(){
		initLegFlipper();
		for(SubModel leg : legs)
			leg.rotationX = 0;
	}
	
	protected void initArmFlipper(){
		boolean flipper = false;
		for(int i = 0; i < armProgress.length; i++){
			armProgress[i] = flipper;
			flipper = !flipper;
		}
	}
	
	protected void resetArmRotation(){
		initArmFlipper();
		for(SubModel arm : arms)
			arm.rotationX = 0;
	}
}
