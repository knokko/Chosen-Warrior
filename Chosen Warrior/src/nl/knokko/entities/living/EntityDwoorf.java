package nl.knokko.entities.living;

import java.nio.ByteBuffer;

import nl.knokko.collision.Collider;
import nl.knokko.collision.ColliderBox;
import nl.knokko.entities.tasks.TaskFollowEntity;
import nl.knokko.entities.util.SubModel;
import nl.knokko.main.Game;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.textures.ModelDinoTexture;
import nl.knokko.util.Resources;
import nl.knokko.world.forest.animals.Dinosaurs;

import org.lwjgl.util.vector.Vector3f;

public class EntityDwoorf extends EntityDinosaur {
	
	public static final byte ID = -125;
	
	private SubModel leftLegFront;
	private SubModel rightLegFront;
	private SubModel leftLegBack;
	private SubModel rightLegBack;
	
	private SubModel leftArm;
	private SubModel rightArm;
	
	private SubModel leftNeck;
	private SubModel mainNeck;
	private SubModel rightNeck;

	public EntityDwoorf(Vector3f position, float size) {
		super(new TexturedModel(Resources.getHumanoidModel(Dinosaurs.DWOORF), Dinosaurs.DARK_PURPLE_SKIN), position, Dinosaurs.DWOORF, 90, 180, 0, size);
	}
	
	public EntityDwoorf(Vector3f position, float pitch, float yaw, float roll, float size){
		super(new TexturedModel(Resources.getHumanoidModel(Dinosaurs.DWOORF), Dinosaurs.DARK_PURPLE_SKIN), position, Dinosaurs.DWOORF, pitch, yaw, roll, size);
	}
	
	@Override
	public void update(){
		if(tasks.isEmpty() && Game.getPlayer() != null && !Game.getPlayer().isDestroyed())
			addTask(new TaskFollowEntity(this, Game.getPlayer(), 1));
		super.update();
	}
	
	@Override
	public float getDefaultYaw(){
		return 0;
	}
	
	@Override
	public float getStepsPerSecond() {
		return 1f;
	}
	
	@Override
	public float getWalkForce() {
		return 2000;
	}

	@Override
	protected void initBody() {
		ModelDinoTexture texture = (ModelDinoTexture) getModel().getTexture();
		settings = Dinosaurs.DWOORF;
		leftLegFront = Resources.createDwoorfLeg(settings, texture, true, true);
		rightLegFront = Resources.createDwoorfLeg(settings, texture, false, true);
		leftLegBack = Resources.createDwoorfLeg(settings, texture, true, false);
		rightLegBack = Resources.createDwoorfLeg(settings, texture, false, false);
		leftArm = Resources.createDwoorfArm(settings, texture, true);
		rightArm = Resources.createDwoorfArm(settings, texture, false);
		leftNeck = Resources.createDwoorfHead(settings, texture, -settings.bodyRadius * 0.5f, 30);
		mainNeck = Resources.createDwoorfHead(settings, texture, 0, 0);
		rightNeck = Resources.createDwoorfHead(settings, texture, settings.bodyRadius * 0.5f, -30);
		subModels.add(leftLegFront);
		subModels.add(rightLegFront);
		subModels.add(leftLegBack);
		subModels.add(rightLegBack);
		subModels.add(leftArm);
		subModels.add(rightArm);
		subModels.add(leftNeck);
		subModels.add(mainNeck);
		subModels.add(rightNeck);
		leftLegFront.rotationX = -90;
		rightLegFront.rotationX = -90;
		leftLegBack.rotationX = -90;
		rightLegBack.rotationX = -90;
	}
	
	@Override
	public float defaultLegRotation(){
		return -90;
	}

	@Override
	public SubModel[] getArms() {
		return new SubModel[]{leftArm, rightArm};
	}
	
	@Override
	public SubModel[] getLegs() {
		return new SubModel[]{leftLegFront, rightLegFront, leftLegBack, rightLegBack};
	}

	@Override
	public SubModel getMainHead() {
		return mainNeck;
	}
	
	@Override
	public Collider createCollider(){
		float minY = settings.legLength + settings.footHeight * 0.5f;
		float minXZ = settings.bodyLength * 0.7f;
		float maxY = settings.bodyRadius + settings.headRadius * 2;
		return new ColliderBox(-minXZ * size, -minY * size, -minXZ * size, minXZ * size, maxY * size, minXZ * size);
	}
	
	@Override
	public float getMaxHealth(){
		return 250;
	}

	@Override
	public byte getDinoID() {
		return ID;
	}
	
	public static EntityDwoorf loadMapData(ByteBuffer buffer){
		Vector3f position = new Vector3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
		float rotX = buffer.getFloat();
		float rotY = buffer.getFloat();
		float rotZ = buffer.getFloat();
		float size = buffer.getFloat();
		return new EntityDwoorf(position, rotX, rotY, rotZ, size);
	}
}
