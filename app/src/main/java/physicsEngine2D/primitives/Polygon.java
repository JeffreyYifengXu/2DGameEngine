package physicsEngine2D.primitives;

import org.joml.Vector2f;

public class Polygon extends Shape{

    private int width;
    private int height;

    public Polygon(int width, int height) {
        this.width = width;
        this.height = height;
        this.vertices = new Vector2f[4];
        this.AABBupdateRequired = true;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void setVertices(Vector2f pos) {
        this.vertices[0] = new Vector2f(pos);
        this.vertices[1] = new Vector2f(pos.x + width, pos.y);
        this.vertices[2] = new Vector2f(pos.x + width, pos.y + height);
        this.vertices[3] = new Vector2f(pos.x, pos.y + height);
    }

    @Override
    public AABB getAABB() {
        if (this.AABBupdateRequired) {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = - Float.MAX_VALUE;
            float maxY = - Float.MAX_VALUE;
            
            for (int i=0; i < vertices.length; i++) {
                Vector2f vertex = vertices[i];
                
                if (vertex.x < minX) {
                    minX = vertex.x;
                } if (vertex.x > maxX) {
                    maxX = vertex.x;
                } if (vertex.y < minY) {
                    minY = vertex.y;
                } if (vertex.y > maxY) {
                    maxY = vertex.y;
                }
            }
            this.aabb = new AABB(maxX, maxY, minX, minY);
        }
        this.AABBupdateRequired = true;
        return this.aabb;
    }
}
