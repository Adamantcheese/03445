/***************************************************************
* file: FPCameraController.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 5/24/2015
*
* purpose: An implementation of a camera object for OpenGL.
****************************************************************/
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class FPCameraController {
    private Vector3f position;
    private Vector3f lPosition;
    
    private float yaw;
    private float pitch;
    
    //method: constructor
    //purpose: Sets up our camera initially at the given location.
    public FPCameraController(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y + 15f, z);
        yaw = 135.0f;
        pitch = 0.0f;
    }
    
    //method: incYaw
    //purpose: Increases our yaw amount (left/right rotation).
    public void incYaw(float amount) {
        yaw += amount;
    }
    
    //method: incPitch
    //purpose: Increases our pitch amount (up/down rotation).
    public void incPitch(float amount) {
        pitch -= amount;
    }
    
    //method: moveForward
    //purpose: Moves our camera forward in relation to the current yaw.
    public void moveForward(float dist) {
        float xOffset = dist*(float)Math.sin(Math.toRadians(yaw));
        float zOffset = dist*(float)Math.cos(Math.toRadians(yaw));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //method: moveBackward
    //purpose: Moves our camera backward in relation to the current yaw.
    public void moveBackward(float dist) {
        float xOffset = dist*(float)Math.sin(Math.toRadians(yaw));
        float zOffset = dist*(float)Math.cos(Math.toRadians(yaw));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x += xOffset).put(lPosition.y).put(lPosition.z -= zOffset).put(1f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        position.x += xOffset;
        position.z -= zOffset;
    }
    
    //method: moveLeft
    //purpose: Moves our camera left in relation to the current yaw.
    public void moveLeft(float dist) {
        float xOffset = dist*(float)Math.sin(Math.toRadians(yaw - 90));
        float zOffset = dist*(float)Math.cos(Math.toRadians(yaw - 90));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //method: moveRight
    //purpose: Moves our camera right in relation to the current yaw.
    public void moveRight(float dist) {
        float xOffset = dist*(float)Math.sin(Math.toRadians(yaw + 90));
        float zOffset = dist*(float)Math.cos(Math.toRadians(yaw + 90));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //method: moveUp
    //purpose: Moves our camera up.
    public void moveUp(float dist) {
        position.y -= dist;
    }
    
    //method: moveDown
    //purpose: Moves our camera down.
    public void moveDown(float dist) {
        position.y += dist;
    }
    
    //method: lookThrough
    //purpose: Puts our camera in the right place and looks in the direction we specified.
    public void lookThrough() {
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        glTranslatef(position.x, position.y, position.z);
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(lPosition.y).put(lPosition.z).put(1f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
}
