package ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextUITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TextUI ui; 
    private final int numOfOperations; 

    public TextUITest() {
        numOfOperations = 4; 
    }
    
    private void uiWithInput(String input){
        ui = new TextUI(new Scanner(input));
    }
    
    

   
    @Before
    public  void changeOutStream() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreDefaultOutStream() {
        System.setOut(originalOut);
    }


    @Test
    public void printInstructionsPrintsCorrectInstructions(){
        uiWithInput("");
        ui.printInstructions();
        assertEquals("See README.md or documentation for info on supported operations and symbols)\n", outContent.toString());
    }
    
    
    @Test
    public void chooseOperationDisplaysInstructionsAgainIfInputNotInteger(){
        uiWithInput("jänis\n4\n");
        ui.chooseOperation();
        assertTrue(outContent.toString().contains("Input must be an integer between 1-" + numOfOperations));
    }
    
    @Test
    public void chooseOperationDisplaysInstructionsAgainIfNumberTooSmall(){
        uiWithInput("-7\n4\n");
        ui.chooseOperation();
        assertTrue(outContent.toString().contains("Input must be an integer between 1-" + numOfOperations));
    }
    
    @Test
    public void chooseOperationDoesNotDisplayInstructionsAgainIfNumberInRange(){
        uiWithInput("2\n");
        ui.chooseOperation(); 
        assertFalse(outContent.toString().contains("Input must be an integer between")); 
    }
    
    @Test
    public void chooseOperationReturnsCorrectIntegerWhenInputInRightRange(){
        uiWithInput("2\n");
        assertEquals(2, ui.chooseOperation()); 
    }
    
    @Test
    public void chooseOperationReturnsSecondInputWhenFirstNotInRange(){
        uiWithInput("-1\n1\n");
        assertEquals(1, ui.chooseOperation());
    }
    
    
    @Test
    public void testTrickyPerformanceShowsGuidingMessageWhenInputNotInteger(){
        uiWithInput("rabbit\n1\n");
        ui.testTrickyPerformance();
        assertTrue(outContent.toString().contains("Maximum n must be a positive integer!")); 
    }
    
    @Test
    public void testTrickyPerformanceShowsGuidingMessageWhenInputNegativeInteger(){
        uiWithInput("-3\n1\n");
        ui.testTrickyPerformance();
        assertTrue(outContent.toString().contains("Maximum n must be a positive integer!"));
    }
    
    @Test
    public void testTrickyPerformanceDoesNotShowGuidingMessageWhenInputIsPositiveInteger(){
        uiWithInput("4\n");
        ui.testTrickyPerformance();
        assertFalse(outContent.toString().contains("Maximum n must be a positive integer!"));
    }
    
    // in the process of: writing tests for compareTricky
    @Test
    public void compareTrickyDisplaysValueOfNThatIsTheParameter(){
        uiWithInput("");
        ui.compareTricky(3);
        assertTrue(outContent.toString().contains("n = 3:\n"));
    }
    
    //test run method - the combination of everything
    
    @Test
    public void introductionMessageIsDisplayedWithInstantExit(){
        uiWithInput("4\n");
        ui.run(); 
        assertEquals("Welcome to Regex parser!", outContent.toString().substring(0, 24)); 
    }
    
    @Test
    public void goodbyeMessageIsDisplayedWhenExited(){
        uiWithInput(numOfOperations + "\n");
        ui.run(); 
        assertTrue(outContent.toString().contains("Goodbye!\n"));
    }

}
