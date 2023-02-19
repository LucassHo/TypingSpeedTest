package model;

import java.util.Timer;

public class Time {
    private int startTime;
    private Timer timer = new Timer();

    public Time(int startTime) {
        this.startTime = startTime;
        createTimer(this.startTime);

    }

    private void createTimer(int startTime) {

    }
}
