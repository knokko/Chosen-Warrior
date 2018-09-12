package nl.knokko.entities.tiles;

import java.nio.ByteBuffer;

import nl.knokko.collision.Collider;
import nl.knokko.collision.ColliderCilinderUp;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;
import nl.knokko.world.Chunk;
import nl.knokko.world.forest.plants.TreeSettings;
import nl.knokko.world.forest.plants.Trees;

import org.lwjgl.util.vector.Vector3f;

public class TileTree extends EntityTile {
	
	public static final byte ID = -121;
	
	private final TreeSettings settings;

	public TileTree(TreeSettings settings, ModelTexture texture, float x, float z, float rotationY, float size) {
		super(new TexturedModel(Resources.getTreeModel(settings), texture), new Vector3f(x, -1, z), 0, rotationY, 0, size);
		this.settings = settings;
	}
	
	@Override
	public ByteBuffer saveMapData(){
		ByteBuffer buffer = ByteBuffer.allocate(19);
		buffer.put(ID);
		buffer.put(settings.id);
		buffer.put(getModel().getTexture().getCreationID()[1]);
		buffer.putFloat(position.x);
		buffer.putFloat(position.z);
		buffer.putFloat(rotationY);
		buffer.putFloat(size);
		return buffer;
	}
	
	@Override
	public void place(Chunk chunk){
		position.y = chunk.getGroundHeight(position.x, position.z);
	}
	
	@Override
	public Collider createCollider(){
		System.out.println("TileTree#createCollider");
		return new ColliderCilinderUp(0, 0, 0, settings.baseLength * size, settings.baseWidth * size / 2);
	}
	
	public static TileTree loadMapData(ByteBuffer buffer){
		byte treeID = buffer.get();
		byte textureID = buffer.get();
		float x = buffer.getFloat();
		float z = buffer.getFloat();
		float yaw = buffer.getFloat();
		float size = buffer.getFloat();
		return new TileTree(Trees.settingsFromID(treeID), Trees.textureFromID(textureID), x, z, yaw, size);
	}
}
