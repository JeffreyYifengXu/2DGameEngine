package jade;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import Renderer.Shader;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    // private String vertexShaderSrc = "#version 330 core\r\n" + //
    //             "layout (location=0) in vec3 aPos;\r\n" + //
    //             "layout (location=1) in vec4 aColor;\r\n" + //
    //             "\r\n" + //
    //             "out vec4 fColor;\r\n" + //
    //             "\r\n" + //
    //             "void main() {\r\n" + //
    //             "    fColor = aColor;\r\n" + //
    //             "    gl_Position = vec4(aPos, 1.0);\r\n" + //
    //             "}";

    // private String fragmentShaderSrc = "#version 330 core\r\n" + //
    //             "\r\n" + //
    //             "in vec4 fColor;\r\n" + //
    //             "\r\n" + //
    //             "out vec4 color;\r\n" + //
    //             "\r\n" + //
    //             "void main() {\r\n" + //
    //             "    color = fColor;\r\n" + //
    //             "}";

    // private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
        //position                  //color
         50.5f, -50.5f, 0.0f,         1.0f, 0.0f, 0.0f, 1.0f, //Bottom right 
        -50.5f, 50.5f, 0.0f,          0.0f, 1.0f, 0.0f, 1.0f, //Top left     
         50.5f, 50.5f, 0.0f,          0.0f, 0.0f, 1.0f, 1.0f, //Top right
        -50.5f, -50.5f, 0.0f,         1.0f, 1.0f, 0.0f, 1.0f, //Bottom left
    };

    //Must be in counter-clockwise order
    private int[] elementArray = {
        2, 1, 0, //Top right triangle
        0, 1, 3  // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    
    public LevelEditorScene() {
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("app/assests/shaders/default.glsl");
        defaultShader.compile();
        //Generaate VAO, VBO and EBO buffer objects, and send to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //Bind shader program
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        //bind the VAO
        glBindVertexArray(vaoID);
        
        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
}
