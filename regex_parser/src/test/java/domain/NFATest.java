package domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;

public class NFATest {

    public NFATest() {
    }

    @Test
    public void constructorSetsStartingStateCorrectly() {
        State s = new State(1);
        NFA nfa = new NFA(s, new HashSet());
        assertEquals(s, nfa.getStartingState());
    }

    @Test
    public void constructorSetsAcceptingStatesCorrecty() {
        Set<State> states = new HashSet();
        states.add(new State(1));
        states.add(new State(2));
        NFA nfa = new NFA(new State(0), states);
        assertEquals(states, nfa.getAcceptingStates());
    }

    @Test
    public void acceptsReturnsTrueWhenThereIsOnlyOneStateAndInputIsEmpty() {
        Set<State> accepting = new HashSet();
        accepting.add(new State(0));
        NFA nfa = new NFA(new State(0), accepting);
        assertTrue(nfa.accepts(""));
    }

    @Test
    public void acceptsReturnsFalseWhenThereIsOnlyOneStateAndInputIsNotEmpty() {
        Set<State> accepting = new HashSet();
        accepting.add(new State(0));
        NFA nfa = new NFA(new State(0), accepting);
        assertTrue(!nfa.accepts("1"));
    }

    @Test
    public void acceptReturnsTrueWithASimpleNFAAndMatchingInputString() {
        State s = new State(0);
        s.setNextStateForSymbol('0', new State(1));
        Set<State> accepting = new HashSet();
        accepting.add(new State(1));
        NFA nfa = new NFA(s, accepting);
        assertTrue(nfa.accepts("0"));
    }

    @Test
    public void acceptReturnsFalseWithASimpleNFAAndNonmatchingInputString() {
        State s = new State(0);
        s.setNextStateForSymbol('1', new State(1));
        Set<State> accepting = new HashSet();
        accepting.add(new State(1));
        NFA nfa = new NFA(s, accepting);
        assertTrue(!nfa.accepts("0"));
    }

    @Test
    public void acceptReturnsTrueWithUnionWithMatchingString() {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        State s4 = new State(4);
        State s5 = new State(5);
        s0.addNextStateForSymbol('#', s1);
        s0.addNextStateForSymbol('#', s2);
        s1.addNextStateForSymbol('0', s3);
        s2.addNextStateForSymbol('1', s4);
        s3.addNextStateForSymbol('#', s5);
        s4.addNextStateForSymbol('#', s5);
        Set<State> accepting = new HashSet();
        accepting.add(s5);

        NFA unionNFA = new NFA(s0, accepting);
        assertTrue(unionNFA.accepts("0") && unionNFA.accepts("1"));
    }
    
    @Test
    public void acceptReturnsTrueWithUnionWithEmptyStringWhenThatIsOneOption(){}

    @Test
    public void acceptReturnsFalseWithUnionWithNonmatchingString() {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        State s4 = new State(4);
        State s5 = new State(5);
        s0.addNextStateForSymbol('#', s1);
        s0.addNextStateForSymbol('#', s2);
        s1.addNextStateForSymbol('0', s3);
        s2.addNextStateForSymbol('1', s4);
        s3.addNextStateForSymbol('#', s5);
        s4.addNextStateForSymbol('#', s5);
        Set<State> accepting = new HashSet();
        accepting.add(s5);

        NFA unionNFA = new NFA(s0, accepting);
        boolean accepts = unionNFA.accepts("01");
        assertTrue(!accepts);
    }

    @Test
    public void acceptReturnsTrueWithKeeneStarWithMatchingString() {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        s0.addNextStateForSymbol('#', s1);
        s0.addNextStateForSymbol('#', s3);
        s1.addNextStateForSymbol('a', s2);
        s2.addNextStateForSymbol('#', s1);
        s2.addNextStateForSymbol('#', s3);
        Set<State> accepting = new HashSet(); 
        accepting.add(s3);
        NFA keene_nfa = new NFA(s0, accepting);
        assertTrue(keene_nfa.accepts("") && keene_nfa.accepts("a") && keene_nfa.accepts("aaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void acceptReturnsFalseWithKeeneStarWithNonmatchingString() {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        s0.addNextStateForSymbol('#', s1);
        s0.addNextStateForSymbol('#', s3);
        s1.addNextStateForSymbol('a', s2);
        s2.addNextStateForSymbol('#', s1);
        s2.addNextStateForSymbol('#', s3);
        Set<State> accepting = new HashSet(); 
        accepting.add(s3);
        NFA keene_nfa = new NFA(s0, accepting);
        assertTrue(!keene_nfa.accepts("b"));
    }
    
    @Test
    public void acceptReturnsTrueWithConcatenationAndMatchingString(){}
    
    @Test
    public void acceptReturnsFalseWithConcatenationAndEmptyString(){}
    
    @Test
    public void acceptReturnsFalseWithConcatenationAndNonMatchingString(){}
    
    @Test
    public void NFAIsNotDFAByDefault(){}
    
    @Test
    public void NFACanBeSetToIdentifyAsDFA(){}
    
    

}
