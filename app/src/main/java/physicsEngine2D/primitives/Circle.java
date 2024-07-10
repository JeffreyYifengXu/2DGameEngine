package physicsEngine2D.primitives;

import org.joml.Vector2f;

public class Circle extends Shape {

    private float radius;
    private Vector2f centre;

    public Circle(float radius) {
        this.radius = radius;
    }

    public void setVertices(Vector2f pos) {
        this.centre = pos;
    }
    
}
