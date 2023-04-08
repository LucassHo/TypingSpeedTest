package persistence;

import model.History;
import model.Stats;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// JsonReader is a class that reads history.json file and creates a History list for current instance
public class JsonReader {
    private String source;

    //MODIFIES: this
    //EFFECTS: constructor for JsonReader with json location
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads through the json file and returns the history inside
    //throws IOException if an error occurs while reading the data from file
    public History read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseHistory(jsonObject);
    }

    //EFFECTS: reads the source file and returns it as JsonData string
    private String readFile(String source) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> stringBuilder.append(s));
        }
        return stringBuilder.toString();
    }

    //EFFECTS: turn JSONObject into History
    private History parseHistory(JSONObject jsonObject) {
        History history = new History();
        JSONArray jsonArray = jsonObject.getJSONArray("History");
        for (Object json : jsonArray) {
            JSONObject newStat = (JSONObject) json;
            Stats stats = new Stats(newStat.getString("Date"),
                    newStat.getInt("wpm"),
                    newStat.getInt("cpm"),
                    newStat.getInt("Accuracy"),
                    newStat.getDouble("Time"),
                    newStat.getString("Difficulty"));
            history.initStats(stats);
        }
        return history;
    }




}
