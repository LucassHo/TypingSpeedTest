package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    @BeforeEach
    void init() {
        game = new Game(1);
        System.out.println(game.getRandomWords());
    }

    @Test
    void initTest() {
        assertTrue(game.getRandomWords().length() > 0);
    }

    @Test
    void userInputTest() {
        String s = "Test";
        char a = s.charAt(0);
        char b = s.charAt(1);
        assertEquals(0, game.getCurrentPosition());
        game.addToInput(a);
        assertEquals(1, game.getCurrentPosition());
        assertEquals("T", game.getUserInput());
        game.addToInput(b);
        assertEquals(2, game.getCurrentPosition());
        assertEquals("Te", game.getUserInput());
        game.removeInput();
        assertEquals(1, game.getCurrentPosition());
        assertEquals("T", game.getUserInput());
        game.addToInput(a);
        System.out.println(game.getUserInput());
        game.addToInput(a);
        System.out.println(game.getUserInput());
        game.addToInput(a);
        System.out.println(game.getUserInput());
        game.addToInput(a);
        System.out.println(game.getUserInput());
        game.addToInput(a);
        System.out.println(game.getUserInput());
        game.removeInput();
        System.out.println(game.getUserInput());
        game.removeInput();
        System.out.println(game.getUserInput());
        game.removeInput();
        System.out.println(game.getUserInput());
        game.removeInput();
        System.out.println(game.getUserInput());
        game.removeInput();
        System.out.println(game.getUserInput());
    }

    @Test
    void getCorrectnessTest() {
        String correctOne = game.getRandomWords();
        char a = correctOne.charAt(0);
        char b = correctOne.charAt(1);
        char c = correctOne.charAt(2);
        char d = correctOne.charAt(3);
        game.addToInput(a);
        game.addToInput(a);
        List<Boolean> correctness = game.getCorrectness();
        assertTrue(correctness.get(0));
        assertFalse(correctness.get(1));
        game.removeInput();
        game.addToInput(b);
        game.addToInput(c);
        game.addToInput(d);
        List<Boolean> newCorrectness = game.getCorrectness();
        assertTrue(newCorrectness.get(0));
        assertTrue(newCorrectness.get(1));
        assertTrue(newCorrectness.get(2));
        assertTrue(newCorrectness.get(3));


    }
}
