package nl.knokko.textures;

public enum Material {
	
	GUI(0, 0),
	ORGANIC(1f, 0.1f),
	METAL(1f, 0.5f);
	
	private float shineDamper;
	private float reflectivity;
	
	Material(float shineDamper, float reflectivity){
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	
	public float getShineDamper(){
		return shineDamper;
	}
	
	public float getReflectivity(){
		return reflectivity;
	}

	public static Material fromID(byte id) {
		if(id == 0)
			return GUI;
		if(id == 1)
			return ORGANIC;
		if(id == 2)
			return METAL;
		throw new IllegalArgumentException("Unkown Material ID: " + id);
	}
}
