package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//History is class with a list containing all the previous game stats
public class History {
    private ArrayList<Stats> history;

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
}
