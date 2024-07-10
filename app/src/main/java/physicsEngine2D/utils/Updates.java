package physicsEngine2D.utils;

import org.joml.Vector2f;

public class Updates {

    /*
     * Update the vector of an object
     */
    public static Vector2f updateVector(Vector2f vec, Vector2f pos, float sin, float cos) {
        float rx = cos * vec.x - sin * vec.y;
        float ry = sin * vec.x + cos * vec.y;

        return new Vector2f(rx + pos.x, ry + pos.y);
    }
    
}
