package nl.knokko.design.world;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import nl.knokko.entities.Entity;
import nl.knokko.world.Chunk;
import nl.knokko.world.World;
import nl.knokko.world.heights.IWorldHeight;

public class ChunkBuilder extends Chunk {
	
	private ArrayList<Entity> allEntities;

	public ChunkBuilder(World world, float minX, float minZ, float maxX, float maxZ, int chunkX, int chunkZ) {
		super(world, minX, minZ, maxX, maxZ, chunkX, chunkZ);
	}
	
	@Override
	public void spawnEntity(Entity entity){
		super.spawnEntity(entity);
		if(allEntities == null)
			allEntities = new ArrayList<Entity>();
		allEntities.add(entity);
	}
	
	public void save(){
		new File("chunks").mkdir();
		try {
			FileOutputStream output = new FileOutputStream("chunks/" + chunkX + " " + chunkZ + ".chunk");
			if(allEntities != null){
				for(Entity entity : allEntities)
					if(entity.canSave())
						output.write(entity.saveMapData().array());
			}
			for(IWorldHeight height : getHeightMods())
				output.write(height.saveMapData().array());
			output.close();
		} catch (Exception e) {
			System.out.println("Failed to save chunk[" + chunkX + "][" + chunkZ + "]:");
			System.out.println();
			e.printStackTrace();
			System.out.println();
		}
	}
}
