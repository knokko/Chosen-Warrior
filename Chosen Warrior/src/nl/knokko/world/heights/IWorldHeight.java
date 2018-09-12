package nl.knokko.world.heights;

import java.nio.ByteBuffer;

import nl.knokko.world.World;

public interface IWorldHeight {
	
	ByteBuffer saveMapData();
	
	float extraHeight(float x, float z);
	
	float getMinX();
	float getMinZ();
	float getMaxX();
	float getMaxZ();
	
	boolean isThere(float x, float z);
	
	void spawn(World world);
}
