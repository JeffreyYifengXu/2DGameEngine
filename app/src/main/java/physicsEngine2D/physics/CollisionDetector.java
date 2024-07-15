package physicsEngine2D.physics;

import org.joml.Vector2f;

import physicsEngine2D.primitives.AABB;
import physicsEngine2D.primitives.RigidBody;

public class CollisionDetector {
    /*
     * detect collision between two axis aligned boxes without rotation
     */
    public static boolean AABBCollision(RigidBody rbA, RigidBody rbB) {

        float rbAX = rbA.transform.position.x;
        float rbAY = rbA.transform.position.y;
        float rbBX = rbB.transform.position.x;
        float rbBY = rbB.transform.position.y;

        AABB shapeB = (AABB) rbB.transform.shape;
        AABB shapeA = (AABB) rbA.transform.shape;

        boolean collisionX = (rbAX + shapeA.getWidth())> rbBX && //X axis collision rbA on the left
                             (rbAX < rbBX + shapeB.getWidth()) ||
                             (rbBX + shapeA.getWidth())> rbAX && //rbB on the lft
                             (rbBX < rbAX + shapeB.getWidth());

        boolean collisionY = rbAY < (rbBY + shapeB.getHeight()) && //rbA on top
                             rbBY < (rbAY + shapeA.getHeight()) ||
                             rbBY < (rbAY + shapeB.getHeight()) && //rbB on top
                             rbAY < (rbBY + shapeA.getHeight());
        
        return collisionX && collisionY; // Collision condition is satisfied when both x and y axis of two bodies intersect.
    }

    /**
     * Collision detection using separating axis theorem. 
     * @param bodyA
     * @param bodyB
     * @return
     */
    public static CollisionHelper polygonCollision(RigidBody rbA, RigidBody rbB) {

        Vector2f[] verticesA = rbA.transform.shape.vertices;
        Vector2f[] verticesB = rbB.transform.shape.vertices;

        Vector2f normal = new Vector2f();
        float depth = Float.MAX_VALUE;

        for (int i=0; i < verticesA.length; i++) { //For verticesA
            Vector2f va = new Vector2f(verticesA[i]);
            Vector2f vb = new Vector2f(verticesA[(i + 1) % verticesA.length]);

            Vector2f edge = vb.sub(va);
            Vector2f axis = new Vector2f(-edge.y, edge.x).normalize(); //Normal to the chosen edge

            float[] minMaxA = CollisionDetector.projectVertices(verticesA, new Vector2f(axis)); //get projection for polygon A
            float[] minMaxB = CollisionDetector.projectVertices(verticesB, axis); //get projection for polygon B

            //Structure of the returned array: {minimum, maximum}
            //Compare the min-max of A and B, if true, there is a separation
            if (minMaxA[0] > minMaxB[  1] || minMaxB[0] > minMaxA[1]) {
                return null;
            }

            //Store the minimum depth to resolve the collision.
            float axisDepth = Math.min(minMaxB[1] - minMaxA[0], minMaxA[1] - minMaxB[0]); //Find the minimum depth on axis
            if (axisDepth < depth) {
                depth = axisDepth;
                normal = axis;
            }
        }

        for (int i=0; i < verticesB.length; i++) { //for verticesB
            Vector2f va = new Vector2f(verticesB[i]);
            Vector2f vb = new Vector2f(verticesB[(i + 1) % verticesB.length]);

            Vector2f edge = vb.sub(va);
            Vector2f axis = new Vector2f(-edge.y, edge.x).normalize(); //Normal to the chosen edge

            float[] minMaxA = CollisionDetector.projectVertices(verticesA, axis); //get projection for polygon A
            float[] minMaxB = CollisionDetector.projectVertices(verticesB, axis); //get projection for polygon B

            if (minMaxA[0] >= minMaxB[1] || minMaxB[0] >= minMaxA[1]) {
                return null;
            }
        }

        //Check which side the axis we are on
        Vector2f posA = rbA.getCenter();
        Vector2f posB = rbB.getCenter();
        Vector2f direction = new Vector2f(posB).sub(posA);

        if (direction.dot(normal) < 0) {
            return new CollisionHelper(normal.negate(), depth);
        } else {
            return new CollisionHelper(normal, depth);
        }
    }

    /**
     * Realistically resolve collision between two bodies
     * @param bodyA
     * @param bodyB
     * @param normal
     * @param depth
     */
    public static void resolveCollision(RigidBody rbA, RigidBody rbB, Vector2f normal, float depth) {
        Vector2f relativeVel = new Vector2f(rbB.transform.velocity).sub(rbA.transform.velocity);
        float invMassA = rbA.getInverseMass();
        float invMassB = rbB.getInverseMass();

        // //Do nothing if two blocks are already moving away from each other
        if (relativeVel.dot(normal) > 0.0f) {
            return;
        }

        //impulse calculation
        float e = Math.min(rbB.transform.restitution, rbA.transform.restitution);
        float j = (-(1.0f + e) * relativeVel.dot(normal));
        j = j / (invMassA + invMassB);
 
        Vector2f impulseA =  new Vector2f(normal).mul(j * invMassA);
        Vector2f impulseB =  new Vector2f(normal).mul(j * invMassB);

        //Convert impulse to acceleration (divide impulse by mass)
        rbA.applyForce(impulseA.negate().mul(invMassA));
        rbB.applyForce(impulseB.mul(invMassB));
    }

    public static void iterImpulseResolution(RigidBody rbA, RigidBody rbB, Vector2f normal, float depth) {

    }

    /*
     * Projects the input vertices onto the axis. Then return the maximum and minimum vertex when projected
     * onto the axis.
     */
    private static float[] projectVertices(Vector2f[] vertices, Vector2f axis) {

        float[] vals = {Float.MAX_VALUE, -Float.MAX_VALUE};

        for (int i=0; i < vertices.length; i++) {
            Vector2f v = new Vector2f(vertices[i]);
            float projection = v.dot(axis);

            if (projection < vals[0]) {
                vals[0] = projection;
            }
            if (projection > vals[1]) {
                vals[1] = projection;
            }
        }
        return vals;
    }
}




