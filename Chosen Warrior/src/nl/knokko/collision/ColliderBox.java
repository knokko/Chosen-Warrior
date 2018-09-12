package nl.knokko.collision;

import nl.knokko.util.Facing;
import nl.knokko.util.Maths;

import org.lwjgl.util.vector.Vector3f;

public class ColliderBox extends Collider {
	
	protected float minX;
	protected float minY;
	protected float minZ;
	
	protected float maxX;
	protected float maxY;
	protected float maxZ;

	public ColliderBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	@Override
	public String toString(){
		return "Box Collider:[" + minX + "," + minY + "," + minZ + " - " + maxX + "," + maxY + "," + maxZ + "]";
	}

	@Override
	public float getMinX(Vector3f pos) {
		return pos.getX() + minX;
	}

	@Override
	public float getMinY(Vector3f pos) {
		return pos.getY() + minY;
	}

	@Override
	public float getMinZ(Vector3f pos) {
		return pos.getZ() + minZ;
	}

	@Override
	public float getMaxX(Vector3f pos) {
		return pos.getX() + maxX;
	}

	@Override
	public float getMaxY(Vector3f pos) {
		return pos.getY() + maxY;
	}

	@Override
	public float getMaxZ(Vector3f pos) {
		return pos.getZ() + maxZ;
	}

	@Override
	public Vector3f doesIntersect(Vector3f ownPos, Vector3f otherPos, Collider other) {
		if(other instanceof ColliderBox){
			ColliderBox box = (ColliderBox) other;
			if(getMinX(ownPos) <= box.getMaxX(otherPos) && box.getMinX(otherPos) <= getMaxX(ownPos) && getMinY(ownPos) <= box.getMaxY(otherPos) && box.getMinY(otherPos) <= getMaxY(ownPos) && getMinZ(ownPos) <= box.getMaxZ(otherPos) && box.getMinZ(otherPos) <= getMaxZ(ownPos))
				return getVector(ownPos, box.getMinX(otherPos), box.getMinY(otherPos), box.getMinZ(otherPos), box.getMaxX(otherPos), box.getMaxY(otherPos), box.getMaxZ(otherPos));
			return null;
		}
		if(other instanceof ColliderSphere){
			ColliderSphere sp = (ColliderSphere) other;
			float x = otherPos.getX();
			if(x > getMaxX(ownPos))
				x = getMaxX(ownPos);
			else if(x < getMinX(ownPos))
				x = getMinX(ownPos);
			float y = otherPos.getY();
			if(y > getMaxY(ownPos))
				y = getMaxY(ownPos);
			else if(y < getMinY(ownPos))
				y = getMinY(ownPos);
			float z = otherPos.getZ();
			if(z > getMaxZ(ownPos))
				z = getMaxZ(ownPos);
			if(z < getMinZ(ownPos))
				z = getMinZ(ownPos);
			if(sp.isInside(otherPos, x, y, z))
				return getVector(ownPos, x, y, z, x, y, z);
			return null;
		}
		if(other instanceof ColliderCilinderUp){
			ColliderCilinderUp cil = (ColliderCilinderUp) other;
			float x = cil.x + otherPos.x;
			if(x > getMaxX(ownPos))
				x = getMaxX(ownPos);
			if(x < getMinX(ownPos))
				x = getMinX(ownPos);
			float z = cil.z + otherPos.z;
			if(z > getMaxZ(ownPos))
				z = getMaxZ(ownPos);
			if(z < getMinZ(ownPos))
				z = getMinZ(ownPos);
			if(Math.hypot(x - cil.x - otherPos.x, z - cil.z - otherPos.z) <= cil.radius){
				System.out.println("ColliderBox#doesIntersect() x = " + x + " and z = " + z + " and cil.x = " + cil.x + " and cil.z = " + cil.z + " and radius = " + cil.radius);
				return getVector(ownPos, x, cil.getMinY(otherPos), z, x, cil.getMaxY(otherPos), z);
			}
			return null;
		}
		if(other instanceof ColliderList)
			return other.doesIntersect(otherPos, ownPos, this);
		throw new CollissionException(ColliderBox.class, other.getClass());
	}
	
	@Override
	public boolean isInside(Vector3f pos, float x, float y, float z){
		return x >= getMinX(pos) && x <= getMaxX(pos) && y >= getMinY(pos) && y <= getMaxY(pos) && z >= getMinZ(pos) && z <= getMaxZ(pos);
	}
	
	protected Vector3f getVector(Vector3f pos, float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
		float deltaAX = Math.abs(getMaxX(pos) - minX);
		float deltaAY = Math.abs(getMaxY(pos) - minY);
		float deltaAZ = Math.abs(getMaxZ(pos) - minZ);
		float deltaIX = Math.abs(getMinX(pos) - maxX);
		float deltaIY = Math.abs(getMinY(pos) - maxY);
		float deltaIZ = Math.abs(getMinZ(pos) - maxZ);
		float min = Maths.min(deltaAX, deltaAY, deltaAZ, deltaIX, deltaIY, deltaIZ);
		if(min == deltaAX)
			return Facing.EAST.getVector();
		if(min == deltaAY)
			return Facing.UP.getVector();
		if(min == deltaAZ)
			return Facing.SOUTH.getVector();
		if(min == deltaIX)
			return Facing.WEST.getVector();
		if(min == deltaIY)
			return Facing.DOWN.getVector();
		return Facing.NORTH.getVector();
	}
	
	protected Vector3f getCentre(Vector3f pos){
		return new Vector3f(pos.getX() + (minX + maxX) / 2, pos.getY() + (minY + maxY) / 2, pos.getZ() + (minZ + maxZ) / 2);
	}

	@Override
	public float relMinX() {
		return minX;
	}

	@Override
	public float relMinY() {
		return minY;
	}

	@Override
	public float relMinZ() {
		return minZ;
	}

	@Override
	public float relMaxX() {
		return maxX;
	}

	@Override
	public float relMaxY() {
		return maxY;
	}

	@Override
	public float relMaxZ() {
		return maxZ;
	}

	@Override
	public ColliderBox createScaledClone(float scale) {
		return new ColliderBox(minX * scale, minY * scale, minZ * scale, maxX * scale, maxY * scale, maxZ * scale);
	}
}
