import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;


public class GameEngine {
    private Cell[][] map;
    private int horizontalLength;
    private int verticalLength;
    //Used for displaying
    private int longerNameLength;
    private int nbrOfTurn;
    private ArrayList<Adventurer> adventurers;


    public GameEngine(){
        this.adventurers = new ArrayList<>();
        this.longerNameLength=0;
        this.nbrOfTurn=0;
    }

    public Cell[][] getMap() {
        return map;
    }

    public int getHorizontalLength() {
        return horizontalLength;
    }

    public int getVerticalLength() {
        return verticalLength;
    }

    public int getLongerNameLength() {
        return longerNameLength;
    }

    public int getNbrOfTurn() {
        return nbrOfTurn;
    }

    public ArrayList<Adventurer> getAdventurers() {
        return adventurers;
    }

    // Get input from the txt file and transform it to an ArrayList of String corresponding to the lines of the file
    public LinkedList<String> extractData(String fileName){
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            LinkedList<String> lines = new LinkedList<>();
            String line = br.readLine();
            while(line!=null){
               lines.add(line);
               line = br.readLine();
            }
            br.close();
            return lines;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //Initialize the map depending on the lines retrieved during the data extraction
    public void parseData(LinkedList<String> lines){
        //Remove all the comments
        String firstLine = lines.removeFirst().trim();
        while(firstLine.substring(0,1).equals("#")){
            firstLine = lines.removeFirst().trim();
        }
        //Check this first uncommented line
        firstLine = firstLine.replaceAll("\\s+","");
        String[] separated = firstLine.split("-");
        //If this first uncommented line start with a C, initialize the map with the right lengths
        if (separated[0].equals("C")){
            this.horizontalLength = Integer.parseInt(separated[1]);
            this.verticalLength = Integer.parseInt(separated[2]);
            this.map = new Cell[verticalLength][horizontalLength];
            this.populateMap();
        }
        //Otherwise, impossible to continue
        else{
            System.out.println("Wrong entry file");
            return;
        }
        //Then, for each remaining uncommented lines create the corresponding item on the map
        for(String line : lines){
            line = line.trim();
            String startingLetter = line.substring(0,1);
            if (!startingLetter.equals("#")){
                line = line.replaceAll("\\s+","");
                separated = line.split("-");
                switch (startingLetter){
                    case "M":
                        createMountainFromStrings(separated);
                        break;
                    case "T":
                        createTreasuresFromStrings(separated);
                        break;
                    case "A":
                        createAdventurerFromStrings(separated);
                        break;
                    default:
                        System.out.println("Unexpected value: " + startingLetter);
                        break;
                }
            }
        }
    }
    //Set corresponding cell to mountainous
    public void createMountainFromStrings(String[] separated){
        int x = Integer.parseInt(separated[1]);
        int y = Integer.parseInt(separated[2]);
        map[y][x].setMountainous(true);
    }
    //Add treasure(s) to the corresponding cell
    public void createTreasuresFromStrings(String[] separated){
        int x = Integer.parseInt(separated[1]);
        int y = Integer.parseInt(separated[2]);
        int nbrOfTreasures = Integer.parseInt(separated[3]);
        if(!map[y][x].isMountainous()){
            map[y][x].setNbrOfTreasures(nbrOfTreasures);
        }
        else{
            System.out.println("Impossible to add treasures onto mountainous cell");
        }
    }
    //Create adventurer corresponding to the strings, if the cell is not already occupied
    public void createAdventurerFromStrings(String[] separated){
        int x = Integer.parseInt(separated[2]);
        int y = Integer.parseInt(separated[3]);
        if(map[y][x].isOccupied()){
            System.out.println("Cell already occupied");
            return;
        }
        String adventurerName = separated[1];
        //Used to determine the longer name, useful for displaying
        if(adventurerName.length()>longerNameLength){longerNameLength=adventurerName.length();}
        Adventurer.Orientation orientation = Adventurer.Orientation.valueOf(separated[4]);
        String path = separated[5];
        //Used to determine the number of turns to compute
        if(path.length()>nbrOfTurn){nbrOfTurn=path.length();}
        Adventurer adventurer = new Adventurer(adventurerName,x,y,orientation,path);
        addAdventurer(adventurer);
        map[y][x].attachAdventurer(adventurer);
    }

    public void addAdventurer(Adventurer adventurer){
        adventurers.add(adventurer);
    }

    //Initialize the map with the cells
    public void populateMap(){
        for(int y=0; y<verticalLength; y++)
            for(int x=0; x<horizontalLength; x++)
                this.map[y][x] = new Cell();
    }

    public void visualizeMap(){
        for(int y=0; y<verticalLength; y++) {
            for (int x = 0; x < horizontalLength; x++) {
                System.out.print(map[y][x].displayCell(this.longerNameLength));
            }
            System.out.print("\n");
        }
    }
    //Compute the number of turns needed
    public void run(){
        System.out.println("Initial map : \n");
        visualizeMap();
        for(int i=1;i<=nbrOfTurn;i++){
            System.out.println("\nStart of turn "+i+"\n");
            computeTurn();
        }
    }
    //For each player determine the next action to compute
    public void computeTurn(){
        for(Adventurer adventurer : adventurers){
            switch (adventurer.retrieveAction()){
                case "G":
                    adventurer.turnLeft();
                    System.out.println(adventurer.getName()+" turned to the left, facing now "+adventurer.getCurrentOrientation().toString());
                    break;
                case "D":
                    adventurer.turnRight();
                    System.out.println(adventurer.getName()+" turned to the right, facing now "+adventurer.getCurrentOrientation().toString());
                    break;
                case "A":
                    if(!moveAdventurer(adventurer)){
                        System.out.println("Unattainable case");
                    }
                    break;
                case "":
                    System.out.println("Player already finished all of its actions");
                    break;
                default:
                    System.out.println("Unexpected action character");
                    break;
            }

        }
    }
    //determine the axis to change depending of the adventurer's orientation and if possible move the adventurer to the cell he is facing
    public boolean moveAdventurer(Adventurer adventurer){
        int previousx = adventurer.getX();
        int previousy = adventurer.getY();
        int newx = previousx;
        int newy = previousy;
        switch (adventurer.getCurrentOrientation()) {
            case E:
                newx++;
                break;
            case S:
                newy++;
                break;
            case O:
                newx--;
                break;
            case N:
                newy--;
                break;
        }
        if(attainableCase(newx,newy)){
            System.out.println(adventurer.getName()+" moved from "+previousx+";"+previousy+" to "+newx+";"+newy);
            map[previousy][previousx].detachAdventurer();
            map[newy][newx].attachAdventurer(adventurer);
            adventurer.setX(newx);
            adventurer.setY(newy);
            visualizeMap();
            return true;
        }
        else {return false;}
    }

    public boolean attainableCase(int x,int y){
        return (x>=0)&&(y>=0)&&(y<verticalLength)&&(x<horizontalLength)&&(!map[y][x].isOccupied())&&(!map[y][x].isMountainous());
    }


    //Generate an output to a text file by scanning each cell looking for specific item
    public void generateOutput(){
        try {
            File fout = new File("out.txt");
            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write("C - "+horizontalLength+" - "+verticalLength);
            bw.newLine();
            for(int y=0; y<verticalLength; y++) {
                for (int x = 0; x < horizontalLength; x++) {
                    if(map[y][x].isMountainous()){
                        bw.write("M - "+x+" - "+y);
                        bw.newLine();
                    }
                }
            }
            for(int y=0; y<verticalLength; y++) {
                for (int x = 0; x < horizontalLength; x++) {
                    if(map[y][x].getNbrOfTreasures()>0){
                        bw.write("T - "+x+" - "+y+" - "+map[y][x].getNbrOfTreasures());
                        bw.newLine();
                    }
                }
            }
            for(Adventurer adventurer : adventurers){
                bw.write("A - "+adventurer.getName()+" - "+adventurer.getX()+" - "+adventurer.getY()+" - "+adventurer.getCurrentOrientation()+" - "+adventurer.getLootedTreasures());
                bw.newLine();
            }
            bw.close();
        }catch (Exception e ){
            e.printStackTrace();
        }

    }



}
