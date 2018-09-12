package nl.knokko.items;

import nl.knokko.entities.util.SubModel;

public class ItemEquipment extends Item {
	
	protected SubModel model;

	public ItemEquipment(SubModel model, int texture) {
		super(texture);
		this.model = model;
	}
	
	public SubModel getModel(){
		return model;
	}
}
