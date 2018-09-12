package nl.knokko.collision;

import org.lwjgl.util.vector.Vector3f;

public abstract class Collider {
	
	public abstract float getMinX(Vector3f currentPosition);
	
	public abstract float getMinY(Vector3f currentPosition);
	
	public abstract float getMinZ(Vector3f currentPosition);
	
	public abstract float getMaxX(Vector3f currentPosition);
	
	public abstract float getMaxY(Vector3f currentPosition);
	
	public abstract float getMaxZ(Vector3f currentPosition);
	
	public abstract float relMinX();
	
	public abstract float relMinY();
	
	public abstract float relMinZ();
	
	public abstract float relMaxX();
	
	public abstract float relMaxY();
	
	public abstract float relMaxZ();
	
	public abstract Collider createScaledClone(float scale);
	
	/**
	 * Checks for intersection with another collider.
	 * @param other The collider to check for intersection with.
	 * @return The normal vector where the other collider hit this collider, or null if no intersection occured.
	 */
	public abstract Vector3f doesIntersect(Vector3f ownPosition, Vector3f otherPosition, Collider other);
	
	/**
	 * @param currentPosition The current position of the owner of this Collider.
	 * @param x
	 * @param y
	 * @param z
	 * @return True if the location (x,y,z) is inside this collider.
	 */
	public abstract boolean isInside(Vector3f currentPosition, float x, float y, float z);
	
	public boolean couldIntersect(Vector3f currentPosition, Vector3f otherPosition, Collider other){
		return other.getMinX(otherPosition) <= getMaxX(currentPosition) && getMinX(currentPosition) <= other.getMaxX(otherPosition) && other.getMinY(otherPosition) <= getMaxY(currentPosition) && getMinY(currentPosition) <= other.getMaxY(otherPosition) && other.getMinZ(otherPosition) <= getMaxZ(currentPosition) && getMinZ(currentPosition) <= other.getMaxZ(otherPosition);
	}
	
	public String toString(Vector3f position){
		return getClass().getSimpleName() + ": bounds are [" + getMinX(position) + "," + getMinY(position) + "," + getMinZ(position) + "," + getMaxX(position) + "," + getMaxY(position) + "," + getMaxZ(position) + "]";
	}
}
