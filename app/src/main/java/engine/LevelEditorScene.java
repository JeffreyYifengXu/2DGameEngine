package engine;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import util.AssetPool;

/**
 * Allows admin to edit the levels of the game, contains the main update loop of the game
 * 
 * @author Jeffrey Xu, referencing GamesWithGabe
 */

public class LevelEditorScene extends Scene{

    private GameObject tesObject;
    private Spritesheet sprites;

    private GameObject mario;

    public LevelEditorScene() {

    }

    /**
     * Makes the preparation before the main game loop starts, called once when game starts.
     * Load resources: shader and spirte sheet.
     * Sets up the game camera.
     * Creates the gameObjects: main character, enimies etc.
     */
    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f());

        if (levelLoaded) {
            return;
        }

        this.sprites = AssetPool.getSpritesheet("app/assets/images/spritesheet.png");

        //Object 1
        mario = new  GameObject("Mario", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 4);
        SpriteRenderer marioSpriteRenderer = new SpriteRenderer();
        Sprite marioSprite = new Sprite();
        // marioSpriteRenderer.setColour(new Vector4f(1, 0, 0, 0));
        marioSprite.setTexture(AssetPool.getTexture("app/assets/images/blendImage1.png"));
        marioSpriteRenderer.setSprite(marioSprite);
        mario.addComponent(marioSpriteRenderer);
        this.addGameObjectToScene(mario);
        this.activeGameObject = mario;

        //Object 2
        GameObject obj2 = new  GameObject("Goomba", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), -1);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();

        obj2Sprite.setTexture(AssetPool.getTexture("app/assets/images/blendImage2.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);
 
        // Gson gson = new GsonBuilder()
        //     .setPrettyPrinting()
        //     .registerTypeAdapter(Component.class, new DeserializerComponent())
        //     .registerTypeAdapter(GameObject.class, new GameObjectDeserializer()) // Use custom deserializer
        //     .create();

        // // System.out.println(gson.toJson("Hello World"));
        // // System.out.println(gson.toJson(marioSprite));
        // String serialized = gson.toJson(mario);
        // System.out.println(serialized);
        // GameObject mario = gson.fromJson(serialized, GameObject.class);
        // System.out.println(mario);
    }

    /**
     * Load the shader and spritesheet from designated directory
     */
    private void loadResources() {
        AssetPool.getShader("app/assets/shaders/default.glsl");

        AssetPool.addSpritesheet("app/assets/images/spritesheet.png", 
                new Spritesheet(AssetPool.getTexture("app/assets/images/spritesheet.png"),
                16, 16, 26, 0));
    }

    /**
     * The main update function of the game.
     * Render the sprites and update the gameobjects. Called by the main loop from Window class
     */
    @Override
    public void update(float dt) {

        for (GameObject go: this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("test window");
        ImGui.text("Testing testing");
        ImGui.end();
    }
}
