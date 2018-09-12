package nl.knokko.entities;

import org.lwjgl.util.vector.Vector3f;

public interface ICamera {
	
	float getRadPitch();
	float getRadYaw();
	float getRadRoll();
	
	Vector3f getPosition();
}
