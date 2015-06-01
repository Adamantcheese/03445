/***************************************************************
* file: Frustum.java
* author: Lauren Gamboa, Nikko Medina
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 5/28/2015
*
* purpose: This class is responsible for the frustum culling
* algorithm responsible for rendering objects within the viewing window.
* Any cube or chunk without it's 8 corners outside the viewing window
* should be culled, anything else should return true. 
* 
* Currently non-functional with our Project.
****************************************************************/
import org.lwjgl.BufferUtils;
import java.nio.*;
import static org.lwjgl.opengl.GL11.*;

public class Frustum{
    // These fields give a value for each plane within the viewing window,
    //assuming viewing window is normally shaped.
    private final int right = 0;
    private final int left = 1;
    private final int bottom = 2;
    private final int top = 3;
    private final int back = 4;
    private final int front = 5;

    // These fields are coefficients for the equation of a plane:
    // Ax + By + Cz + D = 0
    //result of frustum is used by checking points against these values
    private final int a = 0;
    private final int b = 1;
    private final int c = 2;
    private final int d = 3;
    
    float[][] frustum = new float[6][4];
    FloatBuffer modelBuf;
    FloatBuffer projectionBuf;

    // method: normalizePlane
    // purpose: Normalizes each coefficient of the plane
    public void normalizePlane(int side){
        float coeff = (float) Math.sqrt(frustum[side][a] * frustum[side][a] + frustum[side][b] * frustum[side][b] + frustum[side][c] * frustum[side][c]);

        frustum[side][a] /= coeff;
        frustum[side][b] /= coeff;
        frustum[side][c] /= coeff;
        frustum[side][d] /= coeff;
    }

