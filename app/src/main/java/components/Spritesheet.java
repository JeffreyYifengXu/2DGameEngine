package components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import renderer.Texture;

public class Spritesheet {

    private Texture texture;
    private List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;//Bottom left corner of top left sprite in the spritesheet

        for (int i=0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight - 2) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth - 2) / (float)texture.getWidth();
            float leftX = (currentX + 2)/ (float)texture.getWidth();
            float bottomY = (currentY + 2)/ (float)texture.getHeight();

            Vector2f[] texCoords = {
                new Vector2f(rightX, topY),
                new Vector2f(rightX, bottomY),
                new Vector2f(leftX, bottomY),
                new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();//Create new sprite
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) { //Move onto the second line of the spritesheet
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    public int size() {
        return sprites.size();
    }
}
