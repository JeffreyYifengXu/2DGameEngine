package engine;

import java.util.ArrayList;
import java.util.List;

import components.Component;

/**
 * Represents an in game object. 
 * @author Jeffrey, referencing GmaesWithGabe
 */

public class GameObject {
    private boolean serialize = true;
    private static int ID_COUNTER = 0;
    private int uid = -1;


    private String name;
    private List<Component> components;

    public transient Transform transform;

    /**
     * Initializes the gameobject
     * @param name
     */
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.uid = ID_COUNTER++;
    }

    /**
     * 
     * @param <T>
     * @param componentClass
     * @return
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        //Abstract class T of type Component. Returned type is a subclass of component. Need to take in 
        //the same class thats returning

        for (Component c : components) {
            //If componentClass is a superclass/Abstract class of c or the same
            if (componentClass.isAssignableFrom(c.getClass())) {
                try{
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }

    /**
     * Removes a component from the current game object, given that the target component is in the gameobject
     * @param <T>
     * @param componentClass
     */

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    /**
     * Add a component to current gameobject
     * @param component
     */

    public void addComponent(Component c) {
        c.generateId(); //generate id for the object
        this.components.add(c);
        c.gameObject = this;
    }

    /**
     * Update all components of current gameobject
     * @param deltatime
     */
    public void update(float dt) {
        for (int i=0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    /**
     * Called once, to initiate all components of current gameobject
     */
    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    /**
     * Update any changes done to current gameObject through imgui.
     */
    public void imgui() {
        for (Component c : components) {
            c.imgui(); //Exposes the components variables to the user through imgui
        }
    }

    public String getName() {
        return this.name;
    }

    public void getProperties() {
        System.out.println("name: " + this.name);
        System.out.println("Position: " + this.transform.position);
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
        if (ID_COUNTER > this.uid) {
            return;
        }
        else{
            ID_COUNTER++;
        }
    }

    public List<Component> getAllComponent() {
        return this.components;
    }

    public void setNoSerialize() {
        this.serialize = false;
    }

    public boolean doSerialization() {
        return this.serialize;
    }
}
