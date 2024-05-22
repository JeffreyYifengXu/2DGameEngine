package edittool;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

import org.joml.Vector2f;

import components.Component;
import engine.Camera;
import engine.KeyListener;
import engine.MouseListener;

public class EditorCamera extends Component {

    private Vector2f prevMousePos;

    private float dragSensitivity = 20.0f;
    private float zoomSensitivity = 0.2f;

    private Camera levelEditorCamera;

    private boolean isDragging;

    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.isDragging = false;
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
                Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());

                //Calculate distance moved by mouse
                Vector2f movedDistance = mousePos.sub(this.prevMousePos);

                //Update camera position
                this.levelEditorCamera.position.sub(movedDistance.mul(dt).mul(this.dragSensitivity));

                //Linear interpolation, allowing for smoother animation
                prevMousePos.lerp(mousePos, dt);
            }
        }

        if (!MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {//If middle button is released
            isDragging = false;
        }

        //------------------Check scroll update, for zoom------------------
        //Zooming out
        if (MouseListener.getScrollY() != 0) {

            //Use exponential function for the zooming factor. 
            //Allows finner adjustment when scrolling slow, and faster change when scrolling fast
            float zoomFactor = (float)Math.pow(Math.abs(MouseListener.getScrollY()), 1 / levelEditorCamera.getZoom()) * zoomSensitivity;

            //signum function returns -1 if value passed in is smaller than zero, and so on
            zoomFactor *= -Math.signum(MouseListener.getScrollY());

            levelEditorCamera.addZoom(zoomFactor);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            resetCameraPos();
        }

    }

    private void resetCameraPos() {
        levelEditorCamera.position = new Vector2f(-250, 0);
    }

}

