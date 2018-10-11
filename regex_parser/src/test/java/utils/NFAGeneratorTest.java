package utils;

import domain.NFA;
import domain.OwnSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NFAGeneratorTest {

    NFAGenerator g;
    OwnSet<Character> alphabet;

    public NFAGeneratorTest() {
        alphabet = Utilities.defaultAlphabet();
    }

    @Before
    public void setUp() {
        g = new NFAGenerator(alphabet);
    }

    private NFA complexNFA() {
        NFA result = g.generateNFA("ab*(c|a)*cabba(bb|aa|cc)(ab)*");
        return result;
    }

    @Test
    public void constructorSetsAlphabetCorrectly() {
        assertEquals(alphabet.size(), g.getAlphabet().size());
    }

    @Test
    public void cacheIsEnabledByDefault() {
        assertTrue(g.getCacheEnabled());
    }

    @Test
    public void cacheCanBeDisabledWithConstructor() {
        NFAGenerator genWithoutCache = new NFAGenerator(alphabet, false);
        assertFalse(genWithoutCache.getCacheEnabled());
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
        NFAGenerator withoutCache = new NFAGenerator(Utilities.defaultAlphabet(), false);
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
        assertTrue(!nfa.accepts("abbbcabb*"));
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
        assertFalse(nfa.accepts(""));
    }

    @Test
    public void generateNFAFromSimpleUnionDoesNotAcceptSimpleConcatenation() {
        NFA nfa = g.generateNFA("a|b");
        assertFalse(nfa.accepts("ab"));
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
        assertFalse(nfa.accepts("bb"));
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
    public void generateNFAFromCharacterAndQuestionmarkAcceptsEpsilon() {
        NFA nfa = g.generateNFA("a?");
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateNFAFromCharacterAndQuestionmarkAcceptsSameCharacter() {
        NFA nfa = g.generateNFA("a?");
        assertTrue(nfa.accepts("a"));
    }

    @Test
    public void generateNFAFromCharacterAndQuestionmarkDoesNotAcceptTwoRepetitions() {
        NFA nfa = g.generateNFA("a?");
        assertTrue(!nfa.accepts("aa"));
    }

    @Test
    public void generateNFAFromCharacterAndQuestionmarkDoesNotAcceptDifferentCharacter() {
        NFA nfa = g.generateNFA("a?");
        assertTrue(!nfa.accepts("b"));
    }

    @Test
    public void generateNFAFromParenthesesAndQuestionmarkAcceptsParenthesizedPart() {
        NFA nfa = g.generateNFA("(abba)?");
        assertTrue(nfa.accepts("abba"));
    }

    @Test
    public void generateNFAFromParenthesesAndQuestionmarkAcceptsEmptyString() {
        NFA nfa = g.generateNFA("(abba)?");
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateNFAFromParenthesesAndQuestionmarkDoesNotAcceptMultipleRepetitions() {
        NFA nfa = g.generateNFA("(abba)?");
        assertTrue(!nfa.accepts("abbaabba"));
    }

    @Test
    public void generateNFAFromCharacterAndPlusDoesNotAcceptEpsilon() {
        NFA nfa = g.generateNFA("a+");
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateNFAFromCharacterAndPlusAcceptsSameCharacter() {
        NFA nfa = g.generateNFA("a+");
        assertTrue(nfa.accepts("a"));
    }

    @Test
    public void generateNFAFromParenthesesAndPlusAcceptsSameParenthesizedPart() {
        NFA nfa = g.generateNFA("(abba)+");
        assertTrue(nfa.accepts("abba"));
    }

    @Test
    public void generateNFAFromParenthesesAndPlusDoesNotAcceptEmptyString() {
        NFA nfa = g.generateNFA("(abba)+");
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateNFAFromParenthesesAndPlusAcceptsMultipleRepetitions() {
        NFA nfa = g.generateNFA("(abba)+");
        assertTrue(nfa.accepts("abbaabbaabba"));
    }

    @Test
    public void generateNFAFromCharacterAndPlusAcceptsTwoCharacters() {
        NFA nfa = g.generateNFA("a+");
        assertTrue(nfa.accepts("aa"));
    }

    @Test
    public void generateNFAFromCharacterAndPlusDoesNotAccceptDifferentCharacter() {
        NFA nfa = g.generateNFA("a+");
        assertTrue(!nfa.accepts("b"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithBothLimitsPresentAcceptsAnyOfTheOptions() {
        NFA nfa = g.generateNFA("a[3,5]");
        assertTrue(nfa.accepts("aaa") && nfa.accepts("aaaa") && nfa.accepts("aaaaa"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithBothLimitsPresentDoesnotAcceptTooFewRepetitions() {
        NFA nfa = g.generateNFA("a[3,5]");
        assertTrue(!nfa.accepts("aa"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithBothLimitsPresentDoesNotAcceptToomanyRepetitions() {
        NFA nfa = g.generateNFA("a[3,5]");
        assertTrue(!nfa.accepts("aaaaaaa"));
    }

    @Test
    public void generateNFAFromParenthesesAndRepetitionsWithBothLimitsAcceptsSuitableNumberOfRepetitions() {
        NFA nfa = g.generateNFA("(ac)[3,5]");
        assertTrue(nfa.accepts("acacac") && nfa.accepts("acacacac") && nfa.accepts("acacacacac"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionWithoutMinimunAcceptsEmptyString() {
        NFA nfa = g.generateNFA("a[,4]");
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionWithoutMinimumDoesNotAcceptTooManyRepetitions() {
        NFA nfa = g.generateNFA("a[,4]");
        assertTrue(!nfa.accepts("aaaaaaa"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithoutMaximumDoesNotAcceptTooFewRepetitions() {
        NFA nfa = g.generateNFA("a[3,]");
        assertTrue(!nfa.accepts("aa"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithoutMaximumAcceptsMinimumRepetitions() {
        NFA nfa = g.generateNFA("a[3,]");
        assertTrue(nfa.accepts("aaa"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithoutMaximumAcceptsVeryManyRepetitions() {
        NFA nfa = g.generateNFA("a[3,]");
        assertTrue(nfa.accepts("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithoutLimitsAcceptsEmptyString() {
        NFA nfa = g.generateNFA("a[,]");
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithoutLimitationsAcceptsTwoRepetitions() {
        NFA nfa = g.generateNFA("a[,]");
        assertTrue(nfa.accepts("aa"));
    }

    @Test
    public void generateNFAFromCharacterAndRepetitionsWithoutLimitationsAcceptsVeryManyRepetitions() {
        NFA nfa = g.generateNFA("a[,]");
        assertTrue(nfa.accepts("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void generateNFAFromMultipleChoiceAcceptsEveryChoice() {
        NFA nfa = g.generateNFA("a-d");
        assertTrue(nfa.accepts("a") && nfa.accepts("b") && nfa.accepts("c") && nfa.accepts("d"));
    }

    @Test
    public void generateNFAFromMultipleChoiceDoesNotAcceptEmptyString() {
        NFA nfa = g.generateNFA("a-d");
        assertTrue(!nfa.accepts(""));
    }

    @Test
    public void generateNFAFromMultipleChoiceDoesNotAcceptNonchoiceCharacter() {
        NFA nfa = g.generateNFA("a-d");
        assertTrue(!nfa.accepts("k"));
    }

    @Test
    public void generateNFAFromMultipleChoiceWorksWithOneDigitNumbers() {
        NFA nfa = g.generateNFA("4-7");
        assertTrue(nfa.accepts("4") && nfa.accepts("5") && nfa.accepts("6") && nfa.accepts("7"));
    }

    @Test
    public void generateNFAFromNegationProducesNFAThatAcceptsNegation() {
        NFA nfa = g.generateNFA("!a");
        assertTrue(nfa.accepts("c") && nfa.accepts("ilmava") && nfa.accepts(""));
    }

    @Test
    public void generateNFAFromNegationProducesNFAThatDoesNotAcceptOriginal() {
        NFA nfa = g.generateNFA("!a");
        assertTrue(!nfa.accepts("a"));
    }

    @Test
    public void generateNFAWorksWithNegationOfParentheses() {
        NFA nfa = g.generateNFA("!(ab)");
        assertTrue(!nfa.accepts("ab") && nfa.accepts("aa") && nfa.accepts("a") && nfa.accepts("carne"));
    }

    @Test
    public void generateNFAWorksWithNegationOfUnion() {
        NFA nfa = g.generateNFA("!(a|b)");
        assertTrue(!nfa.accepts("a") && !nfa.accepts("b") && nfa.accepts("ilves"));
    }

    @Test
    public void generateNFAWorksWithComplementInComplexCase1() {
        NFA nfa = g.generateNFA("(a|cd*)!(a*)");
        assertTrue(!nfa.accepts("aa") && nfa.accepts("aba") && nfa.accepts("cruntti"));
    }

    @Test
    public void generateNFAWorksWithComplementInComplexCase2() {

        NFA nfa = g.generateNFA("!(u|(a!f))");
        assertTrue(!nfa.accepts("ac") && !nfa.accepts("u") && nfa.accepts("af") && nfa.accepts("remps"));
    }

    @Test
    public void generateNFAGeneratesNFAThatAcceptsCorrectlyWithLongPattern1() {
        NFA nfa = g.generateNFA("(a|b)[3,5]C(1-9)(a-z)(A-Z)!#");
        assertTrue(nfa.accepts("abbaaC6kPf"));
    }

    @Test
    public void generateNFAGeneratesNFAThatAcceptsCorrectlyWithLongPattern2() {
        NFA nfa = g.generateNFA("(a|b)[3,5]C(1-9)(a-z)(A-Z)!#");
        assertTrue(nfa.accepts("aaaC1zLlongstringattheend123"));

    }

    @Test
    public void generateNFAGeneratesNFAThatAcceptsCorrectlyWithLongPattern3() {
        NFA nfa = g.generateNFA("(car)+W?!(nafta|#)");
        assertTrue(nfa.accepts("carcarcarcarWNAFTA"));
    }

    @Test
    public void generateNFAGeneratesNFAThatAcceptsCorrectlyWithLongPattern4() {
        NFA nfa = g.generateNFA("(car)+W?!(nafta|#)");
        assertTrue(nfa.accepts("cars"));
    }

    @Test
    public void generateNFAGeneratesNFAThatDoesNotAcceptNonmatchingStringWithLongPattern1() {
        NFA nfa = g.generateNFA("(a|b)[3,5]C(1-9)(a-z)(a-Z)!#");
        assertFalse(nfa.accepts("abaC6kP"));
    }

    @Test
    public void generateNFAGeneratesNFAThatDoesNotAcceptNonmatchingStringWithLongPattern2() {
        NFA nfa = g.generateNFA("(a|b)[3,5]C(1-9)(a-z)(a-Z)!#");
        assertFalse(nfa.accepts("bbC3yKnipsu"));
    }

    @Test
    public void generateNFAGeneratesNFAThatDoesNotAcceptNonmatchingStringWithLongPattern3() {
        NFA nfa = g.generateNFA("(car)+W?!(nafta|#)");
        assertFalse(nfa.accepts("napsta"));
    }

    @Test
    public void generateNFAGeneratesNFAThatDoesNotAcceptNonmatchingStringWithLongPattern4() {
        NFA nfa = g.generateNFA("(car)?W+!(nafta|#)");
        assertFalse(nfa.accepts("carW"));
    }

    @Test
    public void generatesNFAFromSingleLiteralShorthandSymbolAcceptsSameSymbol() {
        NFA nfa = g.generateNFA("/?");
        assertTrue(nfa.accepts("?"));
    }

    @Test
    public void generateNFAFromSingleLiteralKleeneStarAcceptsStar() {
        NFA nfa = g.generateNFA("/*");
        assertTrue(nfa.accepts("*"));
    }

    @Test
    public void generateNFAFromUnionOfTwoLiteralSpecialSymbolsAcceptsEither() {
        NFA nfa = g.generateNFA("/||/+");
        assertTrue(nfa.accepts("|") && nfa.accepts("+"));
    }

    @Test
    public void generateNFAFromConcatenationOfMultipleLiteralSpecialSymbolsAcceptsCorrectString() {
        NFA nfa = g.generateNFA("/(/?/)/[/]/!/+/|");
        assertTrue(nfa.accepts("(?)[]!+|"));
    }

    @Test
    public void generateNFAFromLiteralSlashAcceptsSlash() {
        NFA nfa = g.generateNFA("//");
        assertTrue(nfa.accepts("/"));
    }

    @Test
    public void generateComplexNFAWithLiteralsAcceptsCorrectly1() {
        NFA nfa = g.generateNFA("ka/](/!|(ab/+ra))[3,4](!(/*))");
        
        assertTrue(nfa.accepts("ka]!ab+ra!!kamy")); 
    }

    @Test
    public void generateComplexNFAWithLiteralsDoesNotAcceptsWrong() {
        NFA nfa = g.generateNFA("ka(]((/!|(ab/+ra)))[3,4](!(/*))");
        assertFalse(nfa.accepts("ka]!ab+ra!*"));
    }


}
