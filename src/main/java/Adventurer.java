import java.util.Arrays;
import java.util.LinkedList;

public class Adventurer {

    public enum Orientation {
        N, E, S, O;

        private static Orientation[] vals = values();

        Orientation turnTo(Turn t) {
            return vals[(4 + this.ordinal() + (t == Turn.D ? 1 : -1)) % 4];
        }
    }
    public enum Turn { G, D }


    private String name;
    private int x;
    private int y;
    private Orientation currentOrientation;

    public String getName() {
        return name;
    }

    private String initialPath;
    private String remainingPath;
    private int lootedTreasures;

    public Adventurer(String name,int x, int y, Orientation orientation,String initialPath){
        this.name=name;
        this.x=x;
        this.y=y;
        this.currentOrientation=orientation;
        this.initialPath = initialPath;
        this.remainingPath= initialPath;
        this.lootedTreasures=0;
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
