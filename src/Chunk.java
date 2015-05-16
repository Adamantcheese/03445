/***************************************************************
* file: Chunk.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 5/11/2015
*
* purpose: This class implements a chunk containing multiple blocks.
****************************************************************/
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk {
    static final int CHUNK_SIZE = 30;
    static final float[] CUBE_COLOR = {1, 1, 1};
    static final int TEXTURE_SIZE = 64;
    
    private Block[][][] blocks;
    
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    
    private Texture texture;
    
    private int startX;
    private int startY;
    private int startZ;

    //method: render
    //purpose: Renders this chunk using GL Buffers.
    public void render() {
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2, GL_FLOAT, 0, 0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*24);
        glPopMatrix();
    }
    
    //method: rebuildMesh
    //purpose: Constructs the GL Buffers used to render this chunk.
    private void rebuildMesh() {
        VBOVertexHandle = glGenBuffers();
        VBOColorHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*72);
        FloatBuffer vertexColorData = BufferUtils.createFloatBuffer(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*72);
        FloatBuffer vertexTextureData = BufferUtils.createFloatBuffer(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*72);
        
        for(float x = 0; x < CHUNK_SIZE; x++) {
            for(float z = 0; z < CHUNK_SIZE; z++) {
                for(float y = 0; y < CHUNK_SIZE; y++) {
                    vertexPositionData.put(createCube((float) (startX + x*Block.BLOCK_LENGTH), 
                            (float) (y*Block.BLOCK_LENGTH + (int) (CHUNK_SIZE*.8)), 
                            (float) (startZ + z*Block.BLOCK_LENGTH)));
                    vertexColorData.put(createCubeVertexCol(getCubeColor(blocks[(int)x][(int)y][(int)z])));                   
                    vertexTextureData.put(createTexCube((float) 0, (float) 0, blocks[(int) x][(int) y][(int) z]));
                }
            }
        }
        vertexPositionData.flip();
        vertexColorData.flip();
        vertexTextureData.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    public static float[] createTexCube(float x,float y, Block b){
        float offset = (256f/4)/256;
        //System.out.println(b.getID());
        //How???
        
        //Get the ID by calling b.getID();
        //You can see what types there are in the block class, so compare to those
        // i.e. BlockType_Grass or BlockType_Stone
        //Once we know the blocktype, return a float array of stuff. works like this:
        // x + offset * COLUMN, y + offset * ROW
        // x + offset * (COLUMN - 1), y + offset * ROW
        // x + offset * (COLUMN - 1), y + offset * (ROW - 1)
        // x + offset * COLUMN, y + offset * (ROW - 1)
        
        //Do that for each of the faces, where COLUMN is the column of the texture you want and ROW is the row of the texture you want
        
        //Then do that for each of the block types, and everything should work fine
        
        //switch (b.getID()) {
            //case 1:
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*3, y + offset*10,
                x + offset*2, y + offset*10,
                x + offset*2, y + offset*9,
                x + offset*3, y + offset*9,
                // TOP!
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                // FRONT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // BACK QUAD
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                // LEFT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // RIGHT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1};
        
       // }
        
    }
    //method: getCubeColor
    //purpose: returns cube color
    private float[] getCubeColor(Block block) {
        return new float[] { 1, 1, 1 };  
    }
    //method: createCubeVertexCol
    //purpose: Creates an array of cube colors for the given column in the chunk.
    private float[] createCubeVertexCol(float[] cubeColorArray) {
        float[] cubeColors = new float[cubeColorArray.length * 24];
        for(int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = cubeColorArray[i%cubeColorArray.length];
        }
        return cubeColors;
    }
    
    //method: createCube
    //purpose: Creates a cube at the given corner point and returns it as a float array.
    //The float array is a multiple of 4 so that GL_QUADS may be drawn.
    public static float[] createCube(float x, float y, float z) {
        int offset = Block.BLOCK_LENGTH/2;
        return new float[] {
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - Block.BLOCK_LENGTH,
            x + offset, y + offset, z - Block.BLOCK_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - Block.BLOCK_LENGTH, 
            x - offset, y - offset, z - Block.BLOCK_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - Block.BLOCK_LENGTH, 
            x - offset, y + offset, z - Block.BLOCK_LENGTH, 
            x - offset, y - offset, z - Block.BLOCK_LENGTH,
            x + offset, y - offset, z - Block.BLOCK_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z, 
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - Block.BLOCK_LENGTH, 
            x - offset, y + offset, z, 
            x - offset, y - offset, z, 
            x - offset, y - offset, z - Block.BLOCK_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z, 
            x + offset, y + offset, z - Block.BLOCK_LENGTH, 
            x + offset, y - offset, z - Block.BLOCK_LENGTH,
            x + offset, y - offset, z
        };
    }
    
    //method: constructor
    //purpose: Creates this chunk based on the given starting location, and generates all render buffers for it.
    public Chunk(int startX, int startY, int startZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        
        try{
            texture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("textures.png"));
        }catch(Exception e){
            System.out.print("MY MIND IS THE INTERNET. I KNOW EVERY CONTINUITY MISTAKE"
                                + "EVER MADE ON TELEVISION.");
        }
        
        Random r = new Random();
        blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (r.nextFloat() > 0.7f) {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    } else if (r.nextFloat() > 0.4f) {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    } else if (r.nextFloat() > 0.2f) {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    } else {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                    }
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        rebuildMesh();
    }
}
