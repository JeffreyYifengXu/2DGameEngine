package components;

import org.joml.Vector2f;

import renderer.Texture;

public class Sprite {

    private Texture texture = null;
    private Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
            };
    
    private float width, height;

    public Texture getTexture() {
        return this.texture;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return this.height;
    }

    public  Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public void setTexture(Texture tex) {
        this.texture = tex;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public int getTexId() {
        if (texture == null) {
            return -1;
        } else {
            return texture.getId();
        }
    }
}
