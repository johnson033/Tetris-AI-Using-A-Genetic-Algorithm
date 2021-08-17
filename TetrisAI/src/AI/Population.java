package AI;

import GUI.MainWindow.GamePanel;
import GUI.MainWindow.MainWindow;
import Game.Game;
import Threads.GameThread;
import java.awt.*;
import java.util.ArrayList;

public class Population {

    private Frame mainWindow;
    public static ArrayList<GamePanel> population = new ArrayList<>();
    private final ArrayList<GamePanel> availableToView = new ArrayList<>();
    private final ArrayList<Game> matingPool = new ArrayList<>();
    private final int ScreenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final int ScreenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final int populationSize = MainWindow.PopulationSize;
    public static int mainX, mainY, mainWidth, mainHeight;
    private int numberOfMutations, numberOfDeadGames, previousBestGame, previousAvgLines, generation = 0;


    /* Creates the population of games for each generation */

    public void createPopulation(ArrayList<GamePanel> population){
        this.generation++;
        this.numberOfDeadGames = 0;
        setLabels();
        this.previousAvgLines = 0;
        Population.population.clear();
        if(population == null){
            for(int i = 0; i < populationSize; i++){
                double[] GameDNA = new double[]{(Math.random() * 10) - 5,(Math.random() * 10) - 5,(Math.random() * 10) - 5,(Math.random() * 10) - 5};
                Game game = new Game(i, GameDNA);
                Population.population.add(new GamePanel(0,0,0,0,game, i));
            }
        }else{
            Population.population.addAll(population);
        }
    }

    /* Used to draw the population to the screen to be seen. 33 of the PopulationSize will be shown and swapped out  */

