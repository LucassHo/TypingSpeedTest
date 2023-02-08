package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static final int TICKS_PER_SECOND = 15;
    private final int wordsLength = 216;
    private final String allInString = "be and of a in to have too it I that for you he with on do say this they at " +
            "but we his from that not can't won't by she or as what go their can who get if would her all my make" +
            " about know will as up one time there year so think when which them some me people take out into just " +
            "time there year see him your come could now than like other how then its our two more these want way " +
            "look first also new because day more use no man find here thing give many well";
    private final List<String> words = List.of(allInString.split(" "));
    private String randomWords = "";



    public Game() {
        generateRandomWords();

    }

    private void generateRandomWords() {
        int i;
        for (i = 0, i < wordsLength, i++) {
            Random rand = new Random();
            this.randomWords = this.randomWords + words.get(rand.nextInt(words.size()));
        }
    }

    public String getRandomWords() {
        return randomWords;
    }
}





