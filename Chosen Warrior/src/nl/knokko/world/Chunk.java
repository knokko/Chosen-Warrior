package nl.knokko.world;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.Entity;
import nl.knokko.entities.EntityCollectible;
import nl.knokko.entities.EntityDecorative;
import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.living.EntityDwoorf;
import nl.knokko.entities.living.EntityScatch;
import nl.knokko.entities.tiles.EntityTile;
import nl.knokko.entities.tiles.TileSimpleHouse;
import nl.knokko.entities.tiles.TileTree;
import nl.knokko.render.MasterRenderer;
import nl.knokko.render.Renderer;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.util.Resources;
import nl.knokko.world.heights.Hill;
import nl.knokko.world.heights.IWorldHeight;

public class Chunk {
	
	public static final int SIZE = 50;
	
	private static final HashMap<Byte, Class<?>> ID_MAP = new HashMap<Byte, Class<?>>();
	
	static {
		ID_MAP.put(EntityDecorative.ID, EntityDecorative.class);
		ID_MAP.put(EntityCollectible.ID, EntityCollectible.class);
		ID_MAP.put(EntityPhysical.ID, EntityPhysical.class);
		ID_MAP.put(EntityDwoorf.ID, EntityDwoorf.class);
		ID_MAP.put(EntityTile.ID, EntityTile.class);
		ID_MAP.put(EntityScatch.ID, EntityScatch.class);
		ID_MAP.put(Hill.ID, Hill.class);
		ID_MAP.put(TileTree.ID, TileTree.class);
		ID_MAP.put(TileSimpleHouse.ID, TileSimpleHouse.class);
	}
	
	private ArrayList<Entity> updatingEntities = new ArrayList<Entity>();
	private ArrayList<Entity> collidingEntities = new ArrayList<Entity>();
	private ArrayList<Entity> collectibleEntities = new ArrayList<Entity>();
	
	private ArrayList<PlaceTask> entitiesToPlace = new ArrayList<PlaceTask>();
	
	private ArrayList<IWorldHeight> heightMods;
	
	private World world;
	private MasterRenderer renderer;
	private EntityDecorative ground;
	
	private float minX;
	private float minZ;
	private float deltaX;
	private float deltaZ;
	
	protected int chunkX;
	protected int chunkZ;
	
	private boolean isBuild;

	public Chunk(World world, float minX, float minZ, float maxX, float maxZ, int chunkX, int chunkZ) {
		this.world = world;
		this.renderer = new MasterRenderer();
		this.minX = minX;
		this.minZ = minZ;
		this.deltaX = maxX - minX;
		this.deltaZ = maxZ - minZ;
		this.heightMods = new ArrayList<IWorldHeight>();
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		placeHills();
		load();
	}
	
	protected void placeHills(){}
	
	protected void buildUp(){
		placeGround();
		for(PlaceTask task : entitiesToPlace){
			try {
				spawnWorldObject(task.entityClass.getMethod("loadMapData", World.class, ByteBuffer.class).invoke(null, world, task.dataBuffer));
			} catch(Exception ex){
				System.out.println("Failed to place an entity in chunk[" + chunkX + "][" + chunkZ + "]:");
				System.out.println();
				ex.printStackTrace();
				System.out.println();
			}
		}
		entitiesToPlace.clear();
	}
	
	protected void placeGround(){
		ground = new EntityDecorative(new TexturedModel(Resources.createGroundModel(world, minX, minZ, minX + deltaX, minZ + deltaZ, 0.5f), Resources.getGroundTexture()), new Vector3f(0, 0, 0), 0, 0, 0, 1, false);
		spawnEntity(ground);
	}
	
	public void refreshGround(){
		if(ground != null)
			removeEntity(ground);
		ground = new EntityDecorative(new TexturedModel(Resources.createGroundModel(world, minX, minZ, minX + deltaX, minZ + deltaZ, 2f), Resources.getGroundTexture()), new Vector3f(0, 0, 0), 0, 0, 0, 1, false);
		spawnEntity(ground);
	}
	
	public void spawnWorldObject(Object object){
		if(object instanceof Entity)
			spawnEntity((Entity) object);
		if(object instanceof IWorldHeight)
			addHeightMod((IWorldHeight)object);
	}
	
