package physicsEngine2D.physics;

import org.joml.Vector2f;

public class CollisionHelper {
    private Vector2f normal;
    private float depth;

    public CollisionHelper(Vector2f normal, float depth) {
        this.normal = normal;
        this.depth = depth;
    }

    public Vector2f getNormal() {
        return new Vector2f(normal);
    }

    public float getdepth() {
        return depth;
    }
}
