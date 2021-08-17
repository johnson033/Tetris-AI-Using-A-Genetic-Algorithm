package Threads;

import GUI.MainMenu.MainMenu;
import GUI.MainWindow.MainWindow;
import Game.Block;

public class MainMenuThread extends Thread{
    private final MainMenu mainMenu;
    public MainMenuThread(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }
    @Override
    public void run(){
        while(mainMenu.isVisible()){
            mainMenu.setBlock(new Block(MainWindow.random.nextInt(7)));
            mainMenu.getBlock().setX(MainWindow.random.nextInt(38));
            while(mainMenu.getBlock().getY() < 40){
                mainMenu.getBlock().moveDown();
                try {
                    sleep();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainMenu.repaint();
            }
        }
    }

    private void sleep() throws InterruptedException {
        sleep(30);
    }
}
