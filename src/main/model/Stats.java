package model;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

//Stats is a class that contains the time of game, with wpm and cpm obtained in game
public class Stats {
    private String dateOfGame;
    private int wpm;
    private int cpm;
    private int accuracy;
    private double time;
    private String difficulty;

    //constructs stats with given wpm and cpm
    public Stats(int wpm, int cpm, int accuracy, double time, String difficulty) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.dateOfGame = sdf.format(date);
        this.wpm = wpm;
        this.cpm = cpm;
        this.accuracy = accuracy;
        this.time = time;
        this.difficulty = difficulty;
    }

    //constructs stats with given wpm and cpm
    public Stats(String date, int wpm, int cpm, int accuracy, double time, String difficulty) {
        this.dateOfGame = date;
        this.wpm = wpm;
        this.cpm = cpm;
        this.accuracy = accuracy;
        this.time = time;
        this.difficulty = difficulty;
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

    public int getAccuracy() {
        return accuracy;
    }

    public double getTime() {
        return time;
    }

    public String getDifficulty() {
        return difficulty;
    }


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Date", dateOfGame);
        json.put("wpm", wpm);
        json.put("cpm", cpm);
        json.put("Accuracy", accuracy);
        json.put("Time", time);
        json.put("Difficulty", difficulty);
        return json;
    }
}
