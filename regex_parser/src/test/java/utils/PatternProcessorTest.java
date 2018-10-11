package utils;

import domain.OwnSet; 
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternProcessorTest {

    private final PatternProcessor processor;



    public PatternProcessorTest() {
        processor = new PatternProcessor();
    }

    @Test
    public void constructorSetsAlphabetCorrectly() {
        assertEquals(Utilities.defaultAlphabet(), processor.getAlphabet());
    }

    @Test
    public void constructorSetsShorthandsCorrectly() {
        assertEquals(Utilities.defaultShorthands(), processor.getShorthandSymbols());
    }

    @Test
    public void addConcatenationSymbolsDoesNotModifyEmptyString() {
        assertEquals(processor.addConcatenationSymbols(""), "");
    }

    @Test
    public void addConcatenationSymboslDoesNotModifySingleCharacter() {
        assertEquals(processor.addConcatenationSymbols("A"), "A");
    }

    @Test
    public void addConcatenationSymbolsDoesNotModifyUnionOfTwoCharacters() {
        assertEquals(processor.addConcatenationSymbols("a|b"), "a|b");
    }

    @Test
    public void addConcatenationSymbolsDoesNotModifySimpleKeeneStar() {
        assertEquals(processor.addConcatenationSymbols("a*"), "a*");
    }

    @Test
    public void addConcatenationSymbolsDoesNotModifyLongStringThatContainsNoConcatenation() {
        assertEquals(processor.addConcatenationSymbols("((a|b*)|c|f|9|n*)"), "((a|b*)|c|f|9|n*)");
    }

    @Test
    public void addConcatenationSymbolsAddsSymbolBetweenTwoCharacters() {
        assertEquals(processor.addConcatenationSymbols("ab"), "a&b");
    }

    @Test
    public void addConcatenationSymbolsAddsSymbolBetweenTwoParenthesizedParts() {
        assertEquals(processor.addConcatenationSymbols("(a)(b)"), "(a)&(b)");
    }

    @Test
    public void addConcatenationSymbolsAddsSymbolBetweenStarAndCharacter() {
        assertEquals(processor.addConcatenationSymbols("a*b"), "a*&b");
    }

    @Test
    public void addConcatenationSymbolsAddsSymbolBetweenStarAndParenthesis() {
        assertEquals(processor.addConcatenationSymbols("a*(b)"), "a*&(b)");
    }

    @Test
    public void addConcatenationSymbolsWorksForLongString() {
        assertEquals("(a&b|c*&d)&f", processor.addConcatenationSymbols("(ab|c*d)f"));
    }

    @Test
    public void addConcatenationSymbolsAddsSymbolBetweenCharacterAndNegation() {
        assertEquals("a&!c", processor.addConcatenationSymbols("a!c"));
    }

    @Test
    public void addConcatenationSymbolsAddsSymbolBetweenClosingParenthesisAndnegation() {
        assertEquals("a&!c", processor.addConcatenationSymbols("a!c"));
    }

    @Test
    public void addConcatenationSymbolsAddsSymbolBetweenStarAndNegation() {
        assertEquals(processor.addConcatenationSymbols("a*!c"), "a*&!c");
    }

    @Test
    public void addConcatenationSymbolsDoesNotAddBetweenNegationAndCharacter() {
        assertEquals(processor.addConcatenationSymbols("!c"), "!c");
    }

    @Test
    public void addConcatenationSymbolsDoesNotAddBetweenNegationAndStartingParenthesis() {
        assertEquals(processor.addConcatenationSymbols("!(c|s)"), "!(c|s)");
    }

    @Test
    public void determineAffectedPartReturnsWholeStringForSingleCharacter() {
        assertEquals("a", processor.determineAffectedPart("a", 0));

    }

    @Test
    public void determineAffectedPartReturnsPrecedingSingleCharacterIfNoParentheses() {
        assertEquals("x", processor.determineAffectedPart("abbaxyz", 4));
    }

    @Test
    public void determineAffectedPartReturnsWholeStringIfStringIsParenthisizedAndCharacterIsClosingParenthesis() {
        assertEquals("(juusto)", processor.determineAffectedPart("(juusto)", 7));
    }

    @Test
    public void determineAffectedPartReturnsParenthesesAndContentWhenCharacterIsClosingParenthesis() {
        assertEquals("(cat)", processor.determineAffectedPart("big(cat)seven", 7));
    }

    @Test
    public void determineAffectedPartReturnsOuterParenthesisAndContentWhenNestedParenthesisAndCharacterIsClosingParenthisis() {
        assertEquals("(cid(soul)|(l(in)))", processor.determineAffectedPart("aa(cid(soul)|(l(in)))bb", 20));
    }

    @Test
    public void elongateRegularExpressionTurnsEmptyStringToEmptyCharacter() {
        assertEquals("#", processor.elongateRegularExpression(""));
    }

    @Test
    public void elongateRegularExpressionDoesnotModifySingleCharacter() {
        assertEquals("a", processor.replaceShorthands("a"));
    }

    @Test
    public void replaceShorthandsWorksWithSimpleQuestionMark() {
        assertEquals("(a|#)", processor.replaceShorthands("a?"));
    }

    @Test
    public void replaceShorthandsWorksWithParenthesisQuestionMark() {
        assertEquals("((a|cc)|#)", processor.replaceShorthands("(a|cc)?"));
    }

    @Test
    public void replaceShorthandsWorksWithSimplePlusSymbol() {
        assertEquals("(aa*)", processor.replaceShorthands("a+"));
    }

    @Test
    public void replaceShorthandsWorksWithParenthesisPlusSymbol() {
        assertEquals("((abc)(abc)*)", processor.replaceShorthands("(abc)+"));
    }

    @Test
    public void replaceShorthandsWorksWithRepetitionWhenBothValuesSpecified() {
        assertEquals("(aaaa|aaa|aa)", processor.replaceShorthands("a[2,4]"));
    }

    @Test
    public void replaceShorthandsWorksWithRepetitionWhenBothValuesDoubleDigits() {
        assertEquals("(aaaaaaaaaaaaaaa|aaaaaaaaaaaaaa)", processor.replaceShorthands("a[14,15]"));
    }

    @Test
    public void replaceShorthandsWorksWithRepetitionWhenOnlyMinimumSpecified() {
        assertEquals("(aaaa*)", processor.replaceShorthands("a[3,]"));
    }

    @Test
    public void replaceShorthandsWorksWithRepetitionWhenOnlyMaximumSpecified() {
        assertEquals("(aaa|aa|a|#)", processor.replaceShorthands("a[,3]"));
    }

    @Test
    public void replaceShorthandsWorksWithRepetitionWhenBothNumbersSame() {
        assertEquals("(aa)", processor.replaceShorthands("a[2,2]"));
    }

    @Test
    public void replaceShorthandsWorksWithMultipleChoiceWithSameNumberInBothPlaces() {
        assertEquals("(3)", processor.replaceShorthands("3-3"));
    }

    @Test
    public void replaceShorthandsWorksWithMultipleChoiceWithOneDigitNumbers() {
        assertEquals("(7|6|5|4)", processor.replaceShorthands("4-7"));
    }

    @Test
    public void replaceShorthandsWorksWithMultipleChoiceWithLetters() {

        assertEquals("(F|E|D|C|B)", processor.replaceShorthands("B-F"));
    }

    @Test
    public void removeUnnecessaryNegationsDoesNotModifyEmptyString() {
        assertEquals("", processor.removeUnnecessaryNegations(""));
    }

    @Test
    public void removeUnnecessaryNegationsDoesNotModifySingleAlphabetCharacter() {
        assertEquals("a", processor.removeUnnecessaryNegations("a")); 
    }

    @Test
    public void removeUnnecessaryNegationsDoesNotModifySingleNegation() {
        assertEquals("!", processor.removeUnnecessaryNegations("!"));
    }

    @Test
    public void removeUnnecessaryNegationsDoesNotModifyNegationsSeparatedByParentheses() {
        assertEquals("!(!(!(!(!a))))", processor.removeUnnecessaryNegations("!(!(!(!(!a))))")); 
    }

    @Test
    public void removeUnnecessaryNegationsReturnsEmptyStringFromTwoNegations() {
        assertEquals("", processor.removeUnnecessaryNegations("!!"));
    }

    @Test
    public void removeUnnecessaryNegationsRemovesEverythingFromEvenNumberOfNegations() {
        assertEquals("", processor.removeUnnecessaryNegations("!!!!!!!!!!!!!!!!"));
    }

    @Test
    public void removeUnnecessaryNegationsLeavesOneNegationFromOddNumberOfNegations() {
        assertEquals("!", processor.removeUnnecessaryNegations("!!!")); 
    }

    @Test
    public void removeUnnecessaryNegationsRemovesCorrectly1() {
        assertEquals("a(b|c)f+!(abba)", processor.removeUnnecessaryNegations("a!!(b|c)f+!(abba)")); 
    }

    @Test
    public void removeUnnecessaryNegationsRemovesCorrectly2() {
        assertEquals("(kuikka!(b[2,3]))",processor.removeUnnecessaryNegations("!!!!(kuikka!!!(b[2,3]))")); 
    }
    
    @Test
    public void escapeCharacterAndSymbolAreOnlyParenthesized(){
        assertEquals("(/a)", processor.elongateRegularExpression("/a"));
    }
    
    @Test
    public void escapeCharacterPrecedingExclamationProtectsExclamationFromBeingRemoved(){
        assertEquals("(/!)&!a", processor.elongateRegularExpression("/!!a"));
    }
    
    @Test
    public void escapeCharacterPrecedingParenthesesProtectsThemFromBeingInterpretedAsOperationalSymbols(){
        assertEquals("(/()&n&a&k&s&u&(/))*", processor.elongateRegularExpression("/(naksu/)*"));
    }
    
    @Test
    public void fourEscapeCharactersTurnsIntoASingleConcatenation(){
        assertEquals("(//)&(//)", processor.elongateRegularExpression("////"));
    }
    
    @Test
    public void escapapingQuestionMarkWorks(){
        assertEquals("((/?)&(/?)*)", processor.elongateRegularExpression("/?+"));
    }
    
    @Test
    public void escapingPlusMarkWorksWithQuestionMark(){
        assertEquals("((/+)|#)", processor.elongateRegularExpression("/+?"));
    }
    
    @Test
    public void escapingPlusMarkWorksWithRepetition(){
        assertEquals("((/[)&(/[)&(/[)&(/[)&(/[)|(/[)&(/[)&(/[)&(/[)|(/[)&(/[)&(/[))", processor.elongateRegularExpression("/[[3,5]"));
    }
    
    @Test
    public void escapingDifferentOperationsWorksAtSameTime(){
        assertEquals("(/#)&(/])|(/-)", processor.elongateRegularExpression("/#/]|/-"));
    }

    @Test
    public void spaceIsNotModified(){
        assertEquals(" ", processor.elongateRegularExpression(" ")); 
    }
    
    @Test
    public void spaceWorksWithRepetition(){
        assertEquals("( & & | & )", processor.elongateRegularExpression(" [2,3]"));
    }
    
    @Test
    public void spaceIsModifiedCorrectlyAsPartOfLongerString(){
        assertEquals(" &(w|#)&a&y& ", processor.elongateRegularExpression(" w?ay ")); 
    }
    
   

}
