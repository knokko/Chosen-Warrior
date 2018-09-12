package nl.knokko.entities;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Resources;
import nl.knokko.world.World;

public class EntityDecorative extends Entity {
	
	public static final byte ID = -128;
	
	protected TexturedModel model;
	
	private boolean canSave = true;;

	public EntityDecorative(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float size) {
		this.model = model;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.size = size;
	}
	
	public EntityDecorative(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float size, boolean canSave){
		this(model, position, rotationX, rotationY, rotationZ, size);
		this.canSave = canSave;
	}
	
	@Override
	public boolean canSave(){
		return canSave;
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
		return false;
	}

	@Override
	public boolean shouldRenderNow() {
		return true;
	}
	
	@Override
	public boolean shouldBePermanent(){
		return true;
	}

	@Override
	public void setWorld(World world) {}
	
	@Override
	public TexturedModel getModel(){
		return model;
	}

	@Override
	public boolean shouldBeCollectible() {
		return false;
	}

	@Override
	public ByteBuffer saveMapData() {
		byte[] modelCreation = model.getModel().getCreationID();
		byte[] textureCreation = model.getTexture().getCreationID();
		ByteBuffer buffer = ByteBuffer.allocate(31 + modelCreation.length + textureCreation.length);
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
	
	public static EntityDecorative loadMapData(ByteBuffer buffer){
		Vector3f position = new Vector3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
		float rotX = buffer.getFloat();
		float rotY = buffer.getFloat();
		float rotZ = buffer.getFloat();
		float size = buffer.getFloat();
		byte[] modelCreation = new byte[buffer.get()];
		buffer.get(modelCreation);
		byte[] textureCreation = new byte[buffer.get()];
		buffer.get(textureCreation);
		return new EntityDecorative(new TexturedModel(Resources.getModelFromID(modelCreation), Resources.getTextureFromID(textureCreation)), position, rotX, rotY, rotZ, size);
	}
}
