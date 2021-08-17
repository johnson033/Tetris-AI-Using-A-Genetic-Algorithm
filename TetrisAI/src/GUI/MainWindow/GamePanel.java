package GUI.MainWindow;

import AI.Population;
import Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import Game.Block;

public class GamePanel extends JPanel implements MouseListener {

    // final Objects ---------------------------------------------------------------------------------------------------

    private final JLabel LinesClearedLabel;
    private final Image[][] background;
    private final Game game;

    // final Ints ------------------------------------------------------------------------------------------------------

    private final int tileColumns;
    private final int tileRows;

    // private booleans ------------------------------------------------------------------------------------------------

    private boolean onScreen = false;
    private boolean selected = false;
    private boolean mainPanel = false;
    private boolean AIState = true;

    // Non final Objects -----------------------------------------------------------------------------------------------

    private Block block;

    // private ints ----------------------------------------------------------------------------------------------------

    private int windowWidth;
    private int windowHeight;
    private int windowX;
    private int windowY;
    private int tileSize;

    // Constructor -----------------------------------------------------------------------------------------------------

    public GamePanel(int x, int y, int width, int height, Game game, int gameIndex){

        this.addMouseListener(this);
        this.game = game;

        this.tileColumns = this.game.getTileColumns();
        this.tileRows = this.game.getTileRows();
        this.background = this.game.getBackground();
        this.windowWidth = width;

        this.windowHeight = height;
        this.windowX = x;
        this.windowY = y;
        this.tileSize = width / this.tileColumns;
        this.block = this.game.getBlock();

        if(this.getGame().isGameMutated()){
            JLabel mutatedLabel = new JLabel();
            mutatedLabel.setBounds(0, 10,this.getWindowWidth(),30);
            mutatedLabel.setForeground(new Color(255, 31, 31));
            mutatedLabel.setFont(MainWindow.customFont);
            mutatedLabel.setText("Mutated");
            this.add(mutatedLabel);
        }

        LinesClearedLabel = new JLabel();
        LinesClearedLabel.setBounds(0, 0,this.getWindowWidth(),30);
        LinesClearedLabel.setForeground(new Color(200,200,255));
        LinesClearedLabel.setFont(MainWindow.customFont);
        LinesClearedLabel.setText(String.valueOf(gameIndex));
        LinesClearedLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(LinesClearedLabel);

        this.setBounds(x,y, width, height);
        this.setBackground(new Color(23, 27, 38));
        this.setBorder(BorderFactory.createMatteBorder(3,3,3,3,new Color(200,200,255)));
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    /* Returns this game Object */

    public Game getGame(){return this.game;}

    /* Returns the Window width of this Game Object, Used for swapping positions on screen and setting block size */

    public int getWindowWidth(){
        return this.windowWidth;
    }

    /* Returns the Window Height, Again is mostly needed for swapping positions on the screen. */

    public int getWindowHeight(){
        return this.windowHeight;
    }

    /* Returns the Window X Positions, Once again, needed for swapping positions.  */

    public int getWindowX(){
        return this.windowX;
    }

    /* Returns the Y Position of the Window, You guessed it, For swapping positions.  */

    public int getWindowY(){
        return this.windowY;
    }

    // Setters ---------------------------------------------------------------------------------------------------------

    /* Sets the boolean value of weather this game object is on screen. This is checked once the game dies. */

    public void setOnScreen(Boolean val){
        this.onScreen = val;
    }

    /* Sets weather this game is the main panel or not, Mostly needed for when main panel dies and needs to be swapped */

    public void setMainPanel(boolean val){
        this.mainPanel = val;
    }

    /* Sets the Block object for the game. Called from the GameThread class to set the block */

    public void setBlock(Block block){
        this.block = block;
    }

    /* Sets the border of the game. Purely Visual, Used for when games die */

    public void setBorder(){

        if(!selected)
            this.setBorder(BorderFactory.createMatteBorder(3,3,3,3,new Color(210, 36, 36)));
    }

    /* Sets the UpdatedBounds of the game, Again this is needed to swap the game objects on screen */

    public void setUpdatedBounds(int x, int y, int width, int height){
        this.setBounds(x, y, width, height);
        this.windowX = x;
        this.windowY = y;
        this.windowWidth = width;
        this.windowHeight = height;
        this.tileSize = this.windowWidth / this.tileColumns;
    }

    /* Sets the AI On or Off, This is used to end a generation, or enable human play */

    public void setAIState(){
        this.AIState = !this.AIState;
    }

    // Checkers --------------------------------------------------------------------------------------------------------

    /* Checks if the block is out of bounds, Checked to see if the game should be set to Over */

    public boolean checkIfBlockIsOutOfBounds(){
        return block.getY() <= 0;
    }

    /* Checks to see if this is the main Panel. */

    public boolean checkIfIsMainPanel(){
        return this.mainPanel;
    }

    /* Checks to see if the AI is On Or Off, Needed in the GameThread to see if AI should place Block */

    public boolean checkAIState(){
        return this.AIState;
    }

    /* Checks to see if the Game is On the Screen. If not, and is going, available to swap to screen */

    public boolean checkIfOnScreen(){
        return this.onScreen;
    }

    // Visual ----------------------------------------------------------------------------------------------------------

    /* This method is called every frame. Used to Draw the screen */

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        paintShape(g);
        paintBackground(g);
        if(selected){
            MainWindow.linesClearedLabel.setText("Lines Cleared: " + this.game.getLinesCleared());
        }
        LinesClearedLabel.setText(String.valueOf(getGame().getLinesCleared()));
    }

