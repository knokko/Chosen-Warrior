package nl.knokko.entities.living;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.moves.Move;
import nl.knokko.entities.tasks.Task;
import nl.knokko.entities.util.EntityMover;
import nl.knokko.entities.util.SubModel;
import nl.knokko.items.ItemEquipmentHand;
import nl.knokko.render.DisplayManager;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.patterns.PatternAverage;

public abstract class EntityLiving extends EntityPhysical {
	
	protected ArrayList<Task> tasks = new ArrayList<Task>();
	private SubModel[] nails;
	private SubModel[] hands;
	
	protected EntityMover mover;
	protected Move currentMove;
	protected Color effectColor;
	protected Random random = new Random();
	
	protected float health = getMaxHealth();
	
	private int enabled = 0;
	protected int effectTime;

	public EntityLiving(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float size) {
		super(model, position, rotationX, rotationY, rotationZ, size);
		initBody();
		mover = new EntityMover(this);
	}
	
	@Override
	public Color getEffectColor(){
		if(effectColor != null)
			return PatternAverage.getDifColor(random, effectColor, 0.3f);
		return effectColor;
	}
	
	@Override
	public void update(){
		super.update();
		if(effectTime == 0)
			effectColor = null;
		if(effectTime > 0)
			--effectTime;
		if(enabled > 0)
			--enabled;
		if(mover != null)
			mover.update();
		for(int i = 0; i < tasks.size(); i++)
			tasks.get(i).update();
		if(currentMove != null)
			currentMove.update();
	}
	
	@Override
	public Vector3f getBaseModelPosition(){
		if(currentMove != null && currentMove.getBasePosition() != null)
			return currentMove.getBasePosition();
		return super.getBaseModelPosition();
	}
	
	public void setEffectColor(Color color, int ticks){
		effectColor = color;
		effectTime = ticks;
	}
	
	public EntityMover getMover(){
		return mover;
	}
	
	public float getHealth(){
		return health;
	}
	
	public void attack(float damage){
		health -= damage;
		setEffectColor(Color.RED, DisplayManager.FPS_CAP / 2);
		if(health <= 0)
			die();
	}
	
	public void addTask(Task task){
		tasks.add(task);
	}
	
	public boolean removeTask(Task task){
		return tasks.remove(task);
	}
	
	public void die(){
		destroy();
	}
	
	public void disableControl(int ticks){
		enabled = ticks;
		mover.disable(ticks);
		for(Task task : tasks)
			task.disable(ticks);
	}
	
	public void enableControl(){
		enabled = 0;
		mover.enable();
		for(Task task : tasks)
			task.enable();
	}
	
	public boolean isEnabled(){
		return enabled == 0;
	}
	
	public void completeMove(){
		currentMove = null;
	}
	
	public void setMove(Move move){
		if(currentMove != null)
			currentMove.interupt();
		currentMove = move;
		if(currentMove != null)
			currentMove.start();
	}
	
	public Move getCurrentMove(){
		return currentMove;
	}
	
	public float getDefaultYaw(){
		return 0;
	}
	
	public abstract float getMaxHealth();
	
	/**
	 * The force applied for each step.
	 */
	public abstract float getWalkForce();
	
	public abstract float getJumpForce();
	
	public float getPunchForce(){
		return 5000 * size;
	}
	
	public float getKickForce(){
		return 7000 * size;
	}
	
	public int getStepDuration(){
		return DisplayManager.FPS_CAP;
	}
	
	/**
	 * The number of steps per second.
	 */
	public abstract float getStepsPerSecond();
	
	/**
	 * All models that should animate like legs.
	 */
	public abstract SubModel[] getLegs();
	
	/**
	 * This method is required for collect animation.
	 * @return
	 */
	public abstract float getLegLength();
	
	public abstract float getArmLength();
	
	public float defaultLegRotation(){
		return 0;
	}
	
	/**
	 * All models that should animate like arms.
	 * @return
	 */
	public abstract SubModel[] getArms();
	
	public SubModel[] getHands(){
		if(hands == null){
			SubModel[] arms = getArms();
			hands = new SubModel[arms.length];
			for(int i = 0; i < arms.length; i++)
				hands[i] = arms[i].childModels.get(0).childModels.get(0);
		}
		return hands;
	}
	
	public SubModel[] getHandNails(){
		if(nails == null){
			ArrayList<SubModel> list = new ArrayList<SubModel>();
			SubModel[] arms = getArms();
			for(SubModel arm : arms)
				addLastSubModels(list, arm);
			nails = list.toArray(new SubModel[list.size()]);
		}
		return nails;
	}
	
	private static void addLastSubModels(ArrayList<SubModel> list, SubModel part){
		if(part.childModels.isEmpty())
			list.add(part);
		else {
			for(SubModel next : part.childModels)
				addLastSubModels(list, next);
		}
	}
	
	public abstract SubModel getMainHead();
	
	public float getHeadPitch(){
		SubModel head = getMainHead();
		if(head != null)
			return rotationX - head.rotationX;
		return rotationX;
	}
	
	public SubModel getMainHand(){
		SubModel[] hands = getHandNails();
		if(hands.length == 0)
			throw new RuntimeException("The main hand of an entity without hands is requested.");
		return hands[0];
	}
	
	public void setHeldItem(ItemEquipmentHand item, SubModel hand){
		SubModel oldWeapon = hand.getHeldItemModel();
		TexturedModel old = null;
		if(oldWeapon != null)
			old = oldWeapon.getModel();
		hand.setHeldItem(item);
		world.getChunk(position.x, position.z).getRenderer().changeWeapon(this, hand, old);
	}
	
	public void setHeldItem(ItemEquipmentHand item){
		setHeldItem(item, getMainHand());
	}
	
	protected void initBody(){}
}
