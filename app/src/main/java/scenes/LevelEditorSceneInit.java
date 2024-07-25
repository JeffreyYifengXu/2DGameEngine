package scenes;

import org.joml.Vector2f;
import components.GridLines;
import components.MouseControls;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import components.Translate;
import edittool.EditorCamera;
import engine.Camera;
import engine.GameObject;
import engine.Prefab;
import engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import util.AssetPool;
import util.Settings;

/**
 * Allows admin to edit the levels of the game, contains the main update loop of the game
 * 
 * @author Jeffrey Xu, referencing GamesWithGabe
 */

public class LevelEditorSceneInit extends SceneInit{

    private Spritesheet sprites;
    private Spritesheet arrowSpritesheet;
    private GameObject levelEditorUtil;

    public LevelEditorSceneInit() {

    }

    /**
     * Makes the preparation before the main game loop starts, called once when game starts.
     * Load resources: shader and spirte sheet.
     * Sets up the game camera.
     * Creates the gameObjects: main character, enimies etc.
     */
    @Override
    public void init(Scene scene) {
        this.sprites = AssetPool.getSpritesheet("tilemap");
        this.arrowSpritesheet = AssetPool.getSpritesheet("arrows");
        
        this.levelEditorUtil = scene.createGameObject("LevelEditor");
        levelEditorUtil.setNoSerialize();
        levelEditorUtil.addComponent(new MouseControls());
        levelEditorUtil.addComponent(new GridLines()); 
        levelEditorUtil.addComponent(new EditorCamera(scene.camera()));
        levelEditorUtil.addComponent(new Translate(this.arrowSpritesheet, Window.getImGuiLayer().getPropertiesWindow()));

        scene.addGameObjectToScene(levelEditorUtil);
    }

    /**
     * Load the shader and spritesheet from designated directory
     */
    @Override
    public void loadResources(Scene scene) {
        System.out.println("\n######################################");
        System.out.println("Loading shader... ");

        //Load shader
        AssetPool.getShader("app/assets/shaders/default.glsl");
        System.out.println("Shader successfully loaded\n");
        System.out.println("Loading spritesheets...");

        //Load spritesheets, and create new spritesheet object
        //Platform game sprite sheet
        AssetPool.addSpritesheet("tilemap", 
            new Spritesheet(AssetPool.getTexture("app/assets/images/spritesheets/tilemap.png"),
            18, 18, 180, 0));
        
        //Arrows sprite sheet
        AssetPool.addSpritesheet("arrows", 
            new Spritesheet(AssetPool.getTexture("app/assets/images/arrows.png"),
            24, 48, 3, 0));

        System.out.println("SpriteSheets successfully loaded\n");

        //Ensure each gameobject have unique texture
        for (GameObject gobj : scene.getGameObjects()) {
            if (gobj.getComponent(SpriteRenderer.class) != null) {

                SpriteRenderer spr = gobj.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        }
    }

    /**
     * Manage the imgui game block selection window, register clicks on to the icons
     */
    @Override
    public void imgui() {
        ImGui.begin("Level Editor utilities");
        levelEditorUtil.imgui();
        ImGui.end();

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
                GameObject obj = Prefab.generateSpriteObject(sprite, Settings.GRID_HEIGHT, Settings.GRID_WIDTH, 0);
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






