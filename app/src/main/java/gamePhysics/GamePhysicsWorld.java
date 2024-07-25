package gamePhysics;

import org.joml.Vector2f;

import components.Component;
import engine.GameObject;


import physicsEngine2D.PhysicsWorld;
import physicsEngine2D.primitives.RigidBody;
import physicsEngine2D.primitives.Shape;

public class GamePhysicsWorld {

    private PhysicsWorld world;

    public GamePhysicsWorld() {
        this.world = new PhysicsWorld();
    }

    public void addRigidBody(GameObject ob, boolean isStatic) {
        //Check if gameobject already have an attached rigidbody
        for (Component component : ob.getAllComponent()) {
            if (component instanceof PhysicsBlock) {
                return;
            }
        }

        //Create new rigidBody and add to the physics world
        Vector2f position = ob.transform.position;

        RigidBody rigidBody = new RigidBody(ob.transform.position, isStatic);
        Shape shape = rigidBody.transform.shape;
        shape.setVertices(position);
        world.addBody(rigidBody);

        //Create the new physicsBlock for integration into the gameEngine
        PhysicsBlock physicsBlock = new PhysicsBlock(rigidBody);
        ob.addComponent(physicsBlock);
        System.out.println(rigidBody);
        System.out.println(ob.getAllComponent());
        System.out.println(world.bodies);

    }

    public void update(float dt) {
        this.world.update(dt);
    }

    public void destroyGameObject(GameObject go) {
        PhysicsBlock block = go.getComponent(PhysicsBlock.class);
        if (block != null) {
            //Destroy the rigidbody in the physics world
            block.destroy();
        }
    }
    
}
