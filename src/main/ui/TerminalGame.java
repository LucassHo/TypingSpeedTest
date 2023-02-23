package ui;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Game;

import java.io.IOException;
import java.util.List;

import static com.googlecode.lanterna.Symbols.BLOCK_DENSE;

public class TerminalGame {
    private Game game;
    private Screen screen;
    private Panel panel;
    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private final TextColor black = TextColor.ANSI.BLACK;
    private final TextColor red = TextColor.ANSI.RED;
    private final TextColor green = TextColor.ANSI.GREEN;
    private final TextColor white = TextColor.ANSI.WHITE;
    private int time = 0;
    private BasicWindow window;
    private MultiWindowTextGUI gui;

    public int start() throws Exception {
        getUserInput();

        return time;
    }


    public void getUserInput() throws Exception {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        width = screen.getTerminalSize().getColumns() - 4;
        height = screen.getTerminalSize().getRows() - 4;
        centerX = width / 2;
        centerY = height / 2;
        System.out.println(width);
        System.out.println(height);

        panel = new Panel();
        panel.setFillColorOverride(TextColor.ANSI.WHITE);
        panel.setLayoutManager(new AbsoluteLayout());
        panel.setPreferredSize(new TerminalSize(width, height));

        Label howLong = new Label("How long is your test? (in mins)");
        howLong.setSize(new TerminalSize(howLong.getText().length(), 1));
        howLong.setForegroundColor(black);
        howLong.setPosition(new TerminalPosition(centerX - howLong.getText().length(), centerY));
        panel.addComponent(howLong);

        TextBox userTime = new TextBox();
        userTime.setSize(new TerminalSize(5, 1));
        userTime.setPosition(new TerminalPosition(centerX + 1, centerY));
        panel.addComponent(userTime);

        Button enter = new Button("Enter", new Runnable() {
            @Override
            public void run() {
                time = Integer.parseInt(userTime.getText());
            }
        });
        enter.setSize(new TerminalSize(5, 1));
        enter.setPosition(new TerminalPosition(centerX - 3, centerY / 2));
        panel.addComponent(enter);




        window = new BasicWindow();
        window.setComponent(panel);


        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.WHITE));
        gui.addWindowAndWait(window);
    }

    public void createInstance(int minutes) throws InterruptedException, IOException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        width = screen.getTerminalSize().getColumns();
        height = screen.getTerminalSize().getRows();
        centerX = width / 2;
        centerY = height / 2;
        System.out.println(width);
        System.out.println(height);


        game = new Game(minutes);
        System.out.println(game.getRandomWords().length());
        long startTime = System.currentTimeMillis() / 1000;
        long elapsedTime = System.currentTimeMillis() / 1000;
        while (elapsedTime - startTime < minutes * 60) {
            long timeLeft = minutes * 60 - (elapsedTime - startTime);
            tick(timeLeft);
            Thread.sleep(1000L / Game.TICKS_PER_SECOND);

            elapsedTime = System.currentTimeMillis() / 1000;
        }

        endScreen();
    }


    private void tick(long timeLeft) throws IOException {
        handleUserInput();

        updateScreen(timeLeft);

    }

    private void endScreen() throws IOException {
        screen.close();

    }


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

    private void updateScreen(long timeLeft) throws IOException {
        screen.clear();
        int currentPosition = game.getCurrentPosition();
        String generatedWords = game.getRandomWords();
        List<Boolean> correctness;

        correctness = game.getCorrectness();

        addTyped(currentPosition, correctness, generatedWords);
        addUntyped(currentPosition, generatedWords);
        addTimer(timeLeft);
        //System.out.println(currentPosition);

        TextCharacter indicator = new TextCharacter(BLOCK_DENSE);
        screen.setCharacter(new TerminalPosition(centerX, centerY + 1), indicator);

        screen.refresh();




    }

    private void addTyped(int currentPosition, List<Boolean> correctness, String generatedWords) {
        if (currentPosition > centerX) {
            for (int i = 0; i < centerX; i++) {
                char c = generatedWords.charAt(i - centerX + currentPosition);
                boolean correct = correctness.get(i - centerX + currentPosition);
                if (correct) {
                    screen.setCharacter(
                            new TerminalPosition(i, centerY), new TextCharacter(c).withForegroundColor(green));
                } else {
                    screen.setCharacter(
                            new TerminalPosition(i, centerY), new TextCharacter(c).withForegroundColor(red));
                }
            }
        } else {
            for (int i = 0; i <= centerX - currentPosition; i++) {
                String space = " ";
                char spacing = space.charAt(0);
                screen.setCharacter(i, centerY, new TextCharacter(spacing).withForegroundColor(black));
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
    }

    private void addUntyped(int currentPosition, String generatedWords) {
        for (int i = 0; i <= centerX; i++) {
            char c = generatedWords.charAt(i + currentPosition);
            screen.setCharacter(new TerminalPosition(i + centerX, centerY),
                    new TextCharacter(c).withForegroundColor(white));
        }
    }

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
}
