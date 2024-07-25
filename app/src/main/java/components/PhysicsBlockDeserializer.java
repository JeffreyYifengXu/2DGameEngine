package components;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import gamePhysics.PhysicsBlock;

public class PhysicsBlockDeserializer implements JsonSerializer<PhysicsBlock>, 
    JsonDeserializer<PhysicsBlock>{

    @Override
    public PhysicsBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

                return null;

    }

    @Override
    public JsonElement serialize(PhysicsBlock src, Type typeOfSrc, JsonSerializationContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }


    
}
