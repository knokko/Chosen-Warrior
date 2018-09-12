package nl.knokko.entities.moves;

import nl.knokko.entities.Entity;
import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.util.SubModel;
import nl.knokko.render.DisplayManager;

public class MoveThrowUp extends Move {
	
	private static final float BACK_ROTATION = -45f;
	
	private static final int BACK_TIME = DisplayManager.FPS_CAP / 2;
	private static final int THROW_TIME = DisplayManager.FPS_CAP / 3;
	private static final int RETURN_TIME = DisplayManager.FPS_CAP / 2;
	
	private static final float BACK_DELTA = BACK_ROTATION / BACK_TIME;
	
	private static final int STATE1 = BACK_TIME;
	private static final int STATE2 = BACK_TIME + THROW_TIME;
	private static final int STATE3 = BACK_TIME + THROW_TIME + RETURN_TIME;
	
	private SubModel upArm;
	private SubModel lowArm;
	
	private Entity throwEntity;
	
	private int state;
	
	private float pitch;
	
	private float throwDelta = (pitch - BACK_ROTATION) / THROW_TIME;
	private float throwDelta2 = pitch / THROW_TIME;
	private float RETURN_DELTA = -pitch / RETURN_TIME;

	public MoveThrowUp(EntityLiving user, SubModel upperArm, Entity throwEntity, float pitch) {
		super(user);
		this.throwEntity = throwEntity;
		upArm = upperArm;
		lowArm = upArm.childModels.get(0);
		this.pitch = pitch / 2;
	}

	@Override
	public void update() {
		if(state < STATE1){
			upArm.rotationX += BACK_DELTA;
		}
		else if(state < STATE2){
			upArm.rotationX += throwDelta;
			lowArm.rotationX += throwDelta2;
		}
		else if(state == STATE2){
			entity.setHeldItem(null, lowArm.childModels.get(0));
			entity.getWorld().spawnEntity(throwEntity);
		}
		else if(state <= STATE3){
			upArm.rotationX += RETURN_DELTA;
			lowArm.rotationX -= throwDelta2;
		}
		else
			finish();
		state++;
	}

	@Override
	public int duration() {
		return STATE3;
	}
	
	@Override
	public void start(){
		super.start();
		reset();
	}
	
	@Override
	public void interupt(){
		super.interupt();
		reset();
	}
	
	private void reset(){
		upArm.rotationX = 0;
		lowArm.rotationX = 0;
	}
}
