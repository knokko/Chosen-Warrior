package nl.knokko.collision;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class ColliderList extends Collider {
	
	Collider[] colliders;
	
	float minX;
	float minY;
	float minZ;
	
	float maxX;
	float maxY;
	float maxZ;

	public ColliderList(Collider... colliders){
		if(colliders.length == 0)
			throw new IllegalArgumentException("Empty colliders array: " + colliders);
		this.colliders = colliders;
		minX = colliders[0].relMinX();
		minY = colliders[0].relMinY();
		minZ = colliders[0].relMinZ();
		maxX = colliders[0].relMaxX();
		maxY = colliders[0].relMaxY();
		maxZ = colliders[0].relMaxZ();
		for(int i = 1; i < colliders.length; i++){
			Collider c = colliders[i];
			if(c.relMinX() < minX)
				minX = c.relMinX();
			if(c.relMinY() < minY)
				minY = c.relMinY();
			if(c.relMinZ() < minZ)
				minZ = c.relMinZ();
			if(c.relMaxX() > maxX)
				maxX = c.relMaxX();
			if(c.relMaxY() > maxY)
				maxY = c.relMaxY();
			if(c.relMaxZ() > maxZ)
				maxZ = c.relMaxZ();
		}
	}
	
	public ColliderList(ArrayList<Collider> colliders){
		this(colliders.toArray(new Collider[colliders.size()]));
	}

	@Override
	public float getMinX(Vector3f p) {
		return p.x + minX;
	}

	@Override
	public float getMinY(Vector3f p) {
		return p.y + minY;
	}

	@Override
	public float getMinZ(Vector3f p) {
		return p.z + minZ;
	}

	@Override
	public float getMaxX(Vector3f p) {
		return p.x + maxX;
	}

	@Override
	public float getMaxY(Vector3f p) {
		return p.y + maxY;
	}

	@Override
	public float getMaxZ(Vector3f p) {
		return p.z + maxZ;
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
	public Collider createScaledClone(float scale) {
		Collider[] newColliders = new Collider[colliders.length];
		for(int i = 0; i < colliders.length; i++)
			newColliders[i] = colliders[i].createScaledClone(scale);
		return new ColliderList(newColliders);
	}

	@Override
	public Vector3f doesIntersect(Vector3f ownPosition, Vector3f otherPosition, Collider other) {
		if(other instanceof ColliderList){
			ColliderList list = (ColliderList) other;
			for(Collider own : colliders){
				for(Collider others : list.colliders){
					Vector3f vec = own.doesIntersect(ownPosition, otherPosition, others);
					if(vec != null)
						return vec;
				}
			}
			return null;
		}
		for(Collider col : colliders){
			Vector3f vec = col.doesIntersect(ownPosition, otherPosition, other);
			if(vec != null)
				return vec;
		}
		return null;
	}

	@Override
	public boolean isInside(Vector3f currentPosition, float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}

}
