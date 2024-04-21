package engine;

public abstract class Component {

    public transient GameObject gameObject; //Cicular dependency of component and gameObject
    

    public void start(){
        
    }

    public void update(float dt) {

    }

    public void imgui() {

    }
}
