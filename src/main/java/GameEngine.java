import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class GameEngine {
    private Cell[][] map;
    private int horizontalLength;
    private int verticalLength;
    private int longerNameLength;
    private ArrayList<Adventurer> adventurers;


    public GameEngine(){
        this.adventurers = new ArrayList<>();
        this.longerNameLength=0;
    };

    public void parseData(String fileName){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String line = br.readLine().trim();
                while(line.substring(0,1).equals("#")){
                    line = br.readLine().trim();
                }
                line = line.replaceAll("\\s+","");
                String[] separated = line.split("-");
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
                line = br.readLine();
                while(line != null){
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
                                Adventurer adventurer = new Adventurer(adventurerName,x,y,orientation,separated[5]);
                                this.adventurers.add(adventurer);
                                map[y][x].setOccupied(true);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + startingLetter);
                        }
                    }
                    line= br.readLine();
                }

            } finally {
                br.close();
            }
        }catch (Exception e){
            e.printStackTrace();
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
                if(map[y][x].isOccupied()){
                    System.out.print(findAdventurer(x,y).displayAdventurer());
                }
                else {
                    System.out.print(map[y][x].displayCell(this.longerNameLength));
                }
            }
            System.out.print("\n");
        }
    }

    public Adventurer findAdventurer(int x,int y){
        for(Adventurer adventurer : adventurers){
            if((adventurer.getX()==x)&&(adventurer.getY()==y)){
                return adventurer;
            }
        }
        return null;
    }
}
