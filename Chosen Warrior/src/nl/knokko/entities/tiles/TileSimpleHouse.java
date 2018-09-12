package nl.knokko.entities.tiles;

import java.nio.ByteBuffer;

import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Resources;
import nl.knokko.world.Chunk;
import nl.knokko.world.structures.SimpleHouseTexture;
import nl.knokko.world.structures.SimpleHouses;

import org.lwjgl.util.vector.Vector3f;

public class TileSimpleHouse extends EntityTile {
	
	public static final byte ID = -120;
	
	private final SimpleHouseTexture texture;

	public TileSimpleHouse(SimpleHouseTexture texture, float x, float z) {
		super(new TexturedModel(Resources.getSimpleHouseModel(texture.settings), Resources.getSimpleHouseTexture(texture)), new Vector3f(x, -1, z), 0, 0, 0, 1);
		this.texture = texture;
	}
	
	@Override
	public void place(Chunk chunk){
		position.y = chunk.getGroundHeight(position.x, position.z) + 0.1f;
	}
	
	@Override
	public ByteBuffer saveMapData(){
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.put(ID);
		buffer.put(texture.id);
		buffer.putFloat(position.x);
		buffer.putFloat(position.z);
		return buffer;
	}
	
	public static TileSimpleHouse loadMapData(ByteBuffer buffer){
		byte id = buffer.get();
		float x = buffer.getFloat();
		float z = buffer.getFloat();
		return new TileSimpleHouse(SimpleHouses.fromID(id), x, z);
	}
}
