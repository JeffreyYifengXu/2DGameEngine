package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Renderer.Texture;
import jade.Component;
import jade.Transform;

public class SpriteRenderer extends Component {

    private Vector4f colour;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty = false;

    public SpriteRenderer(Vector4f colour) {
        this.colour = colour;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.colour = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        boolean unchanged = this.lastTransform.equals(this.gameObject.transform);
        if (!unchanged) {
            this.gameObject.transform.copy(this.lastTransform);
            this.isDirty = true;
        }
    }

    public Vector4f getColour() {
        return this.colour;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.gettexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColour(Vector4f colour) {
        if (this.colour.equals(colour)) {
            this.isDirty = true;
            this.colour.set(colour);
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }
}
