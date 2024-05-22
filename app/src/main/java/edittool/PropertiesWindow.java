package edittool;

import engine.GameObject;
import engine.MouseListener;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

/*
 * Scans for perfect mouse picking
 * Update the current active gameobject
 */
public class PropertiesWindow {

    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();

            System.out.println("Location at: [" + x + ", " + y + "]");

            int gameObjectId = pickingTexture.readPixel(x, y); //The pixel value is the id of gameobject that occupies the pixel
            System.out.println("Target gameObject id: " + gameObjectId);
            activeGameObject = currentScene.getGameObject(gameObjectId);
        }
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
}