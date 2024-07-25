package physicsEngine2D.primitives;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class ShapeInstanceCreator implements InstanceCreator<Shape> {

    @Override
    public Shape createInstance(Type type) {
        return new Polygon(32, 32);
    }
    
}
