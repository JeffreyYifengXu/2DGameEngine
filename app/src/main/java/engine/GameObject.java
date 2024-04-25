package engine;

import java.util.ArrayList;
import java.util.List;

import components.Component;
import components.SpriteRenderer;

/**
 * Represents an in game object. 
 * @author Jeffrey, referencing GmaesWithGabe
 */

public class GameObject {

    private String name;
    private List<Component> components;
    private int zIndex;

    public Transform transform;

    /**
     * Initializes the gameobject
     * Sets the name and initialize new array for components and transform
     * @param name
     */
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = 0;
    }

    /**
     * Initializes the gameobject
     * @param name
     * @param transform
     */
    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
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

    public int zIndex() {
        return this.zIndex;
    }

    public String getName() {
        return this.name;
    }

    public void getProperties() {
        System.out.println("name: " + this.name);
        System.out.println("zIndex: " + this.zIndex);
        System.out.println("Position: " + this.transform.position);
    }
}
