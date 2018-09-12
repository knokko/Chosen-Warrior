package nl.knokko.textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper;
	private float reflectivity;
	
	private byte[] creationID;

	public ModelTexture(int id, Material material, byte[] creationID) {
		textureID = id;
		this.creationID = creationID;
		shineDamper = material.getShineDamper();
		reflectivity = material.getReflectivity();
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof ModelTexture){
			ModelTexture texture = (ModelTexture) other;
			return texture.textureID == textureID && texture.shineDamper == shineDamper && texture.reflectivity == reflectivity;
		}
		return false;
	}
	
	public int getID(){
		return textureID;
	}
	
	public float getShineDamper(){
		return shineDamper;
	}
	
	public float getReflectivity(){
		return reflectivity;
	}
	
	public byte[] getCreationID(){
		if(creationID == null)
			throw new RuntimeException("ModelTexture " + this + " does not have a creation ID!");
		return creationID;
	}
}
