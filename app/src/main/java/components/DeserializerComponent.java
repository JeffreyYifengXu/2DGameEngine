package components;

import com.google.gson.JsonSerializer;

import gamePhysics.PhysicsBlock;
import physicsEngine2D.primitives.RigidBody;
import physicsEngine2D.primitives.Shape;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DeserializerComponent implements JsonSerializer<Component>, 
    JsonDeserializer<Component>{
    
    /**
     * Custom deserializer for the component class.
     * Can properly deserialize different types of components
     */
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString(); //Obtain the type of the stored object
        JsonElement element = jsonObject.get("properties"); //Obtain the properties of the stored object
        System.out.println(type);
        try{
            // //Custom deserializer for the physicsblock
            // if ("gamePhysics.PhysicsBlock".equals(type)) {
            //     return deserializePhysicsBlock(jsonObject, context);
            // }

            return context.deserialize(element, Class.forName(type)); //Deserialize according to the type of object
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        //Manually add name value pairs to store
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName())); //Save the type of the class
        result.add("properties", context.serialize(src, src.getClass())); // Save the properties for the added type
        return result;
    }

    // private PhysicsBlock deserializePhysicsBlock(JsonObject jsonObject, JsonDeserializationContext context) {
    //     JsonObject properties = jsonObject.getAsJsonObject("properties");

    //     //setup the required variables
    //     JsonObject rigidBodyJson = properties.getAsJsonObject("rigidBody");
    //     JsonObject transformJson = rigidBodyJson.getAsJsonObject("transform");

    //     //deserialize shape separately
    //     JsonElement shapeJson = transformJson.get("shape");
    //     Shape shape = context.deserialize(shapeJson, Shape.class);

    //     System.out.println(shape);
    //     //create new rigid body class with shape

    //     //create new physics body class


    //     // return new PhysicsBlock(rigidBody);
    //     return null;
    // }


}
