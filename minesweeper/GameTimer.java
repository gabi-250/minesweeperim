package minesweeper;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class GameTimer {

    private static int seconds;
    private static Timer timer;

    public static void start() {

        seconds = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run () {

                ++seconds;
            }
        }, 0, 1000);
    }

    public static void stop() {
        timer.cancel();
    }

    public static int getCurrentTime() {
        return seconds;
    }

    public static int getElapsedTime() {
        return seconds;
    }
}
