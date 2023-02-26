package model;

import java.util.ArrayList;

//History is class with a list containing all the previous game stats
public class History {
    ArrayList<Stats> history;

    //constructor for History
    public History() {
        history = new ArrayList<>();
    }

    //EFFECTS: adds given stat to history
    public void addStats(Stats stat) {
        history.add(stat);
    }

    //EFFECTS: removes stats at given index
    public void removeStats(int index) {
        history.remove(index);
    }

    //EFFECTS: returns stats at given index
    public Stats getStats(int index) {
        return history.get(index);
    }

    public int size() {
        return history.size();
    }
}
