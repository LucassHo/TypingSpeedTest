package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    @BeforeEach
    void init() {
        game = new Game(1);
    }

    @Test
    void initTest() {
        assertTrue(game.getRandomWords().length() > 0);
        assertEquals(1, game.getTime());
        Game game2 = new Game(5);
        assertEquals(5, game2.getTime());
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
        game.removeInput();
        assertEquals(0, game.getCurrentPosition());
        assertEquals("", game.getUserInput());
        game.removeInput();
        assertEquals(0, game.getCurrentPosition());
        assertEquals("", game.getUserInput());
        game.addToInput(b);
        assertEquals(1, game.getCurrentPosition());
        assertEquals("e", game.getUserInput());
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

    @Test
    void calcCharsTypedTest() {
        String correctOne = game.getRandomWords();
        char a = correctOne.charAt(0);
        char b = correctOne.charAt(1);
        char c = correctOne.charAt(2);
        char d = correctOne.charAt(3);
        game.addToInput(a);
        game.addToInput(a);
        assertEquals(1, game.calcCharsTyped());
        game.removeInput();
        game.addToInput(b);
        game.addToInput(c);
        game.addToInput(d);
        assertEquals(4, game.calcCharsTyped());


    }

    @Test
    void calcWordsTypedTest() {
        String correctOne = game.getRandomWords();
        List<String> words = Arrays.asList(correctOne.split(" "));
        String one = words.get(0);
        String two = words.get(1);
        String three = words.get(2);
        String wrong = "notAWordSinceThisIsTooLong";
        String spaceString = " ";
        char space = spaceString.charAt(0);

        addWord(one);
        assertEquals(0, game.calcWordsTyped());
        game.addToInput(space);
        assertEquals(1, game.calcWordsTyped());
        addWord(two);
        assertEquals(1, game.calcWordsTyped());
        game.addToInput(space);
        assertEquals(2, game.calcWordsTyped());
        addWord(wrong);
        game.addToInput(space);
        assertEquals(2, game.calcWordsTyped());
        addWord(three);
        game.addToInput(space);
        assertEquals(2, game.calcWordsTyped());

    }
    @Test
    void calcAccuracyTest() {
        String correctOne = game.getRandomWords();

        assertEquals(0, game.calcAccuracy());
        game.addToInput(correctOne.charAt(0));
        assertEquals(100, game.calcAccuracy());
        game.addToInput(correctOne.charAt(0));
        assertEquals(50, game.calcAccuracy());
        game.removeInput();
        game.removeInput();
        game.addToInput(correctOne.charAt(1));
        assertEquals(0, game.calcAccuracy());
    }

    private void addWord(String str) {
        for (int i = 0; i < str.length(); i++) {
            game.addToInput(str.charAt(i));
        }
    }
}
