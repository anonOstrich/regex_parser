package domain;

import utils.structures.OwnSet;
import utils.structures.OwnMap;
import java.util.Arrays;

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
     * Key is some symbol of an alphabet or #. The value of the key is the set
     * of states that are reachable with the key symbol transition in some
     * nondeterministic finite automaton. In total, the map contains information
     * about all symbols that may lead to next states in an NFA, and also the
     * specific states that a given symbol can lead to.
     * </p>
     */
    private OwnMap<Character, OwnSet<State>> transitions;

    private OwnSet<State> emptyTransitions;

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
     * @param symbol
     * @param next
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
     * @param symbol
     * @param next_states
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
     * @return Set of states that are reachable from this state with the symbol
     */
    public OwnSet<State> getNextStatesForSymbol(Character symbol) {
        OwnSet<State> result = transitions.get(symbol);
        if (result == null) {
            return new OwnSet();
        }

        return result;
    }
    
    public void replaceStatesReachableWithoutSymbols(OwnSet<State> set){
        this.emptyTransitions = set; 
    }

    public void addStatesReachableWithoutSymbols(OwnSet<State> set){
        this.emptyTransitions.addAll(set);
    }
    
    public void addStatesReachableWithoutSymbols(State s){
        OwnSet<State> set = new OwnSet();
        set.add(s);
        addStatesReachableWithoutSymbols(set);
    }
    
    public OwnSet<State> getNextStatesWithEmptyTransitions() {
//        OwnSet<State> result = transitions.get('#');
//        if(result == null){
//            return new OwnSet(); 
//        }
//        return transitions.get('#');
        return this.emptyTransitions; 
    }

    public void replaceStatesReachableWithAnyCharacter(OwnSet<State> set){
        this.anySymbolTransitions = set; 
    }
    
    public void addStatesReachableWithAnyCharacter(OwnSet<State> set) {
        this.anySymbolTransitions.addAll(set);
    }
    
    public void addStatesReachableWithAnyCharacter(State s){
        OwnSet<State> set = new OwnSet(); 
        set.add(s);
        addStatesReachableWithAnyCharacter(set);
    }
    
    

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

    /**
     *
     *  OUTDATED after 12.10. Not all transitions are in the same map anymore.
     * 
     * @return String representation that shows the state's id, and on their own
     * lines symbols and the states that the symbol can result in.
     */
    @Override
    public String toString() {
        String result = "";
        result += "Id: " + getId() + "\n";
        if (transitions.isEmpty()) {
            return result;
        }
        result += "Symbols and what states are reachable from them:\n";

        for (Character symbol : transitions.keySet()) {

            OwnSet<State> reachable_states = transitions.get(symbol);

            int[] reachable_ids = new int[reachable_states.size()];
            int i = 0;
            for (State s : reachable_states) {
                reachable_ids[i] = s.getId();
                i++;
            }

            result += symbol + " --> " + Arrays.toString(reachable_ids) + "\n";
        }
        return result;
    }

}
