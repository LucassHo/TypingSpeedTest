package persistence;

import model.History;
import model.Stats;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            History history = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyHistory() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyHistory.json");
        try {
            History history = reader.read();
            assertEquals(0, history.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralHistory() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralHistory.json");
        try {
            History history = reader.read();
            assertEquals(3, history.size());
            Stats stat1 = history.getStats(0);
            assertEquals("10/03/2023 18:16:58", stat1.getDateOfGame());
            assertEquals(60, stat1.getWPM());
            assertEquals(350, stat1.getCPM());
            assertEquals(100, stat1.getAccuracy());
            assertEquals(0.2, stat1.getTime());
            assertEquals("Hard", stat1.getDifficulty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
