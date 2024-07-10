package physicsEngine2D.primitives;

import org.joml.Vector2f;

import components.Component;

public class RigidBody extends Component{

    public RBTransform transform;

    private boolean isStatic;
    private boolean updateTransformRequired;

    private float[] colour;

    public RigidBody(Vector2f position, float mass, Shape shape, boolean isStatic) {
        this.transform = new RBTransform(position, mass, shape, 0);
        this.isStatic = isStatic;
        this.updateTransformRequired = true;
        this.colour = new float[] {0.0f, 1.0f, 1.0f};
    }

    //Using euler's method
    public void step(float dt) {
        this.transform.velocity.add(transform.acceleration); //update velocity
        this.transform.position.add(transform.velocity);
        this.transform.rotation += this.transform.rotationVelocity * dt;
        this.updateTransformRequired = true;

        this.transform.acceleration = new Vector2f();
    }
    
    public void move(Vector2f distance) {
        this.transform.position.add(distance);
        this.updateTransformRequired = true;
    }

    public void rotate(float val) {
        this.transform.rotation += val;
        this.updateTransformRequired = true;
    }

    public void applyForce(Vector2f val) {
        this.transform.acceleration = val;
    }

    public void updateVertices() {

        if (updateTransformRequired) {
            AABB shape = (AABB)transform.shape;
            Vector2f[] vertices = shape.vertices;
            
            float centerX = (vertices[0].x + vertices[2].x) / 2.0f;
            float centerY = (vertices[0].y + vertices[2].y) / 2.0f;

            float sin = (float)Math.sin(transform.rotation);
            float cos = (float)Math.cos(transform.rotation);

            //Update vertex position with rotation.
            for (int i=0; i < 4; i++) {
                Vector2f vertex = shape.vertices[i];

                float translatedX = vertex.x - centerX;
                float translatedY = vertex.y - centerY;

                float rx = cos * translatedX - sin * translatedY;
                float ry = sin * translatedX + cos * translatedY;

                shape.vertices[i] = new Vector2f(rx + transform.position.x, ry + transform.position.y);
            }
        }

        this.updateTransformRequired = false;
    }

    //---------------------------------------------------------------------------------------

    public void resetRotation() {
        this.transform.rotation = 0;
    }

    public float[] getColour() {
        return this.colour;
    }

    public void changeColour(float r, float g, float b) {
        this.colour = new float[] {r, g, b};
    }

    public void resetColour() {
        this.colour = new float[] {0, 1f, 1f};
    }

    public Vector2f getCenter() {
        return new Vector2f(this.transform.position).add(16, 16);
    }
}
