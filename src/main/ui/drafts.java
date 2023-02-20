package ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Game;

import java.io.IOException;
import java.util.List;

public class drafts {

    public class TerminalGame {
        private Game game;
        private Screen screen;
        private WindowBasedTextGUI endgui;
        private Panel panel;
        private int width;
        private int height;
        private int centerX;
        private int centerY;
        private final int buttonWidth = 10;
        private final int buttonHeight = 2;
        private final TextColor black = TextColor.ANSI.BLACK;
        private final TextColor red = TextColor.ANSI.RED;
        private final TextColor green = TextColor.ANSI.GREEN;
        private final TerminalSize textSize = new TerminalSize(1, 1);
        private int time = 0;

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

            Button button1 = new Button("1min", new Runnable() {
                @Override
                public void run() {
                    game = new Game(1);
                    //createInstance(1);
                }
            });
            button1.setPosition(new TerminalPosition(width / 4, centerY));
            button1.setSize(new TerminalSize(buttonWidth, buttonHeight));
            panel.addComponent(button1);


            Button button2 = new Button("3min", new Runnable() {
                @Override
                public void run() {
                    game = new Game(3);
                    //createInstance(3);
                }
            });
            button2.setPosition(new TerminalPosition(width / 4 * 2, centerY));
            button2.setSize(new TerminalSize(buttonWidth, buttonHeight));
            panel.addComponent(button2);

            Button button3 = new Button("5min", new Runnable() {
                @Override
                public void run() {
                    game = new Game(5);
                    //createInstance(5);
                }
            });
            button3.setPosition(new TerminalPosition(width / 4 * 3, centerY));
            button3.setSize(new TerminalSize(buttonWidth, buttonHeight));
            panel.addComponent(button3);



            BasicWindow window = new BasicWindow();
            window.setComponent(panel);


            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.WHITE));
            gui.addWindowAndWait(window);


        }

        private void createInstance(int minutes) throws InterruptedException, IOException {
            game = new Game(5);
            panel.removeAllComponents();
            long startTime = System.currentTimeMillis();
            long elapsedTime = System.currentTimeMillis() / 1000;
            while (elapsedTime - startTime < minutes * 60) {
                tick();
                Thread.sleep(1000L / Game.TICKS_PER_SECOND);

                elapsedTime = System.currentTimeMillis() / 1000;
            }

            endScreen();
        }

        private void tick() throws IOException {
            handleUserInput();

            updateScreen();

        }

        private void endScreen() {
            panel.removeAllComponents();

        }

        private void handleUserInput() throws IOException {
            KeyStroke stroke = screen.pollInput();

            if (stroke == null) {
                return;
            }

            if (stroke.getCharacter() != null) {
                game.addToInput(stroke.getCharacter());
            }

            if (stroke.getKeyType() == KeyType.Backspace) {
                game.removeInput();
            }else {
                return;
            }
        }

        private void updateScreen() {
            int currentPosition = game.getCurrentPosition();
            int posX = 0;
            String generatedWords = game.getRandomWords();
            List<Boolean> correctness;

            correctness = game.getCorrectness();

            if (currentPosition > centerX) {
                for (int i = 0; i < centerX; i++) {
                    char c = generatedWords.charAt(i - centerX + currentPosition);
                    Label label = new Label("" + c);
                    label.setPosition(new TerminalPosition(i, centerY));
                    label.setSize(textSize);
                    boolean correct = correctness.get(i - centerX + currentPosition);
                    if (correct) {
                        label.setForegroundColor(green);
                    } else {
                        label.setForegroundColor(red);
                    }
                    panel.addComponent(label);
                }
                for (int i = 0; i <= centerX; i++) {
                    char c = generatedWords.charAt(i + currentPosition);
                    Label label = new Label("" + c);
                    label.setPosition(new TerminalPosition(i, centerY));
                    label.setSize(textSize);
                    label.setForegroundColor(black);
                    panel.addComponent(label);
                }
            }


        }
    }

}
