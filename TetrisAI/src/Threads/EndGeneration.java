package Threads;

import AI.Population;
import GUI.MainWindow.GamePanel;

public class EndGeneration extends Thread{
    public void run(){
        for(GamePanel game: Population.population){
            game.setAIState();
            try {
                sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.stop();
    }
}
