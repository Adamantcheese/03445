/***************************************************************
* file: WindowContainer.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 5/28/2015
*
* purpose: This class contains all of the methods required to construct and show
* an OpenGL window with the given objects and runtime code.
****************************************************************/
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class WindowContainer {
    
    private FPCameraController camera;
    
    private Chunk chunk0;
    private Chunk chunk1;
    private Chunk chunk2;
    private Chunk chunk3;
    
    private final float MOUSE_SENSITIVITY = .2f;
    private final float MOVEMENT_SPEED = 0.75f;
    
    //method: constructor
    //purpose: Makes an object, uninitialized. Call start() to create the window and set everything up.
    public WindowContainer() {
        
    }
    
    //method: start
    //purpose: Initializes the display window, OpenGL, camera position, generates a chunk and begins rendering.
    public void start() {
        try {
            initUI();
            initGL();
            camera = new FPCameraController(0, 0, 0);
            
            chunk0 = new Chunk(0, 0, 0);
            chunk1 = new Chunk(Chunk.CHUNK_SIZE, 0, 0);
            chunk2 = new Chunk(0, 0, Chunk.CHUNK_SIZE);
            chunk3 = new Chunk(Chunk.CHUNK_SIZE, 0, Chunk.CHUNK_SIZE);
            
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //method: initUI
    //purpose: Initializes the display window.
    private void initUI() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
                    && d[i].getHeight() == 480
                    && d[i].getBitsPerPixel() == 32) {
                Display.setDisplayMode(d[i]);
                break;
            }
        }
        Display.setTitle("Project 3");
        Display.create();
        
        Mouse.setGrabbed(true);
    }
    
    //method: initGL
    //purpose: Initializes the OpenGL rendering suite used.
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100.0f, 
                (float)Display.getDisplayMode().getWidth()/(float)Display.getDisplayMode().getHeight(), 
                0.1f, 300.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        FloatBuffer whiteLight= BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);//sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);//sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);//sets our ambient light
        glEnable(GL_LIGHTING);//enables our lighting
        glEnable(GL_LIGHT0);//enables light0
    }
    
    //method: processInput
    //purpose: Changes the camera position based on if the user is pressing certain keys.
    //W moves the camera forward.
    //S moves the camera backward.
    //D moves the camera right.
    //A moves the camera left.
    //Space moves the camera up.
    //Left shift moves the camera down.
    private void processInput() {
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            camera.moveLeft(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            camera.moveRight(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            camera.moveBackward(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            camera.moveForward(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            camera.moveDown(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            camera.moveUp(MOVEMENT_SPEED);
        }
    }
    
    //method: render
    //purpose: Renders the objects that were specified when this WindowContainer was made.
    //If the user presses "Esc" or closes the window, the display is destroyed.
    private void render() {
        Frustum frustum = new Frustum();
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            int dx = Mouse.getDX(), dy = Mouse.getDY();
            camera.incYaw(dx * MOUSE_SENSITIVITY);
            camera.incPitch(dy * MOUSE_SENSITIVITY);
            if (dx != 0 || dy != 0) {
                frustum.calculateFrustum();
            }
            
            processInput();
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                camera.lookThrough();
          
                frustum.render(chunk0);
                frustum.render(chunk1);
                frustum.render(chunk2);
                frustum.render(chunk3);
                
                Display.update();
                Display.sync(60);
            } catch (Exception e) {
                
            }
        }
        Display.destroy();
    }
}
