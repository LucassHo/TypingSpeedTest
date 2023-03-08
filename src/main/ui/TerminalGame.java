package ui;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Game;
import model.History;
import model.Stats;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static com.googlecode.lanterna.Symbols.BLOCK_DENSE;

public class TerminalGame {
    private Game game;
    private Screen screen;
    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private final TextColor black = TextColor.ANSI.BLACK;
    private final TextColor red = TextColor.ANSI.RED;
    private final TextColor green = TextColor.ANSI.GREEN;
    private final TextColor white = TextColor.ANSI.WHITE;
    private double time = 0;
    private WindowBasedTextGUI startGui;
    private Panel panel;
    private BasicWindow window;
    private History history;
    private Boolean continueGame;
    private TerminalSize charSize = new TerminalSize(1, 1);
    private static final String HISTORY_STORE = "./data/history.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    //EFFECTS: gets user choice of game time or check history
    public void start() throws Exception {

        width = screen.getTerminalSize().getColumns() - 4;
        height = screen.getTerminalSize().getRows() - 4;
        centerX = width / 2;
        centerY = height / 2;
        jsonWriter = new JsonWriter(HISTORY_STORE);
        jsonReader = new JsonReader(HISTORY_STORE);
        history = jsonReader.read();
        getUserInput();
        createInstance(time);
    }


