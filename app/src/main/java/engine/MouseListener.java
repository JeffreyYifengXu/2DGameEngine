package engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * Manage and process all mouse input
 * @author Jeffrey Xu, referencing GameWithGabe
 */
public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double x, y, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean isDragging;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();


    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.x = 0.0;
        this.y = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;

        this.isDragging = false;
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
    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = xpos;
        get().y = ypos;
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

    public static float getScreenX() {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / (float) get().gameViewportSize.x) * 1920f;
        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY() - get().gameViewportPos.y; //Y coordinates are flipped in opengl
        currentY = 1080f -((currentY / (float) get().gameViewportSize.y) * 1080f);
        return currentY  ;
    }

    public static float getOrthoX() {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / (float) get().gameViewportSize.x) * 2.0f - 1.0f;

        Vector4f tmp = new Vector4f(currentX, 0, 0, 1); //Need vector4f to do matrix multiplication with another vector4f

        //Convert screenn coords to world coords
        tmp.mul(Window.getScene().camera().getInverseProjection()).
            mul(Window.getScene().camera().getInverseView());
        currentX = tmp.x;
        
        return currentX;
    }

    public static float getOrthoY() {
        float currentY = getY() - get().gameViewportPos.y; //Y coordinates are flipped in opengl
        currentY = -((currentY / (float) get().gameViewportSize.y) * 2.0f - 1.0f);

        Vector4f tmp = new Vector4f(0, currentY, 0, 1); //Need vector4f to do matrix multiplication with another vector4f

        //Convert screenn coords to world coords
        tmp.mul(Window.getScene().camera().getInverseProjection()).
            mul(Window.getScene().camera().getInverseView());
        currentY = tmp.y;
        
        return currentY  ;
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

    public static void setGameViewportPos(Vector2f gameViewportPos) {
        get().gameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f gameViewportSize) {
        get().gameViewportSize.set(gameViewportSize);
    }
}
