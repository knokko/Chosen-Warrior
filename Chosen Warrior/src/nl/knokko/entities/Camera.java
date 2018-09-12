package nl.knokko.entities;

import nl.knokko.entities.living.EntityLiving;
import nl.knokko.entities.util.SubModel;
import nl.knokko.main.Game;
import nl.knokko.util.Maths;
import nl.knokko.util.Options.RotateControl;
import nl.knokko.world.forest.animals.Dinosaurs;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import static java.lang.Math.*;

public class Camera implements ICamera {
	
	private Vector3f position;
	private EntityLiving target;
	
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	
	private float distance = 5f;
	private int cooldown;
	
	private boolean mouseMoved;
	private boolean wasMouse;
	
	public Camera(EntityLiving target, Vector3f position, float rotationX, float rotationY, float rotationZ){
		this.target = target;
		this.position = new Vector3f(position);
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
	}
	
	public Camera(EntityLiving target){
		this(target, new Vector3f(0,10,25), 0, 0, 0);
	}
	
	public void update(){
		int midX = Display.getWidth() / 2;
		int midY = Display.getHeight() / 2;
		int mx = Mouse.getDX();
		int my = Mouse.getDY();
		SubModel head = target.getMainHead();
		if(rotationX > 360)
			rotationX -= 360;
		if(rotationX < 0)
			rotationX += 360;
		if(rotationY > 360)
			rotationY -= 360;
		if(rotationY < 0)
			rotationY += 360;
		if(rotationZ > 360)
			rotationZ -= 360;
		if(rotationZ < 0)
			rotationZ += 360;
		if(mouseMoved){
			mouseMoved = false;
			mx = 0;
			my = 0;
		}
		if(mx != 0 && Mouse.isInsideWindow() && wasMouse){
			if(rotateManually())
				rotationY += Game.getOptions().getMouseSensitivity() * mx;
			else if(target.isEnabled())
				target.rotationY += Game.getOptions().getMouseSensitivity() * mx;
			Mouse.setCursorPosition(midX, midY);
			mouseMoved = true;
		}
		if(my != 0 && Mouse.isInsideWindow() && wasMouse){
			if(rotateManually())
				rotationX += Game.getOptions().getMouseSensitivity() * -my;
			else if(target.isEnabled())
				head.rotationX += Game.getOptions().getMouseSensitivity() * my;
			Mouse.setCursorPosition(midX, midY);
			mouseMoved = true;
		}
		if(!rotateManually()){
			if(head.rotationZ < -45)
				head.rotationZ = -45;
			if(head.rotationZ > 45)
				head.rotationZ = 45;
			if(head.rotationX < -90)
				head.rotationX = -90;
			if(head.rotationX > 90)
				head.rotationX = 90;
			rotationX = target.getRotationX() - head.rotationX;
			rotationY = target.getRotationY() + head.rotationY;
			rotationZ = target.getRotationZ() + head.rotationZ;
		}
		Vector3f forward = Maths.getRotationVector(rotationX, rotationY, rotationZ);
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && Game.getWorld() == null){
			position.x += forward.x;
			position.y += forward.y;
			position.z += forward.z;
		}
		if(Mouse.isInsideWindow()){
			int wheel = Mouse.getDWheel();
			distance -= wheel * Game.getOptions().getScrollSensitivity() * Game.getOptions().getZoomSensitivity();
			if(distance < 0)
				distance = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			if(rotateManually())
				rotationZ -= Game.getOptions().getMouseSensitivity();
			else
				head.rotationZ -= Game.getOptions().getMouseSensitivity();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
			if(rotateManually())
				rotationZ += Game.getOptions().getMouseSensitivity();
			else
				head.rotationZ += Game.getOptions().getMouseSensitivity();
		}
		
		forward.scale(-distance);
		Matrix4f matrix1 = Maths.createTransformationMatrix(Vector3f.add(target.getPosition(), target.getBaseModelPosition(), null), target.getRotationX(), target.getRotationY(), target.getRotationZ(), target.getSize());
		Matrix4f matrix2 = Maths.createTransformationMatrix(Vector3f.add(head.getRelativePosition(), new Vector3f(0, Dinosaurs.HUMANOID.headRadius, 0), null), head.rotationX, head.rotationY, head.rotationZ, target.getSize());
		Matrix4f matrix = Matrix4f.mul(matrix1, matrix2, matrix1);
		Vector3f.add(new Vector3f(matrix.m30, matrix.m31, matrix.m32), forward, position);
		if(!mouseMoved)
			wasMouse = Mouse.isInsideWindow();
		if(cooldown > 0)
			--cooldown;
	}

	public float getPitch() {
		return rotationX;
	}

	public float getYaw() {
		return rotationY;
	}

	public float getRoll() {
		return rotationZ;
	}
	
	public float getRadPitch(){
		return (float) toRadians(getPitch());
	}
	
	public float getRadYaw(){
		return (float) toRadians(getYaw());
	}
	
	public float getRadRoll(){
		return (float) toRadians(getRoll());
	}

	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	
	public boolean isFirstPerson(){
		return distance < 1;
	}
	
	public boolean isThirdPerson(){
		return !isFirstPerson();
	}
	
	public void setYaw(float yaw){
		rotationY = yaw;
	}
	
	public boolean rotateManually(){
		return Game.getOptions().getRotateControl() == RotateControl.ROTATE_MANUALLY || Game.getOptions().getRotateControl() == RotateControl.MANUALLY_IN_THIRD_PERSON && Game.getCamera().isThirdPerson();
	}
}
