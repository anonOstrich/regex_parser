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

    private void uiWithInput(String input) {
        ui = new TextUI(new Scanner(input));
    }

    private void matchExpressionAndStrings(String pattern, String... tests) {
        String start = pattern;
        for (int i = 0; i < tests.length; i++) {
            start += "\n" + tests[i];
        }

        String end = "\n\nn\n4\n";

        uiWithInput(start + "\n" + end);
        ui.matchExpressionsAndStrings();
    }

    @Before
    public void changeOutStream() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreDefaultOutStream() {
        System.setOut(originalOut);
    }

    @Test
    public void printInstructionsPrintsCorrectInstructions() {
        uiWithInput("");
        ui.printInstructions();
        assertEquals("See README.md or documentation for info on supported operations and symbols)\n", outContent.toString());
    }

    @Test
    public void chooseOperationDisplaysInstructionsAgainIfInputNotInteger() {
        uiWithInput("jänis\n4\n");
        ui.chooseOperation();
        assertTrue(outContent.toString().contains("Input must be an integer between 1-" + numOfOperations));
    }

    @Test
    public void chooseOperationDisplaysInstructionsAgainIfNumberTooSmall() {
        uiWithInput("-7\n4\n");
        ui.chooseOperation();
        assertTrue(outContent.toString().contains("Input must be an integer between 1-" + numOfOperations));
    }

    @Test
    public void chooseOperationDoesNotDisplayInstructionsAgainIfNumberInRange() {
        uiWithInput("2\n");
        ui.chooseOperation();
        assertFalse(outContent.toString().contains("Input must be an integer between"));
    }

    @Test
    public void chooseOperationReturnsCorrectIntegerWhenInputInRightRange() {
        uiWithInput("2\n");
        assertEquals(2, ui.chooseOperation());
    }

    @Test
    public void chooseOperationReturnsSecondInputWhenFirstNotInRange() {
        uiWithInput("-1\n1\n");
        assertEquals(1, ui.chooseOperation());
    }

    @Test
    public void testTrickyPerformanceShowsGuidingMessageWhenInputNotInteger() {
        uiWithInput("rabbit\n1\n");
        ui.testTrickyPerformance();
        assertTrue(outContent.toString().contains("Maximum n must be a positive integer!"));
    }

    @Test
    public void testTrickyPerformanceShowsGuidingMessageWhenInputNegativeInteger() {
        uiWithInput("-3\n1\n");
        ui.testTrickyPerformance();
        assertTrue(outContent.toString().contains("Maximum n must be a positive integer!"));
    }

    @Test
    public void testTrickyPerformanceDoesNotShowGuidingMessageWhenInputIsPositiveInteger() {
        uiWithInput("4\n");
        ui.testTrickyPerformance();
        assertFalse(outContent.toString().contains("Maximum n must be a positive integer!"));
    }

    @Test
    public void compareTrickyDisplaysValueOfNThatIsTheParameter() {
        uiWithInput("");
        ui.compareTricky(3);
        assertTrue(outContent.toString().contains("n = 3:\n"));
    }

    @Test
    public void compareTrickyDoesNotDisplayHigherValueForNThanIsGiven() {
        uiWithInput("");
        ui.compareTricky(4);
        assertFalse(outContent.toString().contains("n = 5:"));
    }

    @Test
    public void matchExpressionAndStringsTellsRightlyThatExpressionDoesNotMatch() {
        matchExpressionAndStrings("aaa", "bbb");
        assertTrue(outContent.toString().contains("Regular expression 'aaa' does not match string 'bbb'"));

    }

    @Test
    public void matchExpressionAndStringsTellsRightlyThatExpressionMatches() {
        matchExpressionAndStrings("a|b", "a");
        assertTrue(outContent.toString().contains("Regular expression 'a|b' matches string 'a'"));
    }

    @Test
    public void matchExpressionAndStringsDisplaysSuccessForEveryStringThatMatches() {
        matchExpressionAndStrings("(S|s)?pring(les)?", "Spring", "spring", "Springles", "springles", "pring", "pringles");
        int idx = 0;
        for (int count = 0; count < 6; count++) {
            idx = outContent.toString().indexOf("Regular expression '(S|s)?pring(les)?' matches string '", idx);
            if (idx == -1) {
                assertFalse(true);
                return;
            }
            idx += 10;
        }

        assertTrue(outContent.toString().indexOf("Regular expression '(S|s)?pring(les)?' matches string '", idx) == -1);
    }

    @Test
    public void matchExpressionAndStringsAllowsChangingTheRegularExpression() {
        uiWithInput("!(karjala)\nkarjalla\n\n\ny\n(a-c)[2,5]\nabbaca\n\n\nn\n4\n");
        ui.matchExpressionsAndStrings();
        assertTrue(outContent.toString().contains(
                "Regular expression '!(karjala)' matches string 'karjalla'")
                && outContent.toString().contains("Regular expression '(a-c)[2,5]' does not match string 'abbaca'"));
    }

    @Test
    public void buildStringFromSelectedFieldBuildsFrankensteinWithEmptyInput() {
        uiWithInput("\n");
        String[] info = ui.buildStringFromUserSelectedFile();
        assertEquals("frankenstein.txt", info[0]);
        assertTrue(info[1].length() > 10000);
    }

    @Test
    public void buildStringFromSelectedFieldBuildsFileFromValidFileName() {
        uiWithInput("test.txt\n");
        String[] info = ui.buildStringFromUserSelectedFile();
        assertEquals("test.txt", info[0]);
        assertTrue(info[1].length() < 300);
    }

    @Test
    public void buildStringFromSelectedFieldDoesNotDisplayMessageWhenFileNameValid() {
        uiWithInput("test.txt\n");
        ui.buildStringFromUserSelectedFile();
        assertFalse(outContent.toString().contains("does not exist or some other error occured"));
    }

    @Test
    public void buildStringFromSelectedFieldDispalaysMessageWhenFileNameIsNotValid() {
        uiWithInput("origin_of_species.txt\ntest.txt\n");
        ui.buildStringFromUserSelectedFile();
        assertTrue(outContent.toString().contains("File"
                + " resources/origin_of_species.txt does not exist or some other error occured"));
    }
    
    
    @Test
    public void searchLongTextStatesNonMatchWhenNoPartMatchesInputRegex(){
        uiWithInput("test.txt\nraakile\nn\n4\n"); 
        ui.searchLongText();
        assertTrue(outContent.toString().contains("test.txt does not contain "));
        assertFalse(outContent.toString().contains("text.txt contains "));
    }
    
    @Test
    public void searchLongTextStatesMatchWhenSomePartMatchesInputRegex(){
        uiWithInput("\n(C|c)ountenance\nn\n4\n");
        ui.searchLongText();
        assertTrue(outContent.toString().contains("frankenstein.txt contains ")); 
        assertFalse(outContent.toString().contains("frankenstein.txt does not contain"));
    }
    
    @Test
    public void searchLongTextWorksWithMultipleConsequtiveInputRegexes(){
        uiWithInput("test.txt\nhistoria/:\ny\na[10,15]\nn\n4\n"); 
        ui.searchLongText(); 
        assertTrue(outContent.toString().contains("test.txt contains a part"
           + " that matches the given regular expression 'historia/:'."));
        
        assertTrue(outContent.toString().contains("test.txt does not contain"
        + " a part that matches the given regular expression 'a[10,15]'."));
    }
    
    @Test
    public void searchLongTextWorksWithScands(){
        uiWithInput("test.txt\nä\nn\n4\n");
        ui.searchLongText(); 
        assertTrue(outContent.toString().contains("contains"));
    }

    @Test
    public void introductionMessageIsDisplayedWithInstantExit() {
        uiWithInput("4\n");
        ui.run();
        assertEquals("Welcome to Regex parser!", outContent.toString().substring(0, 24));
    }

    @Test
    public void goodbyeMessageIsDisplayedWhenExited() {
        uiWithInput(numOfOperations + "\n");
        ui.run();
        assertTrue(outContent.toString().contains("Goodbye!\n"));
    }

}
