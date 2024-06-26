package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    private Vector2f projectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 21.0f);

    private float zoom = 1.0f; //Set the zoom of the camera

    public Vector2f position;
    

    /**
     * Initializer for the camera class, takes in the position of the game camera.
     * Creates a new projection matrix and a new view matrix.
     * Calls adjustProjection method
     * 
     * @param position
     */

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }
    
    /**
     * Set the projection matrix to be an identity matrix
     * Apply orthographic projection transformation for a right handed coordinate system.
     */
    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f, projectionSize.y * this.zoom, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    /**
     * Creates the view matrix. 
     * @return Current viewmatrix
     */
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                            cameraFront.add(position.x, position.y, 0.0f),
                                            cameraUp);

        this.viewMatrix.invert(inverseView);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }

    public Vector2f getProjectionSize() {
        return projectionSize;
    }

    public void setProjectionSize(Vector2f projectionSize) {
        this.projectionSize = projectionSize;
    }

    public void updatePos(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void addZoom(float val) {
        this.zoom += val;
    }
}















