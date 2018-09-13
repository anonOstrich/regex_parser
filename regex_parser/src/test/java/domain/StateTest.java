package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class StateTest {

    public StateTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private Map<Character, Set<State>> createTransitionExample() {
        Map<Character, Set<State>> result = new HashMap();
        result.put('0', new HashSet());
        result.put('1', new HashSet());
        result.put('#', new HashSet());

        result.get('0').add(new State(2));
        result.get('#').add(new State(3));
        result.get('#').add(new State(4));

        return result;
    }

    @Test
    public void idIsSetCorrectlyBySimpleConstructor() {
        State s = new State(3);
        assertEquals(3, s.getId());
    }

    @Test
    public void idIsSetCorrectlyByComplexConstructor() {
        Map<Character, Set<State>> data = new HashMap();

        State s = new State(3, data);
        assertEquals(3, s.getId());
    }

    @Test
    public void transitionsAreSetCorrectlyBySimpleConstructor() {
        State s = new State(1);
        assertEquals(new HashMap(), s.getAllTransitions());
    }

    @Test
    public void transitionsAreSetCorrectlyByComplexConstructor() {
        Map<Character, Set<State>> transitions = createTransitionExample();
        State s = new State(3, transitions);
        assertEquals(transitions, s.getAllTransitions());
    }

    @Test
    public void statesAreNotEqualWithDifferentIDs() {
        State s1 = new State(1);
        State s2 = new State(2);

        assertFalse(s1.equals(s2));
    }

    @Test
    public void stateIsNotEqualToNull() {
        State s1 = new State(2);

        assertFalse(s1.equals(null));
    }

    @Test
    public void stateIsNotEqualToAnotherClassObject() {
        State s1 = new State(1);
        assertFalse(s1.equals("Object of class String"));
    }

    @Test
    public void statesAreEqualWhenSameIdButDifferentTransitions() {
        State s1 = new State(1);
        Map<Character, Set<State>> transitions = createTransitionExample();
        State s2 = new State(1, transitions);
        assertEquals(s1, s2);
    }

    @Test
    public void setTransitionsAddsTransitions() {

        State s = new State(1);
        s.setTransitions(createTransitionExample());
        assertEquals(s.getAllTransitions(), createTransitionExample());
    }

    @Test
    public void setTransitionsDiscardsExistingTransitions() {
        State s = new State(1, createTransitionExample());
        s.setTransitions(new HashMap());
        assertEquals(new HashMap(), s.getAllTransitions());
    }

    @Test
    public void addTransitionsAddsTransitions() {
        State s = new State(1);
        s.setTransitions(createTransitionExample());
        assertEquals(s.getAllTransitions(), createTransitionExample());
    }

    @Test
    public void addTransitionsKeepsExistingTransitions() {
        Map<Character, Set<State>> existingTransitions = new HashMap();
        existingTransitions.put('A', new HashSet());
        existingTransitions.get('A').add(new State(10));
        State s = new State(1, existingTransitions);
        s.addTransitions(createTransitionExample());
        assertTrue(s.getAllTransitions().containsKey('A'));

    }

    @Test
    public void getNextStatesForSymbolWorksWhenThereAreNextStates() {
        State s = new State(1, createTransitionExample());
        Set<State> expected = new HashSet();
        expected.add(new State(3));
        expected.add(new State(4));
        assertEquals(expected, s.getNextStatesForSymbol('#'));
    }

    @Test
    public void getNextStatesForSymbolReturnsEmptySetWhenNoNextStates() {

        State s = new State(1, createTransitionExample());
        assertTrue(s.getNextStatesForSymbol('1').isEmpty());
    }
    
    @Test
    public void getNextStatesReturnsEmptySetWhenNoSymbolAsKeyAtAll(){
        State s = new State(1); 
        assertEquals(new HashSet(), s.getNextStatesForSymbol('K'));
    }

    @Test
    public void setNextStatesForSymbolModifiesTransitionsCorrectly() {
        State s = new State(1);
        Set<State> next_states = new HashSet();
        next_states.add(new State(2));
        next_states.add(new State(3));
        s.setNextStatesForSymbol('1', next_states);
        assertEquals(s.getNextStatesForSymbol('1'), next_states);
    }

    @Test
    public void setNextStatesForSymbolReplacesExistingStates() {
        State s = new State(1, createTransitionExample());
        s.setNextStatesForSymbol('0', new HashSet());
        assertEquals(new HashSet(), s.getNextStatesForSymbol('0'));
    }

    @Test
    public void setNextStateForSymbolModifiesTransitionsCorrectly() {
        State s = new State(1);
        s.setNextStateForSymbol('0', new State(5));
        assertTrue(s.getNextStatesForSymbol('0').contains(new State(5)));
    }

    @Test
    public void setNextStateForSymbolReplacesExistingStates() {
        State s = new State(1, createTransitionExample());
        s.setNextStateForSymbol('0', new State(5));
        assertTrue(!s.getNextStatesForSymbol('0').contains(new State(2)));
    }

    @Test
    public void addNextStatesForSymbolAddsStates() {
        State s = new State(1);
        Set<State> added_states = new HashSet();
        added_states.add(new State(2));
        added_states.add(new State(3));
        s.setNextStatesForSymbol('1', added_states);
        assertEquals(s.getNextStatesForSymbol('1'), added_states);
    }

    @Test
    public void addNextStatesForSymbolKeepsExistingStates() {
        State s = new State(1, createTransitionExample());
        Set<State> added_states = new HashSet();
        added_states.add(new State(5));
        added_states.add(new State(9));
        s.addNextStatesForSymbol('0', added_states);
        assertTrue(s.getNextStatesForSymbol('0').contains(new State(2)));
    }

    @Test
    public void addNextStateForSymbolAddsState() {
        State s = new State(1);
        s.setNextStateForSymbol('0', new State(5));
        assertTrue(s.getNextStatesForSymbol('0').contains(new State(5)));
    }

    @Test
    public void addNextStateForSymbolKeepsExistingStates() {
        State s = new State(1, createTransitionExample());
        s.addNextStateForSymbol('0', new State(5));
        assertTrue(s.getNextStatesForSymbol('0').contains(new State(2)));
    }
    
    @Test
    public void hashCodeReturnsSameWhenSameId(){
        State s1 = new State(1);
        State s2 = new State(1, createTransitionExample());
        
        assertTrue(s1.hashCode() == s2.hashCode());
    }
    
    @Test 
    public void hashCodeReturnsDifferentWhenDifferentId(){
        State s1 = new State(2);
        State s2 = new State(5);
        
        assertTrue(s1.hashCode() != s2.hashCode());
    }
    
    @Test
    public void toStringReturnsExpectedResultWhenToTransitions(){
        State s1 = new State(1);
        assertEquals("Id: 1\n", s1.toString());
    }
    
    @Test
    public void toStringReturnsExpectedResultsWhenMultipleTransitions(){
        State s = new State(1, createTransitionExample());
        assertEquals("Id: 1\nSymbols and what states are reachable from them:\n0 --> [2]\n1 --> []\n# --> [3, 4]\n", s.toString());
    }
    

}
