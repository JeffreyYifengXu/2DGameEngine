package engine;

import java.util.ArrayList;
import java.util.List;

import Renderer.Renderer;
import imgui.ImGui;



public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    
    public Scene() {

    }

    public void init() {
        
    }

    public void start() {
        //start each game objects
        System.out.println("Adding " + gameObjects.size() + " gameobjects to renderer");

        for (GameObject go: gameObjects) {
            go.start();
            this.renderer.add(go);
        }

        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public Camera camera() {
        return this.camera;
    }

    public void sceneimgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui(); //Inspect the gameobject of interest
            ImGui.end();
        }

        imgui(); //Create custom scene integrator
    }

    public void imgui() {

    }
}
