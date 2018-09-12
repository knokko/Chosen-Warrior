package nl.knokko.design.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import nl.knokko.entities.ICamera;
import nl.knokko.util.Maths;
import nl.knokko.util.Options;
import static java.lang.Math.*;

public class FreeCamera implements ICamera {
	
	private Vector3f position;
	
	private float pitch;
	private float yaw;
	private float roll;
	
	private boolean mouseMoved;
	private boolean wasMouse;

	public FreeCamera() {
		position = new Vector3f(0, 30, 0);
	}

	@Override
	public float getRadPitch() {
		return (float) toRadians(pitch);
	}

	@Override
	public float getRadYaw() {
		return (float) toRadians(yaw);
	}

	@Override
	public float getRadRoll() {
		return (float) toRadians(roll);
	}
	
	public float getPitch(){
		return pitch;
	}
	
	public float getYaw(){
		return yaw;
	}
	
	public float getRoll(){
		return roll;
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}
	
	public void update(Options options){
		int mx = Mouse.getDX();
		int my = Mouse.getDY();
		int midX = Display.getWidth() / 2;
		int midY = Display.getHeight() / 2;
		if(mouseMoved){
			mouseMoved = false;
			mx = 0;
			my = 0;
		}
		if(mx != 0 && Mouse.isInsideWindow() && wasMouse){
			yaw += options.getMouseSensitivity() * mx;
			Mouse.setCursorPosition(midX, midY);
			mouseMoved = true;
		}
		if(my != 0 && Mouse.isInsideWindow() && wasMouse){
			pitch -= options.getMouseSensitivity() * my;
			Mouse.setCursorPosition(midX, midY);
			mouseMoved = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			Vector3f forward = Maths.getRotationVector(pitch, yaw, roll);
			position.x += forward.x;
			position.y += forward.y;
			position.z += forward.z;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			Vector3f forward = Maths.getRotationVector(pitch, yaw, roll);
			position.x -= forward.x;
			position.y -= forward.y;
			position.z -= forward.z;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			position.y -= 1;
		}
		if(!mouseMoved)
			wasMouse = Mouse.isInsideWindow();
	}
}
