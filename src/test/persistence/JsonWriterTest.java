package persistence;

import model.History;
import model.Stats;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            History history = new History();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyHistory() {
        try {
            History history = new History();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyHistory.json");
            writer.open();
            writer.write(history);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyHistory.json");
            History newHistory = reader.read();
            assertEquals(0, newHistory.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralHistory() {
        try {
            History history = new History();
            Stats stat1 = new Stats(1, 2, 100, 1, "Easy");
            Stats stat2 = new Stats(3, 4, 50, 0.1, "Hard");
            history.addStats(stat1);
            history.addStats(stat2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralHistory.json");
            writer.open();
            writer.write(history);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralHistory.json");
            History newHistory = reader.read();
            assertEquals(2, newHistory.size());
            assertEquals(stat1, history.getStats(0));
            assertEquals(stat2, history.getStats(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
