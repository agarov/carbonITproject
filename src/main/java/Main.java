public class Main {

    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.parseData("entry.txt");
        gameEngine.visualizeMap();
    }
}

