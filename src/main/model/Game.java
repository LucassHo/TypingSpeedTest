package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Game is a class that generates the words for typing, saves the user input and computes the cpm and wpm of the user
public class Game {
    public static final int TICKS_PER_SECOND = 15;
    private final int wordsLength = 216;
    private final String allInString = "be and of a in to have too it I that for you he with on do say this they at "
            + "but we his from that not can't won't by she or as what go their can who get if would her all my make"
            + " about know will as up one time there year so think when which them some me people take out into just "
            + "time there year see him your come could now than like other how then its our two more these want way "
            + "look first also new because day more use no man find here thing give many well";
    private final List<String> words = List.of(allInString.split(" "));
    private String randomWords = "";
    private String userInput = "";
    private int time;
    private List<String> gtList;


    //REQUIRES: minutes > 0
    //MODIFIES: this
    //EFFECTS: constructor for game, saves the length of game and randomizes words for game
    public Game(int minutes) {
        this.time = minutes;
        generateRandomWords(minutes);
        this.gtList = stringToArray(getRandomWords());

    }

    //REQUIRES: minutes > 0
    //MODIFIES: this
    //EFFECTS: randomizes words for game
    private void generateRandomWords(int minutes) {
        Random randStart = new Random();
        this.randomWords = words.get(randStart.nextInt(words.size()));
        for (int i = 1; i < wordsLength * minutes; i++) {
            Random rand = new Random();
            this.randomWords = this.randomWords + " " + words.get(rand.nextInt(words.size()));
        }
    }


    public String getRandomWords() {
        return randomWords;
    }

    public String getUserInput() {
        return userInput;
    }


    public int getCurrentPosition() {
        return userInput.length();
    }


    public int getTime() {
        return time;
    }

    //MODIFIES: this
    //EFFECTS: adds input to current user input
    public void addToInput(char input) {
        this.userInput = this.userInput + input;
    }

    //MODIFIES: this
    //EFFECTS: removes the last input from current user input
    public void removeInput() {
        if (this.userInput.length() <= 1) {
            this.userInput = "";
        } else {
            this.userInput = this.userInput.substring(0, userInput.length() - 1);
        }
    }

    //EFFECTS: for every character in user input, produces true if equal to its corresponding generated character
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

    //EFFECTS: returns the number of words typed so far
    public int calcWordsTyped() {
        int correct = 1;
        int wordsTyped = 0;
        List<String> utList = stringToArray(getUserInput());

        for (int i = 0; i < userInput.length(); i++) {
            if (utList.get(i).equals(gtList.get(i))) {
                if (utList.get(i).equals(" ")) {
                    wordsTyped += correct;
                    correct = 1;
                }
            } else {
                correct = 0;
            }
        }
        return wordsTyped;
    }

    //EFFECTS: returns the number of characters typed so far
    public int calcCharsTyped() {
        int charsTyped = 0;
        List<String> utList = stringToArray(getUserInput());
        System.out.println(utList);

        for (int i = 0; i < userInput.length(); i++) {
            if (utList.get(i).equals(gtList.get(i))) {
                charsTyped++;
            }
        }
        return charsTyped;
    }

    //EFFECTS: turns the given string into an array of individual characters as string
    public List<String> stringToArray(String str) {
        List<String> output = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            output.add("" + str.charAt(i));
        }
        return output;
    }
}





