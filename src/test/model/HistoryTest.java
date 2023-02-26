package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryTest {
    private History history;
    private Stats stat1;
    private Stats stat2;
    private Stats stat3;

    @BeforeEach
    void init() {
        this.history = new History();
        stat1 = new Stats(1, 1);
        stat2 = new Stats(2, 2);
        stat3 = new Stats(3, 3);

    }

    @Test
    void initTest() {
        assertEquals(0, history.size());

    }

    @Test
    void addStatsTest() {
        history.addStats(stat1);
        assertEquals(1, history.size());
        assertEquals(stat1, history.getStats(0));
        history.addStats(stat2);
        assertEquals(2, history.size());
        assertEquals(stat2, history.getStats(1));
        history.addStats(stat2);
        assertEquals(3, history.size());
        assertEquals(stat2, history.getStats(2));

    }

    @Test
    void removeStatsTest() {
        history.addStats(stat1);
        assertEquals(1, history.size());
        assertEquals(stat1, history.getStats(0));
        history.removeStats(0);
        assertEquals(0, history.size());
        history.addStats(stat1);
        history.addStats(stat2);
        history.addStats(stat3);
        history.removeStats(1);
        assertEquals(2, history.size());
        assertEquals(stat1, history.getStats(0));
        assertEquals(stat3, history.getStats(1));

    }
}
