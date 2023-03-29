package ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import model.Game;
import model.History;
import model.Stats;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.lanterna.Symbols.BLOCK_DENSE;

public class TerminalSwing implements ActionListener, KeyListener {
    private int width;
    private int height;
    private JTextField textField;
    private Game game;
    private double time;
    private History history;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Stats result;
    private String difficulty = "Easy";
    private Boolean continueGame;
    private static final String HISTORY_STORE = "./data/history.json";
    private JPanel p;
    private JFrame loadSaveFrame;
    private JFrame startFrame;
    private JFrame gameFrame;
    private JFrame endFrame;
    private JFrame historyFrame;
    private JFrame endSaveFrame;
    private int currentState;
    private Color green = Color.GREEN;
    private Color red = Color.RED;
    private Color black = Color.BLACK;
    private final int CHAR_X = 10;
    private final int CHAR_Y = 16;
    private final int FRAME_LENGTH = 900;
    private final int FRAME_HEIGHT = 900;
    private final int CENTER_X = FRAME_LENGTH / 2;
    private final int CENTER_Y = FRAME_HEIGHT / 2;
    private final int CHAR_TO_HALF = CENTER_X / CHAR_X;

    public TerminalSwing() {
        loadSaveFrame = new JFrame("Typing Speed Test Beta");
        startFrame = new JFrame("Typing Speed Test Beta");
        gameFrame = new JFrame("Typing Speed Test Beta");
        historyFrame = new JFrame("Typing Speed Test Beta");
        endFrame = new JFrame("Typing Speed Test Beta");
        endSaveFrame = new JFrame("Typing Speed Test Beta");
        initiateFrames();
        jsonWriter = new JsonWriter(HISTORY_STORE);
        jsonReader = new JsonReader(HISTORY_STORE);
        currentState = 0;
        width = loadSaveFrame.getWidth();
        height = loadSaveFrame.getHeight();
        loadSaveFrame.setVisible(true);

    }

