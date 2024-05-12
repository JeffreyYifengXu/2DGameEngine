package scenes;

import org.joml.Vector2f;
import components.GridLines;
import components.MouseControls;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import editor.EditorCamera;

import engine.Camera;
import engine.GameObject;
import engine.Prefab;
import engine.Transform;
import imgui.ImGui;
import imgui.ImVec2;
import util.AssetPool;
import util.Settings;

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

    GameObject levelEditorUtil = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

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
        this.sprites = AssetPool.getSpritesheet("app/assets/images/spritesheets/tilemap.png");
        
        levelEditorUtil.addComponent(new MouseControls());
        levelEditorUtil.addComponent(new GridLines());
        levelEditorUtil.addComponent(new EditorCamera(camera));
        // this.sprites = AssetPool.getSpritesheet("app/assets/images/spritesheets/decorationsAndBlocks.png");

        // printAllGameObjects();
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
        //Mario spritesheet
        // AssetPool.addSpritesheet("app/assets/images/spritesheets/decorationsAndBlocks.png", 
        //         new Spritesheet(AssetPool.getTexture("app/assets/images/spritesheets/decorationsAndBlocks.png"),
        //         16, 16, 81, 0));

        //Platform game sprite sheet
        AssetPool.addSpritesheet("app/assets/images/spritesheets/tilemap.png", 
            new Spritesheet(AssetPool.getTexture("app/assets/images/spritesheets/tilemap.png"),
            18, 18, 180, 0));

        System.out.println("SpriteSheets successfully loaded\n");

        //Ensure each gameobject have unique texture
        for (GameObject gobj : gameObjects) {
            if (gobj.getComponent(SpriteRenderer.class) != null) {

                SpriteRenderer spr = gobj.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        } 
    }

    /**
     * The main update function of the game.
     * Render the sprites and update the gameobjects. Called by the main loop from Window class
     */
    @Override
    public void update(float dt) {
        levelEditorUtil.update(dt);

        //Currently no updates done to gameobjects
        for (GameObject go: this.gameObjects) {
            go.update(dt);
        }
    }

    @Override
    public void render() {
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
            //When mouse button is clicked onto a block icon
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject obj = Prefab.generateSpriteObject(sprite, Settings.GRID_HEIGHT, Settings.GRID_WIDTH);
                levelEditorUtil.getComponent(MouseControls.class).pickupObject(obj);
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
