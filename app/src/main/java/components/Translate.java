package components;

import org.joml.Vector4f;

import edittool.PropertiesWindow;
import engine.GameObject;
import engine.Prefab;
import engine.Window;

/*
* Displays arrows when there is an active gameObject.
* Allows for multiplying the gameObject
 */
public class Translate extends Component{

    //Arrows as gameobject
    private GameObject xArrow;
    private GameObject yArrow;

    //Arrows sprites
    private SpriteRenderer xArrowSprite;
    private SpriteRenderer yArrowSprite;

    //Colour
    private Vector4f xAxisColour = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColour = new Vector4f(0, 1, 0, 1);

    //Stores the propertiesWindow object which has a reference to active game object
    private PropertiesWindow propertiesWindow;
    private GameObject activeGameObject = null;
    
    public Translate(Spritesheet spriteSheet, PropertiesWindow propertiesWindow) {

        this.xArrow = Prefab.generateSpriteObject(spriteSheet.getSprite(1), 16, 48, 0);
        this.yArrow = Prefab.generateSpriteObject(spriteSheet.getSprite(1), 16, 48, 0);

        this.xArrowSprite = xArrow.getComponent(SpriteRenderer.class);
        this.yArrowSprite = yArrow.getComponent(SpriteRenderer.class);

        //Add arrow objects to the scene
        Window.getScene().addGameObjectToScene(this.xArrow);
        Window.getScene().addGameObjectToScene(this.yArrow);

        this.propertiesWindow = propertiesWindow;
    }

    @Override
    public void start() {
        this.xArrow.transform.rotation = 90;
        this.yArrow.transform.rotation = 180;
        this.xArrow.setNoSerialize();
        this.yArrow.setNoSerialize();
    }

    @Override
    public void update(float dt) {
        if (this.activeGameObject != null) { //If there is an active game Object
            //Get the location of active game object
            this.xArrow.transform.position.set(this.activeGameObject.transform.position);
            this.yArrow.transform.position.set(this.activeGameObject.transform.position);
        }

        this.activeGameObject = propertiesWindow.getActiveGameObject();

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
    }

    private void setInvisible() {
        this.activeGameObject = null;
        this.xArrowSprite.setColour(new Vector4f(0, 0, 0, 0));
        this.yArrowSprite.setColour(new Vector4f(0, 0, 0, 0));
    }
}




