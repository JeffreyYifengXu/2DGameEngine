package engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an in game object. 
 * @author Jeffrey, referencing GmaesWithGabe
 */

public class GameObject {

    private String name;
    private List<Component> components;

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
    }

    /**
     * Initializes the gameobject
     * @param name
     * @param transform
     */

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
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
}
