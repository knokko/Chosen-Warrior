package nl.knokko.world.structures;

import nl.knokko.util.Facing;
import nl.knokko.util.TextureArea;

public final class SimpleHouseSettings {
	
	/**
	 * Amount of pixels per meter
	 */
	public static final int PS = 10;
	
	public final byte id;
	
	public final float width;
	public final float depth;
	
	public final float wallHeight;
	public final float wallWidth;
	
	public final float roofWidth;
	public final float roofDepth;
	public final float roofHeight;
	
	public final float doorWidth;
	public final float doorDepth;
	public final float doorHeight;
	public final Facing doorSide;
	
	public final TextureArea locationFloor;
	public final TextureArea locationCeiling;
	
	public final TextureArea locationNorthOutWall;
	public final TextureArea locationEastOutWall;
	public final TextureArea locationSouthOutWall;
	public final TextureArea locationWestOutWall;
	
	public final TextureArea locationNorthInWall;
	public final TextureArea locationEastInWall;
	public final TextureArea locationSouthInWall;
	public final TextureArea locationWestInWall;
	
	public final TextureArea locationSouthNorthRoof;
	public final TextureArea locationEastWestRoof;

	public SimpleHouseSettings(byte id, float width, float depth, float wallHeight, float wallWidth, float roofWidth, float roofDepth, float roofHeight, float doorWidth, float doorDepth, float doorHeight, Facing doorSide){
		this.id = id;
		this.width = width;
		this.depth = depth;
		this.wallHeight = wallHeight;
		this.wallWidth = wallWidth;
		this.roofWidth = roofWidth;
		this.roofDepth = roofDepth;
		this.roofHeight = roofHeight;
		this.doorWidth = doorWidth;
		this.doorDepth = doorDepth;
		this.doorHeight = doorHeight;
		float ww = wallWidth * 2;
		if(doorSide == null || doorSide == Facing.UP || doorSide == Facing.DOWN)
			throw new IllegalArgumentException("Invalid side for the door: " + doorSide);
		this.doorSide = doorSide;
		locationFloor = new TextureArea(width, depth, PS);
		locationCeiling = new TextureArea(width, depth, PS);
		locationNorthOutWall = new TextureArea(width, wallHeight, PS);
		locationEastOutWall = new TextureArea(depth, wallHeight, PS);
		locationSouthOutWall = new TextureArea(width, wallHeight, PS);
		locationWestOutWall = new TextureArea(depth, wallHeight, PS);
		locationNorthInWall = new TextureArea(width - 2 * ww, wallHeight, PS);
		locationEastInWall = new TextureArea(depth - 2 * ww, wallHeight, PS);
		locationSouthInWall = new TextureArea(width - 2 * ww, wallHeight, PS);
		locationWestInWall = new TextureArea(depth - 2 * ww, wallHeight, PS);
		locationSouthNorthRoof = new TextureArea(roofWidth, (float) Math.hypot(roofHeight, roofDepth / 2), PS);
		locationEastWestRoof = new TextureArea(roofDepth, (float) Math.hypot(roofHeight, roofWidth / 2), PS);
		TextureArea.setLocations(locationFloor, locationCeiling, locationNorthOutWall, locationEastOutWall, locationSouthOutWall, locationWestOutWall, locationNorthInWall, locationEastInWall, locationSouthInWall, locationWestInWall, locationSouthNorthRoof, locationEastWestRoof);
	}
	
	@Override
	public boolean equals(Object other){
		if(other == this)
			return true;
		if(other instanceof SimpleHouseSettings){
			SimpleHouseSettings s = (SimpleHouseSettings) other;
			return s.width == width && s.depth == depth && s.wallHeight == wallHeight && s.wallWidth == wallWidth && s.roofWidth == roofWidth && s.roofDepth == roofDepth && s.roofHeight == roofHeight && s.doorWidth == doorWidth && s.doorDepth == doorDepth && s.doorHeight == doorHeight && s.doorSide == doorSide;
		}
		return false;
	}
}
