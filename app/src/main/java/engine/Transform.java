package engine;

import org.joml.Vector2f;

/**
 * Represent and manipulate transformations (positions and sclae) of gameobjects in the 2D game environment
*/
public class Transform {

    public Vector2f position;
    public Vector2f scale;

    /**
     * Creates new vector for position and scale
     */
    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    /**
     * Takes in position and create new vector for scale
     * @param position
     */
    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    /**
     * Takes in position and scale
     * @param position
     * @param scale
     */
    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    /**
     * Init function
     * @param position
     * @param scale
     */
    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    /**
     * Copy the current position and scale.
     * @return Copied transform object 
     */
    public Transform copy() {
        Transform t = new Transform(new Vector2f(this.position), new Vector2f(this.scale));
        return t;
    }

    /**
     * Copy the current position and scale to the target Transform object 'to'
     * @param to
     */
    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    /**
     * check if the input object is a transform, and its position an scale is the same as the current instance
     */
    @Override 
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false; // Check if o is a type transform

        Transform t = (Transform) o; //Cast as a transform type
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}
