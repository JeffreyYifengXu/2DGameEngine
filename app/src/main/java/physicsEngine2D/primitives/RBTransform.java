package physicsEngine2D.primitives;

import org.joml.Vector2f;

public class RBTransform {
    public Vector2f position;

    public Vector2f velocity = new Vector2f(0, 0);
    public Vector2f acceleration = new Vector2f(0, 0);
    public Vector2f force = new Vector2f(0, 0);

    public float restitution = 0.4f;
    public float mass;
    public float inverseMass;
    public Shape shape;

    public float rotation = 0f;
    public float rotationVelocity = 0;

    public RBTransform(Vector2f position, float mass, Shape shape, double angle) {
        this.position = position;
        this.shape = shape;
        this.mass = mass;
        if (mass != 0) {
            this.inverseMass = 1f / mass;
        } else {
            this.inverseMass = 0;
        }

        shape.setVertices(position);
    }
}
