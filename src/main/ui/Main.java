package ui;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("WIP");
        TerminalGame gameHandler = new TerminalGame();

        int time = gameHandler.start();
        System.out.println(time);
        gameHandler.createInstance(time);
    }
}
