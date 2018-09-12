package nl.knokko.entities.living;

import java.nio.ByteBuffer;

import nl.knokko.collision.Collider;
import nl.knokko.collision.ColliderBox;
import nl.knokko.entities.util.SubModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.world.forest.animals.DinoSettings;

import org.lwjgl.util.vector.Vector3f;

public abstract class EntityDinosaur extends EntityLiving {
	
	protected DinoSettings settings;

	public EntityDinosaur(TexturedModel model, Vector3f position, DinoSettings settings, float rotationX, float rotationY, float rotationZ, float size) {
		super(model, position, rotationX, rotationY, rotationZ, size);
		this.settings = settings;
	}
	
	@Override
	public void update(){
		super.update();
	}

	@Override
	public float getMaxHealth() {
		return 100;
	}

	@Override
	public float getWalkForce() {
		return 30000;
	}
	
	@Override
	public float getJumpForce(){
		return 80000;
	}
	
	@Override
	public float getMass(){
		return 200;
	}
	
	@Override
	public float getStepsPerSecond() {
		return 1f;
	}
	
	@Override
	public Collider createCollider(){
		float minY = settings.bodyLength * 0.5f + settings.legLength + settings.footHeight;
		float minXZ = settings.bodyRadius + settings.armRadius * 2;
		float maxY = settings.bodyLength * 0.5f + settings.headRadius * 2;
		ColliderBox col = new ColliderBox(-minXZ * size, -minY * size, -minXZ * size, minXZ * size, maxY * size, minXZ * size);
		return col;
	}

	@Override
	public abstract SubModel[] getLegs();
	
	@Override
	protected abstract void initBody();

	@Override
	public abstract SubModel[] getArms();
	
	@Override
	public float getLegLength(){
		return settings.legLength;
	}
	
	@Override
	public float getArmLength(){
		return settings.armLength;
	}
	
	@Override
	public float getAirFrictionFactor(){
		return 0.995f;
	}
	
	@Override
	public ByteBuffer saveMapData() {
		ByteBuffer buffer = ByteBuffer.allocate(29);
		buffer.put(getDinoID());
		putPosition(buffer);
		putRotation(buffer);
		buffer.putFloat(size);
		return buffer;
	}
	
	public abstract byte getDinoID();
}
