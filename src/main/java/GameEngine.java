import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;


public class GameEngine {
    private Cell[][] map;
    private int horizontalLength;
    private int verticalLength;
    private int longerNameLength;
    private int nbrOfTurn;
    private ArrayList<Adventurer> adventurers;


    public GameEngine(){
        this.adventurers = new ArrayList<>();
        this.longerNameLength=0;
        this.nbrOfTurn=0;
    }

    public LinkedList<String> extractData(String fileName){
        try {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
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

    public void parseData(LinkedList<String> lines){
        String firstLine = lines.removeFirst().trim();
        while(firstLine.substring(0,1).equals("#")){
            firstLine = lines.removeFirst().trim();
        }
        firstLine = firstLine.replaceAll("\\s+","");
        String[] separated = firstLine.split("-");
        if (separated[0].equals("C")){
            this.horizontalLength = Integer.parseInt(separated[1]);
            this.verticalLength = Integer.parseInt(separated[2]);
            this.map = new Cell[verticalLength][horizontalLength];
            this.populateMap();
        }
        else{
            System.out.println("Wrong entry file");
            return;
        }
        for(String line : lines){
            line = line.trim();
            String startingLetter = line.substring(0,1);
            if (!startingLetter.equals("#")){
                line = line.replaceAll("\\s+","");
                separated = line.split("-");
                int x;
                int y;
                switch (startingLetter){
                    case "M":
                        x = Integer.parseInt(separated[1]);
                        y = Integer.parseInt(separated[2]);
                        map[y][x].setMountainous(true);
                        break;
                    case "T":
                        x = Integer.parseInt(separated[1]);
                        y = Integer.parseInt(separated[2]);
                        int nbrOfTreasures = Integer.parseInt(separated[3]);
                        map[y][x].setNbrOfTreasures(nbrOfTreasures);
                        break;
                    case "A":
                        String adventurerName = separated[1];
                        if(adventurerName.length()>longerNameLength){longerNameLength=adventurerName.length();}
                        x = Integer.parseInt(separated[2]);
                        y = Integer.parseInt(separated[3]);
                        Adventurer.Orientation orientation = Adventurer.Orientation.valueOf(separated[4]);
                        String path = separated[5];
                        if(path.length()>nbrOfTurn){nbrOfTurn=path.length();}
                        Adventurer adventurer = new Adventurer(adventurerName,x,y,orientation,path);
                        this.adventurers.add(adventurer);
                        map[y][x].attachAdventurer(adventurer);
                        break;
                    default:
                        System.out.println("Unexpected value: " + startingLetter);
                        break;
                }
            }
        }
    }

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

    public void run(){
        System.out.println("Initial map : \n");
        visualizeMap();
        for(int i=1;i<=nbrOfTurn;i++){
            System.out.println("\nStart of turn "+i+"\n");
            computeTurn();
        }
    }

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
                    }
                    else{
                        System.out.println("Unattainable case");
                    }
                    break;
                default:
                    System.out.println("Unexpected action character");
            }

        }
    }

    public boolean attainableCase(int x,int y){
        return (x>=0)&&(y>=0)&&(y<verticalLength)&&(x<horizontalLength)&&(!map[y][x].isOccupied())&&(!map[y][x].isMountainous());
    }



}
