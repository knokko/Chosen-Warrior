package nl.knokko.design.world;

import java.util.ArrayList;

import nl.knokko.design.main.MapDesigner;
import nl.knokko.render.Renderer;
import nl.knokko.world.Chunk;
import nl.knokko.world.World;

public class WorldBuilder extends World {
	
	private ArrayList<ChunkBuilder> loadedChunks = new ArrayList<ChunkBuilder>();
	
	@Override
	public void update(){
		ArrayList<Chunk> list = getChunks(MapDesigner.getCamera().getPosition(), UPDATE_RANGE);
		for(Chunk chunk : list)
			chunk.update();
	}
	
	@Override
	public void render(Renderer renderer){
		ArrayList<Chunk> list = getChunks(MapDesigner.getCamera().getPosition(), UPDATE_RANGE);
		for(Chunk chunk : list)
			chunk.render(renderer);
	}
	
	@Override
	protected Chunk createChunk(float minX, float minZ, float maxX, float maxZ, int cx, int cz){
		ChunkBuilder chunk = new ChunkBuilder(this, minX, minZ, maxX, maxZ, cx, cz);
		loadedChunks.add(chunk);
		return chunk;
	}
	
	public void save(){
		for(ChunkBuilder chunk : loadedChunks)
			chunk.save();
	}
}
