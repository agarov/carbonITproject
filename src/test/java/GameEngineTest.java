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
        assertEquals(true,map[0][1].isMountainous());
        assertEquals(true,map[1][2].isMountainous());
        assertEquals(2,map[3][0].getNbrOfTreasures());
        assertEquals(3,map[3][1].getNbrOfTreasures());
        Adventurer adventurer = testEngine.getAdventurers().get(0);
        assertEquals("Lara",adventurer.getName());
        assertEquals(1,adventurer.getX());
        assertEquals(1,adventurer.getY());
        assertEquals(Adventurer.Orientation.S,adventurer.getCurrentOrientation());
        assertEquals("AADADAGGA",adventurer.getRemainingPath());
    }

}