    public void drawPopulation(){
        mainWindow = Frame.getFrames()[0];

        int mainGameX = (int) (this.ScreenWidth *.64);
        int mainGameY = (int) (this.ScreenHeight *.043);
        int mainGameWidth = (int) (this.ScreenWidth * .25);
        int mainGameHeight = (int) (mainGameWidth * .1) * 20;
        mainX = mainGameX;
        mainY = mainGameY;
        mainWidth = mainGameWidth;
        mainHeight = mainGameHeight;

        Population.population.get(0).setOnScreen(true);
        Population.population.get(0).setMainPanel(true);
        Population.population.get(0).setUpdatedBounds(mainGameX, mainGameY, mainGameWidth, mainGameHeight);

        int gridWidth = (int) (this.ScreenWidth *.7);
        int gridPaneHeight = (int) ((gridWidth *.083) / 10) * 20;
        int gridPaneWidth = (gridPaneHeight / 20) * 10;

        int index = 1;
        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 8; col++){
                int spacing = (int) (this.ScreenWidth * .021);
                int ySpacing = (mainGameHeight - (gridPaneHeight * 4)) / 3;
                int x = spacing + (spacing * col) + (col * gridPaneWidth);
                int y = mainGameY + (ySpacing * row) + (row * gridPaneHeight);

                Population.population.get(index).setUpdatedBounds(x, y, gridPaneWidth, gridPaneHeight);
                Population.population.get(index).setOnScreen(true);
                index++;
            }
        }

        for(int i = 33; i < populationSize; i++){
            Population.population.get(i).setUpdatedBounds(0,0,gridPaneWidth,gridPaneHeight);
            Population.population.get(i).setOnScreen(false);
            this.availableToView.add(Population.population.get(i));
        }

        for(int i = 0; i < populationSize; i++){
            if(Population.population.get(i).checkIfOnScreen()){
                mainWindow.add(Population.population.get(i));
            }
            new GameThread(Population.population.get(i).getGame(), Population.population.get(i)).start();
        }
        mainWindow.repaint();
    }

    /* Checks to see if each game in the population is dead. Called each time a game dies from the game Thread */

    public void isPopulationDead(GamePanel deadPanel){
        MainWindow.numberOfDeadGames.setText("Dead Games: " + ++numberOfDeadGames);
        this.availableToView.remove(deadPanel);
        if(deadPanel.checkIfOnScreen())replaceGameOnScreen(deadPanel);
        for(GamePanel game: Population.population){
             if(!game.getGame().getGameOver())return;
        }
        calcFitness();
        for(int i = 0; i <  populationSize; i++){
            this.matingPool.add(acceptReject());
        }
        Population.population.clear();
        this.numberOfMutations = 0;
        createPopulation(selection());
        this.matingPool.clear();
        this.availableToView.clear();
        drawPopulation();
    }

    /* If a game is on the screen, This is called to swap out that game with one that is still going if any are available */

    private void replaceGameOnScreen(GamePanel game){
        GamePanel replacementPanel;
        if(availableToView.size() > 0) {
            replacementPanel = this.availableToView.remove(MainWindow.random.nextInt(availableToView.size()));
        }else return;

        if(replacementPanel == null) return;
        game.setVisible(false);
        replacementPanel.setUpdatedBounds(game.getWindowX(), game.getWindowY(), game.getWindowWidth(), game.getWindowHeight());
        replacementPanel.setOnScreen(true);
        if(game.checkIfIsMainPanel()){
            game.setMainPanel(false);
            replacementPanel.setMainPanel(true);
        }
        mainWindow.add(replacementPanel);
    }

    /* Calculates the fitness of each game. This is the lines cleared of a game / most lines cleared in the population*/

    private void calcFitness(){
        int maxFitness = Integer.MIN_VALUE;

            for (GamePanel game : Population.population) {
                try {
                    game.getGame().setGameFitness(game.getGame().getLinesCleared());
                    this.previousAvgLines += game.getGame().getLinesCleared();
                    if (game.getGame().getGameFitness() > maxFitness)
                        maxFitness = (int) game.getGame().getGameFitness();
                    game.setVisible(false);
                }catch(Exception e){System.out.println("error");}
            }

        this.previousBestGame = maxFitness;

        for(GamePanel game : Population.population){
            game.getGame().setGameFitness(game.getGame().getGameFitness() / maxFitness);
        }
    }

    /* this is a function to create the mating pool. Allows for the best of a population having a better chance of moving on */

    private Game acceptReject(){
        while(true){
            Game partner = Population.population.get(MainWindow.random.nextInt(populationSize)).getGame();
            double check = Math.random();
            if(check < partner.getGameFitness())
                return partner;
        }
    }

    /* Selects and creates a new Generation */

    private ArrayList<GamePanel> selection() {
        ArrayList<GamePanel> newPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Game ParentA = this.matingPool.get(MainWindow.random.nextInt(populationSize - 1) + 1);
            Game ParentB = this.matingPool.get(MainWindow.random.nextInt(populationSize - 1) + 1);
            newPopulation.add(new GamePanel(0, 0, 0, 0, crossOver(ParentA, ParentB, i), i));
        }
        return newPopulation;
    }

    /* Will take two members and cross over the genes randomly */

    private Game crossOver(Game parentA, Game parentB, int index){
        double[] newGenes = new double[4];
        int mid = MainWindow.random.nextInt(4);
        for(int i = 0; i < newGenes.length; i++){
            if(i < mid){
                newGenes[i] = parentA.getDNA()[i];
            }else newGenes[i] = parentB.getDNA()[i];
        }
        return mutate(new Game(index, newGenes));
    }

    /* Gives a random chance to each game being mutated. Will only change One Gene by +- 1.5%  */

    private Game mutate(Game Game){
            if(Math.random() < .05){
                this.numberOfMutations++;
                Game.setGameMutated(true);
                this.numberOfMutations++;
                double[] mutatedDNA = Game.getDNA();
                double indexToChange = (Math.random() * 100);
                    if (indexToChange <= 25) {
                        mutatedDNA[0] = mutatedDNA[0] * (Math.random() * (3) - 1.5);
                    } else if (indexToChange > 25 && indexToChange <= 50) {
                        mutatedDNA[1] = mutatedDNA[0] * (Math.random() * (3) - 1.5);
                    } else if (indexToChange > 50 && indexToChange <= 75) {
                        mutatedDNA[2] = mutatedDNA[0] * (Math.random() * (3) - 1.5);
                    } else {
                        mutatedDNA[3] = mutatedDNA[0] * (Math.random() * (3) - 1.5);
                    }
                Game.setDNA(mutatedDNA);
            }
            return Game;
    }

    /* Sets the labels for the current generation */

    private void setLabels(){
        MainWindow.generationLabel.setText("Generation: " + this.generation);
        MainWindow.numberOfDeadGames.setText("Dead Games: " + this.numberOfDeadGames);
        MainWindow.numberOfMutations.setText("Mutated Games: " + this.numberOfMutations);
        MainWindow.bestGameLastGenerationLabel.setText("Previous Best: " + this.previousBestGame);
        MainWindow.previousAVGLinesClearedLabel.setText("Previous AVG: " + (this.previousAvgLines / this.populationSize));
    }

}
