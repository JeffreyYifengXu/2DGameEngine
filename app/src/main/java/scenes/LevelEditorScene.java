package scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import Renderer.DebugDraw;
import components.MouseControls;
import components.Rigidbody;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import engine.Camera;
import engine.GameObject;
import engine.Prefab;
import engine.Transform;
import imgui.ImGui;
import imgui.ImVec2;
import util.AssetPool;

/**
 * Allows admin to edit the levels of the game, contains the main update loop of the game
 * 
 * @author Jeffrey Xu, referencing GamesWithGabe
 */

public class LevelEditorScene extends Scene{

    private GameObject tesObject;
    private Spritesheet sprites;

    private GameObject obj1;
    private SpriteRenderer obj1Sprite;

    MouseControls mouseControls = new MouseControls();

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
        this.camera = new Camera(new Vector2f(-250, 0));
        this.sprites = AssetPool.getSpritesheet("app/assets/images/spritesheets/decorationsAndBlocks.png");
         //Initiate debug line functionalities
         DebugDraw.addLine2D(new Vector2f(0, 0), new Vector2f(800, 800), new Vector3f(1, 0, 0), 120);
         
        if (levelLoaded) {
            System.out.println("Level is loaded, current active game object is: ");
            this.activeGameObject = gameObjects.get(0);
            System.out.println(this.activeGameObject.getName());
            this.activeGameObject.getProperties();
            return;
        }

        // //Object 1
        // mario = new GameObject("Mario", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 4);
        // SpriteRenderer marioSpriteRenderer = new SpriteRenderer();
        // Sprite marioSprite = new Sprite();

        // marioSprite.setTexture(AssetPool.getTexture("app/assets/images/blendImage1.png"));
        // marioSpriteRenderer.setSprite(marioSprite);
        // mario.addComponent(marioSpriteRenderer);
        // mario.addComponent(new Rigidbody());
        // this.addGameObjectToScene(mario);

        // //Object 2
        // GameObject obj2 = new  GameObject("Goomba", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), -1);
        // SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        // Sprite obj2Sprite = new Sprite();

        // obj2Sprite.setTexture(AssetPool.getTexture("app/assets/images/blendImage2.png"));
        // obj2SpriteRenderer.setSprite(obj2Sprite);
        // obj2.addComponent(obj2SpriteRenderer);
        // this.addGameObjectToScene(obj2);

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)), 2);
        obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColour(new Vector4f(1, 1, 0, 1));
        obj1.addComponent(obj1Sprite);
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("app/assets/images/blendImage2.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);
    }

    /**
     * Load the shader and spritesheet from designated directory
     */
    private void loadResources() {
        System.out.println("\n######################################");
        System.out.println("Loading shader... ");

        //Load shader
        AssetPool.getShader("app/assets/shaders/default.glsl");
        System.out.println("Shader successfully loaded\n");

        System.out.println("Loading spritesheets...");
        //Load spritesheets, and create new spritesheet object
        AssetPool.addSpritesheet("app/assets/images/spritesheets/decorationsAndBlocks.png", 
                new Spritesheet(AssetPool.getTexture("app/assets/images/spritesheets/decorationsAndBlocks.png"),
                16, 16, 81, 0));
        System.out.println("SpriteSheets successfully loaded\n");

        AssetPool.getTexture("app/assets/images/blendImage2.png");
    }

    /**
     * The main update function of the game.
     * Render the sprites and update the gameobjects. Called by the main loop from Window class
     */
    @Override
    public void update(float dt) {
        mouseControls.update(dt);

        //Currently no updates done to gameobjects
        for (GameObject go: this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    /**
     * Manage the imgui spritesheet window, register clicks on to the icons
     */
    @Override
    public void imgui() {
        ImGui.begin("test window");

        ImVec2 windowPos = new ImVec2();
        ImVec2 windowSize = new ImVec2();
        ImVec2 itemSpacing = new ImVec2();

        ImGui.getWindowPos(windowPos);
        ImGui.getWindowSize(windowSize);
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < sprites.size(); i++) { //Loop through all sprites read in from sprite sheets
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2; //Multipling by a number to adjust the size of the icons
            float spriteHeight = sprite.getHeight() * 2;
            
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i); //Assign a different id for each sprite
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject obj = Prefab.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                mouseControls.pickupObject(obj);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos); //Get the last button position
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
