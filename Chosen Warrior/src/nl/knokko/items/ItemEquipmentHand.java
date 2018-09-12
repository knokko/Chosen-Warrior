package nl.knokko.items;

import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.util.SubModel;
import nl.knokko.render.DisplayManager;

public abstract class ItemEquipmentHand extends ItemEquipment {

	public ItemEquipmentHand(SubModel model, int texture) {
		super(model, texture);
	}
	
	public void onItemUse(EntityLiving holder, SubModel arm, SubModel hand){}
	
	public int getCooldown(){
		return DisplayManager.FPS_CAP;
	}
	
	public abstract float getStabDamage();
	
	public abstract float getPunchDamage();
	
	public abstract float getStrikeDamage();
}
