package Renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));

            //Regex to split the string, glsl code
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            //Find the index of the next word after #type
            //For type vertex
            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, endOfLine).trim(); 
            
            //for type fragment
            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, endOfLine).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];

            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            }
            else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];

            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            }
            else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        }catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shadeL '" + filepath + "'";
        }

        System.out.println(vertexSource);
        System.out.println(fragmentSource);

    }

    public void compile() {
        //############################
        //Compile and link the shaders
        //############################
        int vertexID, fragmentID;
        //Load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader source code to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        //Check for errors in compilation
        int errors = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (errors == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error:'" + filepath + "'defaultShader.glsl' \n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        //Load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the fragment shader source code to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        //Check for errors in compilation
        errors = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (errors == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error:'" + filepath + "'defaultShader.glsl' \n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        //############################
        //Link shaders and check for errors
        //############################

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //check for linking errors
        errors = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (errors == GL_FALSE) {
            int len = glGetShaderi(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + filepath + "'defaultShader.glsl' \n\tLinking of shaders failed.");
            System.out.println(glGetShaderInfoLog(shaderProgramID, len));
            assert false : "";
        }


    }

    public void use() {
        //Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
    
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}
