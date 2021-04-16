import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdventurerTest {

    private Adventurer testAdventurer;

    @BeforeEach
    void initAdventurer(){
        this.testAdventurer = new Adventurer("Test",0,0, Adventurer.Orientation.S,"ADA");
    }

    @AfterEach
    void nullifyAdventurer(){
        this.testAdventurer = null;
    }

    @Test
    void testTreasureAddition() {
        int previousNbr = testAdventurer.getLootedTreasures();
        testAdventurer.addTreasure();
        assertEquals(previousNbr+1,testAdventurer.getLootedTreasures());
    }

    @Test
    void testActionRetrieval(){
        String initialPath = testAdventurer.getRemainingPath();
        String letter = testAdventurer.retrieveAction();
        assertEquals(initialPath.substring(0,1),letter);
        assertEquals(initialPath.substring(1),testAdventurer.getRemainingPath());
    }

    @Test
    void testTurningLeft(){
        testAdventurer.turnLeft();
        assertEquals(Adventurer.Orientation.E,testAdventurer.getCurrentOrientation());
        testAdventurer.turnLeft();
        assertEquals(Adventurer.Orientation.N,testAdventurer.getCurrentOrientation());
        testAdventurer.turnLeft();
        assertEquals(Adventurer.Orientation.O,testAdventurer.getCurrentOrientation());
        testAdventurer.turnLeft();
        assertEquals(Adventurer.Orientation.S,testAdventurer.getCurrentOrientation());
    }

    @Test
    void testTurningRight(){
        testAdventurer.turnRight();
        assertEquals(Adventurer.Orientation.O,testAdventurer.getCurrentOrientation());
        testAdventurer.turnRight();
        assertEquals(Adventurer.Orientation.N,testAdventurer.getCurrentOrientation());
        testAdventurer.turnRight();
        assertEquals(Adventurer.Orientation.E,testAdventurer.getCurrentOrientation());
        testAdventurer.turnRight();
        assertEquals(Adventurer.Orientation.S,testAdventurer.getCurrentOrientation());
    }
}
