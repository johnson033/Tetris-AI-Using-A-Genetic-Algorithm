package Game;

import GUI.MainWindow.MainWindow;

import java.awt.*;
import java.util.ArrayList;

public class Game {

    Frame mainWindow  = Frame.getFrames()[0];
    private final Image[][] background;
    private ArrayList<Block> blockSet;
    private Block nextBlock;
    private Block block;

    private final int gameIndex, numOfShapes, tileRows, tileColumns;
    private boolean gameMutated = false, GameOver;
    private double gameFitness;
    private int linesCleared;
    private double[] DNA;

    public Game(int gameIndex, double[] DNA){
        this.linesCleared = 0;
        this.numOfShapes = new BlockShapes().numOfShapes();
        this.gameIndex = gameIndex;
        this.tileColumns = 10;
        this.tileRows = tileColumns*2;
        this.GameOver = false;
        this.background = new Image[tileRows][tileColumns];
        this.DNA = DNA;
        spawnBlockSet();
        selectBlock();
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    /* Gets the Image array of the current Background. Background refers to the game grid where shapes are shown */

    public Image[][] getBackground(){return this.background;}

    /* Gets the current block. along with its current rotation  */

    public Block getBlock(){return this.block;}

    /* Gets the next block, this is the shape that will be next up to be played */

    public Block getNextBlock(){
        return this.nextBlock;
    }

    /* Gets weather the game is over or not, Checked from inside of the population class */

    public boolean getGameOver(){
        return this.GameOver;
    }

    /* Checks to see weather the game is mutated, Only checked for drawing labels and visuals */

    public boolean isGameMutated(){
        return this.gameMutated;
    }

    /* Moves the current Shape down */

    public boolean moveDown(){
        if(checkBottom()){
            this.block.moveDown();
            updateMainWindow();
            return true;
        }
        return false;
    }

    /* Checks the bottom of the current shape to see weather it is able to move down */

    public boolean checkBottom(){ // Check that the block is able to move down
        if(block.getBottomEdge() == this.tileRows) return false;
        int[][] shape = this.block.getBlock();
        for(int col = 0; col < this.block.getBlockWidth(); col++){
            for(int row = this.block.getBlockHeight() - 1; row >= 0; row--){
                if(shape[row][col] != 0){
                    int x = col + this.block.getX();
                    int y = row + this.block.getY() + 1;
                    if(y < 0)break;
                    if(this.background[y][x] !=null)return false;
                    break;
                }
            }
        }
        return true;
    }

    /* gets how many rows there are in the game, always 20 but could be changed to any number */

    public int getTileRows(){return this.tileRows;}

    /* gets the number of tile columns, this is always 10, but could be changed to any number */

    public int getTileColumns(){return this.tileColumns;}

    /* Gets how many lines have been cleared */

    public int getLinesCleared(){
        return this.linesCleared;
    }

    /* Gets the DNA of this game. These are the values used to calculate how good a given move would be  */

    public double[] getDNA(){
        return this.DNA;
    }

    /* Gets the fitness of the game. This is how many lines it has cleared / the most lines cleared in a population */

    public double getGameFitness(){
        return this.gameFitness;
    }

    // Setters ---------------------------------------------------------------------------------------------------------

    /* Spawns in a block set in a random order, this is a set of 7 blocks, so that we dont get the same shape 2 times */

    private void spawnBlockSet(){
        blockSet = new ArrayList<>(this.numOfShapes);
        for(int i = 0; i < this.numOfShapes; i++){
            this.blockSet.add(new Block(i));
        }
        pickNext();
    }

    /* Randomly picks what the next piece will be from the block set */

    private void pickNext(){
        int index = MainWindow.random.nextInt(this.blockSet.size());
        this.nextBlock = this.blockSet.get(index);
        this.blockSet.remove(index);
        if(this.blockSet.size() == 0)spawnBlockSet();
    }

    /* changes the current block to be the next block, and sets the new next block */

    public void selectBlock(){
        this.block = this.nextBlock;
        pickNext();
    }

    /* called to clear lines from the background */

    public void clearLines(){
        boolean lineFilled;
        for(int r = tileRows -1; r >= 0; r--){
            lineFilled = true;
            for(int c = 0; c < tileColumns; c++){
                if(background[r][c] == null){
                    lineFilled = false;
                    break;
                }
            }
            if(lineFilled){
                clearLine(r);
                shiftDown(r);
                clearLine(0);
                r++;
                this.linesCleared++;
                if(this.gameIndex == 0)
                    MainWindow.linesClearedLabel.setText("Lines Cleared: " + linesCleared);
                updateMainWindow();
            }
        }
    }

    /* Finds which lines need to be cleared and will clear them */

    public void clearLine(int r){
        for(int i =0; i < tileColumns; i++){
            background[r][i] = null;
        }
    }

    /* called when a line is cleared to move all the rows above that line down */

    public void shiftDown(int r){
        for(int row = r; row > 0; row--){
            if (tileColumns >= 0) System.arraycopy(background[row - 1], 0, background[row], 0, tileColumns);
        }
    }

    /* Sets the game over to true */

    public void setGameOver(){
        this.GameOver = true;
    }

    /* sets the game fitness, this is calculated inside of the Population Class  */

    public void setGameFitness(double fitness){
        this.gameFitness = fitness;
    }

    /* this will instantly drop the block down, Used to speed up the games */

    public void dropDown(){
        while(checkBottom()){
            this.block.moveDown();
        }
        updateMainWindow();
    }

    /*  Sets weather a game is mutated, This is used for labels, not actually needed for function  */

    public void setGameMutated(boolean val){
        this.gameMutated = val;
   }

   /* sets the DNA of the game.  This is used when a game is mutated and needs to be changed */

    public void setDNA(double[] DNA){
        this.DNA = DNA;
   }

   /* Updates the MainWindow. When something is changed this is called to redraw the screen */

    public void updateMainWindow(){
        mainWindow.repaint();
    }
}