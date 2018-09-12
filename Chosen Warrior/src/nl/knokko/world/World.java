package nl.knokko.world;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.collision.Collider;
import nl.knokko.entities.*;
import nl.knokko.main.Game;
import nl.knokko.render.Renderer;
import nl.knokko.world.heights.*;

public class World {
	
	public static final long SEED = 2309573946896L;
	public static final int UPDATE_RANGE = 80;
	
	private Random random = new Random(SEED);
	private Light light;
	
	private int minX = -12000;
	private int maxX = 12000;
	private int deltaX = maxX - minX;
	
	private int minZ = -12000;
	private int maxZ = 12000;
	private int deltaZ = maxZ - minZ;
	
	private Chunk[][] chunks = new Chunk[deltaX / Chunk.SIZE][deltaZ / Chunk.SIZE];
	
	public void update(){
		ArrayList<Chunk> list = getChunks(Game.getPlayer(), UPDATE_RANGE);
		for(Chunk chunk : list)
			chunk.update();
	}
	
	public void render(Renderer renderer){
		ArrayList<Chunk> list = getChunks(Game.getPlayer(), UPDATE_RANGE);
		for(Chunk chunk : list)
			chunk.render(renderer);
	}
	
	public void addHeightMod(IWorldHeight heightMod){
		heightMod.spawn(this);
	}
	
	public void spawnEntity(Entity entity){
		Chunk chunk = getChunk(entity.getPosition().x, entity.getPosition().z);
		chunk.spawnEntity(entity);
	}
	
	public void removeEntity(Entity entity){
		Chunk chunk = getChunk(entity.getPosition().x, entity.getPosition().z);
		chunk.removeEntity(entity);
	}
	
	public Light getLight(){
		if(doDisco()){
			if(light == null || random.nextInt(5) == 0)
				light = new Light(new Vector3f(0, 2000000000, 0), new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()));
			return light;
		}
		if(light == null)
			light = new Light(new Vector3f(0, 2000000000, 0), new Vector3f(1, 1, 1));
		return light;
	}
	
	public Vector3f getGravity(Vector3f location){
		return new Vector3f(0, -9.8f, 0);
	}
	
	public ArrayList<Entity> getPossibleColliders(Entity entity, Collider collider){
		ArrayList<Entity> colliders = new ArrayList<Entity>();
		Vector3f position = entity.getPosition();
		ArrayList<Chunk> chunkList = getChunks(collider.getMinX(position), collider.getMinZ(position), collider.getMaxX(position), collider.getMaxZ(position));
		for(Chunk chunk : chunkList){
			for(Entity colEntity : chunk.getCollidingEntities()){
				if(entity != colEntity){
					Collider col = colEntity.getCollider();
					if(col.couldIntersect(colEntity.getPosition(), position, collider))
						colliders.add(colEntity);
				}
			}
		}
		//TODO seems to create collider at wrong position
		return colliders;
	}
	
	public ArrayList<Entity> getPossibleColliders(Entity lookingEntity, float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
		ArrayList<Entity> colliders = new ArrayList<Entity>();
		ArrayList<Chunk> relevantChunks = getChunks(minX, minZ, maxX, maxZ);
		for(Chunk chunk : relevantChunks){
			for(Entity entity : chunk.getCollidingEntities()){
				Collider c = entity.getCollider();
				Vector3f p = entity.getPosition();
				if(entity != lookingEntity && c.getMinX(p) <= maxX && minX <= c.getMaxX(p) && c.getMinY(p) <= maxY && minY <= c.getMaxY(p) && c.getMinZ(p) <= maxZ && minZ <= c.getMaxZ(p))
					colliders.add(entity);
			}
		}
		return colliders;
	}
	
	public Entity getNearestCollectible(Entity centre){
		float maxRange = 10;
		ArrayList<Chunk> list = getChunks(centre, maxRange);
		Entity nearest = null;
		float nearestDistance = 0;
		for(Chunk chunk : list){
			for(Entity collectible : chunk.getCollectibleEntities()){
				float distance = centre.getDistance(collectible);
				if(distance <= collectible.getCollectingRange()){
					if(nearest == null || distance < nearestDistance){
						nearest = collectible;
						nearestDistance = distance;
					}
				}
			}
		}
		return nearest;
	}

	public float getGroundHeight(float x, float z) {
		return getChunk(x, z).getGroundHeight(x, z);
	}
	
	public Vector3f getNormal(float x, float z){
		float delta = 0.1f;
		float minXY = getGroundHeight(x - delta / 2, z);
		float maxXY = getGroundHeight(x + delta / 2, z);
		float minZY = getGroundHeight(x, z - delta / 2);
		float maxZY = getGroundHeight(x, z + delta / 2);
		float deltaXY = maxXY - minXY;
		float deltaZY = maxZY - minZY;
		Vector3f vectorX = (Vector3f) new Vector3f(delta, deltaXY, 0f).normalise();
		Vector3f vectorZ = (Vector3f) new Vector3f(0f, deltaZY, delta).normalise();
		return Vector3f.cross(vectorZ, vectorX, null);
	}
	
	public boolean doDisco(){
		return false;
	}
	
	protected Chunk createChunk(float minX, float minZ, float maxX, float maxZ, int cx, int cz){
		return new Chunk(this, minX, minZ, maxX, maxZ, cx, cz);
	}
	
	public Chunk getChunk(float x, float z){
		x -= minX;
		z -= minZ;
		int cx = (int) (x / Chunk.SIZE);
		int cz = (int) (z / Chunk.SIZE);
		Chunk chunk = chunks[cx][cz];
		if(chunk == null){
			float minX = cx * Chunk.SIZE + this.minX;
			float minZ = cz * Chunk.SIZE + this.minZ;
			chunk = createChunk(minX, minZ, minX + Chunk.SIZE, minZ + Chunk.SIZE, cx, cz);
			chunks[cx][cz] = chunk;
		}
		return chunk;
	}
	
	public ArrayList<Chunk> getChunks(Entity centre, float range){
		return getChunks(centre.getPosition().x, centre.getPosition().z, range);
	}
	
	public ArrayList<Chunk> getChunks(Vector3f position, float range){
		return getChunks(position.x, position.z, range);
	}
	
	public ArrayList<Chunk> getChunks(float centreX, float centreZ, float range){
		return getChunks(centreX - range, centreZ - range, centreX + range, centreZ + range);
	}
	
	public ArrayList<Chunk> getChunks(float minX, float minZ, float maxX, float maxZ){
		ArrayList<Chunk> list = new ArrayList<Chunk>();
		for(float x = minX; x < maxX; x += Chunk.SIZE){
			for(float z = minZ; z < maxZ; z += Chunk.SIZE){
				list.add(getChunk(x, z));
			}
			Chunk chunk = getChunk(x, maxZ);
			if(!list.contains(chunk))
				list.add(chunk);
		}
		for(float z = minZ; z < maxZ; z += Chunk.SIZE){
			Chunk chunk = getChunk(maxX, z);
			if(!list.contains(chunk))
				list.add(chunk);
		}
		Chunk chunk = getChunk(maxX, maxZ);
		if(!list.contains(chunk))
			list.add(chunk);
		return list;
	}
}
