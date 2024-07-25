package physicsEngine2D;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import physicsEngine2D.physics.CollisionDetector;
import physicsEngine2D.physics.CollisionHelper;
import physicsEngine2D.primitives.RigidBody;

public class PhysicsWorld {
    public List<RigidBody> bodies = new ArrayList<>();
    public List<RigidBody> immovableBodies = new ArrayList<>();
    public Vector2f gravity;

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;

    public PhysicsWorld() {
        this.gravity = new Vector2f(0, 9.8f); //Default gravity
    }

    public PhysicsWorld(float gravity) {
        this.gravity = new Vector2f(0, gravity);
    }

    public void addBody(RigidBody rb) {
        this.bodies.add(rb);
    }

    /*
     * Update the physics world. 
     * Called in the game engine update loop.
     */
    public void update(float dt) {
        physicsTime += dt;

        if (physicsTime >= 0.0f) {// ensure update physics engine every 60 frames 
            physicsTime -= physicsTimeStep;
            fixedUpdate(physicsTime);
        }
    }

    /*
     * Check for simplest form of collision, rectangle vs rectangle without rotations
     */
    public void fixedUpdate(float dt) {
        int size = bodies.size();

        //Integration step
        for (int i=0; i < size; i++) {
            RigidBody body = bodies.get(i);
            if (body.isDead()) {
                bodies.remove(body);
                i--;
                continue;
            }
            
            body.applyForce(new Vector2f(gravity).mul(dt)); //Add gravity to objects
            // body.step(dt);
            body.vertletStep(dt);

            body.resetColour();
            body.updateVertices();
        }


        //Loop through all bodies to check for collisions
        for (int i=0; i < size - 1; i++) {
            RigidBody rbA = bodies.get(i);

            for (int j= i+1; j < size; j++) {
                RigidBody rbB = bodies.get(j);

                if (rbA.isStatic() && rbB.isStatic()) { //do not need to check collision between two static bodies
                    continue;
                }

                CollisionHelper vals = CollisionDetector.polygonCollision(rbA, rbB);
                if (vals != null) {
                    Vector2f normal = vals.getNormal();
                    float depth = vals.getdepth();

                    //Debug command to check for collisions
                    // rbA.changeColour(1, 0f, 0f);
                    // rbB.changeColour(1, 0f, 0f);

                    positionCorrection(rbA, rbB, normal, depth);
                    CollisionDetector.resolveCollision(rbA, rbB, normal, depth);
                }
            }
        }

        for (int i=0; i < size; i++) {
            //ensure the body wraps around the screen
            checkBounds(bodies.get(i));
        }
    }

    /*
     * Correct two objects position to avoid overlapping after collision
     */
    private void positionCorrection(RigidBody rbA, RigidBody rbB, Vector2f normal, float depth) {
        if (rbA.isStatic()) {
            rbB.move(new Vector2f(normal).mul(depth));
            // rbB.transform.velocity = new Vector2f();
        } else if (rbB.isStatic()) {
            rbA.move(new Vector2f(normal).mul(depth).negate());
            // rbA.transform.velocity = new Vector2f();
        } else {
            rbA.move(new Vector2f(normal).mul(depth / 2f).negate());
            rbB.move(new Vector2f(normal).mul(depth / 2f));
        }
       
    }

    public float getPhysicsTime() {
        return this.physicsTime;
    }

    //Debug functions ------------------------------------------------------------
    private void checkBounds(RigidBody body) {
        Vector2f position = body.transform.position;

        // Screen dimensions
        float screenWidth = 1920;
        float screenHeight = 1080;

        // Wrap around horizontally
        if (position.x < 0) {
            position.x += screenWidth;
        } else if (position.x >= screenWidth) {
            position.x -= screenWidth;
        }

        // Wrap around vertically
        if (position.y < 0) {
            position.y += screenHeight;
        } else if (position.y >= screenHeight) {
            position.y -= screenHeight;
        }

        // Set the new position back to the body
        body.transform.position.set(position);
    }

}
