// package physicsEngine2D.physics;

// import org.joml.Vector2f;

// import physicsEngine2D.primitives.RigidBody;

// public class Integration {
    
//     public static void verletPosIntegration(RigidBody rb, float dt) {
        
//         Vector2f tempPos = new Vector2f(rb.transform.position);

//         // Calculate the new position using Verlet integration formula
//         Vector2f currentPosition = new Vector2f(rb.transform.position);
//         Vector2f previousPosition = new Vector2f(rb.transform.prevPosition);
//         Vector2f acceleration = new Vector2f(rb.transform.acceleration.mul(dt * dt));

//         Vector2f newPos = currentPosition.mul(2).sub(previousPosition).add(acceleration);

//         rb.setCurrentPos(newPos);
//         rb.setPrevPos(tempPos);

//         //Update the objects velocity
//         rb.getVelocity().add(rb.transform.acceleration);
//     }

//     public static void verletVelIntegration(RigidBody rb, float dt) {
//         Vector2f currentPos = new Vector2f(rb.transform.position);
//         Vector2f velocity = new Vector2f(rb.getVelocity());
//         Vector2f acceleration = new Vector2f(rb.transform.acceleration);

//         Vector2f newPos = currentPos.add(new Vector2f(velocity).mul(dt))
//                                     .add(acceleration.mul(0.5f * dt * dt));

//         rb.setCurrentPos(newPos);

//         //Find the acceleration
//         Vector2f newAcceleration = new Vector2f(rb.getForce()).div(rb.getMass());

//         //Find the velocity
//         Vector2f newVelocity = new Vector2f(velocity).add(newAcceleration).mul(dt);

//         rb.setAcceleration(newAcceleration);
//         rb.setVelocity(newVelocity);
//     }

//     public static void step(RigidBody rb, float dt) {
//         rb.transform.position.add(rb.getVelocity()).mul(dt);
//     }
// }
