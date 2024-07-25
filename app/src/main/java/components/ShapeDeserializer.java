package components;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import physicsEngine2D.primitives.Circle;
import physicsEngine2D.primitives.Polygon;
import physicsEngine2D.primitives.Shape;

public class ShapeDeserializer implements JsonDeserializer<Shape> {

    @Override
    public Shape deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        System.out.println("This was called");

        if (jsonObject.has("width") && jsonObject.has("height")) {
            return context.deserialize(jsonObject, Polygon.class);
        } else if (jsonObject.has("radius")) {
            return context.deserialize(jsonObject, Circle.class);
        } else {
            throw new JsonParseException("Unknown shape type");
        }
    }
}
