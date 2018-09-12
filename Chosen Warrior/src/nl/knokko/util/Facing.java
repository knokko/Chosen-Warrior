package nl.knokko.util;

import org.lwjgl.util.vector.Vector3f;

public enum Facing {
	
	UP(0, 1, 0, -90, 0),
	DOWN(0, -1, 0, 90, 0),
	NORTH(0, 0, -1, 0, 0),
	EAST(1, 0, 0, 0, 90),
	SOUTH(0, 0, 1, 0, 180),
	WEST(-1, 0, 0, 0, 270);
	
	public final float rotationX;
	public final float rotationY;
	
	public final float normalX;
	public final float normalY;
	public final float normalZ;
	
	private Facing(float x, float y, float z, float pitch, float yaw){
		normalX = x;
		normalY = y;
		normalZ = z;
		rotationX = pitch;
		rotationY = yaw;
	}
	
	public Vector3f getVector(){
		return new Vector3f(normalX, normalY, normalZ);
	}
}
