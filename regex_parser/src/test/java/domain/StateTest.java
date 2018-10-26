package domain;

import utils.structures.OwnMap;
import utils.structures.OwnSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class StateTest {

    public StateTest() {
    }

    private OwnMap<Character, OwnSet<State>> createTransitionExample() {
        OwnMap<Character, OwnSet<State>> result = new OwnMap();
        result.put('0', new OwnSet());
        result.put('1', new OwnSet());
        result.put('#', new OwnSet());

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
        OwnMap<Character, OwnSet<State>> data = new OwnMap();
        
        State s = new State(3, data);
        assertEquals(3, s.getId());
    }

    @Test
    public void transitionsAreSetCorrectlyBySimpleConstructor() {
        State s = new State(1);
        assertEquals(new OwnMap(), s.getAllTransitions());
    }

    @Test
    public void transitionsAreSetCorrectlyByComplexConstructor() {
        State s = new State(3, createTransitionExample());
        assertEquals(createTransitionExample(), s.getAllTransitions());
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
        OwnMap<Character, OwnSet<State>> transitions = createTransitionExample();
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
        s.setTransitions(new OwnMap());
        assertEquals(new OwnMap(), s.getAllTransitions());
    }

    @Test
    public void addTransitionsAddsTransitions() {
        State s = new State(1);
        s.setTransitions(createTransitionExample());
        assertEquals(s.getAllTransitions(), createTransitionExample());
    }

    @Test
    public void addTransitionsKeepsExistingTransitions() {
        OwnMap<Character, OwnSet<State>> existingTransitions = new OwnMap();
        existingTransitions.put('A', new OwnSet());
        existingTransitions.get('A').add(new State(10));
        State s = new State(1, existingTransitions);
        s.addTransitions(createTransitionExample());
        assertTrue(s.getAllTransitions().containsKey('A'));

    }

    @Test
    public void getNextStatesForSymbolWorksWhenThereAreNextStates() {
        State s = new State(1, createTransitionExample());
        OwnSet<State> expected = new OwnSet();
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
        assertEquals(new OwnSet(), s.getNextStatesForSymbol('K'));
    }

    @Test
    public void setNextStatesForSymbolModifiesTransitionsCorrectly() {
        State s = new State(1);
        OwnSet<State> next_states = new OwnSet();
        next_states.add(new State(2));
        next_states.add(new State(3));
        s.setNextStatesForSymbol('1', next_states);
        assertEquals(s.getNextStatesForSymbol('1'), next_states);
    }

    @Test
    public void setNextStatesForSymbolReplacesExistingStates() {
        State s = new State(1, createTransitionExample());
        s.setNextStatesForSymbol('0', new OwnSet());
        assertEquals(new OwnSet(), s.getNextStatesForSymbol('0'));
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
        OwnSet<State> added_states = new OwnSet();
        added_states.add(new State(2));
        added_states.add(new State(3));
        s.setNextStatesForSymbol('1', added_states);
        assertEquals(s.getNextStatesForSymbol('1'), added_states);
    }

    @Test
    public void addNextStatesForSymbolKeepsExistingStates() {
        State s = new State(1, createTransitionExample());
        OwnSet<State> added_states = new OwnSet();
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

    

}
