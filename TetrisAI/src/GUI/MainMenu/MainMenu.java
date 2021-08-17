package GUI.MainMenu;

import AI.Population;
import GUI.MainWindow.MainWindow;
import Game.Block;
import Threads.MainMenuThread;
import Threads.TimerThread;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {

    private final int BlockSize;
    private final JSlider populationSize;
    private Block block;
    private final JLabel currentSize;


    Color[] fontColors = new Color[]{
            new Color(255,148,0,255),
            new Color(75,190,255,255),
            new Color(255,51,41,255),
            new Color(255,204,0,255),
            new Color(76,217,99,255),
            new Color(152,41,188,255),
            new Color(230,59,122,255),
    };

    // Constructor -----------------------------------------------------------------------------------------------------

    public MainMenu(int width, int height){
        this.setBounds(0,0,width, height);
        this.setBackground(new Color(7,9,15));
        this.BlockSize = this.getWidth() / 40;
        this.setLayout(null);
        new MainMenuThread(this).start();

        // slider to change the population size (num of games in each generation);

        populationSize = new JSlider(35, 100, 65);
        populationSize.setBounds(this.getWidth() / 2 - 100, (int) (this.getHeight() *.44), 200,20);
        populationSize.setBackground(new Color(7,9,15));
        populationSize.setForeground(new Color(75,190,255,255));
        populationSize.setMinorTickSpacing(5);
        currentSize = new JLabel("Population Size: " + populationSize.getValue());
        currentSize.setForeground(new Color(75,190,255,255));
        currentSize.setFont(MainWindow.customFont);
        currentSize.setBounds(0,(int) (this.getHeight() *.39), this.getWidth(), 25);
        currentSize.setHorizontalAlignment(JLabel.CENTER);
        populationSize.setSnapToTicks(true);
        populationSize.addChangeListener(e -> {
            currentSize.setText("Population Size: " + populationSize.getValue());
        });

        // Button to start the game

        JButton startGameButton = new JButton("Start");
        startGameButton.setFont(MainWindow.customFont);
        startGameButton.setBackground(new Color(7,9,15));
        startGameButton.setBorder(BorderFactory.createMatteBorder(3,3,3,3, new Color(75,190,255,255)));
        startGameButton.setBounds(width/2 - 100, height/2, 200,75);
        startGameButton.addActionListener(e -> {
            MainWindow.PopulationSize = populationSize.getValue();
            MainWindow.population = new Population();
            MainWindow.population.createPopulation(null);
            MainWindow.population.drawPopulation();
            setMainWindow();
            this.setVisible(false);
            new TimerThread().start();
        });



        this.add(currentSize);
        this.add(startGameButton);
        this.add(populationSize);
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    /* Gets the block, this block is only for the main menu and for visuals */

    public Block getBlock(){
        return this.block;
    }

    // Setters ---------------------------------------------------------------------------------------------------------

    /* Sets the block to be shown on the main menu */

    public void setBlock(Block block){
        this.block = block;
    }

    /* Sets all the Main Window Labels, and buttons */

    private void setMainWindow(){
        int leftX = (int) ((this.getWidth() *.64) + (int) (this.getWidth()* .25) + 10);
        JPanel controlLabelContainer = new JPanel();
        controlLabelContainer.setBounds(leftX, (this.getHeight() / 2 - (this.getHeight() / 3) / 2),this.getWidth() - (leftX + 10), this.getHeight()/3);
        controlLabelContainer.setBackground(new Color(7, 9, 15));
        controlLabelContainer.setLayout(null);

        JLabel controlLabel = new JLabel("Controls");
        controlLabel.setForeground(fontColors[2]);
        controlLabel.setFont(MainWindow.customFont);
        controlLabel.setBounds(0,0,this.getWidth() - (leftX + 10),50);
        controlLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel AIToggleControlLabel = new JLabel("W - Toggle AI");
        AIToggleControlLabel.setForeground(fontColors[3]);
        AIToggleControlLabel.setFont(MainWindow.customFont);
        AIToggleControlLabel.setBounds(0,25,this.getWidth() - (leftX + 10),50);
        AIToggleControlLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel slowGameDown = new JLabel("A - Slow Game Down");
        slowGameDown.setForeground(fontColors[4]);
        slowGameDown.setFont(MainWindow.customFont);
        slowGameDown.setBounds(0,50,this.getWidth() - (leftX + 10),50);
        slowGameDown.setHorizontalAlignment(JLabel.CENTER);

        JLabel GameSpeedToggleLabel = new JLabel("S - Toggle Speed");
        GameSpeedToggleLabel.setForeground(fontColors[5]);
        GameSpeedToggleLabel.setFont(MainWindow.customFont);
        GameSpeedToggleLabel.setBounds(0,75,this.getWidth() - (leftX + 10),50);
        GameSpeedToggleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel speedGameUP = new JLabel("D - Speed Game Up");
        speedGameUP.setForeground(fontColors[6]);
        speedGameUP.setFont(MainWindow.customFont);
        speedGameUP.setBounds(0,100,this.getWidth() - (leftX + 10),50);
        speedGameUP.setHorizontalAlignment(JLabel.CENTER);

        JLabel PauseGameToggle = new JLabel("ESC - Pause Game");
        PauseGameToggle.setForeground(fontColors[1]);
        PauseGameToggle.setFont(MainWindow.customFont);
        PauseGameToggle.setBounds(0,125,this.getWidth() - (leftX + 10),50);
        PauseGameToggle.setHorizontalAlignment(JLabel.CENTER);
        controlLabelContainer.add(speedGameUP);
        controlLabelContainer.add(slowGameDown);
        controlLabelContainer.add(controlLabel);
        controlLabelContainer.add(AIToggleControlLabel);
        controlLabelContainer.add(GameSpeedToggleLabel);
        controlLabelContainer.add(PauseGameToggle);

        JButton quitGame = new JButton();
        quitGame.setText("Quit");
        quitGame.setBounds(leftX, this.getHeight() - 150, this.getWidth() - (leftX + 10), 50);
        quitGame.setBackground(new Color(7,9,15));
        quitGame.setBorder(BorderFactory.createMatteBorder(2,2,2,2, fontColors[6]));
        quitGame.setFont(MainWindow.customFont);
        quitGame.setForeground(fontColors[1]);
        quitGame.addActionListener(e -> {
            System.exit(0);
        });
        Frame mainWindow = Frame.getFrames()[0];
        mainWindow.add(controlLabelContainer);
        mainWindow.add(quitGame);

        MainWindow.dropInstant = false;
        MainWindow.gamePaused = false;
    }

    // Visuals ---------------------------------------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintShape(g);
    }
    private void paintShape(Graphics g){
        for(int row = 0;  row < this.block.getBlockHeight(); row++){
            for(int col = 0; col < this.block.getBlockWidth(); col++)
                try {
                    if (this.block.getBlock()[row][col] > 0) {
                        int x = (col * this.BlockSize) + (this.block.getX() * this.BlockSize);
                        int y = (row * this.BlockSize) + (this.block.getY() * this.BlockSize);
                        paintBlock(g, this.block.getBlockTexture(), x, y);
                    }
                }catch (Exception e){
                    System.out.print("");
                }
        }
    }
    private void paintBlock(Graphics g, Image texture, int x, int y){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.drawImage(texture, x,y,BlockSize,this.BlockSize, this);
    }
}
