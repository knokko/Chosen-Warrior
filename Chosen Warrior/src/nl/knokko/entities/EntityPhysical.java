package nl.knokko.entities;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.collision.Collider;
import nl.knokko.render.DisplayManager;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Maths;
import nl.knokko.util.Resources;
import nl.knokko.world.World;

public class EntityPhysical extends Entity {
	
	public static final float MIN_MOTION = 0.001f;
	public static final byte ID = -126;
	
	public Vector3f currentNormal;
	
	private TexturedModel model;
	private Collider collider;
	protected World world;
	
	protected boolean gravity;
	private boolean destroyed;

	public EntityPhysical(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float size) {
		this.model = model;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.size = size;
		gravity = true;
	}
	
	public EntityPhysical(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float size, boolean gravity){
		this(model, position, rotationX, rotationY, rotationZ, size);
		this.gravity = gravity;
	}
	
	public World getWorld(){
		return world;
	}
	
	public EntityPhysical setNoGravity(){
		gravity = false;
		return this;
	}
	
	public void onEntityHit(Entity hitEntity, Vector3f hitLocation){}
	
	public void onGroundHit(Vector3f hitLocation){}
	
	public boolean collideNow(){
		return true;
	}
	
	public void setWorld(World world){
		this.world = world;
	}
	
	public void update(){
		if(applyGravity() && world != null)
			addForce(0, -9.8f * getMass(), 0);
		if(motionX != motionX || motionY != motionY || motionZ != motionZ)
			throw new RuntimeException("Entity " + this + " has invalid motion:[" + motionX + "][" + motionY + "][" + motionZ + "]");
		if(Vector3f.dot(position, new Vector3f()) > 10000)
			world.removeEntity(this);
		if(motionX != 0 || motionY != 0 || motionZ != 0){
			move(motionX, motionY, motionZ);
			if(motionX != 0 || motionY != 0 || motionZ != 0){
				float factor = getGroundFrictionFactor();
				if(currentNormal == null)
					factor = getAirFrictionFactor();
				motionX *= factor;
				motionY *= factor;
				motionZ *= factor;
			}
		}
		if(currentNormal != null)
			addForce(currentNormal);
		if(motionX > 0 && motionX < MIN_MOTION || motionX < 0 && motionX > -MIN_MOTION)
			motionX = 0;
		if(motionY > 0 && motionY < MIN_MOTION || motionY < 0 && motionY > -MIN_MOTION)
			motionY = 0;
		if(motionZ > 0 && motionZ < MIN_MOTION || motionZ < 0 && motionZ > -MIN_MOTION)
			motionZ = 0;
	}
	
	public boolean move(float dx, float dy, float dz){
		currentNormal = null;
		if(world == null || !collideNow()){
			position.x += dx;
			position.y += dy;
			position.z += dz;
			return true;
		}
		boolean intersection = false;
		Collider start = getCollider();
		Vector3f endPos = new Vector3f(position.x + dx, position.y + dy, position.z + dz);
		ArrayList<Entity> colliders = world.getPossibleColliders(this, Math.min(start.getMinX(position), start.getMinX(endPos)), Math.min(start.getMinY(position), start.getMinY(endPos)), Math.min(start.getMinZ(position), start.getMinZ(endPos)), Math.max(start.getMaxX(position), start.getMaxX(endPos)), Math.max(start.getMaxY(position), start.getMaxY(endPos)), Math.max(start.getMaxZ(position), start.getMaxZ(endPos)));
		if(colliders.isEmpty() && start.getMinY(endPos) > world.getGroundHeight(position.x + dx, position.z + dz)){
			position.x += dx;
			position.y += dy;
			position.z += dz;
			position = new Vector3f(position);
			return true;
		}
		float stepSize = 0.01f;
		float progress = 0;
		float distance = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
		float rcx = dx / distance;
		float rcy = dy / distance;
		float rcz = dz / distance;
		while(progress < distance){
			float left = distance - progress;
			if(left < stepSize)
				stepSize = left;
			float nx = position.x + rcx * stepSize;
			float ny = position.y + rcy * stepSize;
			float nz = position.z + rcz * stepSize;
			Vector3f nextPos = new Vector3f(nx, ny, nz);
			boolean flag = true;
			for(Entity collidingEntity : colliders){
				if(collider.couldIntersect(nextPos, collidingEntity.getPosition(), collidingEntity.getCollider())){
					Vector3f intersect = collider.doesIntersect(nextPos, collidingEntity.getPosition(), collidingEntity.getCollider());
					if(intersect != null){
						onEntityHit(collidingEntity, new Vector3f(nx, ny, nz));
						if(destroyed)
							return false;
						Vector3f nextPosX = new Vector3f(nx, position.y, position.z);
						Vector3f nextPosY = new Vector3f(position.x, ny, position.z);
						Vector3f nextPosZ = new Vector3f(position.x, position.y, nz);
						if(collider.doesIntersect(nextPosX, collidingEntity.getPosition(), collidingEntity.getCollider()) != null){
							dx = 0;
							motionX = 0;
						}
						if(this.collider.doesIntersect(nextPosY, collidingEntity.getPosition(), collidingEntity.getCollider()) != null){
							dy = 0;
							motionY = 0;
							currentNormal = world.getNormal(nx, nz);
							currentNormal.scale(9.8f);
							currentNormal.scale(getMass());
						}
						if(this.collider.doesIntersect(nextPosZ, collidingEntity.getPosition(), collidingEntity.getCollider()) != null){
							dz = 0;
							motionZ = 0;
						}
						distance = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
						rcx = dx / distance;
						rcy = dy / distance;
						rcz = dz / distance;
						if(distance == 0)
							return false;
						flag = false;
						intersection = true;
					}
				}
			}
			if((nx == 0 && ny == 0 && nz == 0) || (rcx == 0 && rcy == 0 && rcz == 0))
				return false;
			if(flag){
				position.x = nx;
				position.y = ny;
				position.z = nz;
				//TODO check the entire bottom of your character for ground intersection
				float minY = world.getGroundHeight(nx, nz);
				float deltaY = minY - collider.getMinY(nextPos);
				if(deltaY >= 0){
					onGroundHit(position);
					if(destroyed)
						return false;
					currentNormal = world.getNormal(nx, nz);
					currentNormal.scale(9.8f);
					currentNormal.scale(getMass());
					position.y += deltaY;
					dy = 0;
					motionY = 0;
					intersection = true;
					if(distance == 0)
						return false;
				}
			}
			flag = true;
			progress += stepSize;
		}
		position = new Vector3f(position);
		return !intersection;
	}
	
