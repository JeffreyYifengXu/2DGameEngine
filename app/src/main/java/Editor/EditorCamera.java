package editor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

import org.joml.Vector2f;

import components.Component;
import engine.Camera;
import engine.MouseListener;

public class EditorCamera extends Component {

    private Vector2f prevMousePos;
    private Vector2f prevCameraPos;

    private Camera levelEditorCamera;

    private boolean isDragging;

    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.isDragging = false;
        this.prevCameraPos = levelEditorCamera.position;
    }

    @Override
    public void update(float dt) {

        //------------Check Mouse button update -----------------
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {//If middle button is pressed
            
            if (!isDragging) { //Check if it's first press

                isDragging = true;
                prevMousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY()); //Record the current mouse position

            } else { //Sequential pass

                //Get current mouse position
                float currentX = MouseListener.getOrthoX();
                float currentY = MouseListener.getOrthoY();

                //Calculate distance moved by mouse
                float movedDistanceX = currentX - prevMousePos.x;
                float movedDistanceY = currentY - prevMousePos.y;

                //Update camera position
                levelEditorCamera.updatePos(levelEditorCamera.position.x - movedDistanceX, levelEditorCamera.position.y - movedDistanceY);

                prevMousePos.x = currentX;
                prevMousePos.y = currentY;
            }
        }

        if (!MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {//If middle button is released
            isDragging = false;
        }

    }
}
