package physicsEngine2D.primitives;

import org.joml.Vector2f;

public class AABB {

    public Vector2f min;
    public Vector2f max;

    public AABB(Vector2f max, Vector2f min) {
        this.max = max;
        this.min = min;
    }

    public AABB(float maxX, float maxY, float minX, float minY) {
        this.max = new Vector2f(maxX, maxY);
        this.min = new Vector2f(minX, minY);
    }
}
