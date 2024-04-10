package engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Manage and process all mouse input
 * @author Jeffrey Xu, referencing GameWithGabe
 */
public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double x, y, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.x = 0.0;
        this.y = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    /**
     * @return current MouseListenser instance
     */
    public static MouseListener get() {
        //first time calling get, create a new instance
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    /**
     * Callback function for the movement of the mouse 
     * @param window
     * @param x
     * @param y
     */
    public static void mousePosCallback(long window, double x, double y) {
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = x;
        get().y = y;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    /**
     * Call back function for the mouse buttons
     * @param window
     * @param button
     * @param action
     * @param mods
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            //Ensure that only conventional mouse buttons were pressed
            if (button < get().mouseButtonPressed.length) {
            get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }

    /**
     * Call back function for the scrolling on the mouse
     * @param window
     * @param xOffset
     * @param yOffset
     */
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    /**
     * Update at the end of the each frame
     */
    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().x;
        get().lastY = get().y;
    }


    //##############Get functions################//
    public static float getX() {
        return (float)get().x;
    }

    public static float getY() {
        return (float)get().y;
    }

    public static float getDx() {
        return (float)(get().lastX - get().x);
    }

    public static float getXDy() {
        return (float)(get().lastY - get().y);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }




}
