package nl.knokko.world.structures;

import nl.knokko.util.patterns.ColorPattern;

public class SimpleHouseTexture {
	
	public final SimpleHouseSettings settings;
	
	public final ColorPattern floor;
	public final ColorPattern ceiling;
	
	public final ColorPattern northOutWall;
	public final ColorPattern eastOutWall;
	public final ColorPattern southOutWall;
	public final ColorPattern westOutWall;
	
	public final ColorPattern northInWall;
	public final ColorPattern eastInWall;
	public final ColorPattern southInWall;
	public final ColorPattern westInWall;
	
	public final ColorPattern northRoof;
	public final ColorPattern eastRoof;
	public final ColorPattern southRoof;
	public final ColorPattern westRoof;
	
	public final byte id;
	
	public SimpleHouseTexture(byte id, SimpleHouseSettings settings, ColorPattern floor, ColorPattern ceiling, ColorPattern northOutWall, ColorPattern eastOutWall, ColorPattern southOutWall, ColorPattern westOutWall, ColorPattern northInWall, ColorPattern eastInWall, ColorPattern southInWall, ColorPattern westInWall, ColorPattern northRoof, ColorPattern eastRoof, ColorPattern southRoof, ColorPattern westRoof){
		this.floor = floor;
		this.ceiling = ceiling;
		this.northOutWall = northOutWall;
		this.eastOutWall = eastOutWall;
		this.southOutWall = southOutWall;
		this.westOutWall = westOutWall;
		this.northInWall = northInWall;
		this.eastInWall = eastInWall;
		this.southInWall = southInWall;
		this.westInWall = westInWall;
		this.northRoof = northRoof;
		this.eastRoof = eastRoof;
		this.southRoof = southRoof;
		this.westRoof = westRoof;
		this.id = id;
		this.settings = settings;
	}
}
