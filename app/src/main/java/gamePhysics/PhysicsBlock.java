package gamePhysics;

import org.joml.Vector2f;

import components.Component;
import physicsEngine2D.primitives.RigidBody;
import renderer.DebugDraw;

public class PhysicsBlock extends Component {

    RigidBody rigidBody;
    private Vector2f offset = new Vector2f();

    public PhysicsBlock(Vector2f position, boolean isStatic) {
        this.rigidBody = new RigidBody(position, isStatic);
    }
    
    public PhysicsBlock(RigidBody rb) {
        this.rigidBody = rb;
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addBox2D(center, new Vector2f(32, 32), this.gameObject.transform.rotation);
    }

    public void update(float dt) {
        //TODO: update position of the gameObject according to the rigidBody
        gameObject.transform.position = this.rigidBody.transform.position;
        // System.out.println(this.rigidBody.transform.position);
    }

    public void destroyRigidBody() {
        this.rigidBody.destroy();
    }
}