	public void increaseRotation(float rx, float ry, float rz){
		rotationX += rx;
		rotationY += ry;
		rotationZ += rz;
		if(rotationY > 360)
			rotationY -= 360;
		if(rotationY < 0)
			rotationY += 360;
	}
	
	public void setRotation(float rx, float ry, float rz){
		rotationX = rx;
		rotationY = ry;
		rotationZ = rz;
	}
	
	public void addForce(float forceX, float forceY, float forceZ){
		motionX += forceX / getMass() / DisplayManager.FPS_CAP / DisplayManager.FPS_CAP;
		motionY += forceY / getMass() / DisplayManager.FPS_CAP / DisplayManager.FPS_CAP;
		motionZ += forceZ / getMass() / DisplayManager.FPS_CAP / DisplayManager.FPS_CAP;
		if(forceX != forceX || forceY != forceY || forceZ != forceZ)
			throw new IllegalArgumentException("NaN values are used for addForce(" + forceX + "," + forceY + "," + forceZ + ");");
	}
	
	public EntityPhysical addForce(float rotationX, float rotationY, float rotationZ, float force){
		Vector3f vec = Maths.getRotationVector(rotationX, rotationY, rotationZ);
		addForce(vec, force);
		return this;
	}
	
	public void addForce(Vector3f direction, float force){
		Vector3f vec = direction.normalise(null);
		addForce(vec.x * force, vec.y * force, vec.z * force);
	}
	
	public void addForce(Vector3f force){
		addForce(force.x, force.y, force.z);
	}
	
	public float getMass(){
		return 100;
	}
	
	@Override
	public Collider getCollider(){
		if(collider == null)
			collider = createCollider();
		return collider;
	}
	
	public Collider createCollider(){
		if(model.getModel().hasCollider())
			return model.getModel().getCollider();
		throw new RuntimeException("Unknown collider for EntityPhysical " + this);
	}

	public TexturedModel getModel() {
		return model;
	}
	
	public float getHeadPitch(){
		return getRotationX();
	}
	
	public boolean canMove(){
		return false;
	}
	
	public boolean applyGravity(){
		return gravity;
	}
	
	public boolean shouldRenderNow(){
		return true;
	}
	
	@Override
	public boolean shouldRender(){
		return true;
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}

	@Override
	public boolean shouldCollide() {
		return true;
	}

	@Override
	public boolean shouldBePermanent() {
		return false;
	}
	
	public float getGroundFrictionFactor(){
		return 0.96f;
	}
	
	public float getAirFrictionFactor(){
		return 0.99f;
	}
	
	public boolean isDestroyed(){
		return destroyed;
	}
	
	@Override
	public void destroy(){
		destroyed = true;
		world.removeEntity(this);
	}

	@Override
	public boolean shouldBeCollectible() {
		return false;
	}

	@Override
	public ByteBuffer saveMapData() {
		byte[] modelCreation = model.getModel().getCreationID();
		byte[] textureCreation = model.getTexture().getCreationID();
		ByteBuffer buffer = ByteBuffer.allocate(32 + modelCreation.length + textureCreation.length);
		buffer.put(ID);
		putPosition(buffer);
		putRotation(buffer);
		buffer.putFloat(size);
		buffer.put((byte) (gravity ? 1 : 0));
		buffer.put((byte) modelCreation.length);
		buffer.put(modelCreation);
		buffer.put((byte) textureCreation.length);
		buffer.put(textureCreation);
		return buffer;
	}
	
	public static EntityPhysical loadMapData(ByteBuffer buffer){
		Vector3f position = new Vector3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
		float rotX = buffer.getFloat();
		float rotY = buffer.getFloat();
		float rotZ = buffer.getFloat();
		float size = buffer.getFloat();
		boolean gravity = buffer.get() == 1;
		byte[] modelCreation = new byte[buffer.get()];
		buffer.get(modelCreation);
		byte[] textureCreation = new byte[buffer.get()];
		buffer.get(textureCreation);
		return new EntityPhysical(new TexturedModel(Resources.getModelFromID(modelCreation), Resources.getTextureFromID(textureCreation)), position, rotX, rotY, rotZ, size, gravity);
	}
}
