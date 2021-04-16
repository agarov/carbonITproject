import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
public class CellTest {

    private Cell testCell;

    @BeforeEach
    void initCell(){
        testCell = new Cell();
    }

    @AfterEach
    void nullifyCell(){
        this.testCell = null;
    }

    @Test
    void testAdventurerAttachmentAndTreasureRetrieving(){
        Adventurer adventurer = new Adventurer("Test",0,0, Adventurer.Orientation.S,"AADA");
        testCell.setNbrOfTreasures(2);
        testCell.attachAdventurer(adventurer);
        assertEquals(adventurer,testCell.getAttachedAdventurer());
        assertEquals(true,testCell.isOccupied());
        assertEquals(1, testCell.getNbrOfTreasures());
        assertEquals(1,adventurer.getLootedTreasures());
    }

    @Test
    void testAdventurerDetachment(){
        Adventurer adventurer = new Adventurer("Test",0,0, Adventurer.Orientation.S,"AADA");
        testCell.attachAdventurer(adventurer);
        testCell.detachAdventurer();
        assertEquals(null,testCell.getAttachedAdventurer());
        assertEquals(false,testCell.isOccupied());
    }

    @Test
    void testCellDisplaying(){
        Adventurer adventurer = new Adventurer("Test",0,0, Adventurer.Orientation.S,"AADA");
        testCell.attachAdventurer(adventurer);
        int length = adventurer.getName().length();
        assertEquals("A("+adventurer.getName()+") ",testCell.displayCell(length));
        testCell.detachAdventurer();
        testCell.setMountainous(true);
        assertEquals("M"+String.join("", Collections.nCopies(length+3, " ")),testCell.displayCell(length));
        testCell.setMountainous(false);
        testCell.setNbrOfTreasures(2);
        assertEquals("T("+testCell.getNbrOfTreasures()+")"+String.join("", Collections.nCopies(length, " ")),testCell.displayCell(length));
        testCell.setNbrOfTreasures(0);
        assertEquals("."+String.join("", Collections.nCopies(length+3, " ")), testCell.displayCell(length));
    }

}
