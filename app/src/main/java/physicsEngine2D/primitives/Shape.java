package physicsEngine2D.primitives;

import org.joml.Vector2f;

public abstract  class Shape {
    public Vector2f[] vertices;
    
    protected boolean AABBupdateRequired;
    protected AABB aabb;


    public abstract void setVertices(Vector2f pos);

    public abstract AABB getAABB();
    
}