	public void spawnEntity(Entity entity){
		entity.setWorld(world);
		entity.place(this);
		if(entity.shouldUpdate())
			updatingEntities.add(entity);
		if(entity.shouldRender())
			renderer.processEntity(entity);
		if(entity.shouldCollide())
			collidingEntities.add(entity);
		if(entity.shouldBeCollectible())
			collectibleEntities.add(entity);
	}
	
	public void removeEntity(Entity entity){
		if(entity.shouldUpdate())
			updatingEntities.remove(entity);
		if(entity.shouldRender())
			renderer.removeEntity(entity);
		if(entity.shouldCollide())
			collidingEntities.remove(entity);
		if(entity.shouldBeCollectible())
			collectibleEntities.remove(entity);
	}
	
	public void addHeightMod(IWorldHeight heightMod){
		heightMods.add(heightMod);
	}
	
	public void update(){
		if(!isBuild){
			buildUp();
			isBuild = true;
		}
		for(int i = 0; i < updatingEntities.size(); i++){
			Entity entity = updatingEntities.get(i);
			entity.update();
			Vector3f pos = entity.getPosition();
			Chunk chunk = world.getChunk(pos.x, pos.z);
			if(chunk != this){
				chunk.spawnEntity(entity);
				updatingEntities.remove(i);
				if(entity.shouldRender())
					renderer.removeEntity(entity);
				if(entity.shouldCollide())
					collidingEntities.remove(entity);
				if(entity.shouldBeCollectible())
					collectibleEntities.remove(entity);
				i--;
			}
		}
	}
	
	public void render(Renderer basicRenderer){
		renderer.render(basicRenderer);
	}
	
	public ArrayList<Entity> getUpdatingEntities(){
		return updatingEntities;
	}
	
	public ArrayList<Entity> getCollectibleEntities(){
		return collectibleEntities;
	}
	
	public ArrayList<Entity> getCollidingEntities(){
		return collidingEntities;
	}
	
	public ArrayList<IWorldHeight> getHeightMods(){
		return heightMods;
	}
	
	public float getGroundHeight(float x, float z) {
		float height = 0;
		for(IWorldHeight heightMod : getHeightMods()){
			if(heightMod.isThere(x, z))
				height += heightMod.extraHeight(x, z);
		}
		return height;
	}
	
	public MasterRenderer getRenderer(){
		return renderer;
	}
	
	public void load(){
		try {
			ArrayList<Byte> bytes = new ArrayList<Byte>();
			FileInputStream input = new FileInputStream("chunks/" + chunkX + " " + chunkZ + ".chunk");
			int next = input.read();
			while(next >= 0){
				bytes.add((byte) next);
				next = input.read();
			}
			input.close();
			ByteBuffer buffer = ByteBuffer.allocate(bytes.size());
			for(Byte b : bytes)
				buffer.put(b);
			buffer.flip();
			ArrayList<Entity> entitiesToPlace = new ArrayList<Entity>();
			while(buffer.hasRemaining()){
				byte entityID = buffer.get();
				Class<?> entityClass = ID_MAP.get(entityID);
				if(entityClass == null)
					throw new RuntimeException("Unknown entity ID: " + entityID);
				Object worldObject = entityClass.getMethod("loadMapData", ByteBuffer.class).invoke(null, buffer);
				if(worldObject instanceof IWorldHeight)
					addHeightMod((IWorldHeight) worldObject);
				if(worldObject instanceof Entity)
					entitiesToPlace.add((Entity) worldObject);
			}
			for(Entity placing : entitiesToPlace){
				placing.place(this);
				spawnEntity(placing);
			}
		} catch(Exception ex){
			System.out.println("Failed to load chunk[" + chunkX + "][" + chunkZ + "]:");
			System.out.println();
			ex.printStackTrace();
			System.out.println();
		}
	}
	
	private class PlaceTask {
		
		private PlaceTask(Class<?> entityClass, ByteBuffer dataBuffer){
			this.entityClass = entityClass;
			this.dataBuffer = dataBuffer;
		}
		
		private Class<?> entityClass;
		private ByteBuffer dataBuffer;
	}
}
