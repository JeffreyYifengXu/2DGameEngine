package physicsEngine2D;

import physicsEngine2D.primitives.AABB;

public class CollisionDetector {
    
    /*
     * detect collision between two axis aligned boxes without rotation
     */
    public static boolean AABBCollision(RigidBody rbA, RigidBody rbB) {

        float rbAX = rbA.getPosition().x;
        float rbAY = rbA.getPosition().y;
        float rbBX = rbB.getPosition().x;
        float rbBY = rbB.getPosition().y;

        AABB shapeA = (AABB) rbA.getShape();
        AABB shapeB = (AABB) rbB.getShape();

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
}
