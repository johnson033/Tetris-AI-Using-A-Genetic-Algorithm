package Threads;

import GUI.MainWindow.MainWindow;

public class TimerThread extends Thread{

    private long startTime;

    public TimerThread(){
        this.startTime = System.currentTimeMillis();
    }

    public void run(){
        while(true) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;
            long seconds = elapsedSeconds % 60;
            long minutes = elapsedSeconds / 60;

            MainWindow.timeElapsedLabel.setText("Elapsed Time -     " + minutes + ":" + seconds);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
