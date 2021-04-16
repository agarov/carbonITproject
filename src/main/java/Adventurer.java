public class Adventurer {

    public enum Orientation {
        N, E, S, O;

        private static final Orientation[] values = values();

        Orientation turnTo(Turn t) {
            return values[(4 + this.ordinal() + (t == Turn.D ? 1 : -1)) % 4];
        }
    }
    public enum Turn { G, D }


    private final String name;
    private int x;
    private int y;
    private Orientation currentOrientation;
    private String remainingPath;
    private int lootedTreasures;

    public Adventurer(String name,int x, int y, Orientation orientation,String path){
        this.name=name;
        this.x=x;
        this.y=y;
        this.currentOrientation=orientation;
        this.remainingPath= path;
        this.lootedTreasures=0;
    }


    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getRemainingPath() {
        return remainingPath;
    }

    public void addTreasure(){
        this.lootedTreasures++;
    }

    public Orientation getCurrentOrientation() {
        return currentOrientation;
    }

    public String retrieveAction(){
        String actionLetter = remainingPath.substring(0,1);
        remainingPath = remainingPath.substring(1);
        return actionLetter;
    }

    public int getLootedTreasures() {
        return lootedTreasures;
    }

    public void turnRight(){
        currentOrientation = currentOrientation.turnTo(Turn.D);
    }

    public void turnLeft(){
        currentOrientation = currentOrientation.turnTo(Turn.G);
    }


}
