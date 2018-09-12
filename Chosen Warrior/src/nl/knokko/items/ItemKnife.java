package nl.knokko.items;

import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.moves.MoveStabUp;
import nl.knokko.entities.util.SubModel;
import nl.knokko.textures.Material;
import nl.knokko.util.Resources;
import nl.knokko.weapons.KnifeForm;
import nl.knokko.render.model.TexturedModel;

public class ItemKnife extends ItemEquipmentHand {

	public ItemKnife(KnifeForm form) {
		super(new SubModel(new TexturedModel(Resources.getKnifeModel(form), Resources.getFilledTexture(new Color(100, 100, 100), Material.METAL)), new Vector3f(0, form.holdLength / 2, -form.holdHeight)/*create Arm interface as parameter*/, 90, 0, 90), Resources.getFilledTexture(new Color(100, 100, 100), Material.METAL).getID());
	}
	
	@Override
	public void onItemUse(EntityLiving holder, SubModel arm, SubModel hand){
		holder.setMove(new MoveStabUp(holder, arm, this));
	}
	
	@Override
	public int getCooldown(){
		return 0;
	}

	@Override
	public float getStabDamage() {
		return 3f;
	}

	@Override
	public float getPunchDamage() {
		return 1f;
	}

	@Override
	public float getStrikeDamage() {
		return 1.5f;
	}
}
