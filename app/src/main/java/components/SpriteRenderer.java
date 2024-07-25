package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.Transform;
import imgui.ImGui;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f colour = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

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

    @Override
    public void editorUpdate(float dt) {
        boolean unchanged = this.lastTransform.equals(this.gameObject.transform);
        if (!unchanged) {
            this.gameObject.transform.copy(this.lastTransform);
            this.isDirty = true;
        }
    }
 
    /**
     * Allows for real time colour change of selected 
     * object using the imgui menu
     */
    @Override
    public void imgui() {
        float[] imColour = {colour.x, colour.y, colour.z, colour.w};
        if (ImGui.colorPicker4("Colour Picker: ", imColour)) {
            this.colour.set(imColour[0], imColour[1], imColour[2], imColour[3]);
            this.isDirty = true;
        }

    }

    public void setDirty() {
        this.isDirty = true;
    }

    public Vector4f getColour() {
        return this.colour;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColour(Vector4f colour) {
        if (!this.colour.equals(colour)) {
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

    public void setTexture(Texture texture) {
        this.sprite.setTexture(texture);
    }

    /**
     * Debug function
     */
    public Sprite getSprite() {
        return this.sprite;
    }

}
