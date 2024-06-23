package physicsEngine2D;

import org.joml.Vector2f;

import components.Component;
import physicsEngine2D.primitives.Shape;

public class RigidBody extends Component{

    private Vector2f position;
    private float Rotation;
    private float mass;
    private Vector2f velocity;
    private Vector2f acceleration;
    private Shape shape;

    public RigidBody(Vector2f position, float mass, Shape shape) {
        this.position = position;
        this.mass = mass;
        this.shape = shape;
        this.velocity = new Vector2f();
        this.acceleration = new Vector2f(0, -10);
    }

    public RigidBody(Vector2f position, float mass, Shape shape, Vector2f acceleration) {
        this.position = position;
        this.mass = mass;
        this.shape = shape;
        this.velocity = new Vector2f();
        this.acceleration = acceleration;
    }

    public Shape getShape() {
        return this.shape;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return Rotation;
    }

    public float getMass() {
        return mass;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public Vector2f getAcceleration() {
        return acceleration;
    }

    
}
