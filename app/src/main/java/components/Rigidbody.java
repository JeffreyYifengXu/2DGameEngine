package components;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Rigidbody extends Component{

    // private int colliferType = 0;
    // private float friction = 0.8f;
    public Vector3f velocity = new Vector3f(1, 1, 1);
    public transient Vector4f tmp = new Vector4f(0, 0, 0, 0);

}
