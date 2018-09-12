package nl.knokko.collision;

import nl.knokko.util.Maths;

import org.lwjgl.util.vector.Vector3f;

public class ColliderSphere extends Collider {
	
	protected float radius;

	public ColliderSphere(float radius) {
		super();
		this.radius = radius;
	}
	
	@Override
	public String toString(){
		return "Sphere Collider:[radius " + radius + "]";
	}

	@Override
	public float getMinX(Vector3f pos) {
		return pos.x - radius;
	}

	@Override
	public float getMinY(Vector3f pos) {
		return pos.y - radius;
	}

	@Override
	public float getMinZ(Vector3f pos) {
		return pos.z - radius;
	}

	@Override
	public float getMaxX(Vector3f pos) {
		return pos.x + radius;
	}

	@Override
	public float getMaxY(Vector3f pos) {
		return pos.y + radius;
	}

	@Override
	public float getMaxZ(Vector3f pos) {
		return pos.z + radius;
	}

	@Override
	public Vector3f doesIntersect(Vector3f ownPos, Vector3f otherPos, Collider other) {
		if(other instanceof ColliderSphere){
			ColliderSphere sp = (ColliderSphere) other;
			float distance = (float) Maths.getDistance(ownPos, otherPos);
			if(distance <= radius + sp.radius){
				Vector3f difference = Vector3f.sub(otherPos, ownPos, null);
				difference.normalise();
				return difference;
			}
			return null;
		}
		if(other instanceof ColliderBox){
			ColliderBox box = (ColliderBox) other;
			float x = ownPos.getX();
			if(x > box.getMaxX(otherPos))
				x = box.getMaxX(otherPos);
			else if(x < box.getMinX(otherPos))
				x = box.getMinX(otherPos);
			float y = ownPos.getY();
			if(y > box.getMaxY(otherPos))
				y = box.getMaxY(otherPos);
			else if(y < box.getMinY(otherPos))
				y = box.getMinY(otherPos);
			float z = ownPos.getZ();
			if(z > box.getMaxZ(otherPos))
				z = box.getMaxZ(otherPos);
			if(z < box.getMinZ(otherPos))
				z = box.getMinZ(otherPos);
			if(isInside(ownPos, x, y, z)){
				Vector3f vector = Vector3f.sub(box.getCentre(otherPos), ownPos, null);
				vector.normalise();
				return vector;
			}
			return null;
		}
		if(other instanceof ColliderCilinderUp){
			ColliderCilinderUp cil = (ColliderCilinderUp) other;
			float y = ownPos.y;
			if(y < cil.minY + otherPos.y)
				y = cil.minY + otherPos.y;
			if(y > cil.maxY + otherPos.y)
				y = cil.maxY + otherPos.y;
			if(y == ownPos.y){
				if(Math.hypot(otherPos.x + cil.x - ownPos.x, otherPos.z + cil.z - ownPos.z) <= radius + cil.radius)
					return (Vector3f) new Vector3f(otherPos.x + cil.x - ownPos.x, 0, otherPos.z + cil.z - ownPos.z).normalise();
				return null;
			}
			float dif = Math.abs(ownPos.y - y);
			if(dif < radius){
				double rad = Math.sqrt(radius * radius - dif * dif);
				if(Math.hypot(otherPos.x + cil.x - ownPos.x, otherPos.z + cil.z - ownPos.z) <= rad + cil.radius)
					return (Vector3f) new Vector3f(otherPos.x + cil.x - ownPos.x, y - ownPos.y, otherPos.z + cil.z - ownPos.z).normalise();
				return null;
			}
			return null;
		}
		throw new CollissionException(ColliderSphere.class, other.getClass());
	}
	
	@Override
	public boolean isInside(Vector3f pos, float x, float y, float z){
		return Maths.getDistance(pos, new Vector3f(x, y, z)) <= radius;
	}

	@Override
	public float relMinX() {
		return -radius;
	}

	@Override
	public float relMinY() {
		return -radius;
	}

	@Override
	public float relMinZ() {
		return -radius;
	}

	@Override
	public float relMaxX() {
		return radius;
	}

	@Override
	public float relMaxY() {
		return radius;
	}

	@Override
	public float relMaxZ() {
		return radius;
	}

	@Override
	public ColliderSphere createScaledClone(float scale) {
		return new ColliderSphere(radius * scale);
	}
}
