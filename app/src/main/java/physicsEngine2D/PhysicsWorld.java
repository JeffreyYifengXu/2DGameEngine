package physicsEngine2D;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import physicsEngine2D.physics.CollisionDetector;
import physicsEngine2D.physics.CollisionHelper;
import physicsEngine2D.physics.Collisions;
import physicsEngine2D.primitives.AABB;
import physicsEngine2D.primitives.RigidBody;

public class PhysicsWorld {
    public List<RigidBody> bodies = new ArrayList<>();
    public List<RigidBody> immovableBodies = new ArrayList<>();
    public Vector2f gravity;

    private static float timeElapsed = 0;

    public PhysicsWorld() {
        this.gravity = new Vector2f(0, -10); //Default gravity
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
        fixedUpdate(dt);
    }

    /*
     * Check for simplest form of collision, rectangle vs rectangle without rotations
     */
    public void fixedUpdate(float dt) {
        int size = bodies.size();

        //Integration step
        for (int i=0; i < size; i++) {
            RigidBody body = bodies.get(i);
            body.step(dt);

            body.resetColour();
            body.updateVertices();
        }


        //Loop through all bodies to check for collisions
        for (int i=0; i < size - 1; i++) {
            RigidBody rbA = bodies.get(i);

            for (int j= i+1; j < size; j++) {
                RigidBody rbB = bodies.get(j);

                CollisionHelper vals = CollisionDetector.polygonCollision(rbA, rbB);
                if (vals != null) {
                    System.out.println("Velocities: " + rbA.transform.velocity);
                    System.out.println("            " + rbB.transform.velocity + "\n");

                    Vector2f normal = vals.getNormal();
                    float depth = vals.getdepth();

                    rbA.changeColour(1, 0f, 0f);
                    rbB.changeColour(1, 0f, 0f);

                    rbA.move(new Vector2f(normal).mul(depth / 2f + 1).negate());
                    rbB.move(new Vector2f(normal).mul(depth / 2f + 1));

                    CollisionDetector.resolveCollision(rbA, rbB, normal, depth);
                }
            }
        }

        //Reset acceleration of all bodies to zero
        for (int i=0; i < size; i++) {
            //ensure the body wraps around the screen
            checkBounds(bodies.get(i));
        }
    }

    /*
     * Correct two objects position to avoid overlapping after collision
     */
    public Vector2f positionCorrection(RigidBody rbA, RigidBody rbB) {
        float rbAX = rbA.transform.position.x;
        float rbAY = rbA.transform.position.y;
        float rbBX = rbB.transform.position.x;
        float rbBY = rbB.transform.position.y;

        AABB shapeA = (AABB) rbA.transform.shape;
        AABB shapeB = (AABB) rbB.transform.shape;

        //Find the overlop between the two bodies
        float overlapX = Math.abs(Math.min(rbAX + shapeA.getWidth() - rbBX, rbBX + shapeB.getWidth() - rbAX)) + 0.5f;
        float overlapY = Math.abs(Math.min(rbAY + shapeA.getHeight() - rbBY, rbBY + shapeB.getHeight() - rbAY)) + 0.5f;

        //determine the shortest overlap and calculate minimum translation vector
        Vector2f mtv;
        if (overlapX < overlapY) {
            mtv = new Vector2f(overlapX, 0);

            if (rbAX < rbBX) {
                rbA.transform.position.sub(mtv);
            } else {
                rbA.transform.position.add(mtv);
            }
            return new Vector2f(overlapX / Math.abs(overlapX), 0);

        } else {
            mtv = new Vector2f(0, overlapY);

            if (rbAY < rbBY) {
                rbA.transform.position.sub(mtv);
            } else {
                rbB.transform.position.add(mtv);
            }
            return new Vector2f(0, overlapY / Math.abs(overlapY));
        }
    }

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
