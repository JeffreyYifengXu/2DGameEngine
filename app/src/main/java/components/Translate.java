package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import edittool.PropertiesWindow;
import engine.GameObject;
import engine.Prefab;
import engine.Window;
import util.Settings;

/*
* Displays arrows when there is an active gameObject.
* Allows for multiplying the gameObject
 */
public class Translate extends Component{

    //Arrows as gameobject
    private GameObject xArrow;
    private GameObject yArrow;
    private GameObject xArrowLeft;
    private GameObject yArrowDown;

    //Arrows sprites
    private SpriteRenderer xArrowSprite;
    private SpriteRenderer yArrowSprite;
    private SpriteRenderer xArrowLeftSprite;
    private SpriteRenderer yArrowDownSprite;

    //Offsets
    private Vector2f xOffset = new Vector2f(68, 2);
    private Vector2f yOffset = new Vector2f(25, 66);
    private Vector2f xLeftOffset = new Vector2f(37, -18);
    private Vector2f yDownOffset = new Vector2f(-8, 45);

    //Colour
    private Vector4f xAxisColour = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColour = new Vector4f(0, 1, 0, 1);

    //Stores the propertiesWindow object which has a reference to active game object
    private PropertiesWindow propertiesWindow;

    private GameObject prevActivGameObject = null;
    private GameObject activeGameObject = null;
    
    public Translate(Spritesheet spriteSheet, PropertiesWindow propertiesWindow) {

        this.xArrow = Prefab.generateSpriteObject(spriteSheet.getSprite(1), 16, 48, 1);
        this.yArrow = Prefab.generateSpriteObject(spriteSheet.getSprite(1), 16, 48, 1);
        this.xArrowLeft = Prefab.generateSpriteObject(spriteSheet.getSprite(1), 16, 48, 1);
        this.yArrowDown = Prefab.generateSpriteObject(spriteSheet.getSprite(1), 16, 48, 1);

    
        this.xArrow.setUid(1);
        this.yArrow.setUid(2);
        this.xArrowLeft.setUid(3);
        this.yArrowDown.setUid(4);

        this.xArrowSprite = xArrow.getComponent(SpriteRenderer.class);
        this.yArrowSprite = yArrow.getComponent(SpriteRenderer.class);
        this.xArrowLeftSprite = xArrowLeft.getComponent(SpriteRenderer.class);
        this.yArrowDownSprite = yArrowDown.getComponent(SpriteRenderer.class);

        //Add arrow objects to the scene
        Window.getScene().addGameObjectToScene(this.xArrow);
        Window.getScene().addGameObjectToScene(this.yArrow);
        Window.getScene().addGameObjectToScene(this.xArrowLeft);
        Window.getScene().addGameObjectToScene(this.yArrowDown);


        this.propertiesWindow = propertiesWindow;
    }

    @Override
    public void start() {
        this.xArrow.transform.rotation = 90;
        this.yArrow.transform.rotation = 180;
        this.xArrowLeft.transform.rotation = 270; 


        this.xArrow.setNoSerialize();
        this.yArrow.setNoSerialize();
        this.xArrowLeft.setNoSerialize();
        this.yArrowDown.setNoSerialize();
    }

    @Override
    public void update(float dt) {
        if (this.activeGameObject != null) { //If there is an active game Object
            //Put the arrows to the active game object
            int activeUid = this.activeGameObject.getUid();
            Vector2f activePos = this.activeGameObject.transform.position;

            if (activeUid > 4) {//active gameobject is not the arrows
                //Get the location of active game object
                Vector2f xPos = this.xArrow.transform.position;
                Vector2f yPos = this.yArrow.transform.position;
                Vector2f xLeftPos = this.xArrowLeft.transform.position;
                Vector2f yDownPos = this.yArrowDown.transform.position;

                xPos.set(activePos);
                yPos.set(activePos);
                xLeftPos.set(activePos);
                yDownPos.set(activePos);

                xPos.add(this.xOffset);
                yPos.add(this.yOffset);
                xLeftPos.sub(this.xLeftOffset);
                yDownPos.sub(this.yDownOffset);
                

                this.prevActivGameObject = this.activeGameObject; //Store the current active game object
            }

            else { // Active game object is the arrow
                //Create a new object to the direction that the arrow is pointing.
                System.out.println(activeGameObject.getUid());
                GameObject newObject = Prefab.generateSpriteObject(this.prevActivGameObject.getComponent(SpriteRenderer.class),
                Settings.GRID_WIDTH, Settings.GRID_HEIGHT, 0);

                if (activeUid == 1) { //If clicked onto the right arrows, duplicate current block to the right
                    newObject.transform.position.set(prevActivGameObject.transform.position);
                    newObject.transform.position.x += Settings.GRID_WIDTH;
                } else if (activeUid == 2) { //If clicked onto the up arrow, duplicate current block upward.
                    newObject.transform.position.set(prevActivGameObject.transform.position);
                    newObject.transform.position.y += Settings.GRID_HEIGHT;
                } else if (activeUid == 3) {
                    newObject.transform.position.set(prevActivGameObject.transform.position);
                    newObject.transform.position.x -= Settings.GRID_HEIGHT;
                } else {
                    newObject.transform.position.set(prevActivGameObject.transform.position);
                    newObject.transform.position.y -= Settings.GRID_HEIGHT;
                }

                Window.getScene().addGameObjectToScene(newObject);

                propertiesWindow.setActiveGameObject(newObject);
            }
        }

        this.activeGameObject = propertiesWindow.getActiveGameObject(); //Update current active game object

        //Decide arrows visibility
        if (this.activeGameObject != null) {
            setVisible();
        } else {
            setInvisible();
        }
    }

    public GameObject getXArrowObject() {
        return this.xArrow;
    }

    public GameObject getYArrowObject() {
        return this.yArrow;
    }

    /*
     * If there is an active gameobject, the arrows will appear on the active game Object
     */
    private void setVisible() {
        this.xArrowSprite.setColour(xAxisColour);
        this.yArrowSprite.setColour(yAxisColour);
        this.xArrowLeftSprite.setColour(xAxisColour);
        this.yArrowDownSprite.setColour(xAxisColour);
    }

    private void setInvisible() {
        this.activeGameObject = null;
        this.xArrowSprite.setColour(new Vector4f(0, 0, 0, 0));
        this.yArrowSprite.setColour(new Vector4f(0, 0, 0, 0));
        this.xArrowLeftSprite.setColour(new Vector4f(0, 0, 0, 0));
        this.yArrowDownSprite.setColour(new Vector4f(0, 0, 0, 0));
    }
}




