package nl.knokko.collision;

import nl.knokko.util.Facing;

import org.lwjgl.util.vector.Vector3f;

public class ColliderCilinderUp extends Collider {
	
	float x;
	float z;
	float minY;
	float maxY;
	float radius;

	public ColliderCilinderUp(float x, float z, float minY, float maxY, float radius) {
		this.x = x;
		this.z = z;
		this.minY = minY;
		this.maxY = maxY;
		this.radius = radius;
	}

	@Override
	public float getMinX(Vector3f p) {
		return p.x + x - radius;
	}

	@Override
	public float getMinY(Vector3f p) {
		return p.y + minY;
	}

	@Override
	public float getMinZ(Vector3f p) {
		return p.z + z - radius;
	}

	@Override
	public float getMaxX(Vector3f p) {
		return p.x + x + radius;
	}

	@Override
	public float getMaxY(Vector3f p) {
		return p.y + maxY;
	}

	@Override
	public float getMaxZ(Vector3f p) {
		return p.z + z + radius;
	}

	@Override
	public float relMinX() {
		return x - radius;
	}

	@Override
	public float relMinY() {
		return minY;
	}

	@Override
	public float relMinZ() {
		return z - radius;
	}

	@Override
	public float relMaxX() {
		return x + radius;
	}

	@Override
	public float relMaxY() {
		return maxY;
	}

	@Override
	public float relMaxZ() {
		return z + radius;
	}

	@Override
	public Vector3f doesIntersect(Vector3f ow, Vector3f ot, Collider other) {
		if(other instanceof ColliderBox){
			ColliderBox box = (ColliderBox) other;
			float cx = x + ow.x;
			if(cx < box.getMinX(ot))
				cx = box.getMinX(ot);
			if(cx > box.getMaxX(ot))
				cx = box.getMaxX(ot);
			float cz = z + ow.z;
			if(cz < box.getMinZ(ot))
				cz = box.getMinZ(ot);
			if(cz > box.getMaxZ(ot))
				cz = box.getMaxZ(ot);
			double distance = Math.hypot(cx - x - ow.x, cz - z - ow.z);
			if(distance <= radius){
				float distanceMinY = getMinY(ow) - box.getMaxY(ot);
				float distanceMaxY = box.getMinY(ot) - getMaxY(ow);
				double dis = radius - distance;
				if(distanceMinY <= distanceMaxY && distanceMinY <= dis)
					return Facing.DOWN.getVector();
				if(distanceMaxY <= dis)
					return Facing.UP.getVector();
				return (Vector3f) new Vector3f(cx - x - ow.x, 0, cz - z - ow.z).normalise();
			}
			return null;
		}
		if(other instanceof ColliderSphere){
			ColliderSphere sp = (ColliderSphere) other;
			Vector3f difXZ = new Vector3f(ot.x - ow.x - x, 0, ot.z - ow.z - z);
			if(difXZ.length() > radius){
				difXZ.normalise();
				difXZ.scale(radius);
				float cx = difXZ.x + x + ow.x;
				float cz = difXZ.z + z + ow.z;
				float y = ot.y;
				if(y > getMaxY(ow))
					y = getMaxY(ow);
				if(y < getMinY(ow))
					y = getMinY(ow);
				if(sp.isInside(ot, cx, y, cz))
					return (Vector3f) difXZ.normalise();
				return null;
			}
			float difMinY = ow.y + minY - sp.radius - ot.y;
			float difMaxY = ot.y - sp.radius - ow.y - maxY;
			if(difMinY <= difMaxY && difMinY >= 0)
				return Facing.DOWN.getVector();
			if(difMaxY >= 0)
				return Facing.UP.getVector();
			return null;
		}
		if(other instanceof ColliderCilinderUp){
			ColliderCilinderUp cil = (ColliderCilinderUp) other;
			if(Math.hypot(x + ow.x - cil.x - ot.x, z + ow.z - cil.z - ot.z) <= radius + cil.radius)
				return (Vector3f) new Vector3f(cil.x + ot.x - x - ow.x, 0, cil.z + ot.z - z - ow.z).normalise();
			return null;
		}
		throw new CollissionException(ColliderCilinderUp.class, other.getClass());
	}

	@Override
	public boolean isInside(Vector3f currentPosition, float x, float y, float z) {
		if(y >= currentPosition.y + minY && y <= currentPosition.y + maxY){
			return Math.hypot(x - currentPosition.x - this.x, z - currentPosition.z - this.z) <= radius;
		}
		return false;
	}

	@Override
	public ColliderCilinderUp createScaledClone(float scale) {
		return new ColliderCilinderUp(x * scale, z * scale, minY * scale, maxY * scale, radius * scale);
	}

}
