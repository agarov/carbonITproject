import java.util.Collections;

public class Cell {
    private boolean mountainous;
    private int nbrOfTreasures;
    private boolean occupied;
    private Adventurer attachedAdventurer;


    public Cell(){
        this.mountainous=false;
        this.nbrOfTreasures=0;
        this.occupied=false;
    }

    public Adventurer getAttachedAdventurer() {
        return attachedAdventurer;
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

    // Link an adventurer to this cell, if this cell hides one or more treasures give one to the adventurer
    public void attachAdventurer(Adventurer adventurer){
        this.attachedAdventurer=adventurer;
        this.occupied=true;
        if(nbrOfTreasures>0){
            nbrOfTreasures--;
            attachedAdventurer.addTreasure();
            System.out.println(attachedAdventurer.getName()+" found a treasure !");
        }
    }
    //Unlink the adventurer and the cell
    public void detachAdventurer(){
        this.attachedAdventurer=null;
        this.occupied=false;
    }
    /*
    Display the cell following this pattern and order :
    Adventurer -> A(Name)
    Mountain -> M
    Treasure(s) -> T(Nbr of treasures)
    Else -> .
    Some whitespaces are added to match the displaying of the player with the longer name
     */
    public String displayCell(int length) {
       if(isOccupied()){
            return "A("+attachedAdventurer.getName()+")"+String.join("", Collections.nCopies(length-attachedAdventurer.getName().length()+1, " "));
        }
        else if(isMountainous()){
            return "M"+String.join("", Collections.nCopies(length+3, " "));
        }
        else if (nbrOfTreasures>0){
            return "T("+nbrOfTreasures+")"+String.join("", Collections.nCopies(length, " "));
        }
        else return "."+String.join("", Collections.nCopies(length+3, " "));
    }
}
