package persistence;

import model.History;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// JsonWriter is a writer that writes a History to file
public class JsonWriter {
    private PrintWriter writer;
    private String destination;

    //EFFECTS: constructs JsonWriter with given destination
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    //MODIFIES: this
    //EFFECTS: opens writer, throws FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    //MODIFIES: this
    //EFFECTS: writes History to file
    public void write(History history) {
        JSONObject json = history.toJson();
        saveToFile(json.toString());

    }

    //MODIFIES: this
    //EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    //MODIFIES: this
    //EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