    //MODIFIES: this
    //EFFECTS: starts the screen, separated for recursion
    public void startScreen() throws IOException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
    }


    //EFFECTS: start screen, takes user's input
    private void getUserInput() {

        panel = new Panel();
        panel.setFillColorOverride(white);
        panel.setLayoutManager(new GridLayout(2));
        panel.setPreferredSize(new TerminalSize(width - 4, height - 4));


        Label label1 = new Label("Welcome to Typing Speed Test!");
        panel.addComponent(label1);
        Label emptyLabel = new Label(" ");
        panel.addComponent(emptyLabel);
        Label label2 = new Label("Enter your play time (in min):");
        panel.addComponent(label2);

        addButtonForInput();

        window = new BasicWindow();
        window.setComponent(panel);
        startGui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(black));
        startGui.addWindowAndWait(window);
    }


    //EFFECTS: adds button to start screen for operation
    private void addButtonForInput() {
        TextBox input = new TextBox();
        panel.addComponent(input);

        Button enterButton = new Button("Enter", new Runnable() {
            @Override
            public void run() {
                if (input.getText().equals("")) {
                    return;
                } else {
                    time = Double.parseDouble(input.getText());
                    startGui.removeWindow(window);
                }
            }
        });
        panel.addComponent(enterButton);

        Button historyButton = new Button("History", new Runnable() {
            @Override
            public void run() {
                panel.removeAllComponents();
                historyScreen();
            }
        });
        panel.addComponent(historyButton);
    }


    //MODIFIES: History
    //EFFECTS: opens history menu
    private void historyScreen() {
        addHistoryTable();
        Button exit = new Button("Exit", new Runnable() {
            @Override
            public void run() {
                startGui.removeWindow(window);
                getUserInput();
            }
        });
        panel.addComponent(exit);
    }


    //EFFECTS: adds the table of history
    private void addHistoryTable() {
        Table<String> table = new Table<>("Date", "WPM", "CPM");
        for (int i = 0; i < history.size(); i++) {
            String date = history.getStats(i).getDateOfGame();
            String wpm = "" + history.getStats(i).getWPM();
            String cpm = "" + history.getStats(i).getCPM();
            table.getTableModel().addRow(date, wpm, cpm);
        }
        table.setSelectAction(new Runnable() {
            @Override
            public void run() {
                int selected = table.getSelectedRow();
                history.removeStats(selected);
                panel.removeAllComponents();
                historyScreen();
            }
        });
        panel.addComponent(table);
    }


    //EFFECTS: starts the game with given time
    private void createInstance(double minutes) throws InterruptedException, IOException {

        game = new Game(minutes);
        long startTime = System.currentTimeMillis() / 1000;
        long elapsedTime = System.currentTimeMillis() / 1000;
        while (elapsedTime - startTime < minutes * 60) {
            long timeLeft = (long)(minutes * 60 - (elapsedTime - startTime));
            tick(timeLeft);
            Thread.sleep(1000L / Game.TICKS_PER_SECOND);

            elapsedTime = System.currentTimeMillis() / 1000;
        }

        endScreen();
    }


    //EFFECTS: ticks the game
    private void tick(long timeLeft) throws IOException {
        handleUserInput();

        updateScreen(timeLeft);

    }


    //EFFECTS: generates end screen with WPM and CPM
    private void endScreen() {
        panel = new Panel();
        panel.setFillColorOverride(white);
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setPreferredSize(new TerminalSize(width - 4, height - 4));

        int wpm = (int) ((double) game.calcWordsTyped() / game.getTime());
        int cpm = (int) ((double) game.calcCharsTyped() / game.getTime());
        Stats result = new Stats(wpm, cpm);
        history.addStats(result);

        drawEndScreen(game.calcWordsTyped(), game.calcCharsTyped(), wpm, cpm);

        window = new BasicWindow();
        window.setComponent(panel);
        startGui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(black));
        startGui.addWindowAndWait(window);
    }


    //EFFECTS: draws end screen
    private void drawEndScreen(int w, int c,int wpm, int cpm) {

        Label gameOver = new Label("Game Over!");
        Label printW = new Label("You typed " + w + " words!");
        Label printC = new Label("You typed " + c + " characters!");
        Label printWPM = new Label("Your final WPM is " + wpm + "!");
        Label printCPM = new Label("Your final CPM is " + cpm + "!");
        Label printCorrectness = new Label("Your accuracy is " + game.calcAccuracy() + "%!");
        panel.addComponent(gameOver);
        panel.addComponent(printW);
        panel.addComponent(printC);
        panel.addComponent(printWPM);
        panel.addComponent(printCPM);
        panel.addComponent(printCorrectness);
        drawEndScreenButton();

    }


    //EFFECTS: draws end screen buttons
    private void drawEndScreenButton() {
        Button spacer = new Button("", new Runnable() {
            @Override
            public void run() {
                return;
            }
        });

        Button cont = new Button("Continue", new Runnable() {
            @Override
            public void run() {
                continueGame = true;
                startGui.removeWindow(window);
            }
        });

        Button exit = new Button("Exit", new Runnable() {
            @Override
            public void run() {
                continueGame = false;
                startGui.removeWindow(window);
            }
        });

        panel.addComponent(spacer);
        panel.addComponent(cont);
        panel.addComponent(exit);
    }


    //MODIFIES: game
    //EFFECTS: takes user input and makes corresponding changes
    private void handleUserInput() throws IOException {
        KeyStroke stroke = screen.pollInput();

        if (stroke == null) {
            return;
        }

        if (stroke.getKeyType() == null) {
            return;
        }

        if (stroke.getKeyType() == KeyType.Backspace) {
            game.removeInput();
            return;
        }

        if (stroke.getCharacter() != null) {
            game.addToInput(stroke.getCharacter());
        }
    }


    //EFFECTS: updates the current screen during game
    private void updateScreen(long timeLeft) throws IOException {
        screen.clear();
        int currentPosition = game.getCurrentPosition();
        String generatedWords = game.getRandomWords();
        List<Boolean> correctness;

        correctness = game.getCorrectness();

        addTyped(currentPosition, correctness, generatedWords);
        addUntyped(currentPosition, generatedWords);
        addTimer(timeLeft);

        TextCharacter indicator = new TextCharacter(BLOCK_DENSE);
        screen.setCharacter(new TerminalPosition(centerX, centerY + 1), indicator);
        screen.refresh();




    }


    //EFFECTS: adds typed characters to screen, green if correct and red if incorrect
    private void addTyped(int currentPosition, List<Boolean> correctness, String generatedWords) {
        if (currentPosition > centerX) {
            for (int i = 0; i < centerX; i++) {
                char c = generatedWords.charAt(i - centerX + currentPosition);
                boolean correct = correctness.get(i - centerX + currentPosition);
                if (correct) {
                    screen.setCharacter(new TerminalPosition(i, centerY),
                            new TextCharacter(c).withForegroundColor(green));
                } else {
                    screen.setCharacter(new TerminalPosition(i, centerY),
                            new TextCharacter(c).withForegroundColor(red));
                }
            }
        } else {
            addTypedWithSpace(currentPosition, correctness, generatedWords);
        }
    }


    //EFFECTS: adds typed characters to screen when space is required for positioning
    private void addTypedWithSpace(int currentPosition, List<Boolean> correctness, String generatedWords) {
        for (int i = 0; i <= centerX - currentPosition; i++) {
            String space = " ";
            char spacer = space.charAt(0);
            screen.setCharacter(new TerminalPosition(i, centerY),
                    new TextCharacter(spacer).withForegroundColor(black));
        }
        for (int i = 0; i < currentPosition; i++) {
            char c = generatedWords.charAt(i);
            boolean correct = correctness.get(i);
            if (correct) {
                screen.setCharacter(new TerminalPosition(i + centerX - currentPosition, centerY),
                        new TextCharacter(c).withForegroundColor(green));
            } else {
                screen.setCharacter(new TerminalPosition(i + centerX - currentPosition, centerY),
                        new TextCharacter(c).withForegroundColor(red));
            }
        }
    }


    //EFFECTS: adds untyped characters to screen with color white
    private void addUntyped(int currentPosition, String generatedWords) {
        for (int i = 0; i <= centerX; i++) {
            char c = generatedWords.charAt(i + currentPosition);
            screen.setCharacter(new TerminalPosition(i + centerX, centerY),
                    new TextCharacter(c).withForegroundColor(white));
        }
    }


    //EFFECTS: puts time left on screen
    private void addTimer(long timeLeft) {
        String timer = timeLeft + "";
        int posY = centerY / 2;
        for (int i = 0; i < timer.length(); i++) {
            char c = timer.charAt(i);
            int posX = i - timer.length() / 2 + centerX;
            screen.setCharacter(new TerminalPosition(posX, posY),
                    new TextCharacter(c).withForegroundColor(white));

        }
    }

    public void saveHistory() {
        try {
            jsonWriter.open();
            jsonWriter.write(history);
            jsonWriter.close();
            System.out.println("History Saved!");
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound, " + HISTORY_STORE);
        }
    }


    public Boolean getContinueGame() {
        return continueGame;
    }
}
