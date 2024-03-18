package jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import util.AssetPool;

public class LevelEditorScene extends Scene{

    GameObject tesObject;
    
    public LevelEditorScene() {
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject mario = new  GameObject("Mario", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        mario.addComponent(new SpriteRenderer(AssetPool.getTexture("app/assets/images/testImage.png")));
        this.addGameObjectToScene(mario);

        GameObject goomba = new  GameObject("Goomba", new Transform(new Vector2f(400, 400), new Vector2f(256, 256)));
        goomba.addComponent(new SpriteRenderer(AssetPool.getTexture("app/assets/images/testImage2.png")));
        this.addGameObjectToScene(goomba);
        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("app/assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {
        // System.out.println("FPS: " + 1.0f/dt);

        for (GameObject go: this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }
}
