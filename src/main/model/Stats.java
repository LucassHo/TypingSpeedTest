package model;

import java.text.SimpleDateFormat;
import java.util.Date;

//Stats is a class that contains the time of game, with wpm and cpm obtained in game
public class Stats {
    private String dateOfGame;
    private int wpm;
    private int cpm;

    //constructs stats with given wpm and cpm
    public Stats(int wpm, int cpm) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.dateOfGame = sdf.format(date);
        this.wpm = wpm;
        this.cpm = cpm;
    }

    public String getDateOfGame() {
        return dateOfGame;
    }

    public int getWPM() {
        return wpm;
    }

    public int getCPM() {
        return cpm;
    }
}
