/***************************************************************
* file: Block.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 5/28/2015
*
* purpose: This class implements a specific type of block.
****************************************************************/
import static org.lwjgl.opengl.GL11.*;

public class Block {
    private boolean isActive;
    private BlockType type;
    
    //enum: BlockType
    //purpose: Declares different values for block types.
    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Default(-1);
        
        private int BlockID;
        
        BlockType(int i) {
            BlockID = i;
        }
        
        public int getID(){
            return BlockID;
        }
        
        public void setID(int i){
            BlockID= i;
        } 
    }
    
    //method: constructor
    //purpose: Sets up this block for rendering later with a given corner and side length.
    public Block (BlockType t) {
        type = t;
        if(t == BlockType.BlockType_Default) {
            isActive = false;
        } else {
            isActive = true;
        }
    }
    
    //method: isActive
    //purpose: Returns true if this block should be rendered.
    public boolean isActive() {
        return isActive;
    }
    
    //method: setActive
    //purpose: Sets if this block should be rendered.
    public void setActive(boolean active) {
        isActive = active;
    }
    
    //method: getID
    //purpose: Gets this block's type ID
    public int getID() {
        return type.getID();
    }
}
