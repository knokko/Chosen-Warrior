package nl.knokko.world.heights;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import nl.knokko.world.Chunk;
import nl.knokko.world.World;

public class Hill implements IWorldHeight {
	
	public static final byte ID = -122;
	
	protected float midX;
	protected float midZ;
	
	protected float height;
	
	protected float factor;
	protected float power;
	
	protected float maxDistance;

	public Hill(float midX, float midZ, float height, float factor, float power) {
		this.midX = midX;
		this.midZ = midZ;
		this.height = height;
		this.factor = factor;
		this.power = power;
		//factor * distance ^ power = height
		//distance ^ power = height / factor
		//distance = (height / factor) ^ -power
		maxDistance = (float) Math.pow(height / factor, 1 / power);
	}

	@Override
	public float extraHeight(float x, float z) {
		double distance = Math.hypot(midX - x, midZ - z);
		if(height >= 0 )
			return Math.max(0, (float) (height - factor * Math.pow(distance, power)));
		else
			return Math.min(0, (float) (-height + factor * Math.pow(distance, power)));
	}

	@Override
	public boolean isThere(float x, float z) {
		return Math.hypot(midX - x, midZ - z) <= maxDistance;
	}

	@Override
	public ByteBuffer saveMapData() {
		ByteBuffer buffer = ByteBuffer.allocate(21);
		buffer.put(ID);
		buffer.putFloat(midX);
		buffer.putFloat(midZ);
		buffer.putFloat(height);
		buffer.putFloat(factor);
		buffer.putFloat(power);
		return buffer;
	}
	
	public static Hill loadMapData(ByteBuffer buffer){
		return new Hill(buffer.getFloat(), buffer.getFloat(), buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
	}

	@Override
	public void spawn(World world) {
		ArrayList<Chunk> chunks = world.getChunks(getMinX(), getMinZ(), getMaxX(), getMaxZ());
		for(Chunk chunk : chunks){
			chunk.addHeightMod(this);
			chunk.refreshGround();
		}
	}

	@Override
	public float getMinX() {
		return midX - maxDistance;
	}

	@Override
	public float getMinZ() {
		return midZ - maxDistance;
	}

	@Override
	public float getMaxX() {
		return midX + maxDistance;
	}

	@Override
	public float getMaxZ() {
		return midZ + maxDistance;
	}
}
