package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//History is class with a list containing all the previous game stats
public class History {
    private EventLog eventLog = EventLog.getInstance();
    private ArrayList<Stats> history;

    //constructor for History
    public History() {
        history = new ArrayList<>();
    }

    //EFFECTS: adds given stat to history and logs event
    public void addStats(Stats stat) {
        history.add(stat);
        Event event = new Event("New Game Stat with length " + stat.getTime() + " mins played at "
                + stat.getDateOfGame() + " added to History.");
        eventLog.logEvent(event);

    }

    //EFFECTS: removes stats at given index and logs event
    public void removeStats(int index) {
        Stats s = getStats(index);
        history.remove(index);
        Event event = new Event("Game Stat with length " + s.getTime() + " mins played at "
                + s.getDateOfGame() + " removed from History.");
        eventLog.logEvent(event);
    }

    //EFFECTS: returns stats at given index
    public Stats getStats(int index) {
        return history.get(index);
    }

    public int size() {
        return history.size();
    }


    //EFFECTS: constructs JSONObject of this and returns it
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("History", historyToJson());
        return json;
    }


    //EFFECTS: turns individual elements of history into JSONObject, added to JSONArray and returned
    private JSONArray historyToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Stats stat : history) {
            jsonArray.put(stat.toJson());
        }
        return jsonArray;
    }

    //EFFECTS: adds stats at initialization
    public void initStats(Stats stat) {
        history.add(stat);
    }
}
