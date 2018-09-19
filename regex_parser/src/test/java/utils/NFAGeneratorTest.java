package utils;

import domain.NFA;
import java.util.Collections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class NFAGeneratorTest {

    NFAGenerator g;
    Set<Character> alphabet;


    public NFAGeneratorTest() {
        alphabet = simpleAlphabet();
    }

    @Before
    public void setUp() {
        g = new NFAGenerator(alphabet);
    }

    private Set<Character> simpleAlphabet() {
        Set<Character> alphabet = new HashSet();
        Collections.addAll(alphabet, 'a', 'b', 'c');
        return alphabet;
    }

    private NFA complexNFA() {
        NFA result = g.generateNFA("ab*(c|a)*cabba(bb|aa|cc)(ab)*");
        return result;
    }

    @Test
    public void constructorSetsAlphabetCorrectly() {
        assertEquals(3, g.getAlphabet().size());
    }

    @Test
    public void cacheIsEnabledByDefault() {
        assertTrue(g.getCacheEnabled());
    }

    @Test
    public void cacheCanBeDisabledWithConstructor() {
        NFAGenerator genWithoutCache = new NFAGenerator(alphabet, false);
        assertTrue(!genWithoutCache.getCacheEnabled());
    }

    @Test
    public void cacheIsEmptyAfterInitialization() {
        assertTrue(g.getCache().isEmpty());
    }

    @Test
    public void cacheContainsNFAAfterGenerationWithCacheEnabled() {
        g.generateNFA("a");
        assertEquals(1, g.getCache().size());
    }

    @Test
    public void cacheIsEmptyAfterGenerationWithCacheDisabled() {
        NFAGenerator withoutCache = new NFAGenerator(simpleAlphabet(), false);
        withoutCache.generateNFA("a");
        
        assertEquals(0, withoutCache.getCache().size());
    }

   

    @Test
    public void generateNFAFromEmptyStringAcceptsEmptyString() {
        NFA nfa = g.generateNFA("");
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateNFAFromEmptyStringDoesNotAcceptShortString() {
        NFA nfa = g.generateNFA("");
        assertTrue(!nfa.accepts("a"));
    }

    @Test
    public void generateNFAFromSingleCharacterAcceptsSameCharacter() {
        NFA nfa = g.generateNFA("a");
        assertTrue(nfa.accepts("a"));
    }

    @Test
    public void generateNFAFromSingleCharacterDoesNotAcceptDifferentCharacter() {
        NFA nfa = g.generateNFA("a");
        assertTrue(!nfa.accepts("b"));
    }

    @Test
    public void generateNFAFromSingleCharacterDoesNotAcceptEmptyString() {
        NFA nfa = g.generateNFA("a");
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateNFAFromSingleCharacterDoesNotAcceptLongString() {
        NFA nfa = g.generateNFA("a");
        assertTrue(!nfa.accepts("a|bbb|cabb*"));
    }

    @Test
    public void generateNFAFromSimpleConcatenationAcceptsSameConcatenation() {
        NFA nfa = g.generateNFA("ac");
        assertTrue(nfa.accepts("ac"));
    }

    @Test
    public void generateNFAFromSimpleConcatenationDoesNotAcceptEmptyString() {
        NFA nfa = g.generateNFA("aa");
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateNFAFromSimpleUnionAcceptsBothStrings() {
        NFA nfa = g.generateNFA("a|b");
        assertTrue(nfa.accepts("a") && nfa.accepts("b"));
    }

    @Test
    public void generateNFAFromSimpleUnionDoesNotAcceptEmptyString() {
        NFA nfa = g.generateNFA("a|b");
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateNFAFromSimpleUnionDoesNotAcceptSimpleConcatenation() {
        NFA nfa = g.generateNFA("a|b");
        assertTrue(!nfa.accepts("ab"));
    }

    @Test
    public void generateNFAFromSimpleKeeneStarAcceptsEmptyString() {
        NFA nfa = g.generateNFA("a*");
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateNFAFromSimpleKeeneStarAcceptsOneRepetition() {
        NFA nfa = g.generateNFA("a*");
        assertTrue(nfa.accepts("a"));
    }

    @Test
    public void generateNFAFromSimpleKeeneStarAcceptsMultipleRepetitions() {
        NFA nfa = g.generateNFA("a*");
        assertTrue(nfa.accepts("aaaaaa"));
    }

    @Test
    public void generateNFAFromSimpleKeeneStarDoesNotAcceptDifferentShortConcatenation() {
        NFA nfa = g.generateNFA("a*");
        assertTrue(!nfa.accepts("bb"));
    }

    @Test
    public void generateNFAFromSimpleParenthesesAcceptsContentBetweenParentheses() {
        NFA nfa = g.generateNFA("(ab)");
        assertTrue(nfa.accepts("ab"));
    }

    @Test
    public void generateNFAFromMultipleNestedParenthesesAcceptsContentInsideInnermostOnes() {
        NFA nfa = g.generateNFA("(((abc)))");
        assertTrue(nfa.accepts("abc"));
    }

    @Test
    public void generateNFAFromMultipleParenthesesChangesOrderOfOperations() {
        NFA nfa1 = g.generateNFA("ab*");
        NFA nfa2 = g.generateNFA("(ab)*");
        assertTrue(!nfa1.accepts("abab") && nfa2.accepts("abab"));
    }

    @Test
    public void generateNFAFromComplexExpressionAcceptsPossibility1() {
        NFA nfa = complexNFA();
        assertTrue(nfa.accepts("acacacabbaaa"));
    }

    @Test
    public void generateNFAFromComplexExpressionAcceptsPossibility2() {
        NFA nfa = complexNFA();
        assertTrue(nfa.accepts("abbbbbbbcabbaccababab"));
    }

    @Test
    public void generateNFAFromComplexExpressionAcceptsPossibility3() {
        NFA nfa = complexNFA();
        assertTrue(nfa.accepts("acabbacc"));
    }
    
    @Test
    public void generateNFAFromCharacterAndQuestionmarkAcceptsEpsilon(){}
    
    @Test
    public void generateNFAFromCharacterAndQuestionmarkAcceptsSameCharacter(){}
    
    @Test
    public void generateNFAFromCharacterAndQuestionmarkDoesNotAcceptTwoRepetitions(){}
    
    @Test
    public void generateNFAFromCharacterAndQuesitonmarkDoesNotAcceptDifferentCharacter(){}
    
    @Test
    public void generateNFAFromCharacterAndPlusDoesNotAcceptEpsilon(){}
    
    @Test
    public void generateNFAFromCharacterAndPlusAcceptsSameCharacter(){}
    
    @Test
    public void generateNFAFromCharacterAndPlusAcceptsTwoCharacters(){}
    
    @Test
    public void generateNFAFromCharacterAndPlusDoesNotAccceptDifferentCharacter(){}
    
    @Test
    public void generateNFAFromCharacterAndRepetitionsWithBothLimitsPresentAcceptsAnyOfTheOptions(){
    
    }
    
    @Test
    public void generateNFAFromCharacterAndRepetitionsWithBothLimitsPresentDoesnotAcceptTooFewRepetitions(){}
    
    @Test
    public void generateNFAFromCharacterAndRepetitionsWithBothLimitsPresentDoesNotAcceptToomanyRepetitions(){}
    
    
    
    
    
    @Test
    public void generateNFAFromNegationProducesNFAThatAcceptsNegation(){
        NFA nfa = g.generateNFA("^a");
        assertTrue(nfa.accepts("c") && nfa.accepts("ilmava") && nfa.accepts(""));
    }
    
    @Test
    public void generateNFAFromNegationProducesNFAThatDoesNotAcceptOriginal(){
        NFA nfa = g.generateNFA("^a");
        assertTrue(!nfa.accepts("a"));
    }
    
    @Test
    public void generateNFAWorksWithNegationOfParentheses(){
        NFA nfa = g.generateNFA("^(ab)");
        assertTrue(!nfa.accepts("ab") && nfa.accepts("aa") && nfa.accepts("a") && nfa.accepts("carne"));        
    }
    
    
    @Test
    public void generateNFAWorksWithNegationOfUnion(){
        NFA nfa = g.generateNFA("^(a|b)");
        assertTrue(!nfa.accepts("a") && !nfa.accepts("b") && nfa.accepts("ilves")); 
    }
    
    @Test
    public void generateNFAWorksWithComplexCase1(){
        NFA nfa = g.generateNFA("(a|cd*)^(a*)");
        assertTrue(!nfa.accepts("aa") && nfa.accepts("aba") && nfa.accepts("runtti"));
    }
    
    @Test
    public void generateNFAWorksWithComplexCase2(){
        NFA nfa = g.generateNFA("^(u|(a^f))"); 
        assertTrue(!nfa.accepts("ac") && !nfa.accepts("u") && nfa.accepts("af") && nfa.accepts("remps")); 
    }
    


}
