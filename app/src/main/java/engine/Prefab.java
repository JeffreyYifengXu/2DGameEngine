package engine;

import components.Sprite;
import components.SpriteRenderer;

public class Prefab {
    
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, float zIndex) {
        GameObject block = Window.getScene().createGameObject("Sprite_Object");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);
        
        return block;
    }

    public static GameObject generateSpriteObject(SpriteRenderer spriteRenderer, float sizeX, float sizeY, float zIndex) {
        GameObject block = Window.getScene().createGameObject("Sprite_Object");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        
        block.addComponent(spriteRenderer);
        
        return block;
    }

}
