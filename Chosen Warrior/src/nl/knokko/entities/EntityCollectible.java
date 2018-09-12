package nl.knokko.entities;

import java.nio.ByteBuffer;

import nl.knokko.items.Item;
import nl.knokko.items.Items;
import nl.knokko.main.Game;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Resources;

import org.lwjgl.util.vector.Vector3f;

public class EntityCollectible extends EntityDecorative {
	
	public static final byte ID = -127;
	
	protected float collectRange;
	protected Item item;

	public EntityCollectible(TexturedModel model, Vector3f position, Item item, float rotationX, float rotationY, float rotationZ, float size, float collectRange) {
		super(model, position, rotationX, rotationY, rotationZ, size);
		this.collectRange = collectRange;
		this.item = item;
	}
	
	public EntityCollectible(TexturedModel model, Vector3f position, Item item, float rotationY, float size){
		this(model, position, item, 0, rotationY, 0, size, size * 3);
	}
	
	@Override
	public boolean shouldBeCollectible(){
		return true;
	}
	
	@Override
	public float getCollectingRange(){
		return collectRange;
	}
	
	@Override
	public Item getCollectingItem(){
		return item;
	}
	
	@Override
	public boolean removeAfterCollect(){
		return true;
	}
	
	@Override
	public boolean shouldBePermanent(){
		return false;
	}
	
	@Override
	public void destroy(){
		Game.getWorld().removeEntity(this);
	}
	
	@Override
	public ByteBuffer saveMapData() {
		byte[] modelCreation = model.getModel().getCreationID();
		byte[] textureCreation = model.getTexture().getCreationID();
		ByteBuffer buffer = ByteBuffer.allocate(37 + modelCreation.length + textureCreation.length);
		buffer.put(ID);
		putPosition(buffer);
		putRotation(buffer);
		buffer.putFloat(size);
		buffer.putFloat(collectRange);
		buffer.putShort(item.getID());
		buffer.put((byte) modelCreation.length);
		buffer.put(modelCreation);
		buffer.put((byte) textureCreation.length);
		buffer.put(textureCreation);
		return buffer;
	}
	
	public static EntityCollectible loadMapData(ByteBuffer buffer){
		Vector3f position = new Vector3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
		float rotX = buffer.getFloat();
		float rotY = buffer.getFloat();
		float rotZ = buffer.getFloat();
		float size = buffer.getFloat();
		float collectRange = buffer.getFloat();
		Item item = Items.fromID(buffer.getShort());
		byte[] modelCreation = new byte[buffer.get()];
		buffer.get(modelCreation);
		byte[] textureCreation = new byte[buffer.get()];
		buffer.get(textureCreation);
		return new EntityCollectible(new TexturedModel(Resources.getModelFromID(modelCreation), Resources.getTextureFromID(textureCreation)), position, item, rotX, rotY, rotZ, size, collectRange);
	}
}
