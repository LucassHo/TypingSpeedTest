package model;

public class TypingCalculator {
    private String userTyping;
    private String generatedTyping;

    public TypingCalculator() {
        userTyping = "";
        generatedTyping = "";
    }

    public String getUserTyping() {
        return userTyping;
    }

    public String getGeneratedTyping() {
        return generatedTyping;
    }

    public int calcWordsTyped(String userTyping, String generatedTyping) {
        int correct = 1;
        int wordsTyped = 0;
        this.userTyping = userTyping;
        this.generatedTyping = generatedTyping;
        String[] gtList = generatedTyping.split("(?!^)");
        String[] utList = userTyping.split("(?!^)");

        for (int i = 0; i < userTyping.length(); i++) {
            if (utList[i] == gtList[i]) {
                if (utList[i] == " ") {
                    wordsTyped += correct;
                    correct = 1;
                }
            } else {
                correct = 0;
            }
        }
        return wordsTyped;
    }

    public int calcCharsTyped(String userTyping, String generatedTyping) {
        int charsTyped = 0;
        String[] gtList = generatedTyping.split("(?!^)");
        String[] utList = userTyping.split("(?!^)");

        for (int i = 0; i < userTyping.length(); i++) {
            if (utList[i] == gtList[i]) {
                charsTyped++;
            }
        }
        return charsTyped;
    }
}

