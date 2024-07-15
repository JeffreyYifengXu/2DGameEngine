package physicsEngine2D;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
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
import static org.lwjgl.glfw.GLFW.glfwGetTime;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.Random;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;

import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

import engine.KeyListener;
import engine.MouseListener;
import physicsEngine2D.primitives.AABB;
import physicsEngine2D.primitives.RigidBody;

public class PhysicsSim {
    private long glfwWindow;
    private PhysicsWorld world;

    private int width, height;
    private String title;

    private static PhysicsSim window = null;

    public PhysicsSim() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Physics simulation";
    }

    public static PhysicsSim get() {
        if (PhysicsSim.window == null) {
            PhysicsSim.window = new PhysicsSim();
        }

        return PhysicsSim.window;
    }

    public void run() {
        init();
        loop();

        //Memory menagement: Free memory 
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init() {
         // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

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
            PhysicsSim.setWidth(newWidth);
            PhysicsSim.setHeight(newHeight);
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

        world = new PhysicsWorld();
        initWorld();

        // Set up the orthographic projection
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, this.width, this.height, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public void initWorld() {
        int numOfbodies = 10;
        Random random = new Random();

        // Vector2f pos = new Vector2f(random.nextInt(width - 300), random.nextInt(height - 500));
        Vector2f pos = new Vector2f(500, random.nextInt(height - 500));

        RigidBody body = new RigidBody(pos, 1f, new AABB(32, 32), false);
        AABB shape = (AABB)body.transform.shape;
        shape.setVertices(pos);
        body.changeColour(0, 1, 0);

        world.addBody(body);



        for (int i=0; i < numOfbodies; i++) {
            boolean isStatic = randomBool();

            pos = new Vector2f(random.nextInt(width - 300), random.nextInt(height - 500));
            // pos = new Vector2f(500, random.nextInt(height - 500));
            // body = new RigidBody(pos, (float)Math.random() * 5, new AABB(32, 32), false);
            body = new RigidBody(pos, 1f, new AABB(32, 32), isStatic);

            shape = (AABB)body.transform.shape;
            shape.setVertices(pos);

            world.addBody(body);
        }
    }

    private void loop() {
        //setup delta time variable
        float beginTime = (float)glfwGetTime();
        float endTime;;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {

            glfwPollEvents();
            glViewport(0, 0, 1920, 1080);
            glClearColor(0.3f, 0.3f, 0.3f, 0.3f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            controllerUpdate(dt);
            world.update(dt);
     
            render();

            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void render() {
        for (RigidBody body: world.bodies) {
            float[] colour = body.getColour();
            glColor3f(colour[0], colour[1], colour[2]);

            Vector2f pos = body.transform.position;
            AABB shape = (AABB)body.transform.shape;
            Vector2f[] vertices = shape.vertices;

            if (body.transform.shape.getClass() == AABB.class) {
                glBegin(GL_QUADS);
                for (int i=0; i < 4; i++) {
                    glVertex2f(vertices[i].x, vertices[i].y);
                }
                glEnd();
            }
        }
    }

    public void controllerUpdate(float dt) {
        RigidBody playerBody = world.bodies.get(0);

        float dx = 0;
        float dy = 0;

        float forceMagnitude = 4;

        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            dy--;
        } if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            dy++;
        } if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            dx--;
        } if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            dx++;
        }

        if (dx != 0f || dy != 0f) {
            Vector2f direction = new Vector2f(dx, dy).normalize();
            Vector2f force = direction.mul(forceMagnitude).mul(dt);
            playerBody.applyForce(force);
        }
    } 

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }


    //Debug functions --------------------
    private boolean randomBool() {
        double random = Math.random();

        if (random > 0.7f) {
            return true;
        }
        else {
            return false;
        }
    }
}
