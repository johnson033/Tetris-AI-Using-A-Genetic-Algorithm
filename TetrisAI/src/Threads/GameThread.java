package Threads;

import AI.CheckAllMoves;
import GUI.MainWindow.GamePanel;
import GUI.MainWindow.MainWindow;
import Game.Game;

public class GameThread extends Thread{

    // private Objects -------------------------------------------------------------------------------------------------

    private final Game game;
    private final GamePanel gamePanel;

    // Constructor -----------------------------------------------------------------------------------------------------

    public GameThread(Game game, GamePanel gamePanel){
        this.game = game;
        this.gamePanel = gamePanel;
    }

    // Game Thread -----------------------------------------------------------------------------------------------------

    /* Each Game Object has a GameThread, This is what controls timing, and basically the whole game */

    @Override
    public void run(){

        /* Will run until the game is over */

        while (true) {
            this.game.selectBlock();
            this.gamePanel.setBlock(this.game.getBlock());
            CheckAllMoves check = new CheckAllMoves(this.game.getBackground(), this.game);
            check.findBestMove();

            /* If the Game AI is turned on, It will place the piece in the best spot according to its scoring */

            if (this.gamePanel.checkAIState()) {
                this.game.getBlock().setX(4);
                while (this.game.getBlock().getCurrentRotation() != check.getBestRotation()) {
                    this.game.getBlock().rotate();
                }
                this.game.getBlock().setX(check.getBestXPosition());
            }

            /* Checks to see if the game is Paused, If it is, it shouldn't do any of the following */
            if (!MainWindow.gamePaused) {



                while (this.game.moveDown()) {     /* Moves the block Down */

                    try {
                        sleep();
                        if (MainWindow.dropInstant) {     /* If Max Speed is on, Instantly drop the block */
                            this.game.dropDown();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    this.gamePanel.repaint();     /* Repaints the panel so it looks smooth */
                }

                if (gamePanel.checkIfBlockIsOutOfBounds()) {     /* Checks if the game is over or not */
                    game.setGameFitness(game.getLinesCleared());
                    gamePanel.setBorder();
                    gamePanel.getGame().setGameOver();
                    MainWindow.population.isPopulationDead(this.gamePanel);
                    break;
                }

                this.gamePanel.paintBlockToBackground();     /* Move the block to the background, clear any lines */
                this.game.clearLines();
            }
        }
    }

    public void sleep() throws InterruptedException {
        Thread.sleep(MainWindow.GameSpeed);
    }
}
