package nl.knokko.items;

public class Item {
	
	private static short nextID = Short.MIN_VALUE;
	
	protected int texture;
	
	protected short id;

	public Item(int texture) {
		this.texture = texture;
		this.id = nextID;
		nextID++;
	}
	
	public int getTexture(){
		return texture;
	}
	
	public short getID(){
		return id;
	}
}
