package components;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class AbstractDeserializer implements JsonDeserializer<Object>{

    private static final String CLASS_META_KEY = "CLASS_META_KEY";

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String className = jsonObj.get(CLASS_META_KEY).getAsString();

        try {
            Class<?> cls = Class.forName(className);
            return context.deserialize(json, cls);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }


    
}
