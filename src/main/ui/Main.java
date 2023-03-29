package ui;

public class Main {

    public static void main(String[] args) throws Exception {
        //TerminalGame gameHandler = new TerminalGame();

        //gameHandler.startScreen();
        //run(gameHandler);
        TerminalSwing gameHandler = new TerminalSwing();
        gameHandler.init();

    }

    public static void run(TerminalGame gameHandler) throws Exception {
        gameHandler.start();

        if (gameHandler.getContinueGame() == true) {
            run(gameHandler);
        }
        System.exit(0);
    }
}
