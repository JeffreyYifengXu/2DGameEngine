package scenes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.AbstractDeserializer;
import components.Component;
import components.DeserializerComponent;
import components.ShapeDeserializer;
import engine.Camera;
import engine.GameObject;
import engine.GameObjectDeserializer;
import engine.Transform;
import gamePhysics.GamePhysicsWorld;
import physicsEngine2D.primitives.Shape;
import renderer.Renderer;


public class Scene {

    private Renderer renderer = new Renderer();
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects = new ArrayList<>();
    private boolean madeChanges = false;

    private SceneInit sceneInit;
    private GamePhysicsWorld physicsWorld;
    
    public Scene(SceneInit sceneInit) {
        this.sceneInit = sceneInit;
        this.physicsWorld = new GamePhysicsWorld();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    public void init() {
        this.camera = new Camera(new Vector2f(-250, 0));
        this.sceneInit.loadResources(this);
        this.sceneInit.init(this);
    }

    public void start() {
        //start each game objects
        System.out.println("Adding " + gameObjects.size() + " gameobjects to renderer");

        for (GameObject go: gameObjects) {
            go.start();
            this.renderer.add(go);
            // this.physicsWorld.addRigidBody(go, false);
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
            // this.physicsWorld.addRigidBody(go, false);
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

    public void editorUpdate(float dt) {
        camera.adjustProjection();
        //Currently no updates done to gameobjects
        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);

            if (go.isDead()) { //Remove dead game objects
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physicsWorld.destroyGameObject(go);
                i--;
            }
        }
    }

    public void update(float dt) {
        camera.adjustProjection();
        this.physicsWorld.update(dt);

        //Currently no updates done to gameobjects
        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.update(dt);

            if (go.isDead()) { //Remove dead game objects
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physicsWorld.destroyGameObject(go);
                i--;
            }
        }
    }

    public void render() {
        this.renderer.render();
    }

    public Camera camera() {
        return this.camera;
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);

        return go;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void imgui() {
        this.sceneInit.imgui();
    }

    /**
     * Serialize and save game data (Usiing custom serializer for gameobject)
     */
    public void save() {
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
            .registerTypeAdapter(Shape.class, new ShapeDeserializer())
            .create();
        
        String inFile = "";

        //check if its the first time loading up the game
        File f = new File("level.txt");
        if (f.length() == 0) {
            System.out.println("Level not loaded, because level.txt is empty");
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
        System.out.println("Level successfully loaded");
    }

    public void destroy() {
        for (GameObject go : gameObjects) {
            go.destroy();
        }
    }
}