    private void initiateFrames() {
        ArrayList<JFrame> frames = new ArrayList<>();
        frames.add(loadSaveFrame);
        frames.add(startFrame);
        frames.add(gameFrame);
        frames.add(historyFrame);
        frames.add(endFrame);
        frames.add(endSaveFrame);
        for (JFrame frame : frames) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(FRAME_LENGTH, FRAME_LENGTH);
        }
    }

    public void init() throws InterruptedException, IOException {
        loadSaveScreen();
        waitForState(1);
        loadSaveFrame.setVisible(false);
        startFrame.setVisible(true);
        loadMainScreen();
        waitForState(2);
        startFrame.setVisible(false);
        gameFrame.setVisible(true);
        createInstance(time);
        waitForState(3);
        gameFrame.setVisible(false);
        endFrame.setVisible(true);



    }

    private void waitForState(int state) throws InterruptedException {
        while (currentState != state) {
            Thread.sleep(1000L / Game.TICKS_PER_SECOND);
        }
    }

    //EFFECTS: loads the "Load Save Data?" screen
    private void loadSaveScreen() {
        p = new JPanel();
        p.setBounds(0,0, width, height);
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        JLabel label1 = new JLabel("Load Saved Data?");
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        yes.setMnemonic(KeyEvent.VK_Y);
        yes.setActionCommand("Yes");
        yes.addActionListener(this);
        no.setMnemonic(KeyEvent.VK_N);
        no.setActionCommand("No");
        no.addActionListener(this);
        p.add(label1);
        p.add(yes);
        p.add(no);
        p.setEnabled(true);
        loadSaveFrame.add(p);
        loadSaveFrame.setLayout(new GridLayout(1, 1));

    }

    private void loadMainScreen() {
        JButton b1 = new JButton("Enter");
        b1.setMnemonic(KeyEvent.VK_E);
        b1.setActionCommand("Enter");
        b1.addActionListener(this);
        JButton b2 = new JButton("History");
        b2.setMnemonic(KeyEvent.VK_H);
        b2.setActionCommand("History");
        b2.addActionListener(this);
        JButton b3 = new JButton("Exit");
        b3.setMnemonic(KeyEvent.VK_ESCAPE);
        b3.setActionCommand("Exit");
        b3.addActionListener(this);
        JLabel l1 = new JLabel("Welcome to Typing Speed Test!");
        JLabel l2 = new JLabel("Enter your play time (in min):");
        JLabel l3 = new JLabel("Choose Difficulty");
        textField = new JTextField(20);
        String[] d = {"Easy", "Hard"};
        JComboBox difficultyList = new JComboBox(d);
        difficultyList.setSelectedIndex(0);
        difficultyList.addActionListener(this);


        p = new JPanel();
        p.setBounds(0, 0, width, height);
        p.setLayout(null);
        Insets insets = p.getInsets();
        Dimension size = l1.getPreferredSize();
        l1.setBounds(50 + insets.left, 50 + insets.top, size.width + 10, size.height);
        size = l2.getPreferredSize();
        l2.setBounds(50 + insets.left, 100 + insets.top, size.width, size.height);
        size = textField.getPreferredSize();
        textField.setBounds(250 + insets.left, 100 + insets.top, size.width, size.height);
        size = l3.getPreferredSize();
        l3.setBounds(50 + insets.left, 130 + insets.top, size.width, size.height);
        size = difficultyList.getPreferredSize();
        difficultyList.setBounds(250 + insets.left, 130 + insets.top, size.width, size.height);

        JPanel p2 = new JPanel();
        p2.setBounds(0, 150, width, 50);
        p2.setBackground(Color.YELLOW);
        p2.setLayout(new GridLayout(1, 3));
//        size = b1.getPreferredSize();
//        b1.setBounds(50 + insets.left, 150 + insets.top, size.width, size.height);
//        size = b2.getPreferredSize();
//        b2.setBounds(200 + insets.left, 150 + insets.top, size.width, size.height);
//        size = b3.getPreferredSize();
//        b3.setBounds(450 + insets.left, 150 + insets.top, size.width, size.height);

        p.add(l1);
        p.add(l2);
        p.add(l3);
        p.add(textField);
        p.add(difficultyList);
        p2.add(b1);
        p2.add(b2);
        p2.add(b3);



        p.setEnabled(true);
        p2.setEnabled(true);
        JPanel p3 = new JPanel();
        p3.setLayout(new GridLayout(8, 1));
        p3.add(p2);
        startFrame.add(p);
        startFrame.add(p3);
        startFrame.setLayout(new GridLayout(2, 1));
        startFrame.revalidate();
        startFrame.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == 0) {
            actionState0(e);
        }
        if (currentState == 1) {
            actionState1(e);
        }
        if (currentState == 2) {
            //actionState2(e);
        }
        if (currentState == 3) {
            //actionState3(e);
        }
        if (currentState == 4) {
            //actionState4(e);
        }


    }

    private void actionState0(ActionEvent e) {
        if ("Yes".equals(e.getActionCommand())) {
            try {
                history = jsonReader.read();
                currentState = 1;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if ("No".equals(e.getActionCommand())) {
            history = new History();
            saveHistory();
            currentState = 1;
        }
    }

    private void actionState1(ActionEvent e) {
        if ("Enter".equals(e.getActionCommand())) {
            if (textField.getText() != "") {
                time = Double.parseDouble(textField.getText());
                currentState = 2;
            }
            return;
        }

        if ("History".equals(e.getActionCommand())) {
            //historyScreen()
            return;
        }

        if ("Exit".equals(e.getActionCommand())) {
            System.exit(0);
        }

        if ("Easy".equals(e.getActionCommand())) {
            difficulty = "Easy";
        }

        if ("Hard".equals(e.getActionCommand())) {
            difficulty = "Hard";
        }

        return;
    }

    private void createEndP() {

    }

    //EFFECTS: starts the game with given time
    private void createInstance(double minutes) throws InterruptedException, IOException {

        p.removeAll();
        gameFrame.setLayout(null);
        p.setBounds(0,0,FRAME_LENGTH,FRAME_HEIGHT);
        gameFrame.add(p);
        gameFrame.addKeyListener(this);

        game = new Game(minutes, difficulty);
        long startTime = System.currentTimeMillis() / 1000;
        long elapsedTime = System.currentTimeMillis() / 1000;
        while (elapsedTime - startTime < minutes * 60) {
            long timeLeft = (long) (minutes * 60 - (elapsedTime - startTime));
            tick(timeLeft);
            Thread.sleep(1000L / Game.TICKS_PER_SECOND);

            elapsedTime = System.currentTimeMillis() / 1000;
        }
        gameFrame.removeKeyListener(this);
        currentState = 3;
    }


    //EFFECTS: ticks the game
    private void tick(long timeLeft) throws IOException {
        updateScreen(timeLeft);

    }


    //EFFECTS: updates the current screen during game
    private void updateScreen(long timeLeft) throws IOException {
        p.removeAll();
        int currentPosition = game.getCurrentPosition();
        String generatedWords = game.getRandomWords();
        List<Boolean> correctness = game.getCorrectness();

        addTyped(currentPosition, correctness, generatedWords);
        addUntyped(currentPosition, generatedWords);
        gameFrame.repaint();
        addTimer(timeLeft);
        //addPMBar(timeLeft, "WPM", game.calcWordsTyped(), 100, height * 3 / 4);
        //addPMBar(timeLeft, "CPM", game.calcCharsTyped(), 500, height * 3 / 4 + 5);


    }



    //EFFECTS: adds typed characters to screen, green if correct and red if incorrect
    private void addTyped(int currentPosition, List<Boolean> correctness, String generatedWords) {
        if (currentPosition > CHAR_TO_HALF) {
            for (int i = 0; i < CHAR_TO_HALF; i++) {
                char c = generatedWords.charAt(i - CHAR_TO_HALF + currentPosition);
                boolean correct = correctness.get(i - CHAR_TO_HALF + currentPosition);
                JLabel label = new JLabel(c + "");
                if (correct) {
                    label.setForeground(green);
                } else {
                    label.setForeground(red);
                }
                label.setBounds(i * CHAR_X, CENTER_Y, CHAR_X, CHAR_Y);
                p.add(label);
            }
        } else {
            addTypedWithSpace(currentPosition, correctness, generatedWords);
        }
    }


    //EFFECTS: adds typed characters to screen when space is required for positioning
    private void addTypedWithSpace(int currentPosition, List<Boolean> correctness, String generatedWords) {
        JLabel space = new JLabel(" ");
        for (int i = 0; i <= CHAR_TO_HALF - currentPosition; i++) {
            space.setBounds(i * CHAR_X, CENTER_Y, CHAR_X, CHAR_Y);
            p.add(space);
        }
        for (int i = 0; i < currentPosition; i++) {
            char c = generatedWords.charAt(i);
            boolean correct = correctness.get(i);
            JLabel label = new JLabel(c + "");
            if (correct) {
                label.setForeground(green);
            } else {
                label.setForeground(red);
            }
            label.setBounds((i + CHAR_TO_HALF - currentPosition) * CHAR_X, CENTER_Y, CHAR_X, CHAR_Y);
            p.add(label);
        }
    }


    //EFFECTS: adds untyped characters to screen with color white
    private void addUntyped(int currentPosition, String generatedWords) {
        for (int i = 0; i <= CHAR_TO_HALF; i++) {
            char c = generatedWords.charAt(i + currentPosition);
            JLabel label = new JLabel(c + "");
            label.setForeground(black);
            label.setBounds(CENTER_X + i * CHAR_X, CENTER_Y, CHAR_X, CHAR_Y);
            p.add(label);
        }
    }


    //EFFECTS: puts time left on screen
    private void addTimer(long timeLeft) {
        String timer = timeLeft + "";
        int posY = CENTER_Y / 2;
        for (int i = 0; i < timer.length(); i++) {
            char c = timer.charAt(i);
            int posX = (i - timer.length()) * 14 / 2 + CENTER_X;
            JLabel label = new JLabel(c + "");
            label.setForeground(black);
            label.setBounds(posX, posY, 14, 16);
            p.add(label);

        }
    }

    //EFFECTS: saves the history to json
    public void saveHistory() {
        try {
            jsonWriter.open();
            jsonWriter.write(history);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound, " + HISTORY_STORE);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

        int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            if (c == '\b') {
                return;
            } else {
                game.addToInput(c);
            }
            return;
        }



        return;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            game.removeInput();
            return;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
