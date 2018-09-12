package nl.knokko.entities.living;

import java.awt.Color;

import nl.knokko.entities.tasks.TaskKeyboardControl;
import nl.knokko.entities.util.SubModel;
import nl.knokko.guis.GuiGameOver;
import nl.knokko.main.Game;
import nl.knokko.render.DisplayManager;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.textures.ModelDinoTexture;
import nl.knokko.util.Resources;
import nl.knokko.world.forest.animals.Dinosaurs;

import org.lwjgl.util.vector.Vector3f;

public class EntityPlayer extends EntityDinosaur {
	
	private TaskKeyboardControl control = new TaskKeyboardControl(this, 1);
	
	private SubModel leftArm;
	private SubModel rightArm;
	private SubModel head;
	private SubModel leftLeg;
	private SubModel rightLeg;
	private SubModel tail;
	
	public Color skinColor;
	public Color nailColor;

	public EntityPlayer(Vector3f position, Color skinColor, Color nailColor, float rotationX, float rotationY, float rotationZ, float size) {
		super(new TexturedModel(Resources.getHumanoidModel(Dinosaurs.HUMANOID), Resources.getDinoTexture(skinColor, nailColor, 0.3f, Resources.DINO_SEED)), position, Dinosaurs.HUMANOID, rotationX, rotationY, rotationZ, size);
		tasks.add(control);
		subModels.add(leftArm);
		subModels.add(rightArm);
		subModels.add(head);
		subModels.add(leftLeg);
		subModels.add(rightLeg);
		subModels.add(tail);
		leftArm.rotationZ = -12;
		rightArm.rotationZ = 12;
		leftArm.childModels.get(0).rotationX = 30;
		rightArm.childModels.get(0).rotationX = 30;
		this.skinColor = skinColor;
		this.nailColor = nailColor;
	}
	
	@Override
	public void update(){
		super.update();
		if(isEnabled()){
			leftArm.childModels.get(0).rotationX = 30;
			rightArm.childModels.get(0).rotationX = 30;
		}
	}
	
	@Override
	protected void initBody(){
		ModelDinoTexture texture = (ModelDinoTexture) getModel().getTexture();
		leftArm = Resources.createHumanoidArm(Dinosaurs.HUMANOID, texture, true);
		rightArm = Resources.createHumanoidArm(Dinosaurs.HUMANOID, texture, false);
		head = Resources.createHumanoidHead(Dinosaurs.HUMANOID, texture);
		leftLeg = Resources.createHumanoidLeg(Dinosaurs.HUMANOID, texture, true);
		rightLeg = Resources.createHumanoidLeg(Dinosaurs.HUMANOID, texture, false);
		tail = Resources.createHumanoidTail(Dinosaurs.HUMANOID, texture);
	}
	
	@Override
	public boolean shouldRenderNow(){
		return Game.getCamera().isThirdPerson();
	}
	
	@Override
	public float getWalkForce() {
		return 2500;
	}
	
	@Override
	public SubModel[] getLegs() {
		return new SubModel[]{leftLeg, rightLeg};
	}
	
	@Override
	public SubModel getMainHead() {
		return head;
	}

	@Override
	public SubModel[] getArms() {
		return new SubModel[]{leftArm, rightArm};
	}
	
	@Override
	public void destroy(){
		super.destroy();
		Game.setCurrentGUI(GuiGameOver.getInstance());
	}

	@Override
	public byte getDinoID() {
		throw new RuntimeException("EntityPlayer doesn't need an ID!");
	}
	
	@Override
	public int getStepDuration(){
		return 2 * DisplayManager.FPS_CAP;
	}
}