    /* Paints the shape, Breaks shape down and calls paint Block to draw block to screen */

    private void paintShape(Graphics g){
        for(int row = 0;  row < this.block.getBlockHeight(); row++){
            for(int col = 0; col < this.block.getBlockWidth(); col++)
                try {
                    if (this.block.getBlock()[row][col] > 0) {
                        int x = (col * this.tileSize) + (this.block.getX() * this.tileSize);
                        int y = (row * this.tileSize) + (this.block.getY() * this.tileSize);
                        paintBlock(g, this.block.getBlockTexture(), x, y);
                    }
                }catch (Exception e){
                    System.out.print("");
                }
        }
    }

    /* Called from paintShape, Draws each block to the screen (4 per shape) */

    private void paintBlock(Graphics g, Image texture, int x, int y){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.drawImage(texture, x,y,tileSize,this.tileSize, this);
    }

    /* Paints the Background, Each previous shape that is placed is moved to the background */

    private void paintBackground(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Image texture;
        for(int r = 0; r < tileRows; r++){
            for(int c = 0; c < tileColumns; c++){
                texture = background[r][c];
                int x = tileSize * c;
                int y = tileSize * r;
                if (texture != null) {
                    paintBlock(g2d, texture, x, y);
                }
            }
        }
    }

    /* Moves a shape to the background after it can no longer move down */

    public void paintBlockToBackground(){
        int[][] shape = this.block.getBlock();
        int h = this.block.getBlockHeight();
        int w = this.block.getBlockWidth();
        int xPos = this.block.getX();
        int yPos = block.getY();
        Image texture = this.block.getBlockTexture();
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if (shape[r][c] == 1) {
                    background[r + yPos][c + xPos] = texture;
                }
            }
        }
    }

    // Mouse Event Handlers --------------------------------------------------------------------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {
        for(GamePanel game: Population.population){
            game.selected = false;
            if(game.getGame().getGameOver()){
                game.setBorder();
            }
            else{
                game.setBorder(BorderFactory.createMatteBorder(3,3,3,3,new Color(200,200,255)));
            }
        }
        this.selected = true;
        DecimalFormat format = new DecimalFormat("0.0000");
        MainWindow.bestGameLastGenerationLabel.setBounds(MainWindow.linesClearedLabel.getX(), 250, 200, 50);
        MainWindow.previousAVGLinesClearedLabel.setBounds(MainWindow.linesClearedLabel.getX(), 275, 200, 50);
        this.setBorder(BorderFactory.createMatteBorder(3,3,3,3,new Color(9, 255, 91)));
        MainWindow.HValueA.setText("Height Scoring: " + format.format(this.game.getDNA()[0]));
        MainWindow.HValueB.setText("Cleared Lines Scoring: " + format.format(this.game.getDNA()[1]));
        MainWindow.HValueC.setText("Holes Scoring: " + format.format(this.game.getDNA()[2]));
        MainWindow.HValueD.setText("Bumpiness Scoring : " + format.format(this.game.getDNA()[3]));
        for(GamePanel game: Population.population){
            if(game.checkIfIsMainPanel()){
                game.setMainPanel(false);
                this.setMainPanel(true);
                int tempX = getWindowX();
                int tempY = getWindowY();
                int tempWidth = getWindowWidth();
                int tempHeight = getWindowHeight();
                this.setUpdatedBounds(game.getWindowX(),game.getWindowY(),game.getWindowWidth(),game.getWindowHeight());
                game.setUpdatedBounds(tempX,tempY,tempWidth,tempHeight);
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {
        this.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(9, 255, 91)));
    }
    @Override
    public void mouseExited(MouseEvent e) {
        if(!selected){
            this.setBorder(BorderFactory.createMatteBorder(3,3,3,3,new Color(200,200,255)));
        }
        if(this.game.getGameOver() && !selected){
            this.setBorder(BorderFactory.createMatteBorder(3,3,3,3,new Color(210, 36, 36)));
        }
    }
}
