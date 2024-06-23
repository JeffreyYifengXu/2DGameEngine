package edittool;

import engine.GameObject;
import engine.MouseListener;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/*
 * Scans for perfect mouse picking
 * Update the current active gameobject
 */
public class PropertiesWindow {

    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private boolean activeGameObjectChanged = false;

    private float clickDuration = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    /*
     * Registers mouse click onto gameObjects in the gameview window.
     */
    public void update(float dt, Scene currentScene) {
        clickDuration -= dt;
        
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && !activeGameObjectChanged && clickDuration < 0) { //Register active gameobject
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();

            int gameObjectId = pickingTexture.readPixel(x, y); //The pixel value is the id of gameobject that occupies the pixel
            
            GameObject obOfInterest = currentScene.getGameObject(gameObjectId); 
            activeGameObject = obOfInterest;
            this.clickDuration = 0.2f;
            
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {//Remove an gameObject from the scene
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();

            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject obOfInterest = currentScene.getGameObject(gameObjectId);

            currentScene.removeGameObject(obOfInterest);
        }
        activeGameObjectChanged = false;
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return this.activeGameObject;
    }

    public void setActiveGameObject(GameObject go) {
        this.activeGameObject = go;
        this.activeGameObjectChanged = true;
    }
}