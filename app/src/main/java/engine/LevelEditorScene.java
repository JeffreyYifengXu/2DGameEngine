package engine;

import org.joml.Vector2f;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import util.AssetPool;

public class LevelEditorScene extends Scene{

    private GameObject tesObject;
    private Spritesheet sprites;

    private GameObject mario;
    
    public LevelEditorScene() {
    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        this.sprites = AssetPool.getSpritesheet("app/assets/images/spritesheet.png");

        mario = new  GameObject("Mario", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        mario.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(mario);

        GameObject goomba = new  GameObject("Goomba", new Transform(new Vector2f(400, 400), new Vector2f(256, 256)));
        goomba.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        this.addGameObjectToScene(goomba);
    }

    private void loadResources() {
        AssetPool.getShader("app/assets/shaders/default.glsl");

        AssetPool.addSpritesheet("app/assets/images/spritesheet.png", 
                new Spritesheet(AssetPool.getTexture("app/assets/images/spritesheet.png"),
                16, 16, 26, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTImeLeft = 0.0f;

    @Override
    public void update(float dt) {
        spriteFlipTImeLeft -= dt;
        if (spriteFlipTImeLeft <= 0) {
            spriteFlipTImeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex = 0;
            }
            mario.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }

        for (GameObject go: this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }
}
