package scenes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Renderer.Renderer;
import components.Component;
import components.DeserializerComponent;
import engine.Camera;
import engine.GameObject;
import engine.GameObjectDeserializer;
import imgui.ImGui;



public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false; 
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;
    
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

    /**
     * Serialize and save game data (Usiing custom serializer for gameobject)
     */
    public void saveExit() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Component.class, new DeserializerComponent())
            .registerTypeAdapter(GameObject.class, new GameObjectDeserializer()) // Use custom deserializer
            .create();
        
        try {
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialize and load the saved game file  (Using custom deserializer for gameobject)
     */
    public void load() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Component.class, new DeserializerComponent()) //Use custom deserializer for component
            .registerTypeAdapter(GameObject.class, new GameObjectDeserializer()) // Use custom deserializer for gameobject
            .create();
        
        String inFile = "";

        //check if its the first time loading up the game
        File f = new File("level.txt");
        if (f.length() == 0) {
            System.out.println("Level not loaded, because level.txt is empty");
            this.levelLoaded = false;
            return;
        }

        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt"))); //Read in savec file in json format
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i=0; i < objs.length; i++) {
                System.out.println("GameObjects loaded: " + objs[i].getName());

                addGameObjectToScene(objs[i]);
            }
        }

        this.levelLoaded = true;
        System.out.println("Level successfully loaded");
    }
}
