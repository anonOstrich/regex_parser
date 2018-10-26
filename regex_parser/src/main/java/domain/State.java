package domain;

import utils.structures.OwnSet;
import utils.structures.OwnMap;

/**
 *
 * Represents a node and its forward connections in a nondeterministic finite
 * automaton.
 *
 */
public class State {

    /**
     * Integer that is used to differentiate states from one another.
     */
    private int id;

    /**
     * All the information about what states are accessible from this one.
     *
     * <p>
     * Key is some symbol of an alphabet. The value of the key is the set
     * of states that are reachable with the key symbol transition.
     * In total, the map contains information
     * about all symbols that may lead to next states, and also the
     * specific states that a given symbol can lead to.
     * </p>
     */
    private OwnMap<Character, OwnSet<State>> transitions;

    /**
     * 
     * States that can be reached without reading symbols
     * 
     */
    private OwnSet<State> emptyTransitions;

    /**
     * 
     * States that can be reached with any single symbol
     * 
     * <p>
     * Saves effort to store as own variable
     * when dealing with '.', the any single character symbol. 
     * Otherwise a transition for every single possible character would
     * have to be inserted/included.
     * </p>
     * 
     */
    private OwnSet<State> anySymbolTransitions;

    /**
     * Creates an instance of the class with predetermined transitions.
     *
     * @param id
     * @param transitions Predetermined information about transitions to other
     * states.
     */
    public State(int id, OwnMap<Character, OwnSet<State>> transitions) {
        this.id = id;
        this.transitions = transitions;
        emptyTransitions = new OwnSet(); 
        anySymbolTransitions = new OwnSet(); 
    }

    /**
     *
     * Creates an instance of the class that has no transition information.
     *
     * @param id
     *
     */
    public State(int id) {
        this(id, new OwnMap());
    }

    /**
     *
     * @return Transition info for all the symbols that might lead to next
     * states
     */
    public OwnMap<Character, OwnSet<State>> getAllTransitions() {
        return this.transitions;
    }

    /**
     *
     * Replace all the existing transitions with new ones.
     *
     * @param transitions New transitions.
     */
    public void setTransitions(OwnMap<Character, OwnSet<State>> transitions) {
        this.transitions = transitions;
    }

    /**
     *
     * Add new transition symbols and the corresponding reachable states to the
     * already existing transition information.
     *
     * @param transitions New information to be added.
     */
    public void addTransitions(OwnMap<Character, OwnSet<State>> transitions) {
        this.transitions.putAll(transitions);
    }

    /**
     * Replace the existing possible states for the given symbol with a set that
     * contains only the given single state.
     *
     * @param symbol Which symbol's set of states is replaced
     * @param next The only state reachable with the symbol
     */
    public void setNextStateForSymbol(Character symbol, State next) {
        transitions.put(symbol, new OwnSet());
        transitions.get(symbol).add(next);
    }

    /**
     *
     * Adds the given state to the existing set of states for the given symbol.
     *
     * @param symbol Which symbol's set of states is expanded
     * @param next State to be added
     */
    public void addNextStateForSymbol(Character symbol, State next) {
        if (!transitions.containsKey(symbol)) {
            transitions.put(symbol, new OwnSet());
        }
        transitions.get(symbol).add(next);
    }

    /**
     *
     * Replace the existing possible states for the given symbol with a new set
     * of states.
     *
     * @param symbol Symbol whose states are replaced
     * @param next_states Replacing states
     */
    public void setNextStatesForSymbol(Character symbol, OwnSet<State> next_states) {
        transitions.put(symbol, next_states);
    }

    /**
     *
     * Adds the states in the given set to the existing set of states for the
     * given symbol.
     *
     * @param symbol Symbol whose set of states is expanded.
     * @param next_states Added states
     */
    public void addNextStatesForSymbol(Character symbol, OwnSet<State> next_states) {
        if (!transitions.containsKey(symbol)) {
            transitions.put(symbol, new OwnSet());
        }

        transitions.get(symbol).addAll(next_states);

    }

    /**
     *
     * @param symbol
     * @return Set of states that are reachable from this state with the given symbol
     */
    public OwnSet<State> getNextStatesForSymbol(Character symbol) {
        OwnSet<State> result = transitions.get(symbol);
        if (result == null) {
            return new OwnSet();
        }

        return result;
    }
    
    /**
     * 
     * @param set Set of states that replace current ones. 
     * 
     */
    public void replaceStatesReachableWithoutSymbols(OwnSet<State> set){
        this.emptyTransitions = set; 
    }

    /**
     * 
     * @param set Set of states to be added to the current ones
     */
    public void addStatesReachableWithoutSymbols(OwnSet<State> set){
        this.emptyTransitions.addAll(set);
    }
    
    /**
     * 
     * @param s State to be added to the states reachable without reading symbols
     */
    public void addStatesReachableWithoutSymbols(State s){
        OwnSet<State> set = new OwnSet();
        set.add(s);
        addStatesReachableWithoutSymbols(set);
    }
    
    /**
     * 
     * @return Set of states that can be reached without reading symbols
     */
    public OwnSet<State> getNextStatesWithEmptyTransitions() {
        return this.emptyTransitions; 
    }

    
    /**
     * 
     * @param set Set of states to replace the current ones
     */
    public void replaceStatesReachableWithAnyCharacter(OwnSet<State> set){
        this.anySymbolTransitions = set; 
    }
    
    /**
     * 
     * @param set Set of states to be added to the current ones
     */
    public void addStatesReachableWithAnyCharacter(OwnSet<State> set) {
        this.anySymbolTransitions.addAll(set);
    }
    
    /**
     * 
     * @param s State to be added to the current ones.
     */
    public void addStatesReachableWithAnyCharacter(State s){
        OwnSet<State> set = new OwnSet(); 
        set.add(s);
        addStatesReachableWithAnyCharacter(set);
    }
    
    

    /**
     * 
     * @return Set of states that are reachable with any single character.
     */
    public OwnSet<State> getNextStatesWithAnyCharacter() {
        return anySymbolTransitions;
    }

    /**
     *
     * @return The id of the state.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * States are equal if their ids are the same; the transition information is
     * not considered
     *
     * @param o Compared object
     * @return True if the states share the id, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        State compareState = (State) o;
        return this.getId() == compareState.getId();
    }

    /**
     *
     * @return Hash code generated from the id.
     */
    @Override
    public int hashCode() {
        return ((Integer) id).hashCode();
    }

}
