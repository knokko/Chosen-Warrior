package nl.knokko.items;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.moves.MoveThrowUp;
import nl.knokko.entities.projectile.EntityThrownMushroom;
import nl.knokko.entities.util.SubModel;
import nl.knokko.util.Maths;

public class ItemMushroom extends ItemEquipmentHand {
	
	private boolean isPoison;

	public ItemMushroom(SubModel model, int texture, boolean poison) {
		super(model, texture);
		isPoison = poison;
	}
	
	@Override
	public void onItemUse(EntityLiving holder, SubModel arm, SubModel hand){
		Matrix4f matrix = Maths.createTransformationMatrix(holder);
		matrix = Matrix4f.mul(matrix, Maths.createTransformationMatrix(arm), matrix);
		matrix = Matrix4f.mul(matrix, Maths.createTransformationMatrix(arm.childModels.get(0)), matrix);
		matrix = Matrix4f.mul(matrix, Maths.createTransformationMatrix(arm.childModels.get(0).childModels.get(0)), matrix);
		EntityThrownMushroom mushroom = new EntityThrownMushroom(model.getModel(), holder, new Vector3f(matrix.m30, matrix.m31, matrix.m32), 5000, 1, isPoison);
		holder.setMove(new MoveThrowUp(holder, arm, mushroom, holder.getHeadPitch()));
	}

	@Override
	public float getStabDamage() {
		return 1;
	}

	@Override
	public float getPunchDamage() {
		return 1;
	}

	@Override
	public float getStrikeDamage() {
		return 20;
	}
	
	@Override
	public int getCooldown(){
		return 0;
	}
}
