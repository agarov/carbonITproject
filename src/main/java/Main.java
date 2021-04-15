public class Main {

    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.parseData(gameEngine.extractData("entry.txt"));
        gameEngine.run();
        gameEngine.generateOutput();
    }
}

