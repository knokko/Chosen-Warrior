package nl.knokko.guis;

import nl.knokko.guis.buttons.GuiEnumOptionButton;
import nl.knokko.guis.buttons.GuiKeyBindButton;
import nl.knokko.guis.buttons.GuiLinkButton;
import nl.knokko.guis.buttons.GuiOptionSliceButton;
import nl.knokko.util.Options.RotateControl;

import org.lwjgl.util.vector.Vector2f;

import static nl.knokko.guis.GuiOptions.*;

public class GuiControls extends Gui implements IScrollGui {
	
	private static GuiControls instance;
	
	public static GuiControls getInstance(){
		if(instance == null)
			instance = new GuiControls();
		return instance;
	}

	private GuiControls() {
		addButton(new GuiLinkButton(new Vector2f(0.65f, 0.8f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "back to menu", GuiMenu.class));
		addButton(new GuiLinkButton(new Vector2f(0.65f, 0.55f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "back to options", GuiOptions.class));
		addButton(GuiLinkButton.createCloseButton(new Vector2f(0.65f, 0.3f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR));
		
		addButton(new GuiOptionSliceButton(new Vector2f(-0.3f, 0.7f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "scroll sensitivity", 0.0002f, 0.002f, "ScrollSensitivity", float.class));
		addButton(new GuiOptionSliceButton(new Vector2f(-0.3f, 0.45f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "mouse sensitivity", 0.1f, 0.4f, "MouseSensitivity", float.class));
		addButton(new GuiOptionSliceButton(new Vector2f(-0.3f, 0.2f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "rotate sensitivity", 0.25f, 3.0f, "RotateSensitivity", float.class));
		addButton(new GuiOptionSliceButton(new Vector2f(-0.3f, -0.05f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "zoom sensitivity", 1, 10, "ZoomSensitivity", float.class));
		
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -0.55f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "move forward", "Forward"));
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -0.8f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "move backward", "Backward"));
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -1.05f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "move to left", "Left"));
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -1.3f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "move to right", "Right"));
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -1.55f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "jump", "Jump"));
		
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -2.05f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "turn to left", "RotateLeft"));
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -2.3f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "turn to right", "RotateRight"));
		
		addButton(new GuiEnumOptionButton(new Vector2f(-0.69f, -2.8f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "RotateControl", RotateControl.ROTATE_WITH_CAMERA));
		addButton(new GuiEnumOptionButton(new Vector2f(-0.05f, -2.8f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "RotateControl", RotateControl.ROTATE_MANUALLY));
		addButton(new GuiEnumOptionButton(new Vector2f(0.62f, -2.8f), new Vector2f(0.35f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "RotateControl", RotateControl.MANUALLY_IN_THIRD_PERSON));
		
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -3.3f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "left hand", "LeftHand"));
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -3.55f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "right hand", "RightHand"));
		
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -4.05f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "use 360 kick", "360Kick"));
		addButton(new GuiKeyBindButton(new Vector2f(-0.3f, -4.3f), new Vector2f(0.3f, 0.1f), BUTTON_COLOR, TEXT_COLOR, "collect plants", "Collect"));
	}

	@Override
	public float getMaximumScroll() {
		return 1.7f;
	}
	
	@Override
	public boolean canClose(){
		return currentButton == null;
	}
	
	@Override
	public boolean processMouseInput(int button){
		return true;
	}
}
