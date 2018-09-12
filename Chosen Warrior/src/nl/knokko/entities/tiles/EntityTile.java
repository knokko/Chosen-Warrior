package nl.knokko.entities.tiles;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.collision.Collider;
import nl.knokko.entities.Entity;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Resources;
import nl.knokko.world.World;

public class EntityTile extends Entity {

	public static final byte ID = -124;
	
	private TexturedModel model;
	private Collider collider;

	public EntityTile(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float size) {
		this.model = model;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.size = size;
	}
	
	public Collider createCollider(){
		return model.getModel().getCollider().createScaledClone(size);
	}

	public TexturedModel getModel() {
		return model;
	}
	
	@Override
	public Collider getCollider(){
		if(collider == null)
			collider = createCollider();
		return collider;
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public boolean shouldUpdate() {
		return false;
	}

	@Override
	public boolean shouldCollide() {
		return true;
	}

	@Override
	public boolean shouldRenderNow() {
		return true;
	}

	@Override
	public boolean shouldBePermanent() {
		return true;
	}

	@Override
	public boolean shouldBeCollectible() {
		return false;
	}

	@Override
	public void setWorld(World world) {}

	@Override
	public ByteBuffer saveMapData() {
		byte[] modelCreation = model.getModel().getCreationID();
		byte[] textureCreation = model.getTexture().getCreationID();
		ByteBuffer buffer = ByteBuffer.allocate(32 + modelCreation.length + textureCreation.length);
		buffer.put(ID);
		putPosition(buffer);
		putRotation(buffer);
		buffer.putFloat(size);
		buffer.put((byte) modelCreation.length);
		buffer.put(modelCreation);
		buffer.put((byte) textureCreation.length);
		buffer.put(textureCreation);
		return buffer;
	}
	
	public static EntityTile loadMapData(ByteBuffer buffer){
		Vector3f position = new Vector3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
		float rotX = buffer.getFloat();
		float rotY = buffer.getFloat();
		float rotZ = buffer.getFloat();
		float size = buffer.getFloat();
		byte[] modelCreation = new byte[buffer.get()];
		buffer.get(modelCreation);
		byte[] textureCreation = new byte[buffer.get()];
		buffer.get(textureCreation);
		return new EntityTile(new TexturedModel(Resources.getModelFromID(modelCreation), Resources.getTextureFromID(textureCreation)), position, rotX, rotY, rotZ, size);
	}
}
