
package utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class OwnStringBuilderTest {
    private OwnStringBuilder sb; 
    
    public OwnStringBuilderTest() {
    }
    
    @Before
    public void setUp() {
        sb = new OwnStringBuilder("witchcraft and wizardry");
    }
    
    @Test
    public void toStringReturnsStoredString(){
        assertEquals("witchcraft and wizardry", sb.toString());
    }
    
    @Test
    public void sizeReturnsCorrectsSizeAfterConstructing(){
        assertEquals("witchcraft and wizardry".length(), sb.length());
    }
    
    @Test
    public void charAtReturnsCorrectFirstCharacter(){
        assertEquals('w', sb.charAt(0));
    }
    
    @Test
    public void charAtReturnsCorrectLastCharacter(){
        assertEquals('y', sb.charAt(sb.length() - 1)); 
    }
    
    @Test
    public void deleteCharAtCanDeleteFirstCharacter(){
        sb.deleteCharAt(0);
        assertEquals("itchcraft and wizardry", sb.toString());
    }
    
    @Test
    public void deleteCharAtCanDeleteArbitraryCharacter(){
        sb.deleteCharAt(3); 
        assertEquals("withcraft and wizardry", sb.toString()); 
    }
    
    @Test
    public void deleteDoesNotModifyIfBothArgumentsZero(){
        sb.delete(0,0);
        assertEquals("witchcraft and wizardry", sb.toString());
    }
    
    @Test
    public void deleteDeletesEverythingWithZeroAndLengthAsArguments(){
        sb.delete(0, sb.length());
        assertEquals("", sb.toString());   
    }
    
    @Test
    public void deleteDeletesSpecifiedPartFromTheMiddle(){
        sb.delete(10,14);
        assertEquals("witchcraft wizardry", sb.toString()); 
    }
    
    @Test
    public void insertInsertStringToTheBeginning(){
        sb.insert(0, "s"); 
        assertEquals("switchcraft and wizardry", sb.toString());
    }

    @Test
    public void insertInsertsStringToTheEnd(){
        sb.insert(sb.length(), "."); 
        assertEquals("witchcraft and wizardry.", sb.toString());
    }
    
    @Test
    public void insertInsertsStringToSpecifiedPosition(){
        sb.insert(11, "s");
        assertEquals("witchcraft sand wizardry", sb.toString());
    }
    
    @Test
    public void indexOfReturnsFirstOccurrenceWhenIndexZero(){
        assertEquals(1, sb.indexOf("itch", 0));
    }
    
    @Test
    public void indexOfReturnsSecondOccurrenceWhenIndexIsSuitable(){
        assertEquals(15, sb.indexOf("wi", 7));
    }

    @Test
    public void substringReturnsSpecifiedSubstring(){
        assertEquals("and", sb.substring(11, 14));
    }
    
}
