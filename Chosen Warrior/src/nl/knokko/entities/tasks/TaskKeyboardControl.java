package nl.knokko.entities.tasks;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.Entity;
import nl.knokko.entities.EntityCollectible;
import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.moves.*;
import nl.knokko.entities.util.SubModel;
import nl.knokko.items.ItemEquipmentHand;
import nl.knokko.main.Game;
import nl.knokko.render.DisplayManager;
import nl.knokko.util.Maths;
import nl.knokko.util.Options;

public class TaskKeyboardControl extends Task {
	
	private MoveWalking walker;
	
	private byte walkTimer;
	private byte maxWalkTimer;
	
	private int cooldownLeft;
	private int cooldownRight;
	
	private boolean flipper;
	private boolean didMove;

	public TaskKeyboardControl(EntityLiving taskOwner, int priority) {
		super(taskOwner, priority);
		maxWalkTimer = (byte) (DisplayManager.FPS_CAP / entity.getStepsPerSecond() / 2);
	}

	@Override
	public void updateTask() {
		if(didMove){
			setWalkTimer();
			didMove = false;
		}
		if(cooldownLeft > 0)
			cooldownLeft--;
		if(cooldownRight > 0)
			cooldownRight--;
		if(Options.isKeyDown(Game.getOptions().get360KickKey()))
			entity.setMove(new Move360Kick(entity));
		if(Options.isKeyDown(Game.getOptions().getCollectKey())){
			Entity collected = entity.getWorld().getNearestCollectible(entity);
			if(collected != null)
				entity.setMove(new MoveCollect(entity, entity.getArms()[0], (EntityCollectible) collected));
		}
		if(Options.isKeyDown(Game.getOptions().getLeftHandKey()) && cooldownLeft == 0){
			SubModel hand = entity.getHands()[0];
			ItemEquipmentHand item = hand.getHeldItem();
			if(item != null){
				item.onItemUse(entity, entity.getArms()[0], hand);
				cooldownLeft = item.getCooldown();
			}
			else
				entity.setMove(new MovePunch(entity, entity.getArms()[0], null));
		}
		if(Options.isKeyDown(Game.getOptions().getRightHandKey()) && cooldownRight == 0){
			SubModel hand = entity.getHands()[1];
			ItemEquipmentHand item = hand.getHeldItem();
			if(item != null){
				item.onItemUse(entity, entity.getArms()[1], hand);
				cooldownRight = item.getCooldown();
			}
			else
				entity.setMove(new MovePunch(entity, entity.getArms()[1], null));
		}
		if(Options.isKeyDown(Game.getOptions().getLeftKey()) && entity.currentNormal != null){
			Vector3f left = Maths.getRotationVector(0, entity.getRotationY() - 90, 0);
			entity.addForce(left.x * entity.getWalkForce(), left.y * entity.getWalkForce(), left.z * entity.getWalkForce());
			didMove = true;
		}
		if(Options.isKeyDown(Game.getOptions().getRightKey()) && entity.currentNormal != null){
			Vector3f right = Maths.getRotationVector(0, entity.getRotationY() + 90, 0);
			entity.addForce(right.x * entity.getWalkForce(), right.y * entity.getWalkForce(), right.z * entity.getWalkForce());
			didMove = true;
		}
		if(Options.isKeyDown(Game.getOptions().getJumpKey()) && entity.currentNormal != null)
			entity.addForce(entity.currentNormal, entity.getJumpForce());
		if(Options.isKeyDown(Game.getOptions().getRotateLeftKey()))
			entity.increaseRotation(0, -Game.getOptions().getRotateSensitivity(), 0);
		if(Options.isKeyDown(Game.getOptions().getRotateRightKey()))
			entity.increaseRotation(0, Game.getOptions().getRotateSensitivity(), 0);
		if(Options.isKeyDown(Game.getOptions().getForwardKey()) && entity.currentNormal != null){
			if(!(entity.getCurrentMove() instanceof MoveWalking) && entity.isEnabled()){
				if(walker == null)
					walker = new MoveWalking(entity, true);
				entity.setMove(walker);
			}
			if(entity.getCurrentMove() instanceof MoveWalking)
				((MoveWalking) entity.getCurrentMove()).setForward(true);
		}
		else if(entity.getCurrentMove() == walker && walker != null)
			walker.requestStop();
		if(Options.isKeyDown(Game.getOptions().getBackwardKey()) && entity.currentNormal != null){
			if(!(entity.getCurrentMove() instanceof MoveWalking) && entity.isEnabled()){
				if(walker == null)
					walker = new MoveWalking(entity, false);
				entity.setMove(walker);
			}
			if(entity.getCurrentMove() instanceof MoveWalking)
				((MoveWalking) entity.getCurrentMove()).setForward(false);
		}
	}
	
	public float getC(){
		return walkTimer > 0 ? 0.7f : 1.1f;
	}
	
	private void setWalkTimer(){
		walkTimer = maxWalkTimer;
		flipper = !flipper;
	}
}
