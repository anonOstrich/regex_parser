package domain;

import utils.structures.OwnSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class NFATest {

    public NFATest() {
    }

    private NFA simpleUnionNFA(char firstOption, char secondOption) {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        State s4 = new State(4);
        State s5 = new State(5);
        s0.addStatesReachableWithoutSymbols(s1);
        s0.addStatesReachableWithoutSymbols(s2);
        s1.addNextStateForSymbol(firstOption, s3);
        s2.addNextStateForSymbol(secondOption, s4);
        s3.addStatesReachableWithoutSymbols(s5);
        s4.addStatesReachableWithoutSymbols(s5);
        OwnSet<State> accepting = new OwnSet();
        accepting.add(s5);
        return new NFA(s0, accepting);
    }

    private NFA simpleKleeneStarNFA(char symbol) {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        s0.addStatesReachableWithoutSymbols(s1);
        s0.addStatesReachableWithoutSymbols(s3);
        s1.addNextStateForSymbol(symbol, s2);
        s2.addStatesReachableWithoutSymbols(s1);
        s2.addStatesReachableWithoutSymbols(s3);
        OwnSet<State> accepting = new OwnSet();
        accepting.add(s3);
        return new NFA(s0, accepting);
    }

    private NFA simpleConcatenationNFA(char firstOption, char secondOption) {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);

        s0.addNextStateForSymbol(firstOption, s1);
        s1.addStatesReachableWithoutSymbols(s2);
        s2.addNextStateForSymbol(secondOption, s3);
        OwnSet<State> accepting = new OwnSet();
        accepting.add(s3);

        return new NFA(s0, accepting);
    }

    @Test
    public void constructorSetsStartingStateCorrectly() {
        State s = new State(1);
        NFA nfa = new NFA(s, new OwnSet());
        assertEquals(s, nfa.getStartingState());
    }

    @Test
    public void constructorSetsAcceptingStatesCorrecty() {
        OwnSet<State> states = new OwnSet();
        states.add(new State(1));
        states.add(new State(2));
        NFA nfa = new NFA(new State(0), states);
        assertEquals(states, nfa.getAcceptingStates());
    }

    @Test
    public void acceptsReturnsTrueWhenThereIsOnlyOneStateAndInputIsEmpty() {
        OwnSet<State> accepting = new OwnSet();
        accepting.add(new State(0));
        NFA nfa = new NFA(new State(0), accepting);
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void acceptsReturnsFalseWhenThereIsOnlyOneStateAndInputIsNotEmpty() {
        OwnSet<State> accepting = new OwnSet();
        accepting.add(new State(0));
        NFA nfa = new NFA(new State(0), accepting);
        assertTrue(!nfa.accepts("1"));
    }

    @Test
    public void acceptReturnsTrueWithASimpleNFAAndMatchingInputString() {
        State s = new State(0);
        s.setNextStateForSymbol('0', new State(1));
        OwnSet<State> accepting = new OwnSet();
        accepting.add(new State(1));
        NFA nfa = new NFA(s, accepting);
        assertTrue(nfa.accepts("0"));
    }

    @Test
    public void acceptReturnsFalseWithASimpleNFAAndNonmatchingInputString() {
        State s = new State(0);
        s.setNextStateForSymbol('1', new State(1));
        OwnSet<State> accepting = new OwnSet();
        accepting.add(new State(1));
        NFA nfa = new NFA(s, accepting);
        assertTrue(!nfa.accepts("0"));
    }

    @Test
    public void acceptReturnsTrueWithUnionWithMatchingString() {
        NFA unionNFA = simpleUnionNFA('0', '1');
        assertTrue(unionNFA.accepts("0") && unionNFA.accepts("1"));
    }


    @Test
    public void acceptReturnsFalseWithUnionWithNonmatchingString() {
        NFA unionNFA = simpleUnionNFA('0', '1');
        assertFalse(unionNFA.accepts("01"));
    }

    @Test
    public void acceptReturnsTrueWithKeeneStarWithMatchingString() {
        NFA keene_nfa = simpleKleeneStarNFA('a');
        assertTrue(keene_nfa.accepts("") && keene_nfa.accepts("a") && keene_nfa.accepts("aaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void acceptReturnsFalseWithKeeneStarWithNonmatchingString() {
        NFA keene_nfa = simpleKleeneStarNFA('a');
        assertFalse(keene_nfa.accepts("b"));
    }

    @Test
    public void acceptReturnsTrueWithConcatenationAndMatchingString() {
        NFA nfa = simpleConcatenationNFA('a', 'b');
        assertTrue(nfa.accepts("ab"));
    }

    @Test
    public void acceptReturnsFalseWithConcatenationAndEmptyString() {
        NFA nfa = simpleConcatenationNFA('a', 'b');
        assertFalse(nfa.accepts(""));
    }

    @Test
    public void acceptReturnsFalseWithConcatenationAndNonMatchingString() {
        NFA nfa = simpleConcatenationNFA('a', 'b');
        assertFalse(nfa.accepts("fu"));
    }

    @Test
    public void NFAIsNotDFAByDefault() {
        NFA nfa = new NFA();
        assertFalse(nfa.isDFA());
    }

    @Test
    public void NFACanBeSetToIdentifyAsDFA() {
        NFA nfa = new NFA();
        nfa.setIsDFA(true);
        assertTrue(nfa.isDFA());
    }

    @Test
    public void containsAcceptingStateReturnsTrueWhenOneStateIsAcceptingAndNFANotInverted() {
        State s0 = new State(0);
        OwnSet<State> accepting = new OwnSet();
        accepting.add(s0);
        OwnSet<State> set = new OwnSet();
        set.add(s0);
        NFA nfa = new NFA(s0, accepting);
        assertTrue(nfa.containsAcceptingState(set));

    }

    @Test
    public void containsAcceptingStateReturnsFalseWhenNoStatesAreAcceptingAndNFANotInverted() {
        State s0 = new State(0);
        State s1 = new State(1);
        s0.addNextStateForSymbol('a', s1);
        OwnSet<State> accepting = new OwnSet();
        accepting.add(s0);
        OwnSet<State> set = new OwnSet();
        set.add(s1);
        NFA nfa = new NFA(s0, accepting);
        assertFalse(nfa.containsAcceptingState(set));
    }

    @Test
    public void containsAcceptingStateReturnsFalseWhenOneStateIsAcceptingAndNFAIsInverted() {
        State s0 = new State(0);
        OwnSet<State> accepting = new OwnSet();
        accepting.add(s0);
        OwnSet<State> set = new OwnSet();
        set.add(s0);
        NFA nfa = new NFA(s0, accepting);
        nfa.invert();
        assertFalse(nfa.containsAcceptingState(set));
    }

    @Test
    public void containsAcceptingStateReturnsTrueWhenNoStatesAreAcceptingAndNFAIsInverted() {
        State s0 = new State(0);
        State s1 = new State(1);
        s0.addNextStateForSymbol('a', s1);
        OwnSet<State> accepting = new OwnSet();
        accepting.add(s0);
        OwnSet<State> set = new OwnSet();
        set.add(s1);
        NFA nfa = new NFA(s0, accepting);
        nfa.invert(); 
        assertTrue(nfa.containsAcceptingState(set)); 
    }

    @Test
    public void cachingIsEnabledByDefault() {
        NFA nfa = new NFA();
        assertTrue(nfa.usesCaching());
    }

    @Test
    public void cachingCanBeDisabledWithConstructor() {
        NFA nfa = new NFA(new State(0), new OwnSet(), false, false);
        assertFalse(nfa.usesCaching());
    }

    @Test
    public void sizeOfCacheIsZeroAfterOneRunWhenCacheDisabled() {
        NFA nfa = simpleUnionNFA('a', 'b');
        nfa.disableCaching();
        nfa.accepts("a");
        assertEquals(0, nfa.getCache().size());
    }

    @Test
    public void sizeOfCacheHasBeenIncreadsedAfterOneRunWhenCacheEnabled() {
        NFA nfa = simpleUnionNFA('a', 'b');
        nfa.accepts("a");
        assertTrue(nfa.getCache().size() > 0);

    }

}
