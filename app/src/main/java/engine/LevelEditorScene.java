package engine;

import org.joml.Vector2f;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
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

        this.sprites = AssetPool.getSpritesheet("app/assets/images/spritesheet.png");

        mario = new  GameObject("Mario", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 4);
        mario.addComponent(new SpriteRenderer(new Sprite(
            AssetPool.getTexture("app/assets/images/blendImage1.png")
        )));
        this.addGameObjectToScene(mario);

        GameObject goomba = new  GameObject("Goomba", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), -1);
        goomba.addComponent(new SpriteRenderer(new Sprite(
            AssetPool.getTexture("app/assets/images/blendImage2.png")
        )));
        this.addGameObjectToScene(goomba);
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
}
