package ui;


// Main class for typing speed test, run to start the typing speed test
public class Main {


    //EFFECTS: constructs new typing speed test instance
    public static void main(String[] args) throws Exception {

        TerminalSwing gameHandler = new TerminalSwing();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                gameHandler.printLog();
            }
        }));
        run(gameHandler);

    }


    //EFFECTS: recursive part of game, allows multiple tries if user desire
    public static void run(TerminalSwing gameHandler) throws Exception {
        gameHandler.init();

        if (gameHandler.getContinueGame() == true) {
            run(gameHandler);
        }
        System.exit(0);
    }
}
