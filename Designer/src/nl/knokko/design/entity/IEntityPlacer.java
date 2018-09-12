package nl.knokko.design.entity;

import java.util.Random;

import nl.knokko.render.Renderer;
import nl.knokko.world.World;

public interface IEntityPlacer {
	
	void click(World world, float x, float y, float z, Random random);
	
	void update(World world, Random random);
	
	void render(World world, Renderer renderer);
}
