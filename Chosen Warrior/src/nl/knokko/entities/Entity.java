package nl.knokko.entities;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.collision.Collider;
import nl.knokko.entities.util.SubModel;
import nl.knokko.items.Item;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Maths;
import nl.knokko.world.Chunk;
import nl.knokko.world.World;

public abstract class Entity {
	
	public ArrayList<SubModel> subModels = new ArrayList<SubModel>();
	
	protected Vector3f position;
	
	protected float rotationX,rotationY,rotationZ;
	protected float motionX,motionY,motionZ;
	protected float size;
	
	public abstract boolean shouldRender();
	
	public abstract boolean shouldUpdate();
	
	public abstract boolean shouldCollide();
	
	public abstract boolean shouldRenderNow();
	
	public abstract boolean shouldBePermanent();
	
	public abstract boolean shouldBeCollectible();
	
	public abstract void setWorld(World world);
	
	/**
	 * Place this entity in the world.
	 * @param chunk The chunk where the entity is located
	 */
	public void place(Chunk chunk){}
	
	/**
	 * For saving the data of this entity for the map designing.
	 */
	public abstract ByteBuffer saveMapData();
	
	public boolean canSave(){
		return true;
	}
	
	public Color getEffectColor(){
		return null;
	}
	
	public void destroy(){
		throw new RuntimeException("Subclasses of Entity who can be removed, should override this method!");
	}
	
	public void update(){
		throw new RuntimeException("Subclasses of Entity who are capable of updating, should override this method!");
	}

	public Collider getCollider() {
		throw new RuntimeException("Subclasses of Entity who are capable of colliding, should override this method!");
	}

	public TexturedModel getModel() {
		throw new RuntimeException("Subclasses of Entity who are capable of rendering, should override this method!");
	}
	
	public Vector3f getBaseModelPosition(){
		return new Vector3f();
	}
	
	public float getCollectingRange(){
		throw new RuntimeException("Subclasses of Entity who can be collected, should override this method!");
	}
	
	public Item getCollectingItem(){
		throw new RuntimeException("Subclasses of Entity who can be collected, should override this method!");
	}
	
	public boolean removeAfterCollect(){
		throw new RuntimeException("Subclasses of Entity who can be collected, should override this method!");
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotationX() {
		return rotationX;
	}
	
	public float getRotationY() {
		return rotationY;
	}
	
	public float getRotationZ() {
		return rotationZ;
	}

	public float getSize() {
		return size;
	}
	
	public float getDistance(Vector3f location){
		return (float) Maths.getDistance(position, location);
	}
	
	public float getDistance(Entity other){
		return getDistance(other.position);
	}
	
	protected void putPosition(ByteBuffer buffer){
		buffer.putFloat(position.x);
		buffer.putFloat(position.y);
		buffer.putFloat(position.z);
	}
	
	protected void putRotation(ByteBuffer buffer){
		buffer.putFloat(rotationX);
		buffer.putFloat(rotationY);
		buffer.putFloat(rotationZ);
	}
	
	protected void putMotion(ByteBuffer buffer){
		buffer.putFloat(motionX);
		buffer.putFloat(motionY);
		buffer.putFloat(motionZ);
	}
}
