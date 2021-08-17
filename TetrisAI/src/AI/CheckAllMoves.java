package AI;

import Game.Game;

import java.awt.*;
import java.util.Arrays;
import Game.Block;

public class CheckAllMoves {

    private Image[][] gameState;
    private final Block block, nextBlock;
    private final Game game;
    private int bestXPosition, bestRotation;

    // Constructor ----------------------------------------------------------------------------------------------------

    public CheckAllMoves(Image[][] gameState, Game game){
        this.gameState = new Image[gameState.length][gameState[0].length];
        this.game = game;
        this.block = new Block(this.game.getBlock().getBlockIndex());
        this.nextBlock = new Block(this.game.getNextBlock().getBlockIndex());
        this.bestXPosition = 0;
        this.bestRotation = 0;
    }

   // MainFunction -----------------------------------------------------------------------------------------------------

    /* Goes through every possible move for the current Game State, Roughly 1600 */

    public void findBestMove(){
        double bestScore = Double.NEGATIVE_INFINITY;

        /* Loop One: Go through each rotation of the current piece. */

        for(int currentBlockRotation = 0; currentBlockRotation < 4; currentBlockRotation++){
            this.block.rotate();

            /* Loop Two: Go through each X position for the current piece. Around 10 */

            for(int currentBlockX = 0; currentBlockX < this.gameState[0].length - this.block.getBlockWidth() + 1; currentBlockX++){

                /* Loop Three: Go through each rotation of the next block. */

                for(int nextBlockRotation = 0; nextBlockRotation < 4; nextBlockRotation++){
                    this.nextBlock.rotate();

                    /* Loop Four: Go through each X position for the next block. */

                    for(int nextBlockX = 0; nextBlockX < this.gameState[0].length - this.nextBlock.getBlockWidth() + 1; nextBlockX++){
                        this.gameState = Arrays.stream(this.game.getBackground()).map(Image[]::clone).toArray(Image[][]::new);

                        this.block.setX(currentBlockX);
                        this.block.setY(0);
                        dropBlock(this.block);
                        paintBlockToBackground(this.block);

                        this.nextBlock.setX(nextBlockX);
                        this.nextBlock.setY(0);
                        dropBlock(this.nextBlock);
                        paintBlockToBackground(this.nextBlock);

                        double moveScore = moveScore(getA(),getB(),getC(),getD(0,1,0));
                        if(moveScore > bestScore){
                           this.bestXPosition = currentBlockX;
                           this.bestRotation = this.block.getCurrentRotation();
                           bestScore = moveScore;
                        }
                    }
                }
            }
        }
    }

    // Helper Functions ------------------------------------------------------------------------------------------------

    /* Drops the test block to the lowest position it can go */

    private void dropBlock(Block block){
        while(checkBottomEdge(block)){
            block.moveDown();
        }
    }

    /* Used to check if the test block is able to move down */

    private boolean checkBottomEdge(Block block){
        if(block.getBottomEdge() == game.getTileRows()) return false;
        int[][] shape = block.getBlock();
        for(int col = 0; col < block.getBlockWidth(); col++){
            for(int row = block.getBlockHeight() - 1; row >= 0; row--){
                if(shape[row][col] != 0){
                    int x = col + block.getX();
                    int y = row + block.getY() + 1;
                    if(y < 0)break;
                    if(this.gameState[y][x] !=null)return false;
                    break;
                }
            }
        }
        return true;
    }

    /* will move the block to the gameState array  */

    private void paintBlockToBackground(Block block){
        int[][] shape = block.getBlock();
        int h = block.getBlockHeight();
        int w = block.getBlockWidth();
        int xPos = block.getX();
        int yPos = block.getY();
        Image texture = block.getBlockTexture();
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if (shape[r][c] == 1) {
                    if(yPos < 0)break;
                    this.gameState[r + yPos][c + xPos] = texture;
                }
            }
        }
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    /* Gets the moves A Score ( Aggregated Height ) */

    private int getA(){
        int aHeight = 0;
        for(int col = 0; col < gameState[0].length; col++){
            for(int row = 0; row < gameState.length; row++){
                if(gameState[row][col] != null){
                    aHeight += gameState.length - row;
                    break;
                }
            }
        }
        return aHeight;
    }

    /* Gets the moves B Score ( Lines cleared ) */

    private int getB() {
        boolean lineCleared;
        int linesCleared = 0;
        for (int row = gameState.length - 1; row > 0; row--) {
            lineCleared = true;
            for (int col = 0; col < gameState[0].length; col++) {
                if (gameState[row][col] == null) {
                    lineCleared = false;
                    break;
                }
            }
            if (lineCleared) {
                linesCleared++;
            }
        }
        return linesCleared;
    }

    /* Gets the moves C Score ( Holes Created ) */

    private int getC(){
        int holes = 0;
        for(int row = gameState.length - 1; row > 0; row--){
            for(int col = 0; col < gameState[0].length; col++){
                if(gameState[row][col]==null && gameState[row-1][col] !=null){
                    holes++;
                }
            }
        }
        return holes;
    }

    /* Gets the moves D Score ( Bumpiness of the game ) */

    private int getD(int colOne, int colTwo, int bumpiness){
        if(colTwo == gameState[0].length)return bumpiness;

        int colOneHeight = 0;
        int colTwoHeight = 0;

        for(int row = 0; row < gameState.length; row++){
            if(gameState[row][colOne] != null && colOneHeight == 0){
                colOneHeight = gameState.length - row;
            }
            if(gameState[row][colTwo] != null && colTwoHeight == 0){
                colTwoHeight = gameState.length - row;
            }
        }
        return getD(colTwo, colTwo+1, (bumpiness + Math.abs(colOneHeight - colTwoHeight)));
    }

    /* After finding the best move, called to find what the best rotation was */

    public int getBestRotation(){
        return this.bestRotation;
    }

    /* After finding the best move, called to find the X position of the block  */

    public int getBestXPosition(){
        return this.bestXPosition;
    }

    /* Called to get the score of the move */

    private double moveScore(int A, int B, int C, int D){
        return ( (A * this.game.getDNA()[0]) + (B * this.game.getDNA()[1]) + (C * this.game.getDNA()[2]) + (D * this.game.getDNA()[3]) );
    }
}
