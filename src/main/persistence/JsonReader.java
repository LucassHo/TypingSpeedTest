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

public class JsonReader {
    private String source;

    public JsonReader(String source) {
        this.source = source;
    }

    public History read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseHistory(jsonObject);
    }

    private String readFile(String source) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> stringBuilder.append(s));
        }
        return stringBuilder.toString();
    }

    private History parseHistory(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        History history = new History();
        createHistory(history, jsonObject);
        return history;
    }

    private void createHistory(History history, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("History");
        for(Object json : jsonArray) {
            JSONObject newStat = (JSONObject) json;
            Stats stats = new Stats(newStat.getString("Date"),
                    newStat.getInt("wpm"),
                    newStat.getInt("cpm"));
            history.addStats(stats);
        }
    }



}
