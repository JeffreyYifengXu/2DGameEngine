import engine.Window;
import physicsEngine2D.PhysicsSim;
import physicsEngine2D.PhysicsWorld;

public class Main {
    public static void main(String[] args){
        // Window window = Window.get();
        // window.run();


        //Uncomment this to play the physics engine!!!
        PhysicsSim sim = PhysicsSim.get();
        sim.run();
    }
}