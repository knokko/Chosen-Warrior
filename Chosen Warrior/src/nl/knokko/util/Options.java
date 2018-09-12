package nl.knokko.util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Options {
	
	private RotateControl rotateControl = RotateControl.MANUALLY_IN_THIRD_PERSON;
	
	private float mouseSensitivity = 0.25f;
	private float rotateSensitivity = 1.5f;
	private float scrollSensitivity = 0.001f;
	private float zoomSensitivity = 5f;
	
	private int keyMoveForward = Keyboard.KEY_W;
	private int keyMoveBackward = Keyboard.KEY_S;
	private int keyMoveLeft = Keyboard.KEY_Q;
	private int keyMoveRight = Keyboard.KEY_E;
	private int keyJump = Keyboard.KEY_SPACE;
	
	private int keyTurnLeft = Keyboard.KEY_A;
	private int keyTurnRight = Keyboard.KEY_D;
	
	private int key360Kick = Keyboard.KEY_X;
	private int keyCollect = Keyboard.KEY_C;
	
	private int keyLeftHand = -1;
	private int keyRightHand = -2;
	
	public float getMouseSensitivity(){
		return mouseSensitivity;
	}
	
	public void setMouseSensitivity(float value){
		mouseSensitivity = value;
	}
	
	public float getRotateSensitivity(){
		return rotateSensitivity;
	}
	
	public void setRotateSensitivity(float value){
		rotateSensitivity = value;
	}
	
	public float getScrollSensitivity(){
		return scrollSensitivity;
	}
	
	public void setScrollSensitivity(float value){
		scrollSensitivity = value;
	}
	
	public float getZoomSensitivity(){
		return zoomSensitivity;
	}
	
	public void setZoomSensitivity(float value){
		zoomSensitivity = value;
	}
	
	public int getForwardKey(){
		return keyMoveForward;
	}
	
	public void setForwardKey(int key){
		keyMoveForward = key;
	}
	
	public int getBackwardKey(){
		return keyMoveBackward;
	}
	
	public void setBackwardKey(int key){
		keyMoveBackward = key;
	}
	
	public int getLeftKey(){
		return keyMoveLeft;
	}
	
	public void setLeftKey(int key){
		keyMoveLeft = key;
	}
	
	public int getRightKey(){
		return keyMoveRight;
	}
	
	public void setRightKey(int key){
		keyMoveRight = key;
	}
	
	public int getJumpKey(){
		return keyJump;
	}
	
	public void setJumpKey(int key){
		keyJump = key;
	}
	
	public int getRotateLeftKey(){
		return keyTurnLeft;
	}
	
	public void setRotateLeftKey(int key){
		keyTurnLeft = key;
	}
	
	public int getRotateRightKey(){
		return keyTurnRight;
	}
	
	public void setRotateRightKey(int key){
		keyTurnRight = key;
	}
	
	public int get360KickKey(){
		return key360Kick;
	}
	
	public void set360KickKey(int key){
		key360Kick = key;
	}
	
	public int getCollectKey(){
		return keyCollect;
	}
	
	public void setCollectKey(int key){
		keyCollect = key;
	}
	
	public int getLeftHandKey(){
		return keyLeftHand;
	}
	
	public void setLeftHandKey(int key){
		keyLeftHand = key;
	}
	
	public int getRightHandKey(){
		return keyRightHand;
	}
	
	public void setRightHandKey(int key){
		keyRightHand = key;
	}
	
	public RotateControl getRotateControl(){
		return rotateControl;
	}
	
	public void setRotateControl(RotateControl control){
		rotateControl = control;
	}
	
	public static boolean isKeyDown(int key){
		if(key >= 0)
			return Keyboard.isKeyDown(key);
		return Mouse.isButtonDown(-key - 1);
	}
	
	public static String getKeyName(int key){
		if(key >= 0)
			return Keyboard.getKeyName(key);
		return Mouse.getButtonName(-key - 1);
	}
	
	public static enum RotateControl {
		
		MANUALLY_IN_THIRD_PERSON("Rotate your character using the rotate keys ('A' and 'D' by default) if the camera is in third person. Rotate your character with the mouse if the camera is in first person."),
		ROTATE_MANUALLY("Always rotate your character using the rotate keys ('A' and 'D' by default), you will only rotate the camera with your mouse."),
		ROTATE_WITH_CAMERA("Your character will always have the same rotation as the camera, even if the camera is third person.");
		
		private final String description;
		
		private RotateControl(String description){
			this.description = description;
		}
		
		@Override
		public String toString(){
			return name().toLowerCase().replace('_', ' ');
		}
		
		public String getDescription(){
			return description;
		}
	}
}
