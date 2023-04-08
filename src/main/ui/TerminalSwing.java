package ui;

import model.Event;
import model.EventLog;
import model.Game;
import model.History;
import model.Stats;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


//TerminalSwing is the main GUI for the typing speed test, implements listeners for action detection
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
    private static final Image START_BUTTON = new ImageIcon("./data/images/start_button.png").getImage();
    private static final Image HISTORY_BUTTON = new ImageIcon("./data/images/history_button.png").getImage();
    private static final Image YES_BUTTON = new ImageIcon("./data/images/yes_button.png").getImage();
    private static final Image NO_BUTTON = new ImageIcon("./data/images/no_button.png").getImage();
    private static final Image EXIT_BUTTON = new ImageIcon("./data/images/exit_button.png").getImage();
    private static final Image CONT_BUTTON = new ImageIcon("./data/images/continue_button.jpg").getImage();
    private static final Image DEL_BUTTON = new ImageIcon("./data/images/delete_button.png").getImage();
    private JPanel panel;
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
    private int selectedRowHistory = -1;
    private static final int CHAR_X = 15;
    private static final int CHAR_Y = 16;
    private static final int FRAME_LENGTH = 600;
    private static final int FRAME_HEIGHT = 600;
    private static final int CENTER_X = FRAME_LENGTH / 2;
    private static final int CENTER_Y = FRAME_HEIGHT / 2;
    private static final int CHAR_TO_HALF = CENTER_X / CHAR_X;
    private EventLog eventLog = EventLog.getInstance();


    //Constructor for TerminalSwing
    public TerminalSwing() throws InterruptedException {
        jsonWriter = new JsonWriter(HISTORY_STORE);
        jsonReader = new JsonReader(HISTORY_STORE);
        width = FRAME_LENGTH;
        height = FRAME_HEIGHT;
        loadSaveScreen();
        waitForState(1);

    }


    //EFFECTS: flow of events for typing speed test, proceeds after actions, designed for looping
    public void init() throws InterruptedException, IOException {
        currentState = 1;
        loadSaveFrame.setVisible(false);
        loadMainScreen();
        waitForState(2);
        startFrame.setVisible(false);
        createInstance(time);
        waitForState(3);
        gameFrame.setVisible(false);
        loadEndScreen();
        waitForState(4);
        endFrame.setVisible(false);
        loadEndSaveScreen();
        waitForState(5);
        endSaveFrame.setVisible(false);
    }


    //EFFECTS: waits for currentState to switch to given state
    private void waitForState(int state) throws InterruptedException {
        while (currentState != state) {
            Thread.sleep(1000L / Game.TICKS_PER_SECOND);
        }
    }


    //EFFECTS: resizes given image to given width and height
    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }


    //EFFECTS: loads the "Load Save Data?" screen
    private void loadSaveScreen() {
        loadSaveFrame = new JFrame("Typing Speed Test Beta");
        loadSaveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadSaveFrame.setSize(FRAME_LENGTH, FRAME_LENGTH);
        loadSaveFrame.setVisible(true);
        panel = new JPanel();
        panel.setLayout(null);
        loadSaveScreenComponents("Load Saved Data?");
        loadSaveFrame.setLayout(new GridLayout(1, 1));
        loadSaveFrame.add(panel);
        loadSaveFrame.revalidate();
        loadSaveFrame.repaint();

    }


    //EFFECTS: constructs the main start screen
    private void loadMainScreen() {
        startFrame = new JFrame("Typing Speed Test Beta");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(FRAME_LENGTH, FRAME_LENGTH);
        startFrame.setVisible(true);

        createPanel();
        panel.setEnabled(true);
        startFrame.add(panel);
        startFrame.setLayout(new GridLayout(1, 1));
        startFrame.revalidate();
        startFrame.repaint();
    }


    //EFFECTS: returns panel for buttons in main start screen
    private void buttonPanel() {
        Dimension d = new Dimension(150, 50);
        JButton b1 = new JButton(new ImageIcon(getScaledImage(START_BUTTON, d.width, d.height)));
        b1.setActionCommand("Enter");
        b1.addActionListener(this);
        JButton b2 = new JButton(new ImageIcon(getScaledImage(HISTORY_BUTTON, d.width, d.height)));
        b2.setActionCommand("History");
        b2.addActionListener(this);
        JButton b3 = new JButton(new ImageIcon(getScaledImage(EXIT_BUTTON, d.width, d.height)));
        b3.setActionCommand("Exit");
        b3.addActionListener(this);

        b1.setBounds(50, FRAME_HEIGHT - 300, d.width, d.height);
        b2.setBounds(CENTER_X - d.width / 2, FRAME_HEIGHT - 300, d.width, d.height);
        b3.setBounds(FRAME_LENGTH - 50 - d.width, FRAME_HEIGHT - 300, d.width, d.height);


        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
    }


    //EFFECTS: returns panel for labels and inputs in main start screen
    private void createPanel() {
        panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);
        labelPanel();
        buttonPanel();
    }


    //EFFECTS: constructs the panel for labels and inputs in main start screen, split to adhere function length req.
    private void labelPanel() {
        JLabel l1 = new JLabel("Welcome to Typing Speed Test!");
        JLabel l2 = new JLabel("Enter your play time (in min):");
        JLabel l3 = new JLabel("Choose Difficulty");
        textField = new JTextField(20);
        String[] d = {"Easy", "Hard"};
        JComboBox difficultyList = new JComboBox(d);
        difficultyList.setSelectedIndex(0);
        difficultyList.addActionListener(this);

        Dimension size = l1.getPreferredSize();
        l1.setBounds(50, 50, size.width + 10, size.height);

        size = l2.getPreferredSize();
        l2.setBounds(50, 100, size.width, size.height);

        size = textField.getPreferredSize();
        textField.setBounds(250, 100, size.width, size.height);

        size = l3.getPreferredSize();
        l3.setBounds(50, 130, size.width + 30, size.height);

        size = difficultyList.getPreferredSize();
        difficultyList.setBounds(250, 130, size.width, size.height);

        panel.add(l1);
        panel.add(l2);
        panel.add(l3);
        panel.add(textField);
        panel.add(difficultyList);
    }


    //EFFECTS: constructs the history screen
    private void loadHistoryScreen() {
        historyFrame = new JFrame("History");
        historyFrame.setSize(FRAME_LENGTH, FRAME_LENGTH);
        historyFrame.setLayout(new GridLayout(1, 1));
        historyFrame.setVisible(true);
        JTable table = historyTable();
        JScrollPane scrollPane = new JScrollPane(table);
        Dimension size = scrollPane.getPreferredSize();
        scrollPane.setBounds(CENTER_X - size.width / 2, 50, size.width, size.height);



        JPanel p1 = new JPanel();
        p1.setLayout(null);
        p1.add(scrollPane);
        p1.setEnabled(true);
        addHistoryButtonTo(p1);

        historyFrame.add(p1);
        historyFrame.revalidate();
        historyFrame.repaint();

    }


    //EFFECTS: turns history to JTable object for history screen and returns JTable
    private JTable historyTable() {
        Object[] column = {"Date", "WPM", "CPM", "Accuracy", "Time Played", "Difficulty"};
        Object[][] dataList = new Object[history.size()][6];
        for (int i = 0; i < history.size(); i++) {
            Stats stat = history.getStats(i);
            dataList[i][0] = stat.getDateOfGame();
            dataList[i][1] = stat.getWPM();
            dataList[i][2] = stat.getCPM();
            dataList[i][3] = stat.getAccuracy();
            dataList[i][4] = stat.getTime();
            dataList[i][5] = stat.getDifficulty();

        }


        DefaultTableModel tableModel = new DefaultTableModel(dataList, column);
        JTable table = new JTable(tableModel);
        TableColumn column1 = table.getColumnModel().getColumn(0);
        column1.setPreferredWidth(column1.getPreferredWidth() + 60);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedRowHistory = table.getSelectedRow();
            }
        });
        return table;
    }


    //EFFECTS: returns JPanel containing the buttons for history screen
    private void addHistoryButtonTo(JPanel p1) {
        Dimension d1 = new Dimension(50, 50);
        JButton b1 = new JButton(new ImageIcon(getScaledImage(DEL_BUTTON, d1.width, d1.height)));
        b1.setActionCommand("Delete");
        b1.addActionListener(this);
        Dimension d2 = new Dimension(150, 50);
        JButton b2 = new JButton(new ImageIcon(getScaledImage(EXIT_BUTTON, d2.width, d2.height)));
        b2.setActionCommand("Cancel");
        b2.addActionListener(this);

        b1.setBounds(FRAME_LENGTH - 50 - d1.width, FRAME_HEIGHT - 100, d1.width, d1.height);
        b2.setBounds(FRAME_LENGTH - 100 - d1.width - d2.width, FRAME_HEIGHT - 100, d2.width, d2.height);

        p1.add(b1);
        p1.add(b2);
    }


    //EFFECTS: constructs end screen after game ends
    private void loadEndScreen() {
        endFrame = new JFrame("Typing Speed Test Beta");
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setSize(FRAME_LENGTH, FRAME_LENGTH);
        endFrame.setVisible(true);
        endFrame.setLayout(new GridLayout(1, 1));
        int wpm = (int) ((double) game.calcWordsTyped() / game.getTime());
        int cpm = (int) ((double) game.calcCharsTyped() / game.getTime());
        result = new Stats(wpm, cpm, game.calcAccuracy(), game.getTime(), difficulty);
        panel.removeAll();
        panel.setEnabled(true);
        loadEndScreenComponents(result);

        addEndButton();
        endFrame.add(panel);
        endFrame.revalidate();
        endFrame.repaint();

    }


    //EFFECTS: adds labels to panel for end screen
    private void loadEndScreenComponents(Stats result) {
        JLabel l1 = new JLabel("Game Over!");
        JLabel l2 = new JLabel("You typed " + game.calcWordsTyped() + " words!");
        JLabel l3 = new JLabel("You typed " + game.calcCharsTyped() + " characters!");
        JLabel l4 = new JLabel("Your final WPM is " + result.getWPM() + "!");
        JLabel l5 = new JLabel("Your final CPM is " + result.getCPM() + "!");
        JLabel l6 = new JLabel("Your accuracy is " + result.getAccuracy() + "%!");
        l1.setBounds(50, 50, l1.getPreferredSize().width + 20, l1.getPreferredSize().height);
        l2.setBounds(50, 100, l2.getPreferredSize().width + 20, l2.getPreferredSize().height);
        l3.setBounds(50, 150, l3.getPreferredSize().width + 20, l3.getPreferredSize().height);
        l4.setBounds(50, 200, l4.getPreferredSize().width + 20, l4.getPreferredSize().height);
        l5.setBounds(50, 250, l5.getPreferredSize().width + 20, l5.getPreferredSize().height);
        l6.setBounds(50, 300, l6.getPreferredSize().width + 20, l6.getPreferredSize().height);
        panel.add(l1);
        panel.add(l2);
        panel.add(l3);
        panel.add(l4);
        panel.add(l5);
        panel.add(l6);
    }


    //EFFECTS: returns JPanel containing buttons for end screen
    private void addEndButton() {
        Dimension d1 = new Dimension(200, 50);
        JButton b1 = new JButton(new ImageIcon(getScaledImage(CONT_BUTTON, d1.width, d1.height)));
        b1.setActionCommand("Continue");
        b1.addActionListener(this);
        Dimension d2 = new Dimension(150, 50);
        JButton b2 = new JButton(new ImageIcon(getScaledImage(EXIT_BUTTON, d2.width, d2.height)));
        b2.setActionCommand("Exit");

        b1.setBounds(50, FRAME_HEIGHT - 100, d1.width, d1.height);
        b2.setBounds(50 + d1.width + 50, FRAME_HEIGHT - 100, d2.width, d2.height);

        panel.add(b1);
        panel.add(b2);
    }


    //EFFECTS: loads the "Save Data?" screen
    private void loadEndSaveScreen() {
        endSaveFrame = new JFrame("End Save Screen");
        endSaveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endSaveFrame.setSize(FRAME_LENGTH, FRAME_LENGTH);
        endSaveFrame.setVisible(true);
        panel = new JPanel();
        panel.setLayout(null);
        loadSaveScreenComponents("Save Data?");
        endSaveFrame.setLayout(new GridLayout(1, 1));
        endSaveFrame.add(panel);
        endSaveFrame.revalidate();
        endSaveFrame.repaint();

    }


    //EFFECTS: adds the yes no button for the two save screens, including the label s
    private void loadSaveScreenComponents(String s) {
        int buttonWidth = 75;
        int buttonHeight = 75;
        JLabel label1 = new JLabel(s);
        JButton yes = new JButton(new ImageIcon(getScaledImage(YES_BUTTON, buttonWidth, buttonHeight)));
        JButton no = new JButton(new ImageIcon(getScaledImage(NO_BUTTON, buttonWidth, buttonHeight)));
        yes.setMnemonic(KeyEvent.VK_Y);
        yes.setActionCommand("Yes");
        yes.addActionListener(this);
        no.setMnemonic(KeyEvent.VK_N);
        no.setActionCommand("No");
        no.addActionListener(this);
        Dimension size = label1.getPreferredSize();
        label1.setBounds(CENTER_X - (size.width + 10) / 2, CENTER_Y - 50, size.width + 10, size.height);
        yes.setBounds(CENTER_X - buttonWidth / 2, CENTER_Y, buttonWidth, buttonHeight);
        no.setBounds(CENTER_X - buttonWidth / 2, CENTER_Y + 100, buttonWidth, buttonHeight);
        panel.add(label1);
        panel.add(yes);
        panel.add(no);
        panel.setEnabled(true);
    }


    //EFFECTS: runs when action is detected, consists of multiple states that corresponds to the currentState, so
    // the correct button function is ran
    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == 0) {
            actionState0(e);
        }
        if (currentState == 1) {
            actionState1(e);
        }
        if (currentState == 2) {
            //no buttons here
        }
        if (currentState == 3) {
            actionState3(e);
        }
        if (currentState == 4) {
            actionState4(e);
        }


    }


    //EFFECTS: responds to action done in "Load Save Data?" screen
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


    //EFFECTS: responds to action done in main start screen and history screen
    private void actionState1(ActionEvent e) {
        if ("Enter".equals(e.getActionCommand())) {
            if (textField.getText() != "") {
                time = Double.parseDouble(textField.getText());
                currentState = 2;
            }
            return;
        }

        if ("History".equals(e.getActionCommand())) {
            loadHistoryScreen();
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

        actionStateHistory(e);

        return;
    }


    //EFFECTS: responds to action done in history screen
    private void actionStateHistory(ActionEvent e) {
        if ("Delete".equals(e.getActionCommand())) {
            if (selectedRowHistory != -1) {
                history.removeStats(selectedRowHistory);
                saveHistory();
                selectedRowHistory = -1;
                historyFrame.setVisible(false);
                loadHistoryScreen();
            }
        }

        if ("Cancel".equals(e.getActionCommand())) {
            historyFrame.setVisible(false);
        }

    }


    //EFFECTS: responds to action done in end screen
    private void actionState3(ActionEvent e) {
        if ("Continue".equals(e.getActionCommand())) {
            continueGame = true;
            currentState = 4;
        }

        if ("Exit".equals(e.getActionCommand())) {
            continueGame = false;
            currentState = 4;
        }

    }


    //EFFECTS: responds to action done in end "Save Data?" screen
    private void actionState4(ActionEvent e) {
        if ("Yes".equals(e.getActionCommand())) {
            history.addStats(result);
            saveHistory();
            currentState = 5;
        }
        if ("No".equals(e.getActionCommand())) {
            saveHistory();
            currentState = 5;
        }
    }


    //EFFECTS: starts the game with given time, constructs the game screen and updates every tick
    private void createInstance(double minutes) throws InterruptedException, IOException {
        gameFrame = new JFrame("Typing Speed Test Beta");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(FRAME_LENGTH, FRAME_LENGTH);
        gameFrame.setVisible(true);
        panel.removeAll();
        gameFrame.setLayout(null);
        panel.setBounds(0,0,FRAME_LENGTH,FRAME_HEIGHT);
        gameFrame.add(panel);
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


    //EFFECTS: updates the current screen during game
    private void tick(long timeLeft) throws IOException {
        panel.removeAll();
        int currentPosition = game.getCurrentPosition();
        String generatedWords = game.getRandomWords();
        List<Boolean> correctness = game.getCorrectness();

        addTyped(currentPosition, correctness, generatedWords);
        addUntyped(currentPosition, generatedWords);
        gameFrame.repaint();
        addTimer(timeLeft);


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
                panel.add(label);
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
            panel.add(space);
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
            panel.add(label);
        }
    }


    //EFFECTS: adds untyped characters to screen with color white
    private void addUntyped(int currentPosition, String generatedWords) {
        for (int i = 0; i <= CHAR_TO_HALF; i++) {
            char c = generatedWords.charAt(i + currentPosition);
            JLabel label = new JLabel(c + "");
            label.setForeground(black);
            label.setBounds(CENTER_X + i * CHAR_X, CENTER_Y, CHAR_X, CHAR_Y);
            panel.add(label);
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
            panel.add(label);

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


    //EFFECTS: detects character input during game session, adds the input to game
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


    //EFFECTS: detects backspace during game session, removes one input in game
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            game.removeInput();
            return;
        }
    }


    //unnecessary function
    @Override
    public void keyReleased(KeyEvent e) {

    }

    public Boolean getContinueGame() {
        return continueGame;
    }


    //EFFECTS: prints eventLog to console
    public void printLog() {
        Iterator<Event> eventIterator = eventLog.iterator();
        while (eventIterator.hasNext()) {
            Event e = eventIterator.next();
            System.out.println("Date: " + e.getDate());
            System.out.println(e.getDescription());
        }
    }
}
