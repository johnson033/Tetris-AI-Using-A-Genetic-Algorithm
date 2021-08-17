package GUI.MainWindow;

import AI.Population;
import GUI.MainMenu.MainMenu;
import Threads.EndGeneration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainWindow extends JFrame {

   // Labels -----------------------------------------------------------------------------------------------------------

    public static JLabel previousAVGLinesClearedLabel= new JLabel();
    public static JLabel bestGameLastGenerationLabel= new JLabel();
    public static JLabel linesClearedLabel = new JLabel();
    public static JLabel numberOfDeadGames= new JLabel();
    public static JLabel numberOfMutations= new JLabel();
    public static JLabel generationLabel= new JLabel();
    public static JLabel timeElapsedLabel = new JLabel();
    public static JLabel HValueA= new JLabel();
    public static JLabel HValueB= new JLabel();
    public static JLabel HValueC= new JLabel();
    public static JLabel HValueD= new JLabel();
    public static Random random = new Random();

    // static Objects --------------------------------------------------------------------------------------------------

    public static Population population;
    public static Font customFont;

    // static Booleans -------------------------------------------------------------------------------------------------

    public static boolean dropInstant = false;
    public static boolean gamePaused = false;

    // private variables -----------------------------------------------------------------------------------------------

    private final int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private long lastSkip = 0;
    public static int GameSpeed = 10;
    public static int PopulationSize = 0;

    // Constructor -----------------------------------------------------------------------------------------------------

    public MainWindow() {

        initControls();
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/GUI/Assets/averey-regular.ttf")).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);

        this.setUndecorated(true);
        this.setLayout(null);
        this.setVisible(true);
        this.setResizable(true);
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setSize(width, height);
        this.getContentPane().setBackground(new Color(7, 9, 15));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new MainMenu(this.width, getHeight()));

        int leftX = (int) ((this.width *.64) + (int) (this.width * .25) + 10);
        setLabel(numberOfDeadGames, leftX, 50);
        setLabel(numberOfMutations, leftX, 75);
        setLabel(linesClearedLabel, leftX, 100);
        setLabel(generationLabel, leftX, 125);
        setLabel(HValueA, leftX, 150);
        setLabel(HValueB, leftX, 175);
        setLabel(HValueC, leftX, 200);
        setLabel(HValueD, leftX, 225);
        setLabel(bestGameLastGenerationLabel, leftX, 150);
        setLabel(previousAVGLinesClearedLabel, leftX, 175);
        setLabel(timeElapsedLabel, leftX, 300);

    }

    /* Used to set the position and properties of each label */

    // setup stuff -----------------------------------------------------------------------------------------------------

    private void setLabel(JLabel label, int x, int y){
        label.setFont(customFont);
        label.setBounds(x, y, this.width - (x + 10), 50);
        label.setForeground(getFontColor());
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label);
    }

    /* Initializes the controls */

    public void initControls() {
        InputMap im = this.getRootPane().getInputMap();
        ActionMap am = this.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke('w'), "endGenerationToggle");
        im.put(KeyStroke.getKeyStroke('s'), "dropInstantToggle");
        im.put(KeyStroke.getKeyStroke('a'), "speedGameDown");
        im.put(KeyStroke.getKeyStroke('d'), "speedGameUp");
        im.put(KeyStroke.getKeyStroke((char) 27), "esc");

        am.put("endGenerationToggle", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(System.currentTimeMillis() - lastSkip > 10000){
                    lastSkip = System.currentTimeMillis();
                    new EndGeneration().start();
                }
            }
        });

        am.put("dropInstantToggle", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropInstant = !dropInstant;
            }
        });

        am.put("esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePaused = !gamePaused;
            }
        });
        am.put("speedGameUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(GameSpeed > 11){
                    GameSpeed -= 10;
                }
            }
        });
        am.put("speedGameDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(GameSpeed < 500){
                    GameSpeed += 10;
                }
            }
        });
    }

    /* Function that will return a random color, Visual use only */

    private Color getFontColor(){
        return fontColors[MainWindow.random.nextInt(fontColors.length)];
    }

    /* List of all the colors, Blocks use the same ones */

    Color[] fontColors = new Color[]{
            new Color(255,148,0,255),
            new Color(75,190,255,255),
            new Color(255,51,41,255),
            new Color(255,204,0,255),
            new Color(76,217,99,255),
            new Color(152,41,188,255),
            new Color(230,59,122,255),
    };
}
