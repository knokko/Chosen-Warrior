package nl.knokko.world.structures;

import java.awt.Color;
import java.util.Random;

import nl.knokko.util.Facing;
import nl.knokko.util.patterns.PatternPlanks;

public final class SimpleHouses {
	
	private static final Color COLOR_FLOOR = new Color(50, 20, 10);
	private static final Color COLOR_FLOOR_EDGE = new Color(25, 10, 5);
	
	private static final Color COLOR_CEILING = new Color(100, 40, 20);
	private static final Color COLOR_CEILING_EDGE = new Color(80, 30, 15);
	
	private static final Color COLOR_OUTER_WALL = new Color(60, 23, 12);
	private static final Color COLOR_OUTER_WALL_EDGE = new Color(70, 25, 14);
	
	private static final Color COLOR_INNER_WALL = new Color(90, 35, 18);
	private static final Color COLOR_INNER_WALL_EDGE = new Color(110, 45, 25);
	
	private static final Color COLOR_ROOF = new Color(150, 50, 0);
	private static final Color COLOR_ROOF_EDGE = new Color(120, 40, 0);
	
	private static final PatternPlanks PATTERN_OUTER_WALL = new PatternPlanks(COLOR_OUTER_WALL, COLOR_OUTER_WALL_EDGE, createRandom(), 0.1f, 30, 3, 11);
	private static final PatternPlanks PATTERN_INNER_WALL = new PatternPlanks(COLOR_INNER_WALL, COLOR_INNER_WALL_EDGE, createRandom(), 0.1f, 30, 3, 11);
	private static final PatternPlanks PATTERN_ROOF = new PatternPlanks(COLOR_ROOF, COLOR_ROOF_EDGE, createRandom(), 0.2f, 40, 4, 13);

	public static final SimpleHouseSettings MODEL_NORTH_1 = new SimpleHouseSettings((byte) -128, 5, 8, 3, 0.1f, 6, 9.6f, 2, 1.0f, 0.05f, 2, Facing.NORTH);
	
	public static final SimpleHouseTexture TEXTURE_WOOD_1 = new SimpleHouseTexture((byte) -128, MODEL_NORTH_1, new PatternPlanks(COLOR_FLOOR, COLOR_FLOOR_EDGE, createRandom(), 0.1f, 30, 2, 12), new PatternPlanks(COLOR_CEILING, COLOR_CEILING_EDGE, createRandom(), 0.1f, 30, 2, 12), PATTERN_OUTER_WALL, PATTERN_OUTER_WALL, PATTERN_OUTER_WALL, PATTERN_OUTER_WALL, PATTERN_INNER_WALL, PATTERN_INNER_WALL, PATTERN_INNER_WALL, PATTERN_INNER_WALL, PATTERN_ROOF, PATTERN_ROOF, PATTERN_ROOF, PATTERN_ROOF);
	
	private static final SimpleHouseTexture[] TEXTURES = {TEXTURE_WOOD_1};
	
	public static SimpleHouseTexture fromID(byte id){
		return TEXTURES[id + 128];
	}
	
	private static Random createRandom(){
		return new Random(394856034580L);
	}

}