    /**
     * Method uses GL to calculate the frustum using the equation above.
     * after we clip, we extrat each side of the frustum by adding the lengths.
     */
    public void calculateFrustum(){
        float[] projMatrix = new float[16];
        float[] modMatrix = new float[16];
        float[] clipMatrix = new float[16];

        projectionBuf.rewind();
        glGetFloat(GL_PROJECTION_MATRIX, projectionBuf);
        projectionBuf.rewind();
        projectionBuf.get(projMatrix);

        modelBuf.rewind();
        glGetFloat(GL_MODELVIEW_MATRIX, modelBuf);
        modelBuf.rewind();
        modelBuf.get(modMatrix);

        clipMatrix[0] = modMatrix[0] * projMatrix[0] + modMatrix[1] * projMatrix[4] + modMatrix[2] * projMatrix[8] + modMatrix[3] * projMatrix[12];
        clipMatrix[1] = modMatrix[0] * projMatrix[1] + modMatrix[1] * projMatrix[5] + modMatrix[2] * projMatrix[9] + modMatrix[3] * projMatrix[13];
        clipMatrix[2] = modMatrix[0] * projMatrix[2] + modMatrix[1] * projMatrix[6] + modMatrix[2] * projMatrix[10] + modMatrix[3] * projMatrix[14];
        clipMatrix[3] = modMatrix[0] * projMatrix[3] + modMatrix[1] * projMatrix[7] + modMatrix[2] * projMatrix[11] + modMatrix[3] * projMatrix[15];

        clipMatrix[4] = modMatrix[4] * projMatrix[0] + modMatrix[5] * projMatrix[4] + modMatrix[6] * projMatrix[8] + modMatrix[7] * projMatrix[12];
        clipMatrix[5] = modMatrix[4] * projMatrix[1] + modMatrix[5] * projMatrix[5] + modMatrix[6] * projMatrix[9] + modMatrix[7] * projMatrix[13];
        clipMatrix[6] = modMatrix[4] * projMatrix[2] + modMatrix[5] * projMatrix[6] + modMatrix[6] * projMatrix[10] + modMatrix[7] * projMatrix[14];
        clipMatrix[7] = modMatrix[4] * projMatrix[3] + modMatrix[5] * projMatrix[7] + modMatrix[6] * projMatrix[11] + modMatrix[7] * projMatrix[15];

        clipMatrix[8] = modMatrix[8] * projMatrix[0] + modMatrix[9] * projMatrix[4] + modMatrix[10] * projMatrix[8] + modMatrix[11] * projMatrix[12];
        clipMatrix[9] = modMatrix[8] * projMatrix[1] + modMatrix[9] * projMatrix[5] + modMatrix[10] * projMatrix[9] + modMatrix[11] * projMatrix[13];
        clipMatrix[10] = modMatrix[8] * projMatrix[2] + modMatrix[9] * projMatrix[6] + modMatrix[10] * projMatrix[10] + modMatrix[11] * projMatrix[14];
        clipMatrix[11] = modMatrix[8] * projMatrix[3] + modMatrix[9] * projMatrix[7] + modMatrix[10] * projMatrix[11] + modMatrix[11] * projMatrix[15];

        clipMatrix[12] = modMatrix[12] * projMatrix[0] + modMatrix[13] * projMatrix[4] + modMatrix[14] * projMatrix[8] + modMatrix[15] * projMatrix[12];
        clipMatrix[13] = modMatrix[12] * projMatrix[1] + modMatrix[13] * projMatrix[5] + modMatrix[14] * projMatrix[9] + modMatrix[15] * projMatrix[13];
        clipMatrix[14] = modMatrix[12] * projMatrix[2] + modMatrix[13] * projMatrix[6] + modMatrix[14] * projMatrix[10] + modMatrix[15] * projMatrix[14];
        clipMatrix[15] = modMatrix[12] * projMatrix[3] + modMatrix[13] * projMatrix[7] + modMatrix[14] * projMatrix[11] + modMatrix[15] * projMatrix[15];

        //left side of Frustum, normalize
        frustum[left][a] = clipMatrix[3] + clipMatrix[0];
        frustum[left][b] = clipMatrix[7] + clipMatrix[4];
        frustum[left][c] = clipMatrix[11] + clipMatrix[8];
        frustum[left][d] = clipMatrix[15] + clipMatrix[12];
        normalizePlane(left);

        //right side of Frustum, normalize
        frustum[right][a] = clipMatrix[3] - clipMatrix[0];
        frustum[right][b] = clipMatrix[7] - clipMatrix[4];
        frustum[right][c] = clipMatrix[11] - clipMatrix[8];
        frustum[right][d] = clipMatrix[15] - clipMatrix[12];
        normalizePlane(right);
        
        //back side of Frustum, normalize
        frustum[back][a] = clipMatrix[3] - clipMatrix[2];
        frustum[back][b] = clipMatrix[7] - clipMatrix[6];
        frustum[back][c] = clipMatrix[11] - clipMatrix[10];
        frustum[back][d] = clipMatrix[15] - clipMatrix[14];
        normalizePlane(back);

        //bottom side of Frustum, normalize
        frustum[bottom][a] = clipMatrix[3] + clipMatrix[1];
        frustum[bottom][b] = clipMatrix[7] + clipMatrix[5];
        frustum[bottom][c] = clipMatrix[11] + clipMatrix[9];
        frustum[bottom][d] = clipMatrix[15] + clipMatrix[13];
        normalizePlane(bottom);

        //top side of Frustum, normalize
        frustum[top][a] = clipMatrix[3] - clipMatrix[1];
        frustum[top][b] = clipMatrix[7] - clipMatrix[5];
        frustum[top][c] = clipMatrix[11] - clipMatrix[9];
        frustum[top][d] = clipMatrix[15] - clipMatrix[13];
        normalizePlane(top);

        //front side of Frustum, normalize
        frustum[front][a] = clipMatrix[3] + clipMatrix[2];
        frustum[front][b] = clipMatrix[7] + clipMatrix[6];
        frustum[front][c] = clipMatrix[11] + clipMatrix[10];
        frustum[front][d] = clipMatrix[15] + clipMatrix[14];
        normalizePlane(front);


    }
    /**
     * This method goes through each vertex of the cube and checks if that cube is
     * within the calculated frustum. 
     * 
     * @param x = x position of cube
     * @param y = y position of cube
     * @param z = z position of cube
     * @param size = cubic size 
     * @return true if cube is within viewing window of calculated frustum. false
     *      if not within frustum. 
     */
    public boolean cubeInFrustum(float x, float y, float z, float size){
        for(int i = 0; i < 6; i++ ){
            // If no points are within the viewing window, the cube is rejected
            if(frustum[i][a] * (x - size) + frustum[i][b] * (y - size) + frustum[i][c] * (z - size) + frustum[i][d] > 0)
                continue;
            if(frustum[i][a] * (x + size) + frustum[i][b] * (y - size) + frustum[i][c] * (z - size) + frustum[i][d] > 0)
                continue;
            if(frustum[i][a] * (x - size) + frustum[i][b] * (y + size) + frustum[i][c] * (z - size) + frustum[i][d] > 0)
                continue;
            if(frustum[i][a] * (x + size) + frustum[i][b] * (y + size) + frustum[i][c] * (z - size) + frustum[i][d] > 0)
                continue;
            if(frustum[i][a] * (x - size) + frustum[i][b] * (y - size) + frustum[i][c] * (z + size) + frustum[i][d] > 0)
                continue;
            if(frustum[i][a] * (x + size) + frustum[i][b] * (y - size) + frustum[i][c] * (z + size) + frustum[i][d] > 0)
                continue;
            if(frustum[i][a] * (x - size) + frustum[i][b] * (y + size) + frustum[i][c] * (z + size) + frustum[i][d] > 0)
                continue;
            if(frustum[i][a] * (x + size) + frustum[i][b] * (y + size) + frustum[i][c] * (z + size) + frustum[i][d] > 0)
                continue;
            return false;
        }
        return true;
    }
    // method: chunkInFrustum
    // purpose: Calls the cubeInFrustum method using values from the Chunk class
    public boolean chunkInFrustum(Chunk c){
        return cubeInFrustum(c.getStartX(), c.getStartY(), c.getStartZ(), c.getBlockLength() * c.getChunkSize());
    }
    
    // method: render
    // purpose: This method facilitates the rendering of chunks based on the 
    // condition that they are within the viewing window.
    public void render(Chunk c){
        if(chunkInFrustum(c)){
            c.render();
        }else{
            System.out.println("chunk not rendered");
        }
    }
    /**
     * Constructor for Frustum, initializes frustum buffers.
     */
    public Frustum() {
        modelBuf = BufferUtils.createFloatBuffer(16);
        projectionBuf = BufferUtils.createFloatBuffer(16);
    }
}