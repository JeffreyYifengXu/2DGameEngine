package physicsEngine2D.primitives;

import org.joml.Vector2f;

public class AABB extends Shape{

    private int width;
    private int height;

    public AABB(int width, int height) {
        this.width = width;
        this.height = height;
        this.vertices = new Vector2f[4];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setVertices(Vector2f pos) {
        this.vertices[0] = new Vector2f(pos);
        this.vertices[1] = new Vector2f(pos.x + width, pos.y);
        this.vertices[2] = new Vector2f(pos.x + width, pos.y + height);
        this.vertices[3] = new Vector2f(pos.x, pos.y + height);
    }
}
