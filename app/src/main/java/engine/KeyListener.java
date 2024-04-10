package engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Manage and process all keyboard inputs
 * @author Jeffrey, referencing GameswithGabe
 */

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {

    }

    /**
     * returns the current instanc, create a new KeyListener object if its null
     * @return
     */
    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    /**
     * Record the key press.
     * @param window
     * @param key
     * @param scancode
     * @param action
     * @param mods
     */
    public static void KeyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true; 
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    /**
     * Check if the keycode key is pressed or not
     * @param keyCode
     * @return
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
