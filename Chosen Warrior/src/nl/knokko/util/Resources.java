package nl.knokko.util;

import static java.lang.Math.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.collision.*;
import nl.knokko.entities.util.SubModel;
import nl.knokko.render.Loader;
import nl.knokko.render.model.RawModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.textures.Material;
import nl.knokko.textures.ModelDinoTexture;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.patterns.*;
import nl.knokko.weapons.KnifeForm;
import nl.knokko.world.Chunk;
import nl.knokko.world.World;
import nl.knokko.world.forest.animals.DinoSettings;
import nl.knokko.world.forest.plants.*;
import nl.knokko.world.heights.Hill;
import nl.knokko.world.structures.SimpleHouseSettings;
import nl.knokko.world.structures.SimpleHouseTexture;

public final class Resources {
	
	private static final byte ID_CUBE_MODEL = -128;
	private static final byte ID_TREE_MODEL = -127;
	private static final byte ID_MUSHROOM_MODEL = -126;
	private static final byte ID_FLOWER_MODEL = -125;
	private static final byte ID_PLANT_MODEL = -124;
	private static final byte ID_SIMPLE_HOUSE_MODEL = -123;
	
	private static final byte ID_TRANSPARENT_TEXTURE = -128;
	private static final byte ID_TREE_TEXTURE = -127;
	private static final byte ID_MUSHROOM_TEXTURE = -126;
	private static final byte ID_FLOWER_TEXTURE = -125;
	private static final byte ID_FILLED_TEXTURE = -124;
	private static final byte ID_GROUND_TEXTURE = -123;
	private static final byte ID_SIMPLE_HOUSE_TEXTURE = -122;
	
	public static final long GROUND_SEED = 3819356L;
	public static final long TREE_SEED = 987529854390934L;
	public static final long DINO_SEED = 43682346839469L;
	
	private static final Font TEXT_LINE_FONT = new Font("TimesRoman", 0, 40);
	
	public static final Vector3f ZERO = new Vector3f();
	public static final Vector3f UP = new Vector3f(0, 1, 0);
	public static final Vector3f DOWN = new Vector3f(0, -1, 0);
	public static final Vector3f LEFT_UP = new Vector3f(-1, 1, 0).normalise(null);
	public static final Vector3f RIGHT_UP = new Vector3f(1, 1, 0).normalise(null);
	public static final Vector3f LEFT_DOWN = new Vector3f(-1, -1, 0).normalise(null);
	public static final Vector3f RIGHT_DOWN = new Vector3f(1, -1, 0).normalise(null);
	public static final Vector3f SOUTH = new Vector3f(0, 0, 1);
	public static final Vector3f NORTH = new Vector3f(0, 0, -1);
	public static final Vector3f EAST = new Vector3f(1, 0, 0);
	public static final Vector3f WEST = new Vector3f(-1, 0, 0);
	
	private static HashMap<TreeColor, ModelTexture> treeTextures = new HashMap<TreeColor, ModelTexture>();
	private static HashMap<FlowerColor, ModelTexture> flowerTextures = new HashMap<FlowerColor, ModelTexture>();
	private static HashMap<MushroomColor, ModelTexture> mushroomTextures = new HashMap<MushroomColor, ModelTexture>();
	
	private static HashMap<SkinColor, ModelDinoTexture> dinoTextures = new HashMap<SkinColor, ModelDinoTexture>();
	private static HashMap<FootColor, ModelTexture> footTextures = new HashMap<FootColor, ModelTexture>();
	private static HashMap<HeadColor, ModelTexture> headTextures = new HashMap<HeadColor, ModelTexture>();
	
	private static HashMap<Long, ModelTexture> groundTextures = new HashMap<Long, ModelTexture>();
	private static HashMap<ColorText, ModelTexture> textTextures = new HashMap<ColorText, ModelTexture>();
	private static HashMap<String, ModelTexture> textLineTextures = new HashMap<String, ModelTexture>();
	private static HashMap<Color, ModelTexture> filledTextures = new HashMap<Color, ModelTexture>();
	private static HashMap<Color, ModelTexture> borderTextures = new HashMap<Color, ModelTexture>();
	
	private static HashMap<SimpleHouseTexture, ModelTexture> simpleHouseTextures = new HashMap<SimpleHouseTexture, ModelTexture>();
	
	private static HashMap<DinoSettings, RawModel> dinoModels = new HashMap<DinoSettings, RawModel>();
	
	private static HashMap<TreeSettings, RawModel> treeModels = new HashMap<TreeSettings, RawModel>();
	private static HashMap<FlowerSettings, RawModel> flowerModels = new HashMap<FlowerSettings, RawModel>();
	private static HashMap<PlantSettings, RawModel> plantModels = new HashMap<PlantSettings, RawModel>();
	private static HashMap<MushroomSettings, RawModel> mushroomModels = new HashMap<MushroomSettings, RawModel>();
	
	private static HashMap<SimpleHouseSettings, RawModel> simpleHouses = new HashMap<SimpleHouseSettings, RawModel>();
	
	private static HashMap<KnifeForm, RawModel> knifeModels = new HashMap<KnifeForm, RawModel>();
	
	private static RawModel cubeModel;
	
	
	private static ModelTexture transparent;
	private static ModelTexture crossTexture;
	
	/**
	 * Get a model from it's saved creation ID.
	 * @param creationID The creation ID of the model.
	 * @return The model that would have this creation ID.
	 */
	public static RawModel getModelFromID(byte[] creationID){
		byte id = creationID[0];
		if(id == ID_CUBE_MODEL)
			return getCubeModel();
		if(id == ID_TREE_MODEL)
			return getTreeModel(Trees.settingsFromID(creationID[1]));
		if(id == ID_MUSHROOM_MODEL)
			return getMushroomModel(Mushrooms.settingsFromID(creationID[1]));
		if(id == ID_FLOWER_MODEL)
			return getFlowerModel(Flowers.settingsFromID(creationID[1]));
		if(id == ID_PLANT_MODEL)
			return getPlantModel(Plants.settingsFromID(creationID[1]));
		throw new IllegalArgumentException("Unknown creationID:" + id);
	}
	
	/**
	 * Get a texture from it's saved creation ID.
	 * @param creationID The creation ID of the texture.
	 * @return The texture that would have this creation ID.
	 */
	public static ModelTexture getTextureFromID(byte[] creationID){
		byte id = creationID[0];
		if(id == ID_TRANSPARENT_TEXTURE)
			return getTransparentTexture();
		if(id == ID_TREE_TEXTURE)
			return Trees.textureFromID(creationID[1]);
		if(id == ID_MUSHROOM_TEXTURE)
			return Mushrooms.textureFromID(creationID[1]);
		if(id == ID_FLOWER_TEXTURE)
			return Flowers.textureFromID(creationID[1]);
		if(id == ID_FILLED_TEXTURE)
			return getFilledTexture(new Color(creationID[1] + 128, creationID[2] + 128, creationID[3] + 128), Material.fromID(creationID[4]));
		if(id == ID_GROUND_TEXTURE)
			return getGroundTexture();
		throw new IllegalArgumentException("Unknown creationID:" + id);
	}
	
	public static int getMushroomTexture(MushroomSettings settings, Color stempColor, Color topColor, Color spotColor, int spots, float spotRadius, float maxDifference, long seed){
		return createMushroomItemTexture(settings, new MushroomColor(stempColor, topColor, spotColor, spots, spotRadius, maxDifference, seed));
	}

	public static ModelTexture getGroundTexture(long seed){
		ModelTexture texture = groundTextures.get(seed);
		if(texture == null)
			texture = loadGround(seed);
		return texture;
	}
	
	public static ModelTexture getGroundTexture(){
		return getGroundTexture(GROUND_SEED);
	}
	
	public static ModelTexture getTreeTexture(Color wood, Color leaves, float maxDifference, long seed, byte treeID){
		ModelTexture texture = treeTextures.get(new TreeColor(wood, leaves, maxDifference, seed));
		if(texture == null)
			texture = createTreeTexture(wood, leaves, maxDifference, seed, treeID);
		return texture;
	}
	
	public static ModelTexture getTreeTexture(Color wood, Color leaves, float maxDifference, byte treeID){
		return getTreeTexture(wood, leaves, maxDifference, TREE_SEED, treeID);
	}
	
	public static ModelTexture getFlowerTexture(Color stemp, Color flower, float maxDifference, long seed, byte flowerID){
		FlowerColor key = new FlowerColor(flower, stemp, maxDifference, seed);
		ModelTexture texture = flowerTextures.get(key);
		if(texture == null)
			texture = createFlowerTexture(key, flowerID);
		return texture;
	}
	
	public static ModelTexture getMushroomTexture(Color stemp, Color top, Color spot, int spots, float spotRadius, float maxDifference, long seed, byte mushroomID){
		MushroomColor key = new MushroomColor(stemp, top, spot, spots, spotRadius, maxDifference, seed);
		ModelTexture texture = mushroomTextures.get(key);
		if(texture == null)
			texture = createMushroomTexture(key, mushroomID);
		return texture;
	}
	
	public static ModelTexture getSimpleHouseTexture(SimpleHouseTexture texture){
		ModelTexture tex = simpleHouseTextures.get(texture);
		if(tex == null)
			tex = createSimpleHouseTexture(texture);
		return tex;
	}
	
	public static ModelDinoTexture getDinoTexture(Color skin, Color nails, float maxDifference, long seed){
		ModelDinoTexture texture = dinoTextures.get(new SkinColor(skin, nails, maxDifference, seed));
		if(texture == null)
			texture = createDinoTexture(skin, nails, maxDifference, seed);
		return texture;
	}
	
	public static ModelTexture getDinoFootTexture(Color skin, Color nails, float maxDifference, long seed){
		ModelTexture texture = footTextures.get(new FootColor(new SkinColor(skin, nails, maxDifference, seed), nails));
		if(texture == null)
			texture = createDinoFootTexture(skin, nails, maxDifference, seed);
		return texture;
	}
	
	public static ModelTexture getDinoHeadTexture(Color skin, Color nails, Color eyes, float maxDifference, long seed){
		ModelTexture texture = headTextures.get(new HeadColor(new SkinColor(skin, nails, maxDifference, seed), eyes));
		if(texture == null)
			texture = createDinoHeadTexture(skin, nails, eyes, maxDifference, seed);
		return texture;
	}
	
	public static ModelTexture getFilledTexture(Color color, Material material){
		ModelTexture texture = filledTextures.get(color);
		if(texture == null)
			texture = createFilledTexture(color, material);
		return texture;
	}
	
