package physicsEngine2D;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import physicsEngine2D.primitives.AABB;

public class PhysicsWorld {
    List<RigidBody> bodies = new ArrayList<>();
    public Vector2f gravity;

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

    }

    /*
     * Check for simplest form of collision, rectangle vs rectangle without rotations
     */
    public void detectCollisions() {
        int size = bodies.size();

        //Brute force through on rigidbodies and check for collisions
        for (int i=0; i < size - 1; i++) {
            for (int j= i+1; j < size; j++) {
                RigidBody rbA = bodies.get(i);
                RigidBody rbB = bodies.get(j);
                if (CollisionDetector.AABBCollision(rbA, rbB)) {
                    positionCorrection(rbA, rbB);
                    resolveImpulse(rbA, rbB);
                }
            }
        }
    }

    public void positionCorrection(RigidBody rbA, RigidBody rbB) {
        float rbAX = rbA.getPosition().x;
        float rbAY = rbA.getPosition().y;
        float rbBX = rbB.getPosition().x;
        float rbBY = rbB.getPosition().y;

        AABB shapeA = (AABB) rbA.getShape();
        AABB shapeB = (AABB) rbB.getShape();

        //Find the overlop between the two bodies
        float overlapX = Math.abs(Math.min(rbAX + shapeA.getWidth() - rbBX, rbBX + shapeB.getWidth() - rbAX));
        float overlapY = Math.abs(Math.min(rbAY + shapeA.getHeight() - rbBY, rbBY + shapeB.getHeight() - rbAY));

        //determine the shortest overlap and calculate minimum translation vector
        Vector2f mtv;
        if (overlapX < overlapY) {
            mtv = new Vector2f(overlapX, 0);

            if (rbAX < rbBX) {
                rbA.getPosition().sub(mtv);
            } else {
                rbA.getPosition().add(mtv);
            }

        } else {
            mtv = new Vector2f(0, overlapY);

            if (rbAY < rbBY) {
                rbA.getPosition().sub(mtv);
            } else {
                rbB.getPosition().add(mtv);
            }
        }
    }

    public void resolveImpulse(RigidBody rbA, RigidBody rbB) {

        //TODO: Collision logic, Add the velocities of two bodies, apply force on to the bodies


    }

    /*
     * Apply forces to rigid bodies (collision, gravity etc)
     */
    public void forceDetection() {

    }
}
