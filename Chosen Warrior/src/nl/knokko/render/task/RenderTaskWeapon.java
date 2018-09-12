package nl.knokko.render.task;

import nl.knokko.entities.util.SubModel;
import nl.knokko.render.model.TexturedModel;

public class RenderTaskWeapon extends RenderTaskEquipment {

	public RenderTaskWeapon(RenderTaskSubModel owner) {
		super(owner.getEntity(), owner);
	}

	@Override
	public TexturedModel getModel() {
		SubModel heldItem = ownerTask.getSubModel().getHeldItemModel();
		if(heldItem != null)
			return heldItem.getModel();
		return null;
	}
	
	@Override
	public boolean renderNow(){
		return ownerTask.getSubModel().getHeldItemModel() != null;
	}
}
