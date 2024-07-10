package physicsEngine2D.primitives;

import org.joml.Vector2f;

public class RBTransform {
    public Vector2f position;

    public Vector2f velocity = new Vector2f();
    public Vector2f acceleration = new Vector2f();
    public Vector2f force = new Vector2f();

    public float restitution = 0;
    public float mass;
    public Shape shape;

    public float rotation = 0f;
    public float rotationVelocity = 0;

    public RBTransform(Vector2f position, float mass, Shape shape, double angle) {
        this.position = position;
        this.mass = mass;
        this.shape = shape;
    }

    public void setAcceleration(Vector2f acc) {
        this.acceleration = new Vector2f(acc);
    }
}
