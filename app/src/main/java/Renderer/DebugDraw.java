package renderer;

import java.util.Arrays;

import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20.glDrawArrays;
import static org.lwjgl.opengl.GL20.glLineWidth;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;



import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.Window;
import util.AssetPool;

public class DebugDraw {

    private static int MAX_LINES = 500;

    private static List<Line2D> lines = new ArrayList<>();

    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("app/assets/shaders/debugLine.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    public static void start() {
        //Generate the vertex array object (vao)
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create the vertex buffer object (vbo) and buffer memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Enable the vertex array attributes 
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void beginFrame() {
        if (!started) {
            start();
            started = false;
        }

        //Remove dead lines 
        for (int i=0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw() {
        if (lines.size() <= 0) {
            return;
        }

        int index = 0;
        for (Line2D line: lines) {
            for (int i=0; i < 2; i++) {
                Vector2f position;
                if (i == 0) { position = line.getStart();}
                else {position = line.getEnd();}

                Vector3f colour = line.getColour();

                //Load position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                //Load the colour
                vertexArray[index + 3] = colour.x;
                vertexArray[index + 4] = colour.y;
                vertexArray[index + 5] = colour.z;
                index += 6;
            }
        }

        //Upload to GPU
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        //Use shader to draw
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        //Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //Drawthe batch 
        glDrawArrays(GL_LINES, 0, lines.size() * 2);

        //Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        //unbind shader
        shader.detach();
    }
    
    //#######################################################################//
    //Add linne2D methods
    //#######################################################################//
    public static void addLine2D(Vector2f start, Vector2f end) {
        //Set default parameters
        addLine2D(start, end, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector3f colour) {
        //Set default parameters
        addLine2D(start, end, colour, 1);
    }


    public static void addLine2D(Vector2f start, Vector2f end, Vector3f colour, int lifeTime) {
        if (lines.size() >= MAX_LINES) {
            return;
        }

        DebugDraw.lines.add(new Line2D(start, end, colour, lifeTime));
    }
}
