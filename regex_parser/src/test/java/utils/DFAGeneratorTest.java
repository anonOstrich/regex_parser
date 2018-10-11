package utils;

import domain.NFA;
import domain.OwnSet; 
import org.junit.Test;
import static org.junit.Assert.*;

public class DFAGeneratorTest {

    NFAGenerator nGenerator;
    DFAGenerator dGenerator;

    public DFAGeneratorTest() {
        nGenerator = new NFAGenerator(Utilities.defaultAlphabet());
        dGenerator = new DFAGenerator(-1);
    }

    
    private NFA generateComplementFromPattern(String pattern){
        NFA nfa = nGenerator.generateNFA(pattern);
        return dGenerator.generateComplementDFA(nfa);
    }

    @Test
    public void generateComplementDFAFromSingleCharacterDoesNotAcceptTheCharacter() {
        NFA nfa = generateComplementFromPattern("a");
        assertTrue(!nfa.accepts("a"));
    }

    @Test
    public void generateComplementDFAFromSingleCharacterAcceptsEmptyString() {
        NFA nfa = generateComplementFromPattern("a");
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateComplementDFAFromSingleCharacterAcceptsDifferentCharacter() {
        NFA nfa = generateComplementFromPattern("a");
        assertTrue(nfa.accepts("b"));
    }

    @Test
    public void generateComplementDFAFromSingleCharacterAcceptsLongerStrings() {
        NFA nfa = generateComplementFromPattern("a");
        assertTrue(nfa.accepts("alpakka"));
    }

    @Test
    public void generateComplementDFAFromEmptyStringDoesNotAcceptEmptyString() {
        NFA nfa = generateComplementFromPattern("");
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateComplementDFAFromEmptyStringAcceptsStringOfOneCharacter() {
        NFA nfa = generateComplementFromPattern("");
        assertTrue(nfa.accepts("a"));

    }

    @Test
    public void generateComplementDFAFromEmptyStringAcceptsLongStrings() {
        NFA nfa = generateComplementFromPattern("");
        assertTrue(nfa.accepts("alpaqa"));
    }
    
    @Test
    public void generateComplementDFAFromComplementDFAWorksCorrectly(){
        NFA nfa = nGenerator.generateNFA("(antti)|(rutto)");
        NFA nfa2 = dGenerator.generateComplementDFA(nfa);
        nfa = dGenerator.generateComplementDFA(nfa2);
        assertTrue(nfa.isInverted() && nfa.accepts("antti") && nfa.accepts("rutto"));
    }

}
