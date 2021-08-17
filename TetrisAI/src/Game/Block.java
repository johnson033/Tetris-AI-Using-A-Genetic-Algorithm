package Game;

import java.awt.*;

public class Block extends BlockShapes {

    // Class Variables -------------------------------------------------------------------------------------------------

    private final int[][][] blockRotations;
    private final int shapeIndex;
    private int[][] block;
    private int x, y, currentRotation;
    private final Image blockTexture;

    // Constructor -----------------------------------------------------------------------------------------------------

    public Block(int shapeIndex){
        this.shapeIndex = shapeIndex;
        this.currentRotation = 0;
        this.blockRotations = getBlockRotations(getBlockShape(shapeIndex));
        this.block = blockRotations[0];
        this.blockTexture = getBlockTexture(shapeIndex);
        this.x = 0;
        this.y = - getBlockHeight() - 1;
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    /* Gets the block texture Image. Called when drawing the shape to the screen */

    public Image getBlockTexture(){return this.blockTexture;}

    /* Gets the Block shape as a 2D Int array. Block Present = 1, not present = 0 */

    public int[][] getBlock(){return this.block;}

    /* Gets the Index of the Shape. 7 shapes possible */

    public int getBlockIndex(){
        return this.shapeIndex;
    }

    /* Gets the rotation of the shape. There are 4 possible for each shape */

    public int getCurrentRotation(){
        return this.currentRotation;
    }

    /* Gets the X Position of the current Shape. Used to check to see if it can move left or right */

    public int getX(){return this.x;}

    /* Gets the Y Position of the current Shape. Used to check if the game is over or if the shape can move down */

    public int getY(){return this.y;}

    /* Gets the block height. Top most piece to bottom piece  */

    public int getBlockHeight(){return this.block.length;}

    /* Gets the width of the current shape. */

    public int getBlockWidth(){return this.block[0].length;}

    /* Gets the bottom edge of the shape. Used to check for collision */

    public int getBottomEdge(){return this.y + getBlockHeight();}

    /* Gets the right edge of the shape. Used to check for collision */

    public int getRightEdge(){return this.x + getBlockWidth();}

    /* Gets the Left edge of a shape. Used to check for collision */

    public int getLeftEdge(){return this.x;}

    // setters ---------------------------------------------------------------------------------------------------------

    /* Moves the block down by increasing the Y Position */

    public void moveDown(){
        this.y++;
    }

    /* Rotates the block. 4 Rotations possible and will cycle through them if necessary */

    public void rotate(){
        this.currentRotation++;
        if(this.currentRotation > 3)this.currentRotation = 0;
        if(getLeftEdge() + this.blockRotations[this.currentRotation][0].length > 9)x = 9 - this.blockRotations[this.currentRotation].length;
        else if(getRightEdge() - this.blockRotations[this.currentRotation][0].length <0) x = 0;
        this.block = this.blockRotations[this.currentRotation];
    }

    /* Sets the Y Position of the block. This is strictly used when testing each possible game state */

    public void setY(int y){
        this.y = y;
    }

    /* Sets the X position of the block. Used once finding the best spot for the block */

    public void setX(int x){
        this.x = x;
    }
}
