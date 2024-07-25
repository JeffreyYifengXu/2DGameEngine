package engine;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import components.Component;
import gamePhysics.PhysicsBlock;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");

        GameObject go = new GameObject(name);
        for (JsonElement e: components) {
            // JsonObject componentObject = e.getAsJsonObject();
            // String type = componentObject.get("type").getAsString();

            // if ("gamePhysics.PhysicsBlock".equals(type)) {
                

            // } else {
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }
        go.transform = go.getComponent(Transform.class);
        return go;
    }
}
