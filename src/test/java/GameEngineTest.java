import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;

public class GameEngineTest {

    private GameEngine testEngine;

    @BeforeEach
    void initCell(){
        testEngine = new GameEngine();
    }

    @AfterEach
    void nullifyCell(){
        testEngine = null;
    }

    @Test
    void testMountainCreation(){
        String[] separated = {"M","0","0"};
        createEmptyMap4x4();
        testEngine.createMountainFromStrings(separated);
        assertTrue(testEngine.getMap()[0][0].isMountainous());

    }

    @Test
    void testTreasuresCreation(){
        String[] separated = {"T","0","0","3"};
        createEmptyMap4x4();
        testEngine.createTreasuresFromStrings(separated);
        assertEquals(3,testEngine.getMap()[0][0].getNbrOfTreasures());
    }

    @Test
    void testAdventurerCreation(){
        String[] separated = {"A","Lara","1","1","S","AADADAGGA"};
        createEmptyMap4x4();
        testEngine.createAdventurerFromStrings(separated);
        assertTrue(testEngine.getMap()[1][1].isOccupied());
        Adventurer adventurer = testEngine.getMap()[1][1].getAttachedAdventurer();
        assertEquals("Lara",adventurer.getName());
        assertEquals(1,adventurer.getX());
        assertEquals(1,adventurer.getY());
        assertEquals(Adventurer.Orientation.S,adventurer.getCurrentOrientation());
        assertEquals("AADADAGGA",adventurer.getRemainingPath());
    }

    void createEmptyMap4x4(){
        LinkedList<String> initLines = new LinkedList<>();
        initLines.add("C - 4 - 4");
        testEngine.parseData(initLines);
    }

    @Test
    void testParsingExampleOfSubjectWithComments(){
        LinkedList<String> exampleLines = new LinkedList<>();
        exampleLines.add("   #test");
        exampleLines.add("C - 3 - 4");
        exampleLines.add(" M - 1 - 0");
        exampleLines.add("M - 2 - 1");
        exampleLines.add("#test");
        exampleLines.add("T - 0 - 3 - 2");
        exampleLines.add("T - 1 - 3 - 3");
        exampleLines.add("A - Lara - 1 - 1 - S - AADADAGGA");
        testEngine.parseData(exampleLines);
        assertEquals(3,testEngine.getHorizontalLength());
        assertEquals(4,testEngine.getVerticalLength());
        Cell[][] map = testEngine.getMap();
        assertTrue(map[0][1].isMountainous());
        assertTrue(map[1][2].isMountainous());
        assertEquals(2,map[3][0].getNbrOfTreasures());
        assertEquals(3,map[3][1].getNbrOfTreasures());
        Adventurer adventurer = testEngine.getAdventurers().get(0);
        assertEquals("Lara",adventurer.getName());
        assertEquals(1,adventurer.getX());
        assertEquals(1,adventurer.getY());
        assertEquals(Adventurer.Orientation.S,adventurer.getCurrentOrientation());
        assertEquals("AADADAGGA",adventurer.getRemainingPath());
        assertEquals(4,testEngine.getLongerNameLength());
        assertEquals(9,testEngine.getNbrOfTurn());
    }

    @Test
    void testValidMovementAllOrientations(){
        createEmptyMap4x4();
        Adventurer adventurer = new Adventurer("Test",0,0, Adventurer.Orientation.S,"");
        testEngine.addAdventurer(adventurer);
        assertTrue(testEngine.moveAdventurer(adventurer));

        adventurer.turnLeft();
        assertTrue(testEngine.moveAdventurer(adventurer));
        adventurer.turnLeft();
        assertTrue(testEngine.moveAdventurer(adventurer));
        adventurer.turnLeft();
        assertTrue(testEngine.moveAdventurer(adventurer));
        assertEquals(0,adventurer.getX());
        assertEquals(0,adventurer.getY());
    }

