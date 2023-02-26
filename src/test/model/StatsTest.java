package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsTest {
    private Stats stat1;
    private Stats stat2;
    private String dateOfGame;

    @BeforeEach
    void init() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        dateOfGame = sdf.format(date);
        stat1 = new Stats(1, 2);
        stat2 = new Stats(3, 4);

    }

    @Test
    void statTest() {
        assertEquals(1, stat1.getWPM());
        assertEquals(2, stat1.getCPM());
        assertEquals(3, stat2.getWPM());
        assertEquals(4, stat2.getCPM());
        assertEquals(dateOfGame, stat1.getDateOfGame());
        assertEquals(dateOfGame, stat2.getDateOfGame());

    }

}
