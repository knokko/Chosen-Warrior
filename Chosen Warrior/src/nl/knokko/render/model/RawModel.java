package nl.knokko.render.model;

import nl.knokko.collision.Collider;

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	
	private byte[] creationID;
	private Collider collider;

	public RawModel(int vaoID, int vertexCount, byte[] creationID, Collider collider) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.creationID = creationID;
		this.collider = collider;
	}
	
	public int getVaoID(){
		return vaoID;
	}
	
	public int getVertexCount(){
		return vertexCount;
	}
	
	public byte[] getCreationID(){
		if(creationID == null)
			throw new RuntimeException("Model " + this + " is not recreateable!");
		return creationID;
	}
	
	public boolean hasCollider(){
		return collider != null;
	}
	
	public Collider getCollider(){
		if(collider == null)
			throw new RuntimeException("Model " + this + " does not have a collider!");
		return collider;
	}
	
	@Override
	public boolean equals(Object other){
		if(other.getClass() == getClass()){
			return ((RawModel)other).vaoID == vaoID;
		}
		return false;
	}
}
