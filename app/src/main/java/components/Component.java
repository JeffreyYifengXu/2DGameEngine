package components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector3f;

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

    /**
     * Expose the variables to imGui. Allowing users to change values using the imgui menu
     */
    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field: fields) {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate) {
                    field.setAccessible(true); //Temporarily set private variables to be accessible
                }

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
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
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
}
