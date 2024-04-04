package components;

import engine.Component;

public class FontRenderer extends Component{

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Font Renderer");
        } else {
            System.out.println("no font renderer");
        }
        
    }

    @Override
    public void update(float dt) {

    }
}
