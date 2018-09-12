package nl.knokko.entities.living;

import java.awt.Color;
import java.nio.ByteBuffer;

import nl.knokko.entities.tasks.TaskAttackEntity;
import nl.knokko.entities.tasks.TaskFollowEntity;
import nl.knokko.entities.util.SubModel;
import nl.knokko.items.Items;
import nl.knokko.main.Game;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.textures.ModelDinoTexture;
import nl.knokko.util.Resources;
import nl.knokko.world.forest.animals.Dinosaurs;

import org.lwjgl.util.vector.Vector3f;

public class EntityScatch extends EntityDinosaur {
	
	public static final byte ID = -123;
	
	private SubModel leftArm;
	private SubModel rightArm;
	private SubModel head;
	private SubModel leftLeg;
	private SubModel rightLeg;
	private SubModel tail;

	public EntityScatch(Vector3f position, float rotationX, float rotationY, float rotationZ, float size) {
		super(new TexturedModel(Resources.getHumanoidModel(Dinosaurs.HUMANOID), Resources.getDinoTexture(Color.ORANGE, Color.RED, 0.3f, Resources.DINO_SEED)), position, Dinosaurs.HUMANOID, rotationX, rotationY, rotationZ, size);
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
	}
	
	@Override
	public void update(){
		if(tasks.isEmpty() && Game.getPlayer() != null && !Game.getPlayer().isDestroyed()){
			addTask(new TaskFollowEntity(this, Game.getPlayer(), 1));
			addTask(new TaskAttackEntity(this, Game.getPlayer(), 1));
			setHeldItem(Items.KNIFE, getHands()[0]);
		}
		super.update();
	}

	@Override
	protected void initBody() {
		ModelDinoTexture texture = (ModelDinoTexture) getModel().getTexture();
		leftArm = Resources.createHumanoidArm(Dinosaurs.HUMANOID, texture, true);
		rightArm = Resources.createHumanoidArm(Dinosaurs.HUMANOID, texture, false);
		head = Resources.createHumanoidHead(Dinosaurs.HUMANOID, texture);
		leftLeg = Resources.createHumanoidLeg(Dinosaurs.HUMANOID, texture, true);
		rightLeg = Resources.createHumanoidLeg(Dinosaurs.HUMANOID, texture, false);
		tail = Resources.createHumanoidTail(Dinosaurs.HUMANOID, texture);
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
	public byte getDinoID() {
		return ID;
	}
	
	public static EntityScatch loadMapData(ByteBuffer buffer){
		Vector3f position = new Vector3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
		float rotX = buffer.getFloat();
		float rotY = buffer.getFloat();
		float rotZ = buffer.getFloat();
		float size = buffer.getFloat();
		return new EntityScatch(position, rotX, rotY, rotZ, size);
	}
}
