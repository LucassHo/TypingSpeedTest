package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsTest {
    private Stats stat1;
    private Stats stat2;

    @BeforeEach
    void init() {
        stat1 = new Stats(1, 2);
        stat2 = new Stats(3, 4);

    }

    @Test
    void statTest() {
        assertEquals(1, stat1.getWPM());
        assertEquals(2, stat1.getCPM());
        assertEquals(3, stat2.getWPM());
        assertEquals(4, stat2.getCPM());

    }

}
