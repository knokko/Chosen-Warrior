package nl.knokko.render.task;

import nl.knokko.entities.util.SubModel;
import nl.knokko.render.model.TexturedModel;

public class RenderTaskArmor extends RenderTaskEquipment {

	public RenderTaskArmor(RenderTaskSubModel owner) {
		super(owner.getEntity(), owner);
	}

	@Override
	public TexturedModel getModel() {
		SubModel armor = ownerTask.getSubModel().getArmorModel();
		if(armor != null)
			return armor.getModel();
		return null;
	}
	
	@Override
	public boolean renderNow(){
		return ownerTask.getSubModel().getArmorModel() != null;
	}
}
