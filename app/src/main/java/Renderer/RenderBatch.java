package renderer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import engine.GameObject;
import engine.Window;
import java.util.List;
import java.util.ArrayList;

public class RenderBatch implements Comparable<RenderBatch>{
    //a single vertex
    //Position          Colour                          Tex Coords      Tex id
    //float, float,     float, float, float, float,     float, float    float


    private final int POS_SIZE = 2;
    private final int COLOUR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int MAX_NUM_OF_TEXTURES = 8;
    private final int ENTITY_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOUR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOUR_OFFSET + COLOUR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures; //Textures contained within the current batch
    private int vaoID, vboID;
    private int maxBatchSize;
    private int zIndex;

    public RenderBatch(int maxBatchSize, int zIndex) { //maxBatchsize tells exactly how many quads are going to be used.
        this.zIndex = zIndex;
        
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE]; //Need 4 vertices for a single quad

        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    /* Create all data on the GPU */
    public void start() {
        //Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //Enable the buffer attribute pointers, Talks to the GPU using the shaders
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOUR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOUR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }


    public void addSprite(SpriteRenderer spr) {
        //Get index and add renderObject
        int index = this.numSprites; //Location that we want the spr to be added (the end of array)
        this.sprites[index] = spr;
        this.numSprites++;

        //Check if the sprite already have a texture, add the texture if it don't already have it.
        if (spr.getTexture() != null) {
            if (!textures.contains(spr.getTexture())) {
                textures.add(spr.getTexture());
            }
        }
        
        //Add properties to local vertices array
        loadVertexProperties(index);

        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    public void removeSprite(SpriteRenderer sprite) {
        for (int i=0, j=0; j < this.numSprites; j++) {
            if (sprites[j] == sprite) {
                numSprites --;
                this.hasRoom = true;
            } else {
                sprites[i++] = sprites[j]; 
            }
        }
    }

    public boolean destroyGameObject(GameObject go) {
        SpriteRenderer sprite = go.getComponent(SpriteRenderer.class);
        for (int i=0; i < numSprites; i++) {
            if (sprites[i] == sprite) {

                //Remove sprites by over writing everything, move everything backwards in the list
                for (int j=i; j < numSprites - 1; j++) {
                    sprites[j] = sprites[j + 1];
                    sprites[j].setDirty();
                }

                numSprites --;
                return true;
            }
        }

        return false;
    }

    /**
     * Render the available game sprites
     * Only rebuffer when something is changed on the screen (i.e. marked by dirty flag)
     */
    public void render() {
        boolean rebufferData = false;
        for (int i=0; i < numSprites; i++) {
            SpriteRenderer spr = sprites[i];
            if (spr.isDirty()) {
                loadVertexProperties(i);
                spr.setClean();
                rebufferData = true;
            }
        }

        if (rebufferData) { //Rebuffer frame if something changes
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        //Use Shader
        Shader shader = Renderer.getBoundShader();
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        for (int i=0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1); // Assign slots
            textures.get(i).bind(); //Bind the texture to corresponding slot
        }

        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i=0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        //Find offset within array which is 4 vertices per sprite and 6 floats per vertices 
        //position position red green blue alpha (Floats)
        int offset = index * 4 * VERTEX_SIZE;
                
        Vector4f color = sprite.getColour();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texId = 0;
        //Loop through a list of textures and find the one that matches the one of interest
        if (sprite.getTexture() != null) {
            for (int i=0; i < textures.size(); i++) {
                if (textures.get(i).equals(sprite.getTexture())) {
                    texId = i + 1; //Reserve the first slot to be zero
                    break;
                }       
            }
        }

        boolean isRotated = sprite.gameObject.transform.rotation != 0.0f; 
        Matrix4f transformMatrix = new Matrix4f().identity();

        if (isRotated) {
            transformMatrix.translate(sprite.gameObject.transform.position.x, 
                                      sprite.gameObject.transform.position.y, 0);

            transformMatrix.rotate((float)Math.toRadians(sprite.gameObject.transform.rotation), 0, 0, 1);

            transformMatrix.scale(sprite.gameObject.transform.scale.x, sprite.gameObject.transform.scale.y, 1);
        }

        // add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i=0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            Vector4f currentPos = new Vector4f(sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x),
                sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y), 0, 1);
            
            if (isRotated) {
                currentPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transformMatrix);
            }

            //Load position
            vertices[offset] = currentPos.x;
            vertices[offset + 1] = currentPos.y;

            //Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //Load texture coordinates
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            //Load texture id
            vertices[offset + 8] = texId;

            //Load entity id
            vertices[offset + 9] = sprite.gameObject.getUid() + 1;


            offset += VERTEX_SIZE;
        }
    }

    private void removeVertexProperties(int index) {
        //TODO: Remove the vertex information at index
    }

    private int[] generateIndices() {
        //6 indices per quad
        int[] elements = new int[6 * maxBatchSize];
        for (int i=0; i < maxBatchSize; i++) {
            loadElementindices(elements, i);
        }

        return elements;
    }

    private void loadElementindices(int[] elements, int index) {
        //Off set used to ensure all triangles are created at the right space with the right number
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //Creates the indices for the two triangles that forms a quad
        //Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;
        //Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

    public boolean hasTextureRoom() {
        return this.textures.size() < MAX_NUM_OF_TEXTURES;
    }

    public boolean hasTexture(Texture tex) {
        return this.textures.contains(tex);
    }

    public int getNumSprites() {
        return this.numSprites;
    }

    public int zIndex() {
        return this.zIndex;
    }

    /**
     * Compare two gameobject based on their zIndex. Which determines which is redered first.
     * @param o
     * @return
     */
    @Override
    public int compareTo(RenderBatch o) {
        return  Integer.compare(this.zIndex, o.zIndex());
    }
}
