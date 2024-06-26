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

import components.Component;
import components.DeserializerComponent;
import engine.Camera;
import engine.GameObject;
import engine.GameObjectDeserializer;
import engine.Transform;
import renderer.Renderer;




public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false; 
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected boolean levelLoaded = false;

    protected boolean madeChanges = false;
    
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
        madeChanges = true;
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public GameObject getGameObject(int gameObjectId) {
        for (GameObject ob: gameObjects) {
            if (ob.getUid() == gameObjectId) {
                return ob;
            }
        }
        return null;
    }

    public abstract void update(float dt);

    public abstract void render();

    public Camera camera() {
        return this.camera;
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);

        return go;
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
            //Don't save if no changes were made
            if (madeChanges) {
                FileWriter writer = new FileWriter("level.txt");

                List<GameObject> objsToSave = new ArrayList<>();
                for (GameObject obj: this.gameObjects) {
                    if (obj.doSerialization()) {
                        objsToSave.add(obj);
                    }
                }

                writer.write(gson.toJson(objsToSave));
                writer.close();
            }
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

            int maxGoId = -1;
            int maxCompId = -1;
            int i;

            GameObject.init(5); //Set max id to 3 to make room for the two arrows which takes up 1 and 2.
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (i=0; i < objs.length; i++) {

                addGameObjectToScene(objs[i]);
                
                //Find maximum component id
                for (Component c: objs[i].getAllComponent()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }

                //Find maximum GameObject id
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            System.out.println("Succesfully loaded " + i + " objects");

            maxCompId++;
            maxGoId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }

        this.levelLoaded = true;
        System.out.println("Level successfully loaded");
    }

    /*
     * Remove the specified gameobject from the scene
     */
    public void removeGameObject(GameObject go) {
        
        gameObjects.remove(go);
    }

    //debug function
    public void printAllGameObjects() {
        for (GameObject ob: gameObjects) {
            System.out.println(ob.getUid());
        }
    }
}

