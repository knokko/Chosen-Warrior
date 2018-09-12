package nl.knokko.entities.util;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.items.ItemEquipmentHand;
import nl.knokko.render.model.TexturedModel;

public class SubModel {
	
	public ArrayList<SubModel> childModels = new ArrayList<SubModel>();
	
	/**
	 * equipment[0] returns the armor
	 * equipment[1] returns the held item
	 */
	protected SubModel[] equipment;
	
	protected ItemEquipmentHand heldItem;
	
	private TexturedModel model;
	
	private Vector3f relativePosition;
	
	public float rotationX;
	public float rotationY;
	public float rotationZ;

	public SubModel(TexturedModel texturedModel, Vector3f relativePosition) {
		this.model = texturedModel;
		this.relativePosition = relativePosition;
	}
	
	public SubModel(TexturedModel texturedModel, Vector3f relativePosition, float pitch, float yaw, float roll){
		this(texturedModel, relativePosition);
		rotationX = pitch;
		rotationY = yaw;
		rotationZ = roll;
	}
	
	public TexturedModel getModel(){
		return model;
	}
	
	/**
	 * This method returns the position of this submodel, this is relative to the main model with default rotation.
	 * @return The relative position of this submodel
	 */
	public Vector3f getRelativePosition(){
		return relativePosition;
	}
	
	public void setHeldItem(ItemEquipmentHand item){
		if(item != null){
			if(equipment == null)
				equipment = new SubModel[]{null, item.getModel()};
			else if(equipment.length == 1){
				SubModel armor = equipment[0];
				equipment = new SubModel[]{armor, item.getModel()};
			}
			else
				equipment[1] = item.getModel();
		}
		else {
			if(equipment == null)
				equipment = new SubModel[]{null, null};
			else if(equipment.length == 1){
				SubModel armor = equipment[0];
				equipment = new SubModel[]{armor, null};
			}
			else
				equipment[1] = null;
		}
		heldItem = item;
	}
	
	public SubModel getHeldItemModel(){
		if(equipment != null && equipment.length > 1)
			return equipment[1];
		return null;
	}
	
	public ItemEquipmentHand getHeldItem(){
		return heldItem;
	}
	
	public SubModel getArmorModel(){
		if(equipment != null && equipment.length > 0)
			return equipment[0];
		return null;
	}
}
