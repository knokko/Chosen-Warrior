package nl.knokko.entities.moves;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.util.SubModel;
import nl.knokko.util.Resources;

public class MoveWalking extends Move {
	
	private static final float MAX_ROTATION = 40f;
	
	private final SubModel[] legs;
	private final SubModel[] arms;
	
	private final int duration;
	private final int state1;
	private final int state2;
	private final int state3;
	
	private final float delta;
	private final float speed;
	
	private int state;
	
	private boolean isStopping;
	private boolean moveForward;
	private boolean setToForward;

	public MoveWalking(EntityLiving user, boolean moveForward) {
		super(user);
		legs = user.getLegs();
		arms = user.getArms();
		duration = user.getStepDuration();
		state1 = duration / 4;
		state2 = duration / 2;
		state3 = state1 + state2;
		delta = MAX_ROTATION / state1;
		double stepLength = Math.sin(Math.toRadians(MAX_ROTATION)) * entity.getLegLength();
		speed = (float) (stepLength * 8 / duration);
		this.moveForward = moveForward;
		setToForward = moveForward;
	}
	
	@Override
	public void start(){
		resetLegRotation();
	}
	
	@Override
	public void interupt(){
		resetLegRotation();
		state = 0;
		moveForward = setToForward;
	}

	@Override
	public void update() {
		if(state < state1)
			rotate(moveForward ? delta : -delta);
		else if(state < state2)
			rotate(moveForward ? -delta : delta);
		else if(state == state2){
			if(isStopping){
				entity.setMove(null);
				isStopping = false;
				state = state2;
				moveForward = setToForward;
				return;
			}
			moveForward = setToForward;
		}
		else if(state <= state3)
			rotate(moveForward ? -delta : delta);
		else
			rotate(moveForward ? delta : -delta);
		if(entity.currentNormal != null){
			Vector3f forward = Resources.pointAt(entity.getRotationY() - 90, new Vector3f(), entity.currentNormal.normalise(null), 1);
			if(!moveForward)
				forward.negate();
			entity.move(forward.x * speed, forward.y * speed, forward.z * speed);
		}
		if(state < duration)
			state++;
		else {
			if(isStopping){
				entity.setMove(null);
				isStopping = false;
			}
			moveForward = setToForward;
			state = 0;
		}
	}

	@Override
	public int duration() {
		throw new RuntimeException("Infinite duration");
	}
	
	public void requestStop(){
		isStopping = true;
	}
	
	public void setForward(boolean forward){
		setToForward = forward;
	}
	
	private void resetLegRotation(){
		for(SubModel leg : legs){
			leg.rotationX = 0;
			leg.childModels.get(0).rotationX = 0;
		}
	}
	
	private void rotate(float delta){
		for(int i = 0; i + 1 < legs.length; i++){
			legs[i].rotationX += delta;
			legs[i + 1].rotationX -= delta;
			legs[i].childModels.get(0).rotationX = -Math.abs(legs[i].rotationX);
			legs[i + 1].childModels.get(0).rotationX = -Math.abs(legs[i + 1].rotationX);
		}
		for(int i = 0; i + 1 < arms.length; i++){
			arms[i].rotationX -= delta * 0.4f;
			arms[i + 1].rotationX += delta * 0.4f;
		}
	}
}
