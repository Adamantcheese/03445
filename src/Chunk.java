/***************************************************************
* file: Chunk.java
* author: Jacob Buchowiecki, Lauren Gamboa, Nikko Medina
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 5/16/2015
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
    static final int TEXTURE_SIZE = 64;
    static final int MIN_HEIGHT = 5;
    
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
        Random rand = new Random();
        SimplexNoise noise = new SimplexNoise(CHUNK_SIZE, 0.04, rand.nextInt());
        
        VBOVertexHandle = glGenBuffers();
        VBOColorHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*72);
        FloatBuffer vertexColorData = BufferUtils.createFloatBuffer(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*72);
        FloatBuffer vertexTextureData = BufferUtils.createFloatBuffer(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*72);
        
        for(float x = 0; x < CHUNK_SIZE; x++) {
            for(float z = 0; z < CHUNK_SIZE; z++) {
                int i = (int) (startX + x * Block.BLOCK_LENGTH);
                int k = (int) (startZ + z * Block.BLOCK_LENGTH);
                int maxHeight = (startY + (int) (100 * noise.getNoise(i, k)));
                
                for (float y = 0; y < MIN_HEIGHT; y++) {
                    vertexPositionData.put(createCube(
                            (float) (startX + x*Block.BLOCK_LENGTH), 
                            (float) (y*Block.BLOCK_LENGTH + (int) (CHUNK_SIZE*.8)), 
                            (float) (startZ + z*Block.BLOCK_LENGTH)));
                    vertexColorData.put(createCubeVertexCol());
                    vertexTextureData.put(createTexCube((float) 0, (float) 0, blocks[(int) x][(int) y][(int) z]));
                }
                
                for(float y = MIN_HEIGHT; y <= maxHeight + MIN_HEIGHT; y++) {
                    vertexPositionData.put(createCube(
                            (float) (startX + x*Block.BLOCK_LENGTH), 
                            (float) (y*Block.BLOCK_LENGTH + (int) (CHUNK_SIZE*.8)), 
                            (float) (startZ + z*Block.BLOCK_LENGTH)));
                    vertexColorData.put(createCubeVertexCol());                   
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
    
    //method: createTexCube
    //purpose: Attahes the correct textures to blocks based on their blocktype and location.
    public static float[] createTexCube(float x, float y, Block b) {
        float offset = (256f / 4) / 256;
        switch (b.getID()) {
            case 0:
                //grass texture
                return new float[]{
                    //TOP!
                    x + offset * 1, y + offset * 1,
                    x + offset * 0, y + offset * 1,
                    x + offset * 0, y + offset * 0,
                    x + offset * 1, y + offset * 0,
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 2, y + offset * 0,
                    x + offset * 1, y + offset * 0,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // BACK QUAD
                    x + offset * 2, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 0,
                    x + offset * 2, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 2, y + offset * 0,
                    x + offset * 1, y + offset * 0,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 2, y + offset * 0,
                    x + offset * 1, y + offset * 0,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1};

            case 1:
                //sand texture
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    //sand texture
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // TOP!
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // FRONT QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // BACK QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // LEFT QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1};
            case 2:
                //water texture
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 0, y + offset * 1,
                    // TOP!
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 0, y + offset * 1,
                    // FRONT QUAD
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 0, y + offset * 1,
                    // BACK QUAD
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 0, y + offset * 1,
                    // LEFT QUAD
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 0, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 0, y + offset * 1};

            case 3:
                //dirt texture
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // TOP!
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // BACK QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0};

            case 4:
                //stone texture
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    //sand texture
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9,
                    // TOP!
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9,
                    // BACK QUAD
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9};

            case 5:
                //bedrock texture
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    // TOP!
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // BACK QUAD
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1};
            default:
                //default currently set to ERROR
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 1, y + offset * 3,
                    x + offset * 0, y + offset * 3,
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // TOP! still needs to be just green
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    // FRONT QUAD
                    x + offset * 1, y + offset * 3,
                    x + offset * 0, y + offset * 3,
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // BACK QUAD
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,
                    // LEFT QUAD
                    x + offset * 1, y + offset * 3,
                    x + offset * 0, y + offset * 3,
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // RIGHT QUAD
                    x + offset * 1, y + offset * 3,
                    x + offset * 0, y + offset * 3,
                    x + offset * 0, y + offset * 2,
                    x + offset * 1, y + offset * 2};

        }

    }
    
    //method: createCubeVertexCol
    //purpose: Creates an array of cube colors for the given column in the chunk.
    private float[] createCubeVertexCol() {
        float[] cubeColors = new float[72];
        for(int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = 1;
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
        //added randomization for all types of textures
        Random r = new Random();
        blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    switch (r.nextInt(Integer.MAX_VALUE) % 6) {
                        case 0:
                            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                            break;
                        case 1:
                            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                            break;
                        case 2:
                            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                            break;
                        case 3:
                            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                            break;
                        case 4:
                            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                            break;
                        case 5:
                            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
                            break;
                        default:
                            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                            break;
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
