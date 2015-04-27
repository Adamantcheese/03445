/***************************************************************
* file: Block.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 4/26/2015
*
* purpose: This class implements a block centered on the given location with a given side length.
****************************************************************/
import static org.lwjgl.opengl.GL11.*;

public class Block implements Renderable {
    private float centerX;
    private float centerY;
    private float centerZ;
    private float offset;
    
    //method: constructor
    //purpose: Sets up this block for rendering later with a given center and side length.
    public Block (float x, float y, float z, float s) {
        centerX = x;
        centerY = y;
        centerZ = z;
        offset = s/2.0f;
    }
    
    //method: render
    //purpose: Renders this block onto the screen. Currently all colors are static.
    public void render() {
        glBegin(GL_QUADS);
            //Right face (red)
            glColor3f(1f, 0f, 0f);
            glVertex3f(centerX + offset, centerY + offset, centerZ + offset);
            glVertex3f(centerX + offset, centerY - offset, centerZ + offset);
            glVertex3f(centerX + offset, centerY - offset, centerZ - offset);
            glVertex3f(centerX + offset, centerY + offset, centerZ - offset);
            
            //Left face (green)
            glColor3f(0f, 1f, 0f);
            glVertex3f(centerX - offset, centerY + offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY - offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY - offset, centerZ - offset);
            glVertex3f(centerX - offset, centerY + offset, centerZ - offset);
            
            //Front face (blue)
            glColor3f(0f, 0f, 1f);
            glVertex3f(centerX + offset, centerY + offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY + offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY - offset, centerZ + offset);
            glVertex3f(centerX + offset, centerY - offset, centerZ + offset);
            
            //Back face (yellow)
            glColor3f(1f, 1f, 0f);
            glVertex3f(centerX + offset, centerY + offset, centerZ - offset);
            glVertex3f(centerX - offset, centerY + offset, centerZ - offset);
            glVertex3f(centerX - offset, centerY - offset, centerZ - offset);
            glVertex3f(centerX + offset, centerY - offset, centerZ - offset);
            
            //Top face (cyan)
            glColor3f(0f, 1f, 1f);
            glVertex3f(centerX + offset, centerY + offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY + offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY + offset, centerZ - offset);
            glVertex3f(centerX + offset, centerY + offset, centerZ - offset);
            
            //Bottom face (purple)
            glColor3f(1f, 0f, 1f);
            glVertex3f(centerX + offset, centerY - offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY - offset, centerZ + offset);
            glVertex3f(centerX - offset, centerY - offset, centerZ - offset);
            glVertex3f(centerX + offset, centerY - offset, centerZ - offset);
        glEnd();
    }
}