	public static ModelTexture getCrossTexture(){
		if(crossTexture == null){
			BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.BLACK);
			g.drawLine(0, 7, 15, 7);
			g.drawLine(0, 8, 15, 8);
			g.drawLine(7, 0, 7, 15);
			g.drawLine(8, 0, 8, 15);
			g.dispose();
			crossTexture = new ModelTexture(Loader.loadTexture(image, true), Material.GUI, null);
		}
		return crossTexture;
	}
	
	public static ModelTexture getText(String text, Color color){
		ColorText ct = new ColorText(color, text);
		ModelTexture texture = textTextures.get(ct);
		if(texture == null)
			texture = createTextTexture(color, text);
		return texture;
	}
	
	public static ModelTexture getTextLine(String text){
		ModelTexture texture = textLineTextures.get(text);
		if(texture == null)
			texture = createTextLineTexture(text);
		return texture;
	}
	
	public static ModelTexture getBorderTexture(Color color){
		ModelTexture texture = borderTextures.get(color);
		if(texture == null)
			texture = createBorderTexture(color);
		return texture;
	}
	
	public static ModelTexture getDinoTexture(Color skin, Color nails, float maxDifference){
		return getDinoTexture(skin, nails, maxDifference, DINO_SEED);
	}
	
	public static ModelTexture getTransparentTexture(){
		if(transparent == null)
			transparent = new ModelTexture(Loader.loadTexture(new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB), true), Material.GUI, new byte[]{ID_TRANSPARENT_TEXTURE});
		return transparent;
	}
	
	public static RawModel getTreeModel(TreeSettings settings){
		RawModel model = treeModels.get(settings);
		if(model == null)
			model = createTreeModel(settings);
		return model;
	}
	
	public static RawModel getPlantModel(PlantSettings settings){
		RawModel model = plantModels.get(settings);
		if(model == null)
			model = createPlantModel(settings);
		return model;
	}
	
	public static RawModel getFlowerModel(FlowerSettings settings){
		RawModel model = flowerModels.get(settings);
		if(model == null)
			model = createFlowerModel(settings);
		return model;
	}
	
	public static RawModel getMushroomModel(MushroomSettings settings){
		RawModel model = mushroomModels.get(settings);
		if(model == null)
			model = createMushroomModel(settings);
		return model;
	}
	
	public static RawModel getSimpleHouseModel(SimpleHouseSettings settings){
		RawModel model = simpleHouses.get(settings);
		if(model == null)
			model = createSimpleHouseModel(settings);
		return model;
	}
	
	public static RawModel getHumanoidModel(DinoSettings settings){
		RawModel model = dinoModels.get(settings);
		if(model == null)
			model = createHumanoidBody(settings);
		return model;
	}
	
	public static RawModel getKnifeModel(KnifeForm form){
		RawModel model = knifeModels.get(form);
		if(model == null){
			model = createKnifeModel(form);
			knifeModels.put(form, model);
		}
		return model;
	}
	
	public static RawModel getCubeModel(){
		if(cubeModel != null)
			return cubeModel;
		float[] vertices = {-1,-1,-1,-1,-1,1,1,-1,1,1,-1,-1,-1,1,-1,-1,1,1,1,1,1, 1,1,-1,-1,1,-1,-1,-1,-1, 1,-1,-1, 1,1,-1,1,1,-1, 1,-1,-1,1,-1,1,1,1,1,-1,1,1,-1,-1,1,1,-1,1,1,1,1,-1,1,-1,-1,-1,-1,-1,-1,1,-1,1,1};
		float[] textureCoords = {0,0.5f,0,1,0.25f,1,0.25f,0.5f,0.25f,0.5f,0.25f,1,0.5f,1,0.5f,0.5f,0,0,0,0.5f,0.25f,0.5f,0.25f,0,0.25f,0,0.25f,0.5f,0.5f,0.5f,0.5f,0,0.5f,0,0.5f,0.5f,0.75f,0.5f,0.75f,0,0.75f,0,0.75f,0.5f,1,0.5f,1,0};
		float[] normals = {0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,1,0,0,1,0, 0,1,0,0,1,0,0,0,-1,0,0,-1,0,0,-1,0,0,-1,1,0,0,1,0,0,1,0,0,1,0,0,0,0,1,0,0,1,0,0,1,0,0,1,-1,0,0,-1,0,0,-1,0,0,-1,0,0};
		int[] indices = {0,1,2,2,3,0,4,5,6,6,7,4,8,9,10,10,11,8,12,13,14,14,15,12,16,17,18,18,19,16,20,21,22,22,23,20};
		cubeModel = Loader.loadNormalToVAO(vertices, textureCoords, normals, indices, new byte[]{ID_CUBE_MODEL}, new ColliderBox(-1, -1, -1, 1, 1, 1));
		return cubeModel;
	}
	
	public static RawModel createHillModel(World world, Hill hill, float delta){
		ArrayList<Float> vertices = new ArrayList<Float>();
        ArrayList<Float> textures = new ArrayList<Float>();
        ArrayList<Float> normals = new ArrayList<Float>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        float minX = hill.getMinX();
        float minZ = hill.getMinZ();
        float maxX = hill.getMaxX();
        float maxZ = hill.getMaxZ();
        float deltaX = maxX - minX;
        float deltaZ = maxZ - minZ;
        int sizeX = 0;
        int sizeZ = 0;
        boolean finishedZ = false;
        float x = minX;
        while(x <= maxX){
            float z = minZ;
            while(z <= maxZ){
                float y = world.getGroundHeight(x, z) + hill.extraHeight(x, z);
                addVertice(vertices, new Vector3f(x, y, z));
                float u = ((x - minX) / deltaX) * 10;
                float v = ((z - minZ) / deltaZ) * 10;
                int iu = (int) u;
                int iv = (int) v;
                u -= iu;
                v -= iv;
                textures.add(u);
                textures.add(v);
                addVertice(normals, new Vector3f(0, 1, 0));
                if(!finishedZ)
                    sizeZ++;
                z += delta;
            }
            finishedZ = true;
            x += delta;
            sizeX++;
        }
        int ix = 1;
        while(ix < sizeX){
            int iz = 1;
            while(iz < sizeZ){
                bindFourAngle(indices, (iz - 1) * sizeX + ix - 1, (iz - 1) * sizeX + ix, iz * sizeX + ix - 1, iz * sizeX + ix);
                iz++;
            }
            ix++;
        }
        return loadToVAO(vertices, textures, normals, indices, null, null);
	}
	
	public static RawModel createGroundModel(World world, float minX, float minZ, float maxX, float maxZ, float delta){
        ArrayList<Float> vertices = new ArrayList<Float>();
        ArrayList<Float> textures = new ArrayList<Float>();
        ArrayList<Float> normals = new ArrayList<Float>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        float deltaX = maxX - minX;
        float deltaZ = maxZ - minZ;
        float midX = (minX + maxX) / 2;
        float midZ = (minZ + maxZ) / 2;
        int sizeX = 0;
        int sizeZ = 0;
        int indexX = (int) (midX / Chunk.SIZE);
        boolean swapX = false;
        if((int)(indexX / 2f) == indexX / 2f)
        	swapX = true;
        if(midX < 0)
        	swapX = !swapX;
        int indexZ = (int) (midZ / Chunk.SIZE);
        boolean swapZ = false;
        if((int)(indexZ / 2f) == indexZ / 2f)
        	swapZ = true;
        if(midZ < 0)
        	swapZ = !swapZ;
        boolean finishedZ = false;
        float x = minX;
        while(x <= maxX){
            float z = minZ;
            while(z <= maxZ){
                float y = world.getGroundHeight(x, z);
                addVertice(vertices, new Vector3f(x, y, z));
                float u = ((x - minX) / deltaX);
                float v = ((z - minZ) / deltaZ);
                if(swapX)
                	u = 1 - u;
                if(swapZ)
                	v = 1 - v;
                textures.add(u);
                textures.add(v);
                addVertice(normals, new Vector3f(0, 1, 0));
                if(!finishedZ)
                    sizeZ++;
                z += delta;
            }
            finishedZ = true;
            x += delta;
            sizeX++;
        }
        int ix = 1;
        while(ix < sizeX){
            int iz = 1;
            while(iz < sizeZ){
                bindFourAngle(indices, (iz - 1) * sizeX + ix - 1, (iz - 1) * sizeX + ix, iz * sizeX + ix - 1, iz * sizeX + ix);
                iz++;
            }
            ix++;
        }
        return loadToVAO(vertices, textures, normals, indices, null, null);
    }
 
    private static RawModel loadToVAO(ArrayList<Float> vertices, ArrayList<Float> textures, ArrayList<Float> normals, ArrayList<Integer> indices, byte[] creationID, Collider collider){
        float[] verticeArray = new float[vertices.size()];
        float[] textureArray = new float[textures.size()];
        float[] normalArray = new float[normals.size()];
        int[] indiceArray = new int[indices.size()];
        for(int i = 0; i < vertices.size(); i++)
            verticeArray[i] = vertices.get(i);
        for(int i = 0; i < textures.size(); i++)
            textureArray[i] = textures.get(i);
        for(int i = 0; i < normals.size(); i++)
            normalArray[i] = normals.get(i);
        for(int i = 0; i < indices.size(); i++)
            indiceArray[i] = indices.get(i);
        return Loader.loadNormalToVAO(verticeArray, textureArray, normalArray, indiceArray, creationID, collider);
    }
 
    private static RawModel loadToVAO(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, int imageSize, byte[] creationID, Collider collider){
        float[] verticeArray = new float[vertices.size()];
        float[] textureArray = new float[intTextures.size()];
        float[] normalArray = new float[normals.size()];
        int[] indiceArray = new int[indices.size()];
        for(int i = 0; i < vertices.size(); i++)
            verticeArray[i] = vertices.get(i);
        for(int i = 0; i < intTextures.size(); i++){
            if(i / 2 == i / 2.0)
                textureArray[i] = (float)(intTextures.get(i) + 0.5f) / imageSize;
            else
                textureArray[i] = (float)(intTextures.get(i) + 0.5f) / imageSize;
        }
        for(int i = 0; i < normals.size(); i++)
            normalArray[i] = normals.get(i);
        for(int i = 0; i < indices.size(); i++)
            indiceArray[i] = indices.get(i);
        return Loader.loadNormalToVAO(verticeArray, textureArray, normalArray, indiceArray, creationID, collider);
    }
    
    public static SubModel createDwoorfArm(DinoSettings settings, ModelDinoTexture texture, boolean left){
    	ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random(settings.seed);
		addArmPart(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel model = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		TexturedModel tex = new TexturedModel(model, texture);
		SubModel arm = new SubModel(new TexturedModel(model, texture), new Vector3f(left ? -settings.bodyRadius * 1.11f : settings.bodyRadius * 1.11f, -settings.bodyLength * 0.25f, 0));
		arm.childModels.add(new SubModel(tex, new Vector3f(0, -settings.armLength * 0.5f, 0)));
		arm.childModels.get(0).rotationX = 30;
		vertices = new ArrayList<Float>();
		intTextures = new ArrayList<Integer>();
		normals = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		addClawBase(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel handModel = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		SubModel hand = new SubModel(new TexturedModel(handModel, texture), new Vector3f(0, -settings.armLength * 0.45f, 0));
		arm.childModels.get(0).childModels.add(hand);
		hand.rotationX = 270;
		vertices = new ArrayList<Float>();
		intTextures = new ArrayList<Integer>();
		normals = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		addClawNail(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel nail = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		ModelTexture nailTexture = getFilledTexture(texture.getNailColor(), Material.METAL);
		TexturedModel texNail = new TexturedModel(nail, nailTexture);
		hand.childModels.add(new SubModel(texNail, new Vector3f(0, 0, -settings.armRadius * 1.9f)));
		hand.childModels.add(new SubModel(texNail, new Vector3f(-settings.armRadius * 0.54f, 0, -settings.armRadius * 1.9f)));
		hand.childModels.add(new SubModel(texNail, new Vector3f(settings.armRadius * 0.54f, 0, -settings.armRadius * 1.9f)));
		return arm;
    }
	
	public static SubModel createHumanoidArm(DinoSettings settings, ModelDinoTexture texture, boolean left){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random(settings.seed);
		addArmPart(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel model = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		TexturedModel tex = new TexturedModel(model, texture);
		SubModel arm = new SubModel(tex, new Vector3f(left ? -settings.bodyRadius * 1.11f : settings.bodyRadius * 1.11f, settings.bodyLength * 0.45f, 0));
		arm.childModels.add(new SubModel(tex, new Vector3f(0, -settings.armLength * 0.5f, 0)));
		vertices = new ArrayList<Float>();
		intTextures = new ArrayList<Integer>();
		normals = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		addClawBase(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel handModel = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		SubModel hand = new SubModel(new TexturedModel(handModel, texture), new Vector3f(0, -settings.armLength * 0.45f, 0));
		arm.childModels.get(0).childModels.add(hand);
		hand.rotationX = 270;
		vertices = new ArrayList<Float>();
		intTextures = new ArrayList<Integer>();
		normals = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		addClawNail(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel nail = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		ModelTexture nailTexture = getFilledTexture(texture.getNailColor(), Material.METAL);
		TexturedModel texNail = new TexturedModel(nail, nailTexture);
		hand.childModels.add(new SubModel(texNail, new Vector3f(0, 0, -settings.armRadius * 1.9f)));
		hand.childModels.add(new SubModel(texNail, new Vector3f(-settings.armRadius * 0.54f, 0, -settings.armRadius * 1.9f)));
		hand.childModels.add(new SubModel(texNail, new Vector3f(settings.armRadius * 0.54f, 0, -settings.armRadius * 1.9f)));
		return arm;
	}
	
	public static SubModel createDwoorfLeg(DinoSettings settings, ModelDinoTexture texture, boolean left, boolean front){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random(settings.seed);
		addLegPart(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel model = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		TexturedModel tex = new TexturedModel(model, texture);
		SubModel leg = new SubModel(new TexturedModel(model, texture), new Vector3f(left ? -settings.bodyRadius * 0.6f : settings.bodyRadius * 0.6f, front ? settings.bodyLength * 0.5f : -settings.bodyLength * 0.5f, 0));
		leg.childModels.add(new SubModel(tex, new Vector3f(0, -settings.legLength * 0.5f, 0)));
		vertices = new ArrayList<Float>();
		intTextures = new ArrayList<Integer>();
		normals = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		addFoot(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel foot = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		leg.childModels.get(0).childModels.add(new SubModel(new TexturedModel(foot, getDinoFootTexture(texture.getSkinColor(), texture.getNailColor(), texture.getMaxDifference(), texture.getSeed())), new Vector3f(0, -settings.legLength * 0.5f, -settings.legLength * 0.1f)));
		return leg;
	}
	
	public static SubModel createHumanoidLeg(DinoSettings settings, ModelDinoTexture texture, boolean left){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random(settings.seed);
		addLegPart(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel model = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		TexturedModel tex = new TexturedModel(model, texture);
		SubModel leg = new SubModel(new TexturedModel(model, texture), new Vector3f(left ? -settings.bodyRadius * 0.6f : settings.bodyRadius * 0.6f, -settings.bodyLength * 0.5f, 0));
		leg.childModels.add(new SubModel(tex, new Vector3f(0, -settings.legLength * 0.5f, 0)));
		vertices = new ArrayList<Float>();
		intTextures = new ArrayList<Integer>();
		normals = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		addFoot(vertices, intTextures, normals, indices, settings, new Vector3f(0, 0, 0), random);
		RawModel foot = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		leg.childModels.get(0).childModels.add(new SubModel(new TexturedModel(foot, getDinoFootTexture(texture.getSkinColor(), texture.getNailColor(), texture.getMaxDifference(), texture.getSeed())), new Vector3f(0, -settings.legLength * 0.5f, -settings.legLength * 0.1f)));
		return leg;
	}
	
	public static SubModel createDwoorfHead(DinoSettings settings, ModelDinoTexture texture, float x, float yaw){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textures = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		addNeckPart(vertices, textures, normals, indices, settings);
		RawModel neck = loadToVAO(vertices, textures, normals, indices, null, null);
		TexturedModel texMod = new TexturedModel(neck, texture);
		SubModel sub = new SubModel(texMod, new Vector3f(x, -settings.bodyLength * 0.45f, -settings.bodyRadius * 0.4f));
		sub.rotationX = 40;
		sub.rotationY = yaw;
		SubModel newest = sub;
		float rot = 50f / settings.neckParts;
		float l = -settings.neckLength / settings.neckParts;
		Vector3f baseVector = new Vector3f(0, (float) (l * 0.9f * Math.cos(Math.toRadians(rot))), (float) (l * 0.9f * Math.sin(Math.toRadians(rot))));
		for(int i = 1; i < settings.neckParts; i++){
			SubModel newModel = new SubModel(texMod, baseVector);
			newModel.rotationX = 50f / settings.neckParts;
			newest.childModels.add(newModel);
			newest = newModel;
		}
		vertices = new ArrayList<Float>();
		textures = new ArrayList<Float>();
		normals = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		addDragonHead(vertices, textures, normals, indices, settings, ZERO);
		RawModel head = loadToVAO(vertices, textures, normals, indices, null, null);
		newest.childModels.add(new SubModel(new TexturedModel(head, texture), baseVector));
		return sub;
	}
	
	public static SubModel createHumanoidTail(DinoSettings settings, ModelDinoTexture texture){
		float yaw = 0;
		float x = 0;
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textures = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		addTailPart(vertices, textures, normals, indices, settings);
		RawModel neck = loadToVAO(vertices, textures, normals, indices, null, null);
		TexturedModel texMod = new TexturedModel(neck, texture);
		SubModel sub = new SubModel(texMod, new Vector3f(x, 0, settings.bodyRadius * 3));
		sub.rotationX = 40;
		sub.rotationY = yaw;
		SubModel newest = sub;
		float l = -settings.tailLength / settings.tailParts;
		float rot = 50f / settings.tailParts;
		Vector3f baseVector = new Vector3f(0, (float) (l  * 0.9f * Math.cos(Math.toRadians(rot))), (float) (l * 0.9f * Math.sin(Math.toRadians(rot))));
		for(int i = 1; i < settings.tailParts; i++){
			SubModel newModel = new SubModel(texMod, baseVector);
			newModel.rotationX = rot;
			newest.childModels.add(newModel);
			newest = newModel;
		}
		return sub;
	}
	
	public static SubModel createHumanoidHead(DinoSettings settings, ModelDinoTexture texture){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random(settings.seed);
		addHead(vertices, intTextures, normals, indices, settings, new Vector3f(0, settings.headRadius, 0), random);
		RawModel model = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		return new SubModel(new TexturedModel(model, getDinoHeadTexture(texture.getSkinColor(), texture.getNailColor(), Color.GREEN, texture.getMaxDifference(), texture.getSeed())), new Vector3f(0, settings.bodyLength * 0.5f, 0));
	}
	
	private static RawModel createHumanoidBody(DinoSettings settings){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random(settings.seed);
		addBody(vertices, intTextures, normals, indices, settings, new Vector3f(), random);
		RawModel model = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, null, null);
		dinoModels.put(settings, model);
		return model;
	}
	
	private static void addBody(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location, Random random){
		addBodyPart(vertices, intTextures, normals, indices, settings, location, random, 0.45f, 0.8f, settings.bodyLength, settings.bodyRadius);
	}
	
	private static void addLegPart(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location, Random random){
		addBodyPart(vertices, intTextures, normals, indices, settings, new Vector3f(location.x, location.y - settings.legLength * 0.25f, location.z), random, 0.4f, 0.8f, settings.legLength * 0.5f, settings.legRadius);
		addBodyPart(vertices, intTextures, normals, indices, settings, location, random, 0.4f, 0.8f, settings.legRadius * 2.2f, settings.legRadius * 1.1f, new Vector3f(0,-1,0));
	}
	
	private static void addFoot(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location, Random random){
		int index = vertices.size() / 3;
		float y = location.y - settings.footHeight / 2;
		float toeFactor = 0.3f;
		float toeWidth = 0.5f * settings.footRadius;
		float maxZ = settings.legRadius + settings.footLength * (1 + toeFactor);
		boolean invert = true;
		Vector3f norLeftUp = new Vector3f(-0.3f, 0.6f, 0);
		Vector3f norRightUp = new Vector3f(0.3f, 0.6f, 0);
		Vector3f norLeftDown = new Vector3f(-0.3f, -0.6f, 0);
		Vector3f norRightDown = new Vector3f(0.3f, -0.6f, 0);
		Vector3f norLeftForUp = new Vector3f(-0.3f, 0.6f, 0.3f);
		Vector3f norRightForUp = new Vector3f(0.3f, 0.6f, 0.3f);
		Vector3f norLeftForDown = new Vector3f(-0.3f, -0.6f, 0.3f);
		Vector3f norRightForDown = new Vector3f(0.3f, -0.6f, 0.3f);
		Vector3f norBackUp = new Vector3f(0, 0.5f, -0.5f);
		norLeftUp.normalise();
		norRightUp.normalise();
		norRightDown.normalise();
		norLeftDown.normalise();
		norLeftForUp.normalise();
		norRightForUp.normalise();
		norLeftForDown.normalise();
		norRightForDown.normalise();
		norBackUp.normalise();
		if(invert){
			norLeftUp.negate();
			norRightUp.negate();
			norRightDown.negate();
			norLeftDown.negate();
			norLeftForUp.negate();
			norRightForUp.negate();
			norLeftForDown.negate();
			norRightForDown.negate();
			norBackUp.negate();
		}
		//foot 0
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + settings.legRadius * 0.5f, y, location.z + settings.legRadius * 2), norRightForDown, 0, settings.imageSize / 3);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - settings.legRadius * 0.5f, y, location.z + settings.legRadius * 2), norLeftForDown, 0, 0);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + settings.legRadius, y, location.z + settings.legRadius), norRightDown, (int) (settings.legRadius / maxZ * settings.imageSize), settings.imageSize / 3);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - settings.legRadius, y, location.z + settings.legRadius), norLeftDown, (int) (settings.legRadius / maxZ * settings.imageSize), 0);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + settings.footRadius, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norRightDown, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize / 3);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - settings.footRadius, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norLeftDown, (int) (settings.imageSize * (1 - toeFactor)), 0);
		//begin toes 6
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f + settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norRightForDown, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norRightForDown, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norLeftForDown, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f - settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norLeftForDown, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize / 2);
		//first toe 10
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 1.5f + settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norRightDown, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f + settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norLeftDown, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize / 2);
		//mid toe 12
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norRightDown, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norLeftDown, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize / 2);
		//last toe 14
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 1.5f - settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norLeftDown, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f - settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norRightDown, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize / 2);
		int index2 = vertices.size() / 3;
		y = location.y + settings.footHeight / 2;
		//foot 0
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + settings.legRadius * 0.5f, y, location.z + settings.legRadius * 2), norRightForUp, 0, settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - settings.legRadius * 0.5f, y, location.z + settings.legRadius * 2), norLeftForUp, 0, settings.imageSize / 3 * 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + settings.legRadius, y, location.z + settings.legRadius), norRightUp, (int) (settings.legRadius / maxZ * settings.imageSize), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - settings.legRadius, y, location.z + settings.legRadius), norLeftUp, (int) (settings.legRadius / maxZ * settings.imageSize), settings.imageSize / 3 * 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + settings.footRadius, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norRightUp, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - settings.footRadius, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norLeftUp, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize / 3 * 2);
		//begin toes 6
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f + settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norRightForUp, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norRightForUp, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norLeftForUp, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f - settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor)), norLeftForUp, (int) (settings.imageSize * (1 - toeFactor)), settings.imageSize);
		//first toe 10
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 1.5f + settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norRightUp, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f + settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norLeftUp, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth + settings.footRadius * 0.2f, y + settings.footHeight / 2, location.z + settings.legRadius - settings.footLength), norBackUp, settings.imageSize, settings.imageSize);
		//mid toe 13
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x + toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norRightUp, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norLeftUp, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x, y + settings.footHeight / 2, location.z + settings.legRadius - settings.footLength), norBackUp, settings.imageSize, settings.imageSize);
		//last toe 16
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 1.5f - settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norLeftUp, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth * 0.5f - settings.footRadius * 0.2f, y, location.z + settings.legRadius - settings.footLength * (1 - toeFactor / 2)), norRightUp, (int) (settings.imageSize * (1 - toeFactor / 2)), settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(location.x - toeWidth - settings.footRadius * 0.2f, y + settings.footHeight / 2, location.z + settings.legRadius - settings.footLength), norBackUp, settings.imageSize, settings.imageSize);
		//draw foot
		bindFourAngle(indices, index + 0, index + 1, index2 + 0, index2 + 1);
		bindFourAngle(indices, index + 0, index + 2, index2 + 0, index2 + 2);
		bindFourAngle(indices, index + 1, index + 3, index2 + 1, index2 + 3);
		bindFourAngle(indices, index + 0, index + 1, index + 2, index + 3);
		bindFourAngle(indices, index2 + 0, index2 + 1, index2 + 2, index2 + 3);
		bindFourAngle(indices, index + 2, index + 4, index2 + 2, index2 + 4);
		bindFourAngle(indices, index + 3, index + 5, index2 + 3, index2 + 5);
		bindFourAngle(indices, index + 4, index + 5, index2 + 4, index2 + 5);
		bindFourAngle(indices, index + 2, index + 3, index + 4, index + 5);
		bindFourAngle(indices, index2 + 2, index2 + 3, index2 + 4, index2 + 5);
		//draw edges
		bindFourAngle(indices, index + 4, index + 10, index2 + 4, index2 + 10);
		bindFourAngle(indices, index + 6, index + 11, index2 + 6, index2 + 11);
		bindFourAngle(indices, index + 4, index + 10, index + 6, index + 11);
		bindFourAngle(indices, index2 + 4, index2 + 10, index2 + 6, index2 + 11);
		
		bindFourAngle(indices, index + 7, index + 12, index2 + 7, index2 + 13);
		bindFourAngle(indices, index + 8, index + 13, index2 + 8, index2 + 14);
		bindFourAngle(indices, index + 7, index + 12, index + 8, index + 13);
		bindFourAngle(indices, index2 + 7, index2 + 13, index2 + 8, index2 + 14);
		
		bindFourAngle(indices, index + 5, index + 14, index2 + 5, index2 + 16);
		bindFourAngle(indices, index + 9, index + 15, index2 + 9, index2 + 17);
		bindFourAngle(indices, index + 5, index + 14, index + 9, index + 15);
		bindFourAngle(indices, index2 + 5, index2 + 16, index2 + 9, index2 + 17);
		//draw nails
		bindTriAngle(indices, index2 + 10, index2 + 11, index2 + 12);
		bindTriAngle(indices, index + 10, index + 11, index2 + 12);
		bindTriAngle(indices, index + 10, index2 + 10, index2 + 12);
		bindTriAngle(indices, index + 11, index2 + 11, index2 + 12);
		bindTriAngle(indices, index2 + 13, index2 + 14, index2 + 15);
		bindTriAngle(indices, index + 12, index + 13, index2 + 15);
		bindTriAngle(indices, index + 12, index2 + 13, index2 + 15);
		bindTriAngle(indices, index + 13, index2 + 14, index2 + 15);
		bindTriAngle(indices, index + 14, index + 15, index2 + 18);
		bindTriAngle(indices, index2 + 16, index2 + 17, index2 + 18);
		bindTriAngle(indices, index + 14, index2 + 16, index2 + 18);
		bindTriAngle(indices, index + 15, index2 + 17, index2 + 18);
	}
	
	private static void addClawBase(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f loc, Random random){
		float height = settings.armRadius;
		float width = settings.armRadius * 2.0f;
		float depth = -settings.armRadius * 1.9f;
		int index = vertices.size() / 3;
		Vector3f centre = new Vector3f(loc.x, loc.y, loc.z + depth * 0.5f);
		//arm connection top 0
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - settings.armRadius, loc.y + height * 0.5f, loc.z), centre, 0, (int) (settings.imageSize * 0.75));
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + settings.armRadius, loc.y + height * 0.5f, loc.z), centre, 0, settings.imageSize);
		//hand middle top 2
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - width * 0.5f, loc.y + height * 0.5f, loc.z + depth * 0.5f), centre, settings.imageSize / 2, (int) (settings.imageSize * 0.75));
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + width * 0.5f, loc.y + height * 0.5f, loc.z + depth * 0.5f), centre, settings.imageSize / 2, settings.imageSize);
		//hand end top 4
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - width * 0.4f, loc.y + height * 0.5f, loc.z + depth), centre, settings.imageSize, (int) (settings.imageSize * 0.75));
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + width * 0.4f, loc.y + height * 0.5f, loc.z + depth), centre, settings.imageSize, settings.imageSize);
		//arm connection bottom 6
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - settings.armRadius, loc.y - height * 0.5f, loc.z), centre, 0, (int) (settings.imageSize * 0.25));
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + settings.armRadius, loc.y - height * 0.5f, loc.z), centre, 0, settings.imageSize / 2);
		//hand middle bottom 8
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - width * 0.5f, loc.y - height * 0.5f, loc.z + depth * 0.5f), centre, settings.imageSize / 2, (int) (settings.imageSize * 0.25));
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + width * 0.5f, loc.y - height * 0.5f, loc.z + depth * 0.5f), centre, settings.imageSize / 2, settings.imageSize / 2);
		//hand end bottom 10
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - width * 0.4f, loc.y - height * 0.5f, loc.z + depth), centre, settings.imageSize, (int) (settings.imageSize * 0.25));
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + width * 0.4f, loc.y - height * 0.5f, loc.z + depth), centre, settings.imageSize, settings.imageSize / 2);
		//upper claw
		bindFourAngle(indices, index + 0, index + 1, index + 2, index + 3);
		bindFourAngle(indices, index + 2, index + 3, index + 4, index + 5);
		//bottom claw
		bindFourAngle(indices, index + 6, index + 7, index + 8, index + 9);
		bindFourAngle(indices, index + 8, index + 9, index + 10, index + 11);
		//connection
		bindFourAngle(indices, index + 0, index + 1, index + 6, index + 7);
		//end
		bindFourAngle(indices, index + 4, index + 5, index + 10, index + 11);
		//middle bindings
		bindFourAngle(indices, index + 0, index + 2, index + 6, index + 8);
		bindFourAngle(indices, index + 1, index + 3, index + 7, index + 9);
		bindFourAngle(indices, index + 2, index + 8, index + 4, index + 10);
		bindFourAngle(indices, index + 3, index + 9, index + 5, index + 11);
	}
	
	private static void addClawNail(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f loc, Random random){
		float size = 0.5f;
		float height = settings.armRadius * 0.5f * size;
		float width = settings.armRadius * 1.0f * size;
		float depth = -settings.armRadius * 2.4f * size;
		int index = vertices.size() / 3;
		Vector3f centre = new Vector3f(loc.x, loc.y + 0.5f * height, loc.z + depth * 0.5f);
		//claw connection 0
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x, loc.y + height / 2, loc.z), centre, 0, settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - width / 2, loc.y - height / 2, loc.z), centre, 0, settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + width / 2, loc.y - height / 2, loc.z), centre, 0, 0);
		//nail centre 3
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x, loc.y + height / 2, loc.z + depth / 2), centre, settings.imageSize / 2, settings.imageSize);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x - width / 2, loc.y - height / 2, loc.z + depth * 0.65f), centre, (int) (settings.imageSize * 0.65f), settings.imageSize / 2);
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x + width / 2, loc.y - height / 2, loc.z + depth * 0.65f), centre, (int) (settings.imageSize * 0.65f), 0);
		//nail point 6
		addVertice(vertices, normals, intTextures, settings, new Vector3f(loc.x, loc.y + height, loc.z + depth), centre, settings.imageSize, settings.imageSize);
		//bind connection
		bindTriAngle(indices, index + 1, index + 2, index + 0);
		bindFourAngle(indices, index + 0, index + 3, index + 1, index + 4);
		bindFourAngle(indices, index + 0, index + 3, index + 2, index + 5);
		bindFourAngle(indices, index + 1, index + 4, index + 2, index + 5);
		//bind to point
		bindTriAngle(indices, index + 3, index + 4, index + 6);
		bindTriAngle(indices, index + 3, index + 5, index + 6);
		bindTriAngle(indices, index + 4, index + 5, index + 6);
	}
	
	private static void addArmPart(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location, Random random){
		addBodyPart(vertices, intTextures, normals, indices, settings, new Vector3f(location.x, location.y - settings.armLength * 0.25f, location.z), random, 0.45f, 0.8f, settings.armLength * 0.5f, settings.armRadius, new Vector3f(0,-1,0));
		addBodyPart(vertices, intTextures, normals, indices, settings, location, random, 0.4f, 0.8f, settings.armRadius * 2.2f, settings.armRadius * 1.1f, new Vector3f(0,-1,0));
	}
	
	private static void addHead(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location, Random random){
		addBodyPart(vertices, intTextures, normals, indices, settings, location, random, 0.35f, 0.8f, settings.headRadius * 2, settings.headRadius);
	}
	
	private static void addDragonHead(ArrayList<Float> vertices, ArrayList<Float> textures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location){
		int parts = 10;
		float mouthHeight = settings.headRadius * 0.4f;
		float mouthWidth = settings.headRadius * 0.8f;
		float mouthLength = mouthWidth * 2;
		Vector3f up = new Vector3f(0, 1, 0);
		int indexMidRing = vertices.size() / 3;
		Vector3f midVertice = new Vector3f(location.x, location.y + settings.headRadius, location.z);
		for(int i = 0; i < parts; i++){
			float angle = i / (parts - 1f) * 180 + 180;
			Vector3f vertice = pointAt(angle, midVertice, up, settings.headRadius);
			Vector3f normal = Vector3f.sub(vertice, midVertice, null);
			normal.normalise();
			addVertice(vertices, textures, normals, vertice, angle / 360, 0.5f, normal);
		}
		int indexLowRing = vertices.size() / 3;
		Vector3f lowVertice = new Vector3f(location.x, location.y + settings.headRadius * 0.5f, location.z);
		float radiusHighLow = settings.headRadius * 0.5f * (float) Math.sqrt(2);
		for(int i = 0; i < parts; i++){
			float angle = i / (parts - 1f) * 180 + 180;
			Vector3f vertice = pointAt(angle, lowVertice, up, radiusHighLow);
			Vector3f normal = Vector3f.sub(vertice, midVertice, null);
			normal.normalise();
			addVertice(vertices, textures, normals, vertice, angle / 360, 0.25f, normal);
		}
		int indexHighRing = vertices.size() / 3;
		Vector3f highVertice = new Vector3f(location.x, location.y + settings.headRadius * 1.5f, location.z);
		for(int i = 0; i < parts; i++){
			float angle = i / (parts - 1f) * 180 + 180;
			Vector3f vertice = pointAt(angle, highVertice, up, radiusHighLow);
			Vector3f normal = Vector3f.sub(vertice, midVertice, null);
			normal.normalise();
			addVertice(vertices, textures, normals, vertice, angle / 360, 0.75f, normal);
		}
		int indexBottom = vertices.size() / 3;
		addVertice(vertices, textures, normals, location, 0.5f, 0, DOWN);
		int indexTop = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x, location.y + settings.headRadius * 2, location.z), 0.5f, 1, UP);
		int indexMouthTopTopLeftBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, highVertice.y + mouthHeight / 2, location.z + radiusHighLow), 0.15f, 0.85f, LEFT_UP);
		int indexMouthTopTopRightBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, highVertice.y + mouthHeight / 2, location.z + radiusHighLow), 0.35f, 0.85f, RIGHT_UP);
		int indexMouthTopBottomLeftBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, highVertice.y - mouthHeight / 2, location.z + radiusHighLow), 0.15f, 0.65f, LEFT_DOWN);
		int indexMouthTopBottomRightBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, highVertice.y - mouthHeight / 2, location.z + radiusHighLow), 0.35f, 0.65f, RIGHT_DOWN);
		int indexMouthTopTopLeftFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, highVertice.y + mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.24f, 0.85f, LEFT_UP);
		int indexMouthTopTopRightFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, highVertice.y + mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.26f, 0.85f, RIGHT_UP);
		int indexMouthTopBottomLeftFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, highVertice.y - mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.24f, 0.65f, LEFT_DOWN);
		int indexMouthTopBottomRightFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, highVertice.y - mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.26f, 0.65f, RIGHT_DOWN);
		
		int indexMouthBottomTopLeftBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, lowVertice.y + mouthHeight / 2, location.z + radiusHighLow), 0.15f, 0.85f, LEFT_UP);
		int indexMouthBottomTopRightBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, lowVertice.y + mouthHeight / 2, location.z + radiusHighLow), 0.35f, 0.85f, RIGHT_UP);
		int indexMouthBottomBottomLeftBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, lowVertice.y - mouthHeight / 2, location.z + radiusHighLow), 0.15f, 0.65f, LEFT_DOWN);
		int indexMouthBottomBottomRightBack = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, lowVertice.y - mouthHeight / 2, location.z + radiusHighLow), 0.35f, 0.65f, RIGHT_DOWN);
		int indexMouthBottomTopLeftFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, lowVertice.y + mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.24f, 0.85f, LEFT_UP);
		int indexMouthBottomTopRightFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, lowVertice.y + mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.26f, 0.85f, RIGHT_UP);
		int indexMouthBottomBottomLeftFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x - mouthWidth / 2, lowVertice.y - mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.24f, 0.65f, LEFT_DOWN);
		int indexMouthBottomBottomRightFront = vertices.size() / 3;
		addVertice(vertices, textures, normals, new Vector3f(location.x + mouthWidth / 2, lowVertice.y - mouthHeight / 2, location.z + radiusHighLow + mouthLength), 0.26f, 0.65f, RIGHT_DOWN);
		for(int i = 0; i < parts - 1; i++){
			bindTriAngle(indices, indexBottom, indexLowRing + i, indexLowRing + i + 1);
			bindFourAngle(indices, indexLowRing + i, indexLowRing + i + 1, indexMidRing + i, indexMidRing + i + 1);
			bindFourAngle(indices, indexHighRing + i, indexHighRing + i + 1, indexMidRing + i, indexMidRing + i + 1);
			bindTriAngle(indices, indexTop, indexHighRing + i, indexHighRing + i + 1);
		}
		bindFourAngle(indices, indexMouthTopTopLeftBack, indexMouthTopTopRightBack, indexMouthTopBottomLeftBack, indexMouthTopBottomRightBack);
		bindFourAngle(indices, indexMouthTopTopLeftFront, indexMouthTopTopRightFront, indexMouthTopBottomLeftFront, indexMouthTopBottomRightFront);
		bindFourAngle(indices, indexMouthTopTopLeftBack, indexMouthTopTopRightBack, indexMouthTopTopLeftFront, indexMouthTopTopRightFront);
		bindFourAngle(indices, indexMouthTopBottomLeftBack, indexMouthTopBottomRightBack, indexMouthTopBottomLeftFront, indexMouthTopBottomRightFront);
		bindFourAngle(indices, indexMouthTopBottomLeftBack, indexMouthTopTopLeftBack, indexMouthTopBottomLeftFront, indexMouthTopTopLeftFront);
		bindFourAngle(indices, indexMouthTopBottomRightBack, indexMouthTopTopRightBack, indexMouthTopBottomRightFront, indexMouthTopTopRightFront);
		bindFourAngle(indices, indexMouthTopTopLeftBack, indexMouthTopTopRightBack, indexHighRing, indexTop);
		bindTriAngle(indices, indexMouthTopTopRightBack, indexHighRing + parts - 1, indexTop);
		bindFourAngle(indices, indexMouthTopTopLeftBack, indexMouthTopBottomLeftBack, indexHighRing, indexMidRing);
		bindFourAngle(indices, indexMouthTopTopRightBack, indexMouthTopBottomRightBack, indexHighRing + parts - 1, indexMidRing + parts - 1);
		
		bindFourAngle(indices, indexMouthBottomBottomLeftBack, indexMouthBottomBottomRightBack, indexMouthBottomTopLeftBack, indexMouthBottomTopRightBack);
		bindFourAngle(indices, indexMouthBottomBottomLeftFront, indexMouthBottomBottomRightFront, indexMouthBottomTopLeftFront, indexMouthBottomTopRightFront);
		bindFourAngle(indices, indexMouthBottomBottomLeftBack, indexMouthBottomBottomRightBack, indexMouthBottomBottomLeftFront, indexMouthBottomBottomRightFront);
		bindFourAngle(indices, indexMouthBottomTopLeftBack, indexMouthBottomTopRightBack, indexMouthBottomTopLeftFront, indexMouthBottomTopRightFront);
		bindFourAngle(indices, indexMouthBottomBottomLeftBack, indexMouthBottomTopLeftBack, indexMouthBottomBottomLeftFront, indexMouthBottomTopLeftFront);
		bindFourAngle(indices, indexMouthBottomBottomRightBack, indexMouthBottomTopRightBack, indexMouthBottomBottomRightFront, indexMouthBottomTopRightFront);
		bindFourAngle(indices, indexMouthBottomBottomLeftBack, indexMouthBottomBottomRightBack, indexLowRing, indexBottom);
		bindTriAngle(indices, indexMouthBottomBottomRightBack, indexLowRing + parts - 1, indexBottom);
		bindFourAngle(indices, indexMouthBottomBottomLeftBack, indexMouthBottomTopLeftBack, indexLowRing, indexMidRing);
		bindFourAngle(indices, indexMouthBottomBottomRightBack, indexMouthBottomTopRightBack, indexLowRing + parts - 1, indexMidRing + parts - 1);
	}
	
	private static void addBodyPart(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location, Random random, float deltaY, float radiusFactor, float length, float radius){
		addBodyPart(vertices, intTextures, normals, indices, settings, location, random, deltaY, radiusFactor, length, radius, new Vector3f(0, 1, 0));
	}
	
	private static void addBodyPart(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings, Vector3f location, Random random, float deltaY, float radiusFactor, float length, float radius, Vector3f normal){
		Vector3f[] topVectors = getCircleVectors(new Vector3f(location.x + (deltaY * length) * normal.x, location.y + (deltaY * length) * normal.y, location.z + (deltaY * length) * normal.z), normal, radius * radiusFactor, 10);
		Vector3f[] midVectors = getCircleVectors(new Vector3f(location), normal, radius, 10);
		Vector3f[] botVectors = getCircleVectors(new Vector3f(location.x - (deltaY * length) * normal.x, location.y - (deltaY * length) * normal.y, location.z - (deltaY * length) * normal.z), normal, radius * radiusFactor, 10);
		Vector3f bottom = new Vector3f(location.x - (length * 0.5f) * normal.x, location.y - (length * 0.5f) * normal.y, location.z - (length * 0.5f) * normal.z);
		Vector3f top = new Vector3f(location.x + (length * 0.5f) * normal.x, location.y + (length * 0.5f) * normal.y, location.z + (length * 0.5f) * normal.z);
		int topIndex = vertices.size() / 3;
		int bottomIndex = topIndex + 1;
		int bottomStart = bottomIndex + 1;
		int midStart = bottomStart + botVectors.length;
		int topStart = midStart + midVectors.length;
		addVertice(vertices, normals, settings, top, location);
		intTextures.add(settings.imageSize / 2);
		intTextures.add(settings.imageSize);
		addVertice(vertices, normals, settings, bottom, location);
		intTextures.add(settings.imageSize / 2);
		intTextures.add(0);
		int index = 0;
		int factor = settings.imageSize / (botVectors.length);
		for(Vector3f botVector : botVectors){
			addVertice(vertices, normals, settings, botVector, location);
			intTextures.add(index * factor);
			intTextures.add(settings.imageSize / 4);
			++index;
		}
		index = 0;
		for(Vector3f midVector : midVectors){
			addVertice(vertices, normals, settings, midVector, location);
			intTextures.add(index * factor);
			intTextures.add(settings.imageSize / 2);
			++index;
		}
		index = 0;
		for(Vector3f topVector : topVectors){
			addVertice(vertices, normals, settings, topVector, location);
			intTextures.add(index * factor);
			intTextures.add(settings.imageSize - settings.imageSize / 4);
			++index;
		}
		for(int i = 0; i < topVectors.length - 1; i++){
			indices.add(topIndex);
			indices.add(topStart + i);
			indices.add(topStart + i + 1);
		}
		for(int i = 0; i < botVectors.length - 1; i++){
			indices.add(bottomIndex);
			indices.add(bottomStart + i);
			indices.add(bottomStart + i + 1);
		}
		for(int i = 0; i < midVectors.length - 1; i++){
			indices.add(topStart + i);
			indices.add(midStart + i);
			indices.add(midStart + i + 1);
			indices.add(topStart + i);
			indices.add(topStart + i + 1);
			indices.add(midStart + i + 1);
			indices.add(bottomStart + i);
			indices.add(midStart + i);
			indices.add(midStart + i + 1);
			indices.add(bottomStart + i);
			indices.add(bottomStart + i + 1);
			indices.add(midStart + i + 1);
		}
	}
	
	private static void addNeckPart(ArrayList<Float> vertices, ArrayList<Float> textures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings){
		int indexB = vertices.size() / 3;
		float length = settings.neckLength / settings.neckParts;
		Vector3f[] bottomVectors = getCircleVectors(ZERO, UP, settings.neckRadius, 10);
		for(int i = 0; i < bottomVectors.length; i++)
			addVertice(vertices, textures, normals, bottomVectors[i], (float)i / (bottomVectors.length - 1), 0, Vector3f.sub(bottomVectors[i], ZERO, null).normalise(null));
		int indexT = vertices.size() / 3;
		Vector3f top = new Vector3f(0, length, 0);
		Vector3f[] topVectors = getCircleVectors(top, UP, settings.neckRadius, 10);
		for(int i = 0; i < topVectors.length; i++)
			addVertice(vertices, textures, normals, topVectors[i], (float)i / (topVectors.length - 1), 1, Vector3f.sub(topVectors[i], top, null).normalise(null));
		for(int i = 0; i < bottomVectors.length - 1; i++)
			bindFourAngle(indices, indexB + i, indexB + i + 1, indexT + i, indexT + i + 1);
	}
	
	private static void addTailPart(ArrayList<Float> vertices, ArrayList<Float> textures, ArrayList<Float> normals, ArrayList<Integer> indices, DinoSettings settings){
		int indexB = vertices.size() / 3;
		float length = settings.tailLength / settings.tailParts;
		Vector3f[] bottomVectors = getCircleVectors(ZERO, UP, settings.tailRadius, 10);
		for(int i = 0; i < bottomVectors.length; i++)
			addVertice(vertices, textures, normals, bottomVectors[i], (float)i / (bottomVectors.length - 1), 0, Vector3f.sub(bottomVectors[i], ZERO, null).normalise(null));
		int indexT = vertices.size() / 3;
		Vector3f top = new Vector3f(0, length, 0);
		Vector3f[] topVectors = getCircleVectors(top, UP, settings.tailRadius, 10);
		for(int i = 0; i < topVectors.length; i++)
			addVertice(vertices, textures, normals, topVectors[i], (float)i / (topVectors.length - 1), 1, Vector3f.sub(topVectors[i], top, null).normalise(null));
		for(int i = 0; i < bottomVectors.length - 1; i++)
			bindFourAngle(indices, indexB + i, indexB + i + 1, indexT + i, indexT + i + 1);
	}
	
	private static void addVertice(ArrayList<Float> vertices, ArrayList<Float> normals, DinoSettings settings, Vector3f vector, Vector3f centre){
		vertices.add(vector.x);
		vertices.add(vector.y);
		vertices.add(vector.z);
		Vector3f normal = Vector3f.sub(vector, centre, null).normalise(null);
		normals.add(normal.x);
		normals.add(normal.y);
		normals.add(normal.z);
	}
	
	private static void addVertice(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Integer> intTextures, DinoSettings settings, Vector3f vector, Vector3f centre, int u, int v){
		addVertice(vertices, normals, settings, vector, centre);
		intTextures.add(u);
		intTextures.add(v);
	}
	
	private static void addVertice(ArrayList<Float> vertices, ArrayList<Float> textures, ArrayList<Float> normals, Vector3f vertice, float u, float v, Vector3f normal){
		addVertice(vertices, vertice);
		addVertice(normals, normal);
		textures.add(u);
		textures.add(v);
	}
	
	private static BufferedImage createMushroomItemImage(MushroomSettings settings, MushroomColor color){
		BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		float stemWidth = settings.stemRadius / settings.topRadius;
		int stemRadius = (int) (stemWidth / 2 * image.getWidth());
		int midX = image.getWidth() / 2;
		int midY = (int) (settings.stemHeight / (settings.stemHeight + settings.topHeight) * image.getHeight());
		int minX = midX - stemRadius;
		int maxX = midX + stemRadius;
		Random random = new Random(color.seed);
		PatternAverage.paint(image, minX, 0, maxX, midY - 1, color.stemColor, color.maxDifference, random);
		float scaleFactor = settings.topHeight / settings.topRadius;
		float maxDifference = settings.topRadius * image.getHeight();
		int y = midY;
		while(y < image.getHeight()){
			int x = 0;
			while(x < image.getWidth()){
				int difX = midX - x;
				float difY = (y - midY) * scaleFactor;
				double difference = hypot(difX, difY);
				if(difference <= maxDifference)
					image.setRGB(x, y, PatternAverage.getDifColor(random, color.topColor, color.maxDifference).getRGB());
				++x;
			}
			++y;
		}
		//TODO create a new pattern for this
		return image;
	}
	
	private static int createMushroomItemTexture(MushroomSettings settings, MushroomColor color){
		return Loader.loadTexture(createMushroomItemImage(settings, color), true);
	}
	
	private static RawModel createTreeModel(TreeSettings settings){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> intTextures = new ArrayList<Integer>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		addTwig(vertices, intTextures, normals, indices, new Vector3f(), new Vector3f(0, 1, 0), settings.baseLength, settings.baseWidth, settings.baseTwigs, settings, new Random(settings.seed));
		RawModel model = loadToVAO(vertices, intTextures, normals, indices, settings.imageSize, new byte[]{ID_TREE_MODEL, settings.id}, new ColliderCilinderUp(0, 0, 0, settings.baseLength, settings.baseWidth / 2));
		treeModels.put(settings, model);
		return model;
	}
	
	private static RawModel createMushroomModel(MushroomSettings settings){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textures = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Vector3f botVector = ZERO;
		Vector3f[] botVectors = getCircleVectors(botVector, UP, settings.stemRadius, 10);
		float textureFactor = settings.stemRadius / settings.topRadius;
		int indexBot = vertices.size() / 3;
		for(int i = 0; i < botVectors.length; i++){
			Vector3f bot = botVectors[i];
			addVertice(vertices, bot);
			addVertice(normals, (Vector3f) Vector3f.sub(bot, botVector, null).normalise());
			textures.add((float)i / (botVectors.length - 1) * textureFactor);
			textures.add(0f);
		}
		Vector3f centre = new Vector3f(0, settings.stemHeight, 0);
		Vector3f[] midVectors = getCircleVectors(centre, UP, settings.stemRadius, 10);
		int indexMid = vertices.size() / 3;
		for(int i = 0; i < midVectors.length; i++){
			Vector3f mid = midVectors[i];
			addVertice(vertices, mid);
			addVertice(normals, (Vector3f) Vector3f.sub(mid, centre, null).normalise());
			textures.add((float)i / (midVectors.length - 1) * textureFactor);
			textures.add(0.5f);
		}
		Vector3f[] outVectors = getCircleVectors(centre, UP, settings.topRadius, 10);
		int indexOut = vertices.size() / 3;
		for(int i = 0; i < outVectors.length; i++){
			Vector3f out = outVectors[i];
			addVertice(vertices, out);
			addVertice(normals, (Vector3f) Vector3f.sub(out, centre, null).normalise());
			double prog = (double)i / (midVectors.length - 1) * 2 * PI;
			float u = (float) cos(prog) * 0.5f + 0.5f;
			float v = (float)(sin(prog) * 0.25 + 0.75);
			textures.add(u);
			textures.add(v);
		}
		int indexTop = vertices.size() / 3;
		addVertice(vertices, new Vector3f(0, settings.stemHeight + settings.topHeight, 0));
		addVertice(normals, UP);
		textures.add(0.5f);
		textures.add(0.75f);
		for(int i = 0; i < botVectors.length - 1; i++){
			bindFourAngle(indices, indexBot + i, indexBot + i + 1, indexMid + i, indexMid + i + 1);
		}
		for(int i = 0; i < outVectors.length / 2 - 1; i++){
			int i1 = i;
			int i2 = i + 1;
			int i3 = outVectors.length - i;
			int i4 = outVectors.length - i - 1;
			if(i1 == i3)
				bindTriAngle(indices, indexOut + i1, indexOut + i2, indexOut + i4);
			else if(i2 == i4)
				bindTriAngle(indices, indexOut + i1, indexOut + i2, indexOut + i3);
			else
				bindFourAngle(indices, indexOut + i1, indexOut + i2, indexOut + i4, indexOut + i3);
		}
		for(int i = 0; i < outVectors.length - 1; i++)
			bindTriAngle(indices, indexOut + i, indexOut + i + 1, indexTop);
		RawModel model = loadToVAO(vertices, textures, normals, indices, new byte[]{ID_MUSHROOM_MODEL, settings.id}, null);
		mushroomModels.put(settings, model);
		return model;
	}
	
	private static RawModel createFlowerModel(FlowerSettings settings){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textures = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Vector3f bottomBase = new Vector3f();
		Vector3f topBase = new Vector3f(0, 0.4f, 0);
		Vector3f up = new Vector3f(0, 1, 0);
		Vector3f direction = Maths.getRotationVector(-30, 0, 0);
		int indexBottom = vertices.size() / 3;
		Vector3f[] bottomVectors = getCircleVectors(bottomBase, up, settings.stemRadius, 10);
		for(int i = 0; i < bottomVectors.length; i++){
			addVertice(vertices, bottomVectors[i]);
			textures.add((float) i / (bottomVectors.length - 1));
			textures.add(0f);
			addVertice(normals, Vector3f.sub(bottomVectors[i], bottomBase, null).normalise(null));
		}
		int indexTop = vertices.size() / 3;
		Vector3f[] topVectors = getCircleVectors(topBase, direction, settings.stemRadius, 10);
		for(int i = 0; i < topVectors.length; i++){
			addVertice(vertices, topVectors[i]);
			textures.add((float) i / (topVectors.length - 1));
			textures.add(0.5f);
			addVertice(normals, Vector3f.sub(topVectors[i], topBase, null).normalise(null));
		}
		int indexFlower = vertices.size() / 3 - 1;
		Vector3f[] flowerVectors = getCircleVectors(topBase, direction, settings.flowerRadius, 10);
		for(int i = 0; i < flowerVectors.length; i++){
			addVertice(vertices, flowerVectors[i]);
			textures.add((float) i / (flowerVectors.length - 1));
			textures.add(0.7f);
			addVertice(normals, Vector3f.sub(flowerVectors[i], topBase, null).normalise(null));
		}
		int index = vertices.size() / 3 - 1;
		Vector3f[] outerVectors = getCircleVectors(topBase, direction, settings.outerRadius, 10);
		for(int i = 0; i < outerVectors.length; i++){
			addVertice(vertices, outerVectors[i]);
			textures.add((float) i / (outerVectors.length - 1));
			textures.add(1.0f);
			addVertice(normals, Vector3f.sub(outerVectors[i], topBase, null).normalise(null));
		}
		for(int i = 0; i < bottomVectors.length - 1; i++)
			bindFourAngle(indices, indexBottom + i, indexBottom + i + 1, indexTop + i, indexTop + i + 1);
		for(int i = 0; i < flowerVectors.length / 2; i++)
			bindFourAngle(indices, indexFlower + i, indexFlower + i + 1, indexFlower + flowerVectors.length - i, indexFlower + flowerVectors.length - i - 1);
		for(int i = 0; i < outerVectors.length; i += 3)
			bindFourAngle(indices, indexFlower + i, indexFlower + i + 1, index + i, index + i + 1);
		RawModel model = loadToVAO(vertices, textures, normals, indices, new byte[]{ID_FLOWER_MODEL, settings.id}, null);
		flowerModels.put(settings, model);
		return model;
	}
	
	private static RawModel createPlantModel(PlantSettings settings){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textures = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Random random = new Random(settings.seed);
		for(int i = 0; i < settings.parts; i++){
			float yaw = ((float)i / settings.parts) * 360;
			addPlantPart(vertices, textures, normals, indices, settings, yaw, -random.nextFloat() * (settings.maxPitch - settings.minPitch) - settings.minPitch, 10 + random.nextInt(30));
		}
		RawModel model = loadToVAO(vertices, textures, normals, indices, new byte[]{ID_PLANT_MODEL, settings.id}, null);
		plantModels.put(settings, model);
		return model;
	}
	
	private static RawModel createSimpleHouseModel(SimpleHouseSettings settings){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textures = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		ArrayList<Collider> colliders = new ArrayList<Collider>();
		float ww = settings.wallWidth;
		float sw = settings.width / 2;
		float sd = settings.depth / 2;
		float swh = settings.wallHeight;
		float srw = settings.roofWidth / 2;
		float srd = settings.roofDepth / 2;
		float srh = settings.roofHeight;
		float sdw = settings.doorWidth / 2;
		Vector3f vecNorth = (Vector3f) new Vector3f(0, srh, -srd).normalise();
		Vector3f vecEast = (Vector3f) new Vector3f(srw, srh, 0).normalise();
		Vector3f vecSouth = (Vector3f) new Vector3f(0, srh, srd).normalise();
		Vector3f vecWest = (Vector3f) new Vector3f(-srw, srh, 0).normalise();
		addTriangle(vertices, normals, textures, indices, -sw, swh, -sd, vecNorth.x, vecNorth.y, vecNorth.z, settings.locationSouthNorthRoof.getMinU(), settings.locationSouthNorthRoof.getMinV(), sw, swh, -sd, vecNorth.x, vecNorth.y, vecNorth.z, settings.locationSouthNorthRoof.getMinU(), settings.locationSouthNorthRoof.getMaxV(), 0, swh + srh, 0, vecNorth.x, vecNorth.y, vecNorth.z, settings.locationSouthNorthRoof.getMaxU(), settings.locationSouthNorthRoof.getMaxV());
		addTriangle(vertices, normals, textures, indices, sw, swh, -sd, vecEast.x, vecEast.y, vecEast.z, settings.locationEastWestRoof.getMinU(), settings.locationEastWestRoof.getMinV(), sw, swh, sd, vecEast.x, vecEast.y, vecEast.z, settings.locationEastWestRoof.getMaxU(), settings.locationEastWestRoof.getMinV(), 0, swh + srh, 0, vecEast.x, vecEast.y, vecEast.z, settings.locationEastWestRoof.getMaxU(), settings.locationEastWestRoof.getMaxV());
		addTriangle(vertices, normals, textures, indices, -sw, swh, sd, vecSouth.x, vecSouth.y, vecSouth.z, settings.locationSouthNorthRoof.getMinU(), settings.locationSouthNorthRoof.getMinV(), sw, swh, sd, vecSouth.x, vecSouth.y, vecSouth.z, settings.locationSouthNorthRoof.getMaxU(), settings.locationSouthNorthRoof.getMinV(), 0, swh + srh, 0, vecSouth.x, vecSouth.y, vecSouth.z, settings.locationSouthNorthRoof.getMaxU(), settings.locationSouthNorthRoof.getMaxV());
		addTriangle(vertices, normals, textures, indices, -sw, swh, -sd, vecWest.x, vecWest.y, vecWest.z, settings.locationEastWestRoof.getMinU(), settings.locationEastWestRoof.getMinV(), -sw, swh, sd, vecWest.x, vecWest.y, vecWest.z, settings.locationEastWestRoof.getMinU(), settings.locationEastWestRoof.getMaxV(), 0, swh + srh, 0, vecWest.x, vecWest.y, vecWest.z, settings.locationEastWestRoof.getMaxU(), settings.locationEastWestRoof.getMaxV());
		colliders.add(new ColliderBox(-sw, swh, -sd, sw, swh + srh, sd));
		addRectangle(vertices, normals, textures, indices, -sw, 0, -sd, settings.locationFloor.getMinU(), settings.locationFloor.getMinV(), sw, 0, sd, settings.locationFloor.getMaxU(), settings.locationFloor.getMaxV(), Facing.UP);
		colliders.add(new ColliderBox(-sw, -0.21f, -sd, sw, -0.11f, sd));
		addRectangle(vertices, normals, textures, indices, -srw, swh, -srd, settings.locationCeiling.getMinU(), settings.locationCeiling.getMinV(), srw, swh, srd, settings.locationCeiling.getMaxU(), settings.locationCeiling.getMaxV(), Facing.DOWN);
		//TODO finish all the uv coords
		if(settings.doorSide == Facing.NORTH){
			float minU = settings.locationNorthOutWall.getMinU();
			float maxU = settings.locationNorthOutWall.getMaxU();
			float minV = settings.locationNorthOutWall.getMinV();
			float maxV = settings.locationNorthOutWall.getMaxV();
			float deltaU = maxU - minU;
			float midU = (maxU + minU) / 2;
			float minDoorU = midU - (settings.doorWidth / settings.width) * deltaU * 0.5f;
			float maxDoorU = midU + (settings.doorWidth / settings.width) * deltaU * 0.5f;
			addRectangle(vertices, normals, textures, indices, -sw, 0, -sd, minU, minV, -sdw, swh, -sd, minDoorU, maxV, Facing.NORTH);
			addRectangle(vertices, normals, textures, indices, sdw, 0, -sd, maxDoorU, minV, sw, swh, -sd, maxU, maxV, Facing.NORTH);
			addRectangle(vertices, normals, textures, indices, -sdw, settings.doorHeight, -sd, minDoorU, minV + (maxV - minV) * (settings.doorHeight / settings.wallHeight), sdw, swh, -sd, maxDoorU, maxV, Facing.NORTH);
			minU = settings.locationNorthInWall.getMinU();
			maxU = settings.locationNorthInWall.getMaxU();
			minV = settings.locationNorthInWall.getMinV();
			maxV = settings.locationNorthInWall.getMaxV();
			deltaU = maxU - minU;
			midU = (maxU + minU) / 2;
			minDoorU = midU - (settings.doorWidth / (settings.width - 2 * ww)) * deltaU * 0.5f;
			maxDoorU = midU + (settings.doorWidth / (settings.width - 2 * ww)) * deltaU * 0.5f;
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, -sd + ww, minU, minV, -sdw, swh, -sd + ww, minDoorU, maxV, Facing.SOUTH);
			addRectangle(vertices, normals, textures, indices, sdw, 0, -sd + ww, maxDoorU, minV, sw - ww, swh, -sd + ww, maxU, maxV, Facing.SOUTH);
			addRectangle(vertices, normals, textures, indices, -sdw, settings.doorHeight, -sd + ww, minDoorU, minV + (maxV - minV) * (settings.doorHeight / settings.wallHeight), sdw, swh, -sd + ww, maxDoorU, maxV, Facing.SOUTH);
			colliders.add(new ColliderBox(-sw, 0, -sd, -sdw, swh, -sd + ww));
			colliders.add(new ColliderBox(-sdw, settings.doorHeight, -sd, sdw, swh, -sd + ww));
			colliders.add(new ColliderBox(sdw, 0, -sd, sw, swh, -sd + ww));
		}
		else {
			addRectangle(vertices, normals, textures, indices, -sw, 0, -sd, settings.locationNorthOutWall.getMinU(), settings.locationNorthOutWall.getMinV(), sw, swh, -sd, settings.locationNorthOutWall.getMaxU(), settings.locationNorthOutWall.getMaxV(), Facing.NORTH);
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, -sd + ww, settings.locationNorthInWall.getMinU(), settings.locationNorthInWall.getMinV(), sw - ww, swh, -sd + ww, settings.locationNorthInWall.getMaxU(), settings.locationNorthInWall.getMaxV(), Facing.SOUTH);
			colliders.add(new ColliderBox(-sw, 0, -sd, sw, swh, -sd + ww));
		}
		if(settings.doorSide == Facing.EAST){
			addRectangle(vertices, normals, textures, indices, sw, 0, -sd, 0, 0, sw, swh, -sdw, 0, 0, Facing.EAST);
			addRectangle(vertices, normals, textures, indices, sw, 0, sdw, 0, 0, sw, swh, sd, 0, 0, Facing.EAST);
			addRectangle(vertices, normals, textures, indices, sw, 0, -sdw, 0, 0, sw, swh, sdw, 0, 0, Facing.EAST);
			addRectangle(vertices, normals, textures, indices, sw - ww, 0, -sd + ww, 0, 0, sw - ww, swh, -sdw, 0, 0, Facing.WEST);
			addRectangle(vertices, normals, textures, indices, sw - ww, 0, sdw, 0, 0, sw - ww, swh, sd - ww, 0, 0, Facing.WEST);
			addRectangle(vertices, normals, textures, indices, sw - ww, 0, -sdw, 0, 0, sw - ww, swh, sdw, 0, 0, Facing.WEST);
			colliders.add(new ColliderBox(sw - ww, 0, -sd, sw, swh, -sdw));
			colliders.add(new ColliderBox(sw - ww, settings.doorHeight, -sdw, sw-sw + ww, swh, sdw));
			colliders.add(new ColliderBox(sw - ww, 0, sdw, sw, swh, sd));
		}
		else {
			addRectangle(vertices, normals, textures, indices, sw, 0, -sd, settings.locationEastOutWall.getMinU(), settings.locationEastOutWall.getMinV(), sw, swh, sd, settings.locationEastOutWall.getMaxU(), settings.locationEastOutWall.getMaxV(), Facing.EAST);
			addRectangle(vertices, normals, textures, indices, sw - ww, 0, -sd + ww, settings.locationEastInWall.getMinU(), settings.locationEastInWall.getMinV(), sw - ww, swh, sd - ww, settings.locationEastInWall.getMaxU(), settings.locationEastInWall.getMaxV(), Facing.WEST);
			colliders.add(new ColliderBox(sw - ww, 0, -sd, sw, swh, sd));
		}
		if(settings.doorSide == Facing.SOUTH){
			addRectangle(vertices, normals, textures, indices, -sw, 0, sd, 0, 0, -sdw, swh, sd, 0, 0, Facing.SOUTH);
			addRectangle(vertices, normals, textures, indices, sdw, 0, sd, 0, 0, sw, swh, sd, 0, 0, Facing.SOUTH);
			addRectangle(vertices, normals, textures, indices, -sdw, swh, sd, 0, 0, sdw, swh, sd, 0, 0, Facing.SOUTH);
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, sd - ww, 0, 0, -sdw, swh, sd - ww, 0, 0, Facing.NORTH);
			addRectangle(vertices, normals, textures, indices, sdw, 0, sd - ww, 0, 0, sw - ww, swh, sd - ww, 0, 0, Facing.NORTH);
			addRectangle(vertices, normals, textures, indices, -sdw, swh, sd - ww, 0, 0, sdw, swh, sd - ww, 0, 0, Facing.NORTH);
			colliders.add(new ColliderBox(-sw, 0, sd - ww, -sdw, swh, sd));
			colliders.add(new ColliderBox(-sdw, settings.doorHeight, sd - ww, sdw, swh, sd));
			colliders.add(new ColliderBox(sdw, 0, sd - ww, sw, swh, sd));
		}
		else {
			addRectangle(vertices, normals, textures, indices, -sw, 0, sd, settings.locationSouthOutWall.getMinU(), settings.locationSouthOutWall.getMinV(), sw, swh, sd, settings.locationSouthOutWall.getMaxU(), settings.locationSouthOutWall.getMaxV(), Facing.SOUTH);
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, sd - ww, settings.locationSouthInWall.getMinU(), settings.locationSouthInWall.getMinV(), sw - ww, swh, sd - ww, settings.locationSouthInWall.getMaxU(), settings.locationSouthInWall.getMaxV(), Facing.NORTH);
			colliders.add(new ColliderBox(-sw, 0, sd - ww, sw, swh, sd));
		}
		if(settings.doorSide == Facing.WEST){
			addRectangle(vertices, normals, textures, indices, -sw, 0, -sd, 0, 0, -sw, swh, -sdw, 0, 0, Facing.WEST);
			addRectangle(vertices, normals, textures, indices, -sw, 0, sdw, 0, 0, -sw, swh, sd, 0, 0, Facing.WEST);
			addRectangle(vertices, normals, textures, indices, -sw, 0, -sdw, 0, 0, -sw, swh, sdw, 0, 0, Facing.WEST);
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, -sd + ww, 0, 0, -sw + ww, swh, -sdw, 0, 0, Facing.EAST);
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, sdw, 0, 0, -sw + ww, swh, sd - ww, 0, 0, Facing.EAST);
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, -sdw, 0, 0, -sw + ww, swh, sdw, 0, 0, Facing.EAST);
			colliders.add(new ColliderBox(-sw, 0, -sd, -sw + ww, swh, -sdw));
			colliders.add(new ColliderBox(-sw, settings.doorHeight, -sdw, -sw + ww, swh, sdw));
			colliders.add(new ColliderBox(-sw, 0, sdw, -sw + ww, swh, sd));
		}
		else {
			addRectangle(vertices, normals, textures, indices, -sw, 0, -sd, settings.locationWestOutWall.getMinU(), settings.locationWestOutWall.getMinV(), -sw, swh, sd, settings.locationWestOutWall.getMaxU(), settings.locationWestOutWall.getMaxV(), Facing.WEST);
			addRectangle(vertices, normals, textures, indices, -sw + ww, 0, -sd + ww, settings.locationWestInWall.getMinU(), settings.locationWestInWall.getMinV(), -sw + ww, swh, sd - ww, settings.locationEastInWall.getMaxU(), settings.locationEastInWall.getMaxV(), Facing.EAST);
			colliders.add(new ColliderBox(-sw, 0, -sd, -sw + ww, swh, sd));
		}
		RawModel model = loadToVAO(vertices, textures, normals, indices, new byte[]{ID_SIMPLE_HOUSE_MODEL, settings.id}, new ColliderList(colliders));
		simpleHouses.put(settings, model);
		return model;
	}
	
	private static RawModel createKnifeModel(KnifeForm knife){
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textures = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int handleFront = 0;
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, knife.holdHeight / 2, knife.holdWidth / 2), 0f, 0.25f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, -knife.holdHeight / 2, knife.holdWidth / 2), 0f, 0f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.holdHeight / 2, knife.holdWidth / 2), 0.2f, 0f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.holdHeight / 2, knife.holdWidth / 2), 0.2f, 0.25f, SOUTH);
		int handleUp = 4;
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, knife.holdHeight / 2, -knife.holdWidth / 2), 0f, 0.5f, UP);
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, knife.holdHeight / 2, knife.holdWidth / 2), 0f, 0.25f, UP);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.holdHeight / 2, knife.holdWidth / 2), 0.2f, 0.25f, UP);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 0.5f, UP);
		int handleBack = 8;
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, knife.holdHeight / 2, -knife.holdWidth / 2), 0f, 0.75f, NORTH);
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, -knife.holdHeight / 2, -knife.holdWidth / 2), 0f, 0.5f, NORTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 0.5f, NORTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 0.75f, NORTH);
		int handleDown = 12;
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, -knife.holdHeight / 2, -knife.holdWidth / 2), 0f, 1f, DOWN);
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, -knife.holdHeight / 2, knife.holdWidth / 2), 0f, 0.75f, DOWN);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.holdHeight / 2, knife.holdWidth / 2), 0.2f, 0.75f, DOWN);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 1f, DOWN);
		int handleLeft = 16;
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 0.1f, WEST);
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, -knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 0f, WEST);
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, -knife.holdHeight / 2, knife.holdWidth / 2), 0.3f, 0f, WEST);
		addVertice(vertices, textures, normals, new Vector3f(-knife.holdLength / 2, knife.holdHeight / 2, knife.holdWidth / 2), 0.3f, 0.1f, WEST);
		int handleRight = 20;
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 0.2f, EAST);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.holdHeight / 2, -knife.holdWidth / 2), 0.2f, 0.1f, EAST);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.holdHeight / 2, knife.holdWidth / 2), 0.3f, 0.1f, EAST);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.holdHeight / 2, knife.holdWidth / 2), 0.3f, 0.2f, EAST);
		int knifeFront = 24;
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.baseKnifeHeight / 2, knife.knifeWidth / 2), 0.3f, 0.4f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.baseKnifeHeight / 2, knife.knifeWidth / 2), 0.3f, 0f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2 + knife.knifeLength * knife.maxHeightPoint, -knife.baseKnifeHeight / 2 - knife.maxExtraKnifeHeight, knife.knifeWidth / 2), 0.3f + 0.7f * knife.maxHeightPoint, 0f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2 + knife.knifeLength, knife.baseKnifeHeight / 2, 0), 1f, 0.4f, EAST);
		int knifeBack = 28;
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, knife.baseKnifeHeight / 2, -knife.knifeWidth / 2), 0.3f, 0.4f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2, -knife.baseKnifeHeight / 2, -knife.knifeWidth / 2), 0.3f, 0f, SOUTH);
		addVertice(vertices, textures, normals, new Vector3f(knife.holdLength / 2 + knife.knifeLength * knife.maxHeightPoint, -knife.baseKnifeHeight / 2 - knife.maxExtraKnifeHeight, -knife.knifeWidth / 2), 0.3f + 0.7f * knife.maxHeightPoint, 0f, SOUTH);
		bindFourAngle(indices, handleFront, handleFront + 1, handleFront + 3, handleFront + 2);
		bindFourAngle(indices, handleUp, handleUp + 1, handleUp + 3, handleUp + 2);
		bindFourAngle(indices, handleBack, handleBack + 1, handleBack + 3, handleBack + 2);
		bindFourAngle(indices, handleDown, handleDown + 1, handleDown + 3, handleDown + 2);
		bindFourAngle(indices, handleLeft, handleLeft + 1, handleLeft + 3, handleLeft + 2);
		bindFourAngle(indices, handleRight, handleRight + 1, handleRight + 3, handleRight + 2);
		bindFourAngle(indices, knifeFront, knifeFront + 1, knifeFront + 3, knifeFront + 2);
		bindFourAngle(indices, knifeBack, knifeBack+ 1, knifeFront + 3, knifeBack + 2);
		bindTriAngle(indices, knifeFront, knifeBack, knifeFront + 3);
		bindTriAngle(indices, knifeFront + 2, knifeBack + 2, knifeFront + 3);
		bindFourAngle(indices, knifeFront + 2, knifeFront + 1, knifeBack + 2, knifeBack + 1);
		return loadToVAO(vertices, textures, normals, indices, null, null);
	}
	
	private static ModelTexture createTreeTexture(Color wood, Color leaves, float maxDifference, long seed, byte treeID){
		Random random = new Random(seed);
		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		PatternAverage.paint(image, 0, 0, image.getWidth() / 2 - 1, image.getHeight() - 1, wood, maxDifference, random);
		PatternAverage.paint(image, image.getWidth() / 2, 0, image.getWidth() - 1, image.getHeight() - 1, leaves, maxDifference, random);
		ModelTexture texture = new ModelTexture(Loader.loadTexture(image, false), Material.ORGANIC, new byte[]{ID_TREE_TEXTURE, treeID});
		treeTextures.put(new TreeColor(wood, leaves, maxDifference, seed), texture);
		return texture;
	}
	
	private static BufferedImage createFlowerImage(FlowerColor flower){
		Random random = new Random(flower.seed);
		BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		PatternAverage.paint(image, 0, 0, image.getWidth() - 1, image.getHeight() / 2 - 1, flower.stempColor, flower.maxDifference, random);
		PatternAverage.paint(image, 0, image.getHeight() / 2, image.getWidth() - 1, image.getHeight() - 1, flower.flowerColor, flower.maxDifference, random);
		return image;
	}
	
	private static BufferedImage createMushroomImage(MushroomColor mush){
		BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		Random random = new Random(mush.seed);
		PatternAverage.paint(image, 0, 0, image.getWidth() - 1, image.getHeight() / 2 - 1, mush.stemColor, mush.maxDifference, random);
		PatternAverage.paint(image, 0, image.getHeight() / 2, image.getWidth() - 1, image.getHeight() - 1, mush.topColor, mush.maxDifference, random);
		PatternRandomSpots.paint(image, 0, image.getHeight() / 2, image.getWidth() - 1, image.getHeight() - 1, mush.spotColor, mush.maxDifference, mush.spotRadius, mush.spots, random);
		return image;
	}
	
	private static BufferedImage createSimpleHouseImage(SimpleHouseTexture texture){
		SimpleHouseSettings s = texture.settings;
		BufferedImage image = new BufferedImage(texture.settings.locationCeiling.getImageWidth(), texture.settings.locationCeiling.getImageHeight(), BufferedImage.TYPE_INT_RGB);
		System.out.println("ceiling = " + s.locationCeiling + " and floor = " + s.locationFloor);
		texture.floor.paint(image, s.locationFloor);
		texture.ceiling.paint(image, s.locationCeiling);
		texture.northOutWall.paint(image, s.locationNorthOutWall);
		texture.eastOutWall.paint(image, s.locationEastOutWall);
		texture.southOutWall.paint(image, s.locationSouthOutWall);
		texture.westOutWall.paint(image, s.locationWestOutWall);
		texture.northInWall.paint(image, s.locationNorthInWall);
		texture.eastInWall.paint(image, s.locationEastInWall);
		texture.southInWall.paint(image, s.locationSouthInWall);
		texture.westInWall.paint(image, s.locationWestInWall);
		texture.northRoof.paint(image, s.locationSouthNorthRoof);
		texture.westRoof.paint(image, s.locationEastWestRoof);
		return image;
	}
	
	private static BufferedImage createDinoImage(Color skin, float maxDifference, long seed){
		Random random = new Random(seed);
		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		PatternAverage.paint(image, 0, 0, image.getWidth() - 1, image.getHeight() - 1, skin, maxDifference, random);
		return image;
	}
	
	private static BufferedImage createDinoFootImage(Color skin, Color nails, float maxDifference, long seed){
		Random random = new Random(seed);
		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		PatternAverage.paint(image, 0, 0, (int) (0.85 * image.getWidth()) - 1, image.getHeight() - 1, skin, maxDifference, random);
		PatternFill.paint(image, (int) (0.85 * image.getWidth()), 0, image.getWidth() - 1, image.getHeight() - 1, nails);
		return image;
	}
	
	private static BufferedImage createDinoHeadImage(Color skin, Color eyes, float maxDifference, long seed){
		BufferedImage image = createDinoImage(skin, maxDifference, seed);
		addDinoEyes(image, eyes);
		addDinoMouth(image, new Color(180, 0, 0), new Color(30, 0, 0), new Color(150, 150, 150));
		return image;
	}
	
	private static void addDinoEyes(BufferedImage image, Color eyes){
		int minEyeXL = (int) (image.getWidth() * 0.58f);
		int maxEyeXL = (int) (image.getWidth() * 0.63f);
		int minEyeXR = (int) (image.getWidth() * 0.67f);
		int maxEyeXR = (int) (image.getWidth() * 0.72f);
		int deltaX = maxEyeXL - minEyeXL;
		int minEyeY = (int) (image.getHeight() * 0.4f);
		int maxEyeY = (int) (image.getHeight() * 0.6f);
		int deltaY = maxEyeY - minEyeY;
		int whiteRGB = Color.WHITE.getRGB();
		int blackRGB = Color.BLACK.getRGB();
		int irisRGB = eyes.getRGB();
		int x = minEyeXL;
		while(x <= maxEyeXL){
			int y = minEyeY;
			float difX = Math.abs((x - minEyeXL - deltaX / 2));
			float disX = difX / deltaX;
			while(y <= maxEyeY){
				float difY = Math.abs((y - minEyeY - deltaY / 2));
				float disY = difY / deltaY;
				float dis = disX * disX + disY * disY;
				if(dis <= 0.05f)
					image.setRGB(x, y, blackRGB);
				else if(dis <= 0.1f)
					image.setRGB(x, y, irisRGB);
				else if(dis <= 0.25f)
					image.setRGB(x, y, whiteRGB);
				++y;
			}
			++x;
		}
		x = minEyeXR;
		while(x <= maxEyeXR){
			int y = minEyeY;
			float difX = Math.abs((x - minEyeXR - deltaX / 2));
			float disX = difX / deltaX;
			while(y <= maxEyeY){
				float difY = Math.abs((y - minEyeY - deltaY / 2));
				float disY = difY / deltaY;
				float dis = disX * disX + disY * disY;
				if(dis <= 0.05f)
					image.setRGB(x, y, blackRGB);
				else if(dis <= 0.1f)
					image.setRGB(x, y, irisRGB);
				else if(dis <= 0.25f)
					image.setRGB(x, y, whiteRGB);
				++y;
			}
			++x;
		}
	}
	
	private static void addDinoMouth(BufferedImage image, Color lips, Color mouth, Color teeth){
		int minLipX = (int) (0.55f * image.getWidth());
		int maxLipX = (int) (0.75f * image.getWidth());
		int minLipY = (int) (0.25f * image.getHeight());
		int maxLipY = (int) (0.35f * image.getHeight());
		int deltaX = maxLipX - minLipX;
		int deltaY = maxLipY - minLipY;
		int lipRGB = lips.getRGB();
		int mouthRGB = mouth.getRGB();
		float lipWidth = 0.2f;
		int x = minLipX;
		while(x <= maxLipX){
			float difX = Math.abs((x - minLipX - deltaX / 2));
			float disX = difX / deltaX * 2;
			int minY = (int) (minLipY + disX * disX * 0.1f * image.getHeight());
			int y = minY;
			int maxY = (int) (maxLipY + disX * deltaY * 0.002f * image.getHeight());
			while(y <= maxY){
				if(y <= minY + deltaY * lipWidth || y >= maxY - deltaY * lipWidth)
					image.setRGB(x, y, lipRGB);
				else
					image.setRGB(x, y, mouthRGB);
				++y;
			}
			++x;
		}
	}
	
	private static ModelDinoTexture createDinoTexture(Color skin, Color nails, float maxDifference, long seed){
		ModelDinoTexture texture = new ModelDinoTexture(Loader.loadTexture(createDinoImage(skin, maxDifference, seed), false), skin, nails, maxDifference, seed);
		dinoTextures.put(new SkinColor(skin, nails, maxDifference, seed), texture);
		return texture;
	}
	
	private static ModelTexture createDinoFootTexture(Color skin, Color nails, float maxDifference, long seed){
		ModelTexture texture = new ModelDinoTexture(Loader.loadTexture(createDinoFootImage(skin, nails, maxDifference, seed), false), skin, nails, maxDifference, seed);
		footTextures.put(new FootColor(new SkinColor(skin, nails, maxDifference, seed), nails), texture);
		return texture;
	}
	
	private static ModelTexture createDinoHeadTexture(Color skin, Color nails, Color eyes, float maxDifference, long seed){
		ModelTexture texture = new ModelDinoTexture(Loader.loadTexture(createDinoHeadImage(skin, eyes, maxDifference, seed), false), skin, null, maxDifference, seed);
		headTextures.put(new HeadColor(new SkinColor(skin, nails, maxDifference, seed), eyes), texture);
		return texture;
	}
	
	private static ModelTexture createFlowerTexture(FlowerColor color, byte flowerID){
		ModelTexture texture = new ModelTexture(Loader.loadTexture(createFlowerImage(color), false), Material.ORGANIC, new byte[]{ID_FLOWER_TEXTURE, flowerID});
		flowerTextures.put(color, texture);
		return texture;
	}
	
	private static ModelTexture createMushroomTexture(MushroomColor color, byte mushroomID){
		ModelTexture texture = new ModelTexture(Loader.loadTexture(createMushroomImage(color), false), Material.ORGANIC, new byte[]{ID_MUSHROOM_TEXTURE, mushroomID});
		mushroomTextures.put(color, texture);
		return texture;
	}
	
	private static ModelTexture createSimpleHouseTexture(SimpleHouseTexture texture){
		ModelTexture tex = new ModelTexture(Loader.loadTexture(createSimpleHouseImage(texture), false), Material.ORGANIC, new byte[]{ID_SIMPLE_HOUSE_TEXTURE, texture.id});
		simpleHouseTextures.put(texture, tex);
		return tex;
	}
	
	private static ModelTexture createFilledTexture(Color color, Material material){
		ModelTexture texture = new ModelTexture(Loader.loadTexture(createFilledImage(color), false), material, new byte[]{ID_FILLED_TEXTURE, (byte) (color.getRed() - 128), (byte) (color.getGreen() - 128), (byte) (color.getBlue() - 128), (byte) material.ordinal()});
		filledTextures.put(color, texture);
		return texture;
	}
	
	private static BufferedImage createFilledImage(Color color){
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, color.getRGB());
		return image;
	}
	
	private static BufferedImage createTextImage(String text, Color color){
		BufferedImage image = new BufferedImage(256, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setFont(new Font("TimesRoman", 0, 40));
		Rectangle2D bounds = g2.getFontMetrics().getStringBounds(text, g2);
		double preferredWidth = image.getWidth() * 0.9;
		double preferredHeight = image.getHeight() * 0.9;
		double factor = min(preferredWidth / bounds.getWidth(), preferredHeight / bounds.getHeight());
		g2.setColor(color);
		g2.setFont(new Font("TimesRoman", 0, (int) (40 * factor)));
		Rectangle2D newBounds = g2.getFontMetrics().getStringBounds(text, g2);
		g2.drawString(text, (int) ((image.getWidth() - newBounds.getWidth()) / 2), (int) ((image.getHeight() - newBounds.getCenterY()) / 2));
		g2.setColor(Color.BLACK);
		g2.drawLine(0, 0, image.getWidth() - 1, 0);
		g2.drawLine(0, 0, 0, image.getHeight() - 1);
		g2.drawLine(0, image.getHeight() - 1, image.getWidth() - 1, image.getHeight() - 1);
		g2.drawLine(image.getWidth() - 1, 0, image.getWidth() - 1, image.getHeight() - 1);
		return image;
	}
	
	private static BufferedImage createTextLineImage(String text){
		BufferedImage image = new BufferedImage(1024, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setFont(TEXT_LINE_FONT);
		g.setColor(Color.BLACK);
		g.drawString(text, 20, 40);
		g.dispose();
		System.out.println("Create new text line image with text " + text);
		return image;
	}
	
	private static ModelTexture createTextTexture(Color color, String text){
		ModelTexture texture = new ModelTexture(Loader.loadTexture(createTextImage(text, color), true), Material.GUI, null);
		textTextures.put(new ColorText(color, text), texture);
		return texture;
	}
	
	private static ModelTexture createTextLineTexture(String text){
		ModelTexture texture = new ModelTexture(Loader.loadTexture(createTextLineImage(text), true), Material.GUI, null);
		textLineTextures.put(text, texture);
		return texture;
	}
	
	private static BufferedImage createBorderImage(Color color){
		BufferedImage image = new BufferedImage(256, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(color);
		g2.drawLine(0, 0, image.getWidth() - 1, 0);
		g2.drawLine(0, image.getHeight() - 1, image.getWidth() - 1, image.getHeight() - 1);
		g2.drawLine(0, 0, 0, image.getHeight() - 1);
		g2.drawLine(image.getWidth() - 1, 0, image.getWidth() - 1, image.getHeight() - 1);
		g2.dispose();
		return image;
	}
	
	private static ModelTexture createBorderTexture(Color color){
		ModelTexture texture = new ModelTexture(Loader.loadTexture(createBorderImage(color), true), Material.GUI, null);
		borderTextures.put(color, texture);
		return texture;
	}
	
	private static ModelTexture loadGround(long seed){
		BufferedImage ground = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		Random random = new Random(seed);
		int x = 0;
		while(x < ground.getWidth()){
			int y = 0;
			while(y < ground.getHeight()){
				ground.setRGB(x, y, new Color(0, 50 + random.nextInt(100), random.nextInt(40)).getRGB());
				++y;
			}
			++x;
		}
		ModelTexture texture = new ModelTexture(Loader.loadTexture(ground, false), Material.ORGANIC, new byte[]{ID_GROUND_TEXTURE});
		groundTextures.put(seed, texture);
		return texture;
	}
	
	private static void addPlantPart(ArrayList<Float> vertices, ArrayList<Float> textures, ArrayList<Float> normals, ArrayList<Integer> indices, PlantSettings settings, float yaw, float pitch, float secondPitch){
		Vector3f start = new Vector3f();
		Vector3f mid = (Vector3f) Maths.getRotationVector(pitch, yaw, 0).scale(settings.length / 2);
		Vector3f rotate = Maths.getRotationVector(pitch, yaw - 90, 0);
		Vector3f normal = Maths.getRotationVector(pitch - 90, yaw, 0);
		Vector3f end = Vector3f.add(mid, (Vector3f) Maths.getRotationVector(secondPitch, yaw, 0).scale(settings.length / 2), null);
		Vector3f left = Vector3f.add(mid, (Vector3f) rotate.scale(settings.width / 2), null);
		Vector3f right = Vector3f.add(mid, (Vector3f) rotate.scale(-settings.width / 2), null);
		int index = vertices.size() / 3;
		addVertice(vertices, start);
		textures.add(0.5f);
		textures.add(0f);
		addVertice(vertices, left);
		textures.add(0f);
		textures.add(0.5f);
		addVertice(vertices, right);
		textures.add(1f);
		textures.add(0.5f);
		addVertice(vertices, end);
		textures.add(0.5f);
		textures.add(1f);
		addVertice(normals, normal);
		addVertice(normals, normal);
		addVertice(normals, normal);
		addVertice(normals, normal);
		bindTriAngle(indices, index + 0, index + 1, index + 2);
		bindTriAngle(indices, index + 1, index + 2, index + 3);
	}
	
	private static void addTwig(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, Vector3f baseVector, Vector3f direction, float distance, float width, float twigs, TreeSettings settings, Random random){
		Vector3f[] goals = new Vector3f[settings.twigParts];
		int i = 1;
		Vector3f[] directions = new Vector3f[settings.twigParts];
		directions[0] = direction;
		while(i < directions.length){
			directions[i] = (Vector3f) new Vector3f(-1 + random.nextFloat() * 2, -1 + random.nextFloat() * 2, -1 + random.nextFloat() * 2).normalise();
			if(directions[i].length() == 0)
				directions[i].y = 1;
			Vector3f prevDirection = directions[i - 1];
			double distanceDir = Maths.getDistance(directions[i], prevDirection);
			double distanceNeg = Maths.getDistance(directions[i], prevDirection.negate(null));
			double distanceOr = Maths.getDistance(directions[i], new Vector3f());
			if(distanceDir > distanceNeg)
				directions[i].negate();
			float maxBow = min(settings.maxBow / width, 0.5f);
			if(distanceDir > distanceOr * maxBow){
				float bow = 1 - maxBow;
				directions[i].x += (prevDirection.x - directions[i].x) * bow;
				directions[i].y += (prevDirection.y - directions[i].y) * bow;
				directions[i].z += (prevDirection.z - directions[i].z) * bow;
				directions[i].normalise();
			}
			++i;
		}
		i = 0;
		while(i < goals.length){
			if(i == 0)
				goals[i] = Vector3f.add(baseVector, (Vector3f) new Vector3f(directions[i]).scale(distance / settings.twigParts), null);
			else
				goals[i] = Vector3f.add(goals[i - 1], (Vector3f) new Vector3f(directions[i]).scale(distance / settings.twigParts), null);
			++i;
		}
		Vector3f[] baseVectors = getCircleVectors(baseVector, direction, width);
		Vector3f[][] goalVectors = new Vector3f[settings.twigParts][];
		i = 0;
		while(i < goalVectors.length){
			goalVectors[i] = getCircleVectors(goals[i], directions[i], width);
			++i;
		}
		int index = vertices.size() / 3;
		float factorX = settings.imageSize / 2f / baseVectors.length;
		i = 0;
		while(i < baseVectors.length){
			vertices.add(baseVectors[i].x);
			vertices.add(baseVectors[i].y);
			vertices.add(baseVectors[i].z);
			intTextures.add((int) (i * factorX));
			intTextures.add(0);
			Vector3f normal = Vector3f.sub(baseVectors[i], baseVector, null).normalise(null);
			normals.add(normal.x);
			normals.add(normal.y);
			normals.add(normal.z);
			++i;
		}
		float factorY = settings.imageSize / goalVectors.length;
		i = 0;
		while(i < goalVectors.length){
			int i1 = 0;
			while(i1 < goalVectors[i].length){
				vertices.add(goalVectors[i][i1].x);
				vertices.add(goalVectors[i][i1].y);
				vertices.add(goalVectors[i][i1].z);
				intTextures.add((int) (i1 * factorX));
				intTextures.add((int) ((i + 1) * factorY));
				Vector3f normal = Vector3f.sub(goalVectors[i][i1], goals[i], null).normalise(null);
				normals.add(normal.x);
				normals.add(normal.y);
				normals.add(normal.z);
				++i1;
			}
			++i;
		}
		i = 0;
		while(i < goalVectors.length){
			int i1 = 0;
			while(i1 < baseVectors.length - 1){
				int extra = i * baseVectors.length;
				indices.add(index + extra + i1);
				indices.add(index + extra + i1 + 1);
				indices.add(index + extra + i1 + baseVectors.length + 1);
				indices.add(index + extra + i1);
				indices.add(index + extra + i1 + baseVectors.length);
				indices.add(index + extra + i1 + baseVectors.length + 1);
				++i1;
			}
			++i;
		}
		if(width > 0.1f){
			int max = (int) twigs;
			float partDelta = distance / settings.twigParts;
			i = 0;
			while(i < max){
				int vectorIndex = random.nextInt(baseVectors.length);
				float lineDistance = settings.minTwigHeight + random.nextFloat() * (1 - settings.minTwigHeight);
				int goalIndex = (int) (lineDistance * distance / partDelta);
				lineDistance = random.nextFloat();
				Vector3f dir1;
				if(goalIndex == 0)
					dir1 = (Vector3f) Vector3f.sub(goalVectors[goalIndex][vectorIndex], baseVectors[vectorIndex], null).scale(lineDistance);
				else
					dir1 = (Vector3f) Vector3f.sub(goalVectors[goalIndex][vectorIndex], goalVectors[goalIndex - 1][vectorIndex], null).scale(lineDistance);
				Vector3f dir2;
				Vector3f dir3;
				if(goalIndex == 0)
					dir3 = (Vector3f) Vector3f.sub(goals[goalIndex], baseVector, null).scale(lineDistance);
				else
					dir3 = (Vector3f) Vector3f.sub(goals[goalIndex], goals[goalIndex - 1], null).scale(lineDistance);
				if(vectorIndex == baseVectors.length - 1){
					if(goalIndex == 0)
						dir2 = (Vector3f) Vector3f.sub(goalVectors[goalIndex][0], baseVectors[0], null).scale(lineDistance);
					else
						dir2 = (Vector3f) Vector3f.sub(goalVectors[goalIndex][0], goalVectors[goalIndex - 1][0], null).scale(lineDistance);
				}
				else {
					if(goalIndex == 0)
						dir2 = (Vector3f) Vector3f.sub(goalVectors[goalIndex][vectorIndex + 1], baseVectors[vectorIndex], null).scale(lineDistance);
					else
						dir2 = (Vector3f) Vector3f.sub(goalVectors[goalIndex][vectorIndex + 1], goalVectors[goalIndex - 1][vectorIndex], null).scale(lineDistance);
				}
				Vector3f v1;
				if(goalIndex == 0)
					v1 = Vector3f.add(baseVectors[vectorIndex], dir1, null);
				else
					v1 = Vector3f.add(goalVectors[goalIndex - 1][vectorIndex], dir1, null);
				Vector3f v2;
				if(vectorIndex == baseVectors.length - 1){
					if(goalIndex == 0)
						v2 = Vector3f.add(baseVectors[0], dir2, null);
					else
						v2 = Vector3f.add(goalVectors[goalIndex - 1][0], dir2, null);
				}
				else {
					if(goalIndex == 0)
						v2 = Vector3f.add(baseVectors[vectorIndex + 1], dir2, null);
					else
						v2 = Vector3f.add(goalVectors[goalIndex - 1][vectorIndex + 1], dir2, null);
				}
				Vector3f v3;
				if(goalIndex == 0)
					v3 = Vector3f.add(baseVector, dir3, null);
				else
					v3 = Vector3f.add(goals[goalIndex - 1], dir3, null);
				Vector3f adder = (Vector3f) Vector3f.sub(v2, v1, null).scale(0.5f);
				Vector3f location = Vector3f.add(v1, adder, null);
				Vector3f normal = Vector3f.sub(location, v3, null).normalise(null);
				addTwig(vertices, intTextures, normals, indices, location, normal, distance / settings.lengthDivider, width / settings.widthDivider, twigs / settings.widthDivider, settings, random);
				++i;
			}
		}
		else {
			Vector3f location = goals[goals.length - 1];
			Vector3f direc = directions[directions.length - 1];
			addPointyLeaf(vertices, intTextures, normals, indices, location, direc, random, settings.imageSize, settings.imageSize);
		}
	}
	
	private static Vector3f[] getCircleVectors(Vector3f base, Vector3f direction, float radius, int amount){
		Vector3f[] vectors = new Vector3f[amount];
		int i = 0;
		float angle = 0;
		while(i < vectors.length){
			vectors[i] = pointAt(angle, base, direction, radius);
			i++;
			angle += 360f / (vectors.length - 1);
		}
		return vectors;
	}
	
	private static Vector3f[] getCircleVectors(Vector3f base, Vector3f direction, float radius){
		return getCircleVectors(base, direction, radius, (int) Math.max(10 * radius, 5));
	}
	
	private static void addPointyLeaf(ArrayList<Float> vertices, ArrayList<Integer> intTextures, ArrayList<Float> normals, ArrayList<Integer> indices, Vector3f base, Vector3f direction, Random random, int imageWidth, int imageHeight){
		float distance = 1.0f;
		float radius1 = distance / 7;
		float radius2 = distance / 2.0f;
		int angle = random.nextInt(360);
		int parts = 3;
		Vector3f goal = Vector3f.add(base, (Vector3f) new Vector3f(direction).scale(distance * (parts + 2.0f) / parts), null);
		Vector3f[] leftParts = new Vector3f[parts * 2];
		Vector3f[] rightParts = new Vector3f[parts * 2];
		int index = vertices.size() / 3;
		int i = 0;
		while(i < parts){
			Vector3f start = Vector3f.add(base, (Vector3f) new Vector3f(direction).scale(distance * (i + 1.0f) / parts), null);
			Vector3f end = Vector3f.add(base, (Vector3f) new Vector3f(direction).scale(distance * (i + 2.5f) / parts), null);
			Vector3f leftOut = pointAt(angle - 90, end, direction, radius2);
			Vector3f rightOut = pointAt(angle + 90, end, direction, radius2);
			Vector3f leftIn = pointAt(angle - 90, start, direction, radius1);
			Vector3f rightIn = pointAt(angle + 90, start, direction, radius1);
			leftParts[i * 2] = leftOut;
			leftParts[i * 2 + 1] = leftIn;
			rightParts[i * 2] = rightOut;
			rightParts[i * 2 + 1] = rightIn;
			++i;
		}
		addVertice(vertices, base);
		addVertice(vertices, goal);
		for(Vector3f left : leftParts)
			addVertice(vertices, left);
		for(Vector3f right : rightParts)
			addVertice(vertices, right);
		Vector3f normal = (Vector3f) pointAt(angle, base, direction, 1).normalise();
		if(normal.y < 0)
			normal.negate();
		for(int i1 = 0; i1 < 2 + parts * 4; i1++){
			addVertice(normals, normal);
			intTextures.add(imageWidth / 2 + random.nextInt(imageWidth / 2));
			intTextures.add(random.nextInt(imageHeight));
		}
		indices.add(index);
		indices.add(index + 2);
		indices.add(index + 3);
		indices.add(index);
		indices.add(index + 3);
		indices.add(index + 3 + parts * 2);
		indices.add(index);
		indices.add(index + 2 + parts * 2);
		indices.add(index + 3 + parts * 2);
		i = 1;
		while(i < parts){
			indices.add(index + 1 + i * 2);
			indices.add(index + 2 + i * 2);
			indices.add(index + 3 + i * 2);
			indices.add(index + 1 + i * 2);
			indices.add(index + 3 + i * 2);
			indices.add(index + 1 + i * 2 + parts * 2);
			indices.add(index + 3 + i * 2);
			indices.add(index + 1 + i * 2 + parts * 2);
			indices.add(index + 3 + i * 2 + parts * 2);
			indices.add(index + 1 + i * 2 + parts * 2);
			indices.add(index + 2 + i * 2 + parts * 2);
			indices.add(index + 3 + i * 2 + parts * 2);
			++i;
		}
		indices.add(index + 1 + parts * 2);
		indices.add(index + 1);
		indices.add(index + 1 + parts * 4);
	}
	
	private static void addVertice(ArrayList<Float> vertices, Vector3f vertice){
		vertices.add(vertice.x);
		vertices.add(vertice.y);
		vertices.add(vertice.z);
	}
	
	public static Vector3f pointAt(float angle, Vector3f center, Vector3f normal, float radius) {
		angle = (float) toRadians(angle);
        float xv = (float) Math.cos(angle);
        float yv = (float) Math.sin(angle);

        Vector3f v = findV(normal);
        Vector3f w = Vector3f.cross(v, normal, null);

        // Return center + r * (V * cos(a) + W * sin(a))
        Vector3f r1 = (Vector3f) v.scale(radius*xv);
        Vector3f r2 = (Vector3f) w.scale(radius*yv);

        return new Vector3f(center.x + r1.x + r2.x,
                         center.y + r1.y + r2.y,
                         center.z + r1.z + r2.z);
    }
	
	public static Vector3f pointAt(Vector3f angle, Vector3f center, Vector3f normal, float radius){
		angle.normalise();
		float xv = angle.x;
		float yv = angle.z;
		Vector3f v = findV(normal);
        Vector3f w = Vector3f.cross(v, normal, null);

        // Return center + r * (V * cos(a) + W * sin(a))
        Vector3f r1 = (Vector3f) v.scale(radius*xv);
        Vector3f r2 = (Vector3f) w.scale(radius*yv);

        return new Vector3f(center.x + r1.x + r2.x,
                         center.y + r1.y + r2.y,
                         center.z + r1.z + r2.z);
	}

    private static Vector3f findV(Vector3f normal) {
        Vector3f vp = new Vector3f(0f, 0f, 0f);
        if (normal.x != 0 || normal.y != 0) {
            vp = new Vector3f(0f, 0f, 1f);
        } else if (normal.x != 0 || normal.z != 0) {
            vp = new Vector3f(0f, 1f, 0f);
        } else if (normal.y != 0 || normal.z != 0) {
            vp = new Vector3f(1f, 0f, 0f);
        } else {
            return null; // will cause an exception later.
        }

        Vector3f cp = Vector3f.cross(normal, vp, null);
        return cp.normalise(null);
    }
    
    private static void bindFourAngle(ArrayList<Integer> indices, int indice1, int indice2, int indice3, int indice4){
    	indices.add(indice1);
    	indices.add(indice2);
    	indices.add(indice3);
    	indices.add(indice2);
    	indices.add(indice3);
    	indices.add(indice4);
    }
    
    private static void bindFourAngle2(ArrayList<Integer> indices, int indice1, int indice2, int indice3, int indice4){
    	indices.add(indice1);
    	indices.add(indice2);
    	indices.add(indice3);
    	indices.add(indice3);
    	indices.add(indice4);
    	indices.add(indice1);
    }
    
    private static void bindTriAngle(ArrayList<Integer> indices, int indice1, int indice2, int indice3){
    	indices.add(indice1);
    	indices.add(indice2);
    	indices.add(indice3);
    }
    
    private static void addRectangle(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Float> textures, ArrayList<Integer> indices, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2, Facing facing){
    	if(facing == Facing.DOWN)
    		addFourangle(vertices, normals, textures, indices, x1, y1, z1, 0, -1, 0, u1, v1, x1, y1, z2, 0, -1, 0, u1, v2, x2, y1, z2, 0, -1, 0, u2, v2, x2, y1, z1, 0, -1, 0, u2, v1);
    	if(facing == Facing.UP)
    		addFourangle(vertices, normals, textures, indices, x1, y1, z1, 0, 1, 0, u1, v1, x1, y1, z2, 0, 1, 0, u1, v2, x2, y1, z2, 0, 1, 0, u2, v2, x2, y1, z1, 0, 1, 0, u2, v1);
    	if(facing == Facing.SOUTH)
    		addFourangle(vertices, normals, textures, indices, x1, y1, z1, 0, 0, 1, u1, v1, x2, y1, z1, 0, 0, 1, u2, v1, x2, y2, z1, 0, 0, 1, u2, v2, x1, y2, z1, 0, 0, 1, u1, v2);
    	if(facing == Facing.NORTH)
    		addFourangle(vertices, normals, textures, indices, x1, y1, z1, 0, 0, -1, u1, v1, x2, y1, z1, 0, 0, -1, u2, v1, x2, y2, z1, 0, 0, -1, u2, v2, x1, y2, z1, 0, 0, -1, u1, v2);
    	if(facing == Facing.EAST)
    		addFourangle(vertices, normals, textures, indices, x1, y1, z1, 1, 0, 0, u1, v1, x1, y2, z1, 1, 0, 0, u2, v1, x1, y2, z2, 1, 0, 0, u2, v2, x1, y1, z2, 1, 0, 0, u1, v2);
    	if(facing == Facing.WEST)
    		addFourangle(vertices, normals, textures, indices, x1, y1, z1, -1, 0, 0, u1, v1, x1, y2, z1, -1, 0, 0, u2, v1, x1, y2, z2, -1, 0, 0, u2, v2, x1, y1, z2, -1, 0, 0, u1, v2);
    }
    
    private static void addFourangle(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Float> textures, ArrayList<Integer> indices, float x1, float y1, float z1, float nx1, float ny1, float nz1, float u1, float v1, float x2, float y2, float z2, float nx2, float ny2, float nz2, float u2, float v2, float x3, float y3, float z3, float nx3, float ny3, float nz3, float u3, float v3, float x4, float y4, float z4, float nx4, float ny4, float nz4, float u4, float v4){
    	int index = vertices.size() / 3;
    	addVertice(vertices, normals, textures, x1, y1, z1, nx1, ny1, nz1, u1, v1);
    	addVertice(vertices, normals, textures, x2, y2, z2, nx2, ny2, nz2, u2, v2);
    	addVertice(vertices, normals, textures, x3, y3, z3, nx3, ny3, nz3, u3, v3);
    	addVertice(vertices, normals, textures, x4, y4, z4, nx4, ny4, nz4, u4, v4);
    	bindFourAngle2(indices, index, index + 1, index + 2, index + 3);
    }
    
    private static void addTriangle(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Float> textures, ArrayList<Integer> indices, float x1, float y1, float z1, float nx1, float ny1, float nz1, float u1, float v1, float x2, float y2, float z2, float nx2, float ny2, float nz2, float u2, float v2, float x3, float y3, float z3, float nx3, float ny3, float nz3, float u3, float v3){
    	int index = vertices.size() / 3;
    	addVertice(vertices, normals, textures, x1, y1, z1, nx1, ny1, nz1, u1, v1);
    	addVertice(vertices, normals, textures, x2, y2, z2, nx2, ny2, nz2, u2, v2);
    	addVertice(vertices, normals, textures, x3, y3, z3, nx3, ny3, nz3, u3, v3);
    	bindTriAngle(indices, index, index + 1, index + 2);
    }
    
    private static void addBox(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Float> textures, ArrayList<Integer> indices, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float u1North, float v1North, float u2North, float v2North, float u1East, float v1East, float u2East, float v2East, float u1South, float v1South, float u2South, float v2South, float u1West, float v1West, float u2West, float v2West, float u1Up, float v1Up, float u2Up, float v2Up, float u1Down, float v1Down, float u2Down, float v2Down){
    	addRectangle(vertices, normals, textures, indices, minX, minY, minZ, u1North, v1North, maxX, maxY, minZ, u2North, v2North, Facing.NORTH);
    	addRectangle(vertices, normals, textures, indices, minX, minY, maxZ, u1South, v1South, maxX, maxY, maxZ, u2South, v2South, Facing.SOUTH);
    	addRectangle(vertices, normals, textures, indices, maxX, minY, minZ, u1East, v1East, maxX, maxY, maxZ, u2East, v2East, Facing.EAST);
    	addRectangle(vertices, normals, textures, indices, minX, minY, minZ, u1West, v1West, minX, maxY, maxZ, u2West, v2West, Facing.WEST);
    	addRectangle(vertices, normals, textures, indices, minX, maxY, minZ, u1Up, v1Up, maxX, maxY, maxZ, u2Up, v2Up, Facing.UP);
    	addRectangle(vertices, normals, textures, indices, minX, minY, minZ, u1Down, v1Down, maxX, minY, maxZ, u2Down, v2Down, Facing.DOWN);
    }
    
    @SuppressWarnings("unused")
	private static void addBox(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Float> textures, ArrayList<Integer> indices, ArrayList<Collider> colliders, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float u1North, float v1North, float u2North, float v2North, float u1East, float v1East, float u2East, float v2East, float u1South, float v1South, float u2South, float v2South, float u1West, float v1West, float u2West, float v2West, float u1Up, float v1Up, float u2Up, float v2Up, float u1Down, float v1Down, float u2Down, float v2Down){
    	addBox(vertices, normals, textures, indices, minX, minY, minZ, maxX, maxY, maxZ, u1North, v1North, u2North, v2North, u1East, v1East, u2East, v2East, u1South, v1South, u2South, v2South, u1West, v1West, u2West, v2West, u1Up, v1Up, u2Up, v2Up, u1Down, v1Down, u2Down, v2Down);
    	colliders.add(new ColliderBox(minX, minY, minZ, maxX, maxY, maxZ));
    }
    
    private static void addVertice(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Float> textures, float x, float y, float z, float nx, float ny, float nz, float u, float v){
    	vertices.add(x);
    	vertices.add(y);
    	vertices.add(z);
    	normals.add(nx);
    	normals.add(ny);
    	normals.add(nz);
    	textures.add(u);
    	textures.add(v);
    }
	
	private static class TreeColor {
		
		private final Color wood;
		private final Color leaves;
		
		private final float difference;
		private final long seed;
		
		private TreeColor(Color wood, Color leaves, float maxDifference, long seed){
			this.wood = wood;
			this.leaves = leaves;
			this.difference = maxDifference;
			this.seed = seed;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof TreeColor){
				TreeColor color = (TreeColor) other;
				return color.wood.equals(wood) && color.leaves.equals(leaves) && color.difference == difference && color.seed == seed;
			}
			return false;
		}
	}
	
	private static class SkinColor {
		
		private final Color skin;
		private final Color nail;
		
		private final float difference;
		private final long seed;
		
		private SkinColor(Color skin, Color nail, float maxDifference, long seed){
			this.skin = skin;
			this.nail = nail;
			this.difference = maxDifference;
			this.seed = seed;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof SkinColor){
				SkinColor color = (SkinColor) other;
				return color.skin.equals(skin) && color.nail.equals(nail) && color.difference == difference && color.seed == seed;
			}
			return false;
		}
	}
	
	private static class FootColor {
		
		private final SkinColor skinColor;
		private final Color nailColor;
		
		private FootColor(SkinColor skin, Color nail){
			skinColor = skin;
			nailColor = nail;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof FootColor){
				FootColor foot = (FootColor) other;
				return foot.skinColor.equals(skinColor) && foot.nailColor.equals(nailColor);
			}
			return false;
		}
	}
	
	private static class HeadColor {
		
		private final SkinColor skinColor;
		private final Color eyeColor;
		
		private HeadColor(SkinColor skin, Color eyes){
			skinColor = skin;
			eyeColor = eyes;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof HeadColor){
				HeadColor head = (HeadColor) other;
				return head.skinColor.equals(skinColor) && head.eyeColor.equals(eyeColor);
			}
			return false;
		}
	}
	
	private static class ColorText {
		
		private final Color color;
		private final String text;
		
		private ColorText(Color color, String text){
			this.color = color;
			this.text = text;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof ColorText){
				ColorText ct = (ColorText) other;
				System.out.println("Resources.ColorText.equals() ct = " + ct + " and this = " + this);
				return ct.color.equals(color) && ct.text.equals(text);
			}
			else
				System.out.println("Resources.ColorText.equals() other = " + other);
			return false;
		}
	}
	
	private static class FlowerColor {
		
		private final Color flowerColor;
		private final Color stempColor;
		
		private final float maxDifference;
		private final long seed;
		
		private FlowerColor(Color flowerColor, Color stempColor, float maxDifference, long seed){
			this.flowerColor = flowerColor;
			this.stempColor = stempColor;
			this.maxDifference = maxDifference;
			this.seed = seed;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof FlowerColor){
				FlowerColor fc = (FlowerColor) other;
				return fc.flowerColor.equals(flowerColor) && fc.stempColor.equals(stempColor) && fc.maxDifference == maxDifference && fc.seed == seed;
			}
			return false;
		}
	}
	
	private static class MushroomColor {
		
		private final Color stemColor;
		private final Color topColor;
		private final Color spotColor;
		
		private final float maxDifference;
		private final long seed;
		
		private final int spots;
		private final float spotRadius;
		
		private MushroomColor(Color stemColor, Color topColor, Color spotColor, int spots, float spotRadius, float maxDifference, long seed){
			this.stemColor = stemColor;
			this.topColor = topColor;
			this.spotColor = spotColor;
			this.spots = spots;
			this.spotRadius = spotRadius;
			this.maxDifference = maxDifference;
			this.seed = seed;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof MushroomColor){
				MushroomColor col = (MushroomColor) other;
				return col.stemColor.equals(stemColor) && col.topColor.equals(topColor) && col.spotColor.equals(spotColor) && col.spots == spots && col.spotRadius == spotRadius && col.maxDifference == maxDifference && col.seed == seed;
			}
			return false;
		}
	}
}