    @Test
    void testOutOfMapMovement(){
        createEmptyMap4x4();
        Adventurer adventurer = new Adventurer("Test",0,0, Adventurer.Orientation.O,"");
        assertFalse( testEngine.moveAdventurer(adventurer));
        adventurer = new Adventurer("Test",3,0, Adventurer.Orientation.E,"");
        assertFalse( testEngine.moveAdventurer(adventurer));
        adventurer = new Adventurer("Test",0,0, Adventurer.Orientation.N,"");
        assertFalse( testEngine.moveAdventurer(adventurer));
        adventurer = new Adventurer("Test",3,3, Adventurer.Orientation.S,"");
        assertFalse( testEngine.moveAdventurer(adventurer));
    }

    @Test
    void testBlockedByMountain(){
        createEmptyMap4x4();
        Adventurer adventurer = new Adventurer("Test",0,0, Adventurer.Orientation.S,"");
        testEngine.getMap()[1][0].setMountainous(true);
        assertFalse(testEngine.moveAdventurer(adventurer));
    }

    @Test
    void testAlreadyOccupied(){
        createEmptyMap4x4();
        Adventurer adventurer1 = new Adventurer("Test",0,0, Adventurer.Orientation.S,"");
        Adventurer adventurer2 = new Adventurer("Test",0,1, Adventurer.Orientation.S,"");
        testEngine.getMap()[1][0].attachAdventurer(adventurer2);
        assertFalse(testEngine.moveAdventurer(adventurer1));
    }

    @Test
    void testTurnComputationAllCases(){
        createEmptyMap4x4();
        Adventurer adventurer1 = new Adventurer("Test",0,0, Adventurer.Orientation.E,"A");
        Adventurer adventurer2 = new Adventurer("Test",0,1, Adventurer.Orientation.S,"D");
        Adventurer adventurer3 = new Adventurer("Test",3,3, Adventurer.Orientation.S,"G");
        Adventurer adventurer4 = new Adventurer("Test",2,2, Adventurer.Orientation.S,"");
        testEngine.addAdventurer(adventurer1);
        testEngine.addAdventurer(adventurer2);
        testEngine.addAdventurer(adventurer3);
        testEngine.addAdventurer(adventurer4);
        testEngine.computeTurn();
        assertEquals(1,adventurer1.getX());
        assertEquals(Adventurer.Orientation.O,adventurer2.getCurrentOrientation());
        assertEquals(Adventurer.Orientation.E,adventurer3.getCurrentOrientation());
        assertEquals(2,adventurer4.getX());
        assertEquals(2,adventurer4.getY());
    }

    @Test
    void testCompleteRunOfSubjectExample(){
        LinkedList<String> exampleLines = new LinkedList<>();
        exampleLines.add("   #test");
        exampleLines.add("C - 3 - 4");
        exampleLines.add(" M - 1 - 0");
        exampleLines.add("M - 2 - 1");
        exampleLines.add("#test");
        exampleLines.add("T - 0 - 3 - 2");
        exampleLines.add("T - 1 - 3 - 3");
        exampleLines.add("A - Lara - 1 - 1 - S - AADADAGGA");
        testEngine.parseData(exampleLines);
        testEngine.run();
        assertEquals(3,testEngine.getHorizontalLength());
        assertEquals(4,testEngine.getVerticalLength());
        Cell[][] map = testEngine.getMap();
        assertTrue(map[0][1].isMountainous());
        assertTrue(map[1][2].isMountainous());
        assertEquals(0,map[3][0].getNbrOfTreasures());
        assertEquals(2,map[3][1].getNbrOfTreasures());
        Adventurer adventurer = testEngine.getAdventurers().get(0);
        assertEquals(adventurer,testEngine.getMap()[3][0].getAttachedAdventurer());
        assertEquals("Lara",adventurer.getName());
        assertEquals(0,adventurer.getX());
        assertEquals(3,adventurer.getY());
        assertEquals(Adventurer.Orientation.S,adventurer.getCurrentOrientation());
        assertEquals("",adventurer.getRemainingPath());
        assertEquals(4,testEngine.getLongerNameLength());
        assertEquals(9,testEngine.getNbrOfTurn());
        assertEquals(3,adventurer.getLootedTreasures());

    }










}
