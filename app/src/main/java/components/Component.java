package components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import edittool.EditorCamera;
import engine.GameObject;
import imgui.ImGui;

public abstract class Component {
    private static int ID_COUNTER = 0; //Associate with component class in general, global
    private int uid = -1;

    public transient GameObject gameObject; //Cicular dependency of component and gameObject
    

    public void start(){
        
    }

    public void update(float dt) {

    }

    public void editorUpdate(float dt) {
        
    }

    /**
     * Expose the variables to imGui. Allowing users to change values using the imgui menu.
     * Allows for real time adjustment of the variables
     */
    public void imgui() {
        if (this.getClass() == EditorCamera.class) {
            return;
        }
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field: fields) {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate) {
                    field.setAccessible(true); //Temporarily set private variables to be accessible
                }

                @SuppressWarnings("rawtypes")
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();
                
                if (type == int.class) { 
                    int val = (int)value;
                    int[] imInt = {val}; //imgui expects a list of integers
                    if (ImGui.dragInt(name + ": ", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if (type == float.class) {
                    float val = (float)value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + ": ", imFloat)) {
                        field.set(this, imFloat[0]);
                    }
                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f)value;
                    float[] imVec = {val.x, val.y};
                    if (ImGui.dragFloat2(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1]);
                    }
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                    
                }

                if(isPrivate) {
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensure each component gets an unique id
     */
    public void generateId() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

    /*
     * Specify a specific uid for a component
     */
    public void setUid(int id) {
        this.uid = id;
    }

    public int getUid() {
        return this.uid;
    }

    /**
     * Make sure that no components gets duplicate id after loading a level
     * @param maxId
     */
    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public void destroy() {
        
    }
}
