package physics_engine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

import physicsEngine2D.PhysicsWorld;
import physicsEngine2D.physics.CollisionDetector;
import physicsEngine2D.primitives.AABB;
import physicsEngine2D.primitives.RigidBody;

public class collisionTests {

    @Test
    public void boxVsbox() {

        RigidBody rbA = new RigidBody(new Vector2f(10, 10), 0, new AABB(32, 32));
        RigidBody rbB = new RigidBody(new Vector2f(40, 10), 0, new AABB(32, 32));

        assertTrue(CollisionDetector.AABBCollision(rbA, rbB));
    }

    @Test
    public void positionCorrectionTest() {
        PhysicsWorld physicsWorld = new PhysicsWorld();

        RigidBody rbA = new RigidBody(new Vector2f(10, 10), 0, new AABB(32, 32));
        RigidBody rbB = new RigidBody(new Vector2f(40, 10), 0, new AABB(32, 32));

        physicsWorld.positionCorrection(rbA, rbB);

        assertTrue(rbA.getPosition().x == 8);

        rbA.getPosition().x = 70;
        physicsWorld.positionCorrection(rbA, rbB);

        assertTrue(rbA.getPosition().x == 72);

    }

    @Test
    public void detectCollisionsTest() {
        PhysicsWorld physicsWorld = new PhysicsWorld();

        RigidBody rbA = new RigidBody(new Vector2f(10, 10), 0, new AABB(32, 32));
        RigidBody rbB = new RigidBody(new Vector2f(40, 10), 0, new AABB(32, 32));

        physicsWorld.addBody(rbA);
        physicsWorld.addBody(rbB);


        physicsWorld.fixedUpdate(0.016f);

        assertTrue(rbA.getPosition().x == 8);
    }
}
