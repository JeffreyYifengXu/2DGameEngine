package engine;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import imgui.ImGui;
import renderer.DebugDraw;
import renderer.Framebuffer;
import renderer.PickingTexture;
import renderer.Renderer;
import renderer.Shader;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.Scene;
import util.AssetPool;

/**
 * Manages the window for the game
 */
public class Window {
    // private static final int GLFW_MOUSE_BUTTON_LEFT = 0;
    private int width, height;
    private String title;
    
    private long glfwWindow; //Stores the id that points to the generated window
    private ImGuiLayer imguiLayer; //Stores all imgui stuff (User menus etc)
    private Framebuffer framebuffer; //Manages frambuffer
    private PickingTexture pickingTexture; //Manages 
    
    public float r, g, b, a;

    private static Window window = null;

    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "My Mario";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    /**
     * Change scene depending on the input parameter
     * LevelEditorScene: For editing
     * LevelScene: For actual game play
     * @param newScene
     */
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "unknow scene '" + newScene + "'";
                break;
        }
        
        
        currentScene.load(); //load current level
        currentScene.init(); 
        currentScene.start();
    }

    /**
     * @return current active window
     */
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    /**
     * @return currentScene
     */
    public static Scene getScene() {
        return currentScene;
    }

    /**
     * Called once, initialize the window and starts the loop
     */
    @SuppressWarnings("null")
    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");
        System.out.println("######################################\n");

        init();
        loop();

        //Memory menagement: Free memory 
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Initialize the window, make preparations using GLFW library
     */
    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // //Retrieve current monitor info
        // int widthMM, heightMM;
        // glfwGetMonitorPhysicalSize(, widthMM, heightMM);

        //Configure GLFW: Set window to be invisible, allow resize, maximize window set to false
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::KeyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });


        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync, ensures an uniform framerate.
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        //Initialize framebuffer object
        this.framebuffer = new Framebuffer(1920, 1080);
        this.pickingTexture = new PickingTexture(1920, 1080);
        glViewport(0, 0, 1920, 1080);

        //Create and initialise imgui
        imguiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        imguiLayer.init();

        Window.changeScene(0);
    }

    /**
     * Main loop for the window, calculates the delta time variable at each iteration.
     * Then, update the current scene.
     */
    public void loop() {
        //setup delta time variable
        float beginTime = (float)glfwGetTime();
        float endTime;;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("app/assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("app/assets/shaders/pickingShader.glsl");

        //Main loop
        while (!glfwWindowShouldClose(glfwWindow)) {
            //Poll events
            glfwPollEvents();

            //----------Render to picking texture stored in frame buffer-------------
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 1920, 1080);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();
            //----------------------------------------------------------------------

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            //Draw the debug lines
            DebugDraw.beginFrame();

            //bind frambuffer, allowing rendering offscreen
            this.framebuffer.bind();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0){
                DebugDraw.draw();

                //---------Render actual game using default shader-----------------
                Renderer.bindShader(defaultShader);
                currentScene.update(dt);
                currentScene.render();
            }

            this.framebuffer.unbind();

            //Imgui stuff
            //Displays active gameobject variables through imgui window
            this.imguiLayer.update(dt, currentScene);

            glfwSwapBuffers(glfwWindow);
            MouseListener.endFrame(); //Ensure scroll value is set to zero

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        currentScene.saveExit(); //Save current progress
        this.imguiLayer.destroy();
    }

    public static ImGuiLayer getImGuiLayer() {
        return get().imguiLayer;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return (float) 16 / 9;
    }
}
