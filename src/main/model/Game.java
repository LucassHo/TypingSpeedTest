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
    private String userInput = "";



    public Game(int minutes) {
        generateRandomWords(minutes);


    }

    private void generateRandomWords(int minutes) {
        for (int i = 0; i < wordsLength * minutes; i++) {
            Random rand = new Random();
            this.randomWords = this.randomWords + words.get(rand.nextInt(words.size()));
        }
    }

    public String getRandomWords() {
        return randomWords;
    }

    public void addToInput(char input) {
        this.userInput = this.userInput + input;
    }

    public void removeInput() {
        if (this.userInput.length() <= 1) {
            this.userInput = "";
        }
        this.userInput = this.userInput.substring(0, userInput.length() - 1);
    }

    public String getUserInput() {
        return userInput;
    }

    public int getCurrentPosition() {
        return userInput.length();
    }
    public List<Boolean> getCorrectness() {
        List<Boolean> correctness = new ArrayList<>();
        for (int i = 0; i < userInput.length(); i++) {
            if (userInput.charAt(i) == randomWords.charAt(i)) {
                correctness.add(true);
            } else {
                correctness.add(false);
            }
        }
        return correctness;
    }

    public List<Boolean> getCorrectness(int startingPoint) {
        List<Boolean> correctness = new ArrayList<>();
        for (int i = startingPoint; i < userInput.length(); i++) {
            if (userInput.charAt(i) == randomWords.charAt(i)) {
                correctness.add(true);
            } else {
                correctness.add(false);
            }
        }
        return correctness;
    }
}





