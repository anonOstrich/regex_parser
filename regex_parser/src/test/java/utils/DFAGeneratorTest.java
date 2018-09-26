/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import domain.NFA;
import java.util.Set;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jesper
 */
public class DFAGeneratorTest {

    Set<Character> alphabet;
    NFAGenerator nGenerator;
    DFAGenerator dGenerator;

    public DFAGeneratorTest() {
        alphabet = simpleAlphabet();
        nGenerator = new NFAGenerator(alphabet);
        dGenerator = new DFAGenerator(-1);
    }

    private Set<Character> simpleAlphabet() {
        Set<Character> result = new HashSet();

        for (int i = (int) 'A'; i < (int) 'z'; i++) {
            result.add((char) i);
        }

        for (int i = (int) '0'; i <= (int) '9'; i++) {
            result.add((char) i);
        }

        return result;
    }

    @Test
    public void generateComplementDFAFromSingleCharacterDoesNotAcceptTheCharacter() {
        NFA nfa = nGenerator.generateNFA("a");
        nfa = dGenerator.generateComplementDFA(nfa, alphabet);
        assertTrue(!nfa.accepts("a"));
    }

    @Test
    public void generateComplementDFAFromSingleCharacterAcceptsEmptyString() {
        NFA nfa = nGenerator.generateNFA("a");
        nfa = dGenerator.generateComplementDFA(nfa, alphabet);
        System.out.println(nfa.getAcceptingStates().size());
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateComplementDFAFromSingleCharacterAcceptsDifferentCharacter() {

        NFA nfa = nGenerator.generateNFA("a");
        nfa = dGenerator.generateComplementDFA(nfa, alphabet);
        assertTrue(nfa.accepts("b"));
    }

    @Test
    public void generateComplementDFAFromSingleCharacterAcceptsLongerStrings() {
        NFA nfa = nGenerator.generateNFA("a");
        nfa = dGenerator.generateComplementDFA(nfa, alphabet);
        assertTrue(nfa.accepts("alpakka"));
    }

    @Test
    public void generateComplementDFAFromEmptyStringDoesNotAcceptEmptyString() {
        NFA nfa = nGenerator.generateNFA("");
        nfa = dGenerator.generateComplementDFA(nfa, alphabet);
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateComplementDFAFromEmptyStringAcceptsStringOfOneCharacter() {
        NFA nfa = nGenerator.generateNFA("");
        nfa = dGenerator.generateComplementDFA(nfa, alphabet);
        assertTrue(nfa.accepts("a"));

    }

    @Test
    public void generateComplementDFAFromEmptyStringAcceptsLongStrings() {
        NFA nfa = nGenerator.generateNFA("");
        nfa = dGenerator.generateComplementDFA(nfa, alphabet);
        assertTrue(nfa.accepts("alpaqa"));
    }
    
    @Test
    public void generateComplementDFAFromComplementDFAWorksCorrectly(){
        NFA nfa = nGenerator.generateNFA("(antti)|(rutto)");
        NFA nfa2 = dGenerator.generateComplementDFA(nfa, alphabet);
        nfa = dGenerator.generateComplementDFA(nfa2, alphabet);
        assertTrue(nfa.isInverted() && nfa.accepts("antti") && nfa.accepts("rutto"));
    }

}
