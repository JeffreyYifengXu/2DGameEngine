package physicsEngine2D.primitives;

import org.joml.Vector2f;

public class Circle extends Shape {

    private float radius;
    private Vector2f centre;

    public Circle(float radius) {
        this.radius = radius;
    }

    @Override
    public void setVertices(Vector2f pos) {
        this.centre = pos;
    }

    @Override
    public AABB getAABB() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAABB'");
    }
    
}
