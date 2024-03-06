package jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene{
    
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("Inside level editor scene.");
    }

    @Override
    public void update(float dt) {

        System.out.println("" + (1.0f/dt) + "FPS");

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }
        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt; //Take away the time that's passed in the last frame
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
            // System.out.println("Changing color!!");
        }
        else if (changingScene) { //change scene
            Window.changeScene(1);
        }

    }

}
