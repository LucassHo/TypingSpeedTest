package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Game is a class that generates the words for typing, saves the user input and computes the cpm and wpm of the user
public class Game {
    public static final int TICKS_PER_SECOND = 15;
    private final int wordsLength = 216;
    private final String allInEasyString = "be and of a in to have too it I that for you he with on do say this they at"
            + " but we his from that not can't won't by she or as what go their can who get if would her all my make "
            + "about know will as up one time there year so think when which them some me people take out into just "
            + "time there year see him your come could now than like other how then its our two more these want way "
            + "look first also new because day more use no man find here thing give many well only those tell one very "
            + "her even back any good woman through us life child there work down may after should call world over "
            + "school still try in as last ask need too feel three when state never become between high really most "
            + "another much family own out leave put old while mean on keep student why let great same big group begin "
            + "seem help talk where turn problem every start hand might show part about against place over such again "
            + "few case most week where each right program hear so question during work play run small off always move "
            + "like live night point hold today bring next happen without before large all must home under water room "
            + "write mother area national money story young fact month lot right study book eye job word issue side "
            + "kind four head far black long both little house yes after since long provide around father sit away "
            + "until power hour game often yet line end among ever stand bad lose pay law meet member car city almost";
    private final List<String> easyWords = Arrays.asList(allInEasyString.split(" "));
    private String randomWords = "";
    private String userInput = "";

    private double time;
    private List<String> gtList;
    private final String allInHardString = "Oxford English diamond puberty strength circumference isolation secure "
            + "cancel endure endurance Brooklyn listen dependence salmon meeting peasant marriage rugby carpet student "
            + "galaxy legislation pneumonia necklace constitution frequency treatment achievement traffic conduct "
            + "posture mastermind paragraph union opinion pound fantasy quantity celebration observation sensation "
            + "colorful suspect flavour survivor service language delete shadow wonder battlefield confession season "
            + "interrupt sailor trouble mention flesh morale ethics deviation explosion teacher agriculture "
            + "biological industrialization immunity diversity flamethrower amphibians baseball tumble Earth betray "
            + "classify minority majority cooling boiling measure summary conclusion please chestnut pistachio "
            + "sentence phrase rocket league mineral vitamin mitochondria illumination lunatic quicker backpack";
    private final List<String> hardWords = Arrays.asList(allInHardString.split(" "));


    //REQUIRES: minutes > 0
    //MODIFIES: this
    //EFFECTS: constructor for game, saves the length of game and randomizes words for game
    public Game(double minutes, String difficulty) {
        this.time = minutes;
        generateRandomWords((int)minutes + 1, difficulty);
        this.gtList = stringToArray(getRandomWords());

    }

    //REQUIRES: minutes > 0
    //MODIFIES: this
    //EFFECTS: randomizes words for game
    private void generateRandomWords(int minutes, String difficulty) {
        Random randStart = new Random();
        if (difficulty.equals("Easy")) {
            this.randomWords = easyWords.get(randStart.nextInt(easyWords.size()));
            for (int i = 1; i < wordsLength * minutes; i++) {
                Random rand = new Random();
                this.randomWords = this.randomWords + " " + easyWords.get(rand.nextInt(easyWords.size()));
            }
        } else {
            this.randomWords = hardWords.get(randStart.nextInt(hardWords.size()));
            for (int i = 1; i < wordsLength * minutes; i++) {
                Random rand = new Random();
                this.randomWords = this.randomWords + " " + hardWords.get(rand.nextInt(hardWords.size()));
            }
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


    public double getTime() {
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

        for (int i = 0; i < userInput.length(); i++) {
            if (utList.get(i).equals(gtList.get(i))) {
                charsTyped++;
            }
        }
        return charsTyped;
    }

    //EFFECTS: returns the accuracy of user
    public int calcAccuracy() {
        List<Boolean> correctness = getCorrectness();
        int correct = 0;
        for (int i = 0; i < correctness.size(); i++) {
            if (correctness.get(i)) {
                correct++;
            }
        }
        return (int) ((double) correct * 100 / correctness.size());
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





