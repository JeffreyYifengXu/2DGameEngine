package physicsEngine2D.physics;

import org.joml.Vector2f;

import physicsEngine2D.primitives.RigidBody;

public class Collisions {

    public static void resolveCollision(RigidBody rbA, RigidBody rbB, Vector2f normal) {
        System.out.println("Normal vector " + normal);


        Vector2f rbAVel = new Vector2f(rbA.transform.velocity);
        Vector2f rbBVel = new Vector2f(rbB.transform.velocity);

        Vector2f relativeVelocity = rbAVel.sub(rbBVel);
        float e = Math.min(rbA.transform.restitution, rbB.transform.restitution);
        float normalVelocity = relativeVelocity.dot(normal);
        float j = -(1 + e) * normalVelocity;
        j = j / ( (1f / rbA.transform.mass) + (1f/rbB.transform.mass));

        System.out.println("rbA velocity before collision: " + rbA.transform.velocity);
        System.out.println("rbB velocity before collision: " + rbB.transform.velocity);

        rbA.transform.velocity.add(normal.mul(j).div(rbA.transform.mass));
        rbB.transform.velocity.add(normal.mul(j).div(rbB.transform.mass));

        System.out.println("rbA velocity after collision: " + rbA.transform.velocity);
        System.out.println("rbB velocity after collision: " + rbB.transform.velocity);


    }
    
}
