package nl.knokko.entities.projectile;

import java.nio.ByteBuffer;

import nl.knokko.collision.Collider;
import nl.knokko.collision.ColliderBox;
import nl.knokko.entities.Entity;
import nl.knokko.entities.EntityCollectible;
import nl.knokko.entities.EntityPhysical;
import nl.knokko.entities.living.EntityLiving;
import nl.knokko.items.Items;
import nl.knokko.render.model.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class EntityThrownMushroom extends EntityProjectile {
	
	protected boolean isPoison;

	public EntityThrownMushroom(TexturedModel model, EntityPhysical shooter, Vector3f location, float startForce, float size, boolean poison) {
		super(model, shooter, location, startForce, size);
		isPoison = poison;
	}

	@Override
	public void onEntityHit(Entity hitEntity, Vector3f impactPosition) {
		if(hitEntity instanceof EntityLiving && isPoison)
			((EntityLiving) hitEntity).attack(50);
		destroy();
	}

	@Override
	public void onGroundHit(Vector3f v) {
		world.spawnEntity(new EntityCollectible(getModel(), new Vector3f(v.x, world.getGroundHeight(v.x, v.z), v.z), Items.RED_MUSHROOM, rotationY, 1));
		destroy();
	}
	
	@Override
	public Collider createCollider(){
		float l = size * 0.5f;
		return new ColliderBox(-l, -l, -l, l, l, l);
	}
	
	@Override
	public float straightPitch(){
		return 90;
	}
	
	@Override
	public ByteBuffer saveMapData() {
		throw new RuntimeException("Thrown Mushrooms can't be saved in the world map!");
	}
}
