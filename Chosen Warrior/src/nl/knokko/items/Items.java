package nl.knokko.items;

import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.util.SubModel;
import nl.knokko.render.model.TexturedModel;
import nl.knokko.textures.ModelTexture;
import nl.knokko.util.Resources;
import nl.knokko.weapons.KnifeForm;
import nl.knokko.world.forest.plants.MushroomSettings;
import nl.knokko.world.forest.plants.Mushrooms;

public final class Items {
	
	private static final ItemMushroom createMushroom(MushroomSettings settings, ModelTexture texture, boolean poison){
		return new ItemMushroom(new SubModel(new TexturedModel(Resources.getMushroomModel(settings), texture), new Vector3f(0, 0, -0.25f)), texture.getID(), poison);
	}
	
	public static final ItemMushroom RED_MUSHROOM = createMushroom(Mushrooms.DEFAULT, Mushrooms.RED_MUSHROOM, false);
	public static final ItemMushroom POISON_MUSHROOM = createMushroom(Mushrooms.DEFAULT, Mushrooms.POISON_MUSHROOM, true);
	public static final ItemKnife KNIFE = new ItemKnife(new KnifeForm(0.15f, 0.05f, 0.05f, 0.35f, 0.025f, 0.05f, 0.025f, 0.35f));

	public static final Item[] ITEMS = {RED_MUSHROOM, POISON_MUSHROOM};
	
	public static final Item fromID(short id){
		int i = (int)id - Short.MIN_VALUE;
		return ITEMS[i];
	}
}
