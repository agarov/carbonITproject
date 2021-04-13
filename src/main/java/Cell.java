import java.util.Collections;

public class Cell {
    private boolean mountainous;
    private int nbrOfTreasures;
    private boolean occupied;



    public Cell(){
        this.mountainous=false;
        this.nbrOfTreasures=0;
        this.occupied=false;
    }


    public boolean isMountainous() {
        return mountainous;
    }

    public void setMountainous(boolean mountainous) {
        this.mountainous = mountainous;
    }

    public int getNbrOfTreasures() {
        return nbrOfTreasures;
    }

    public void setNbrOfTreasures(int nbrOfTreasures) {
        this.nbrOfTreasures = nbrOfTreasures;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String displayCell(int length) {
        if(isMountainous()){
            return "M"+String.join("", Collections.nCopies(length+3, " "));
        }
        else if (nbrOfTreasures>0){
            return "T("+nbrOfTreasures+")"+String.join("", Collections.nCopies(length, " "));
        }
        else return "."+String.join("", Collections.nCopies(length+3, " "));
    }
}
