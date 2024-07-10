package physicsEngine2D.primitives;

import org.joml.Vector2f;

public abstract  class Shape {
    public Vector2f[] vertices;


    public abstract void setVertices(Vector2f pos);
    
}
