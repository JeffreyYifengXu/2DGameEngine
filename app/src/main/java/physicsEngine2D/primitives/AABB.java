package physicsEngine2D.primitives;

public class AABB extends Shape{

    private int width;
    private int height;

    public AABB(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
