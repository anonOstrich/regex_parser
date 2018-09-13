package domain;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a node and its forward connections in a nondeterministic finite
 * automaton
 *
 *
 *
 * @author Jesper Kuutti
 */
public class State {

    /**
     * Integer that is used to differentiate different states from one another.
     */
    private int id;

    /**
     * All the information about what states are accessible from this one.
     *
     * <p>
     * Key is some symbol of an alphabet. The value of the key is the set of
     * states that are possible with the key symbol in some nondeterministic
     * finite automaton. In total, the map contains information about all
     * symbols that may lead to next states in an NFA, and also the specific
     * states that a given symbol can lead to.ö
     * </p>
     */
    private Map<Character, Set<State>> transitions;

    /**
     * Creates an instance of the class with predetermined transitions.
     *
     * @param id Id to differentiate this state from others.
     * @param transitions Predetermined information about transitions to other
     * states.
     */
    public State(int id, Map<Character, Set<State>> transitions) {
        this.id = id;
        this.transitions = transitions;
    }

    /**
     *
     * Creates an instance of the class that has no transition information.
     *
     * @param id
     *
     */
    public State(int id) {
        this(id, new HashMap());
    }

    /**
     *
     *
     * @return Transition info for all the symbols that might lead to next
     * states
     */
    public Map<Character, Set<State>> getAllTransitions() {
        return this.transitions;
    }

    /**
     *
     * Replace all the existing transitions with new ones.
     *
     * @param transitions A map of transitions for the symbols that might lead
     * to next states
     */
    public void setTransitions(Map<Character, Set<State>> transitions) {
        this.transitions = transitions;
    }

    /**
     *
     * Add new transition symbols and the corresponding reachable states to the
     * already existing transition information.
     *
     * @param transitions
     */
    public void addTransitions(Map<Character, Set<State>> transitions) {
        this.transitions.putAll(transitions);
    }

    /**
     * Replace the existing possible states for the given symbol with a set that
     * contains only the given state.
     *
     * @param symbol
     * @param next
     */
    public void setNextStateForSymbol(Character symbol, State next) {
        transitions.put(symbol, new HashSet());
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
            transitions.put(symbol, new HashSet());
        }
        transitions.get(symbol).add(next);
    }

    /**
     *
     * Replace the existing possible states for the given symbol with a new set
     * of states.
     *
     * @param symbol
     * @param next_states
     */
    public void setNextStatesForSymbol(Character symbol, Set<State> next_states) {
        transitions.put(symbol, next_states);
    }

    /**
     *
     * Adds the states in the given set to the existing set of states for the given symbol. 
     * 
     * @param symbol
     * @param next_states
     */
    public void addNextStatesForSymbol(Character symbol, Set<State> next_states) {
        if (!transitions.containsKey(symbol)) {
            transitions.put(symbol, new HashSet());
        }

        transitions.get(symbol).addAll(next_states);

    }

    /** 
     * 
     * @param symbol
     * @return 
     */
    public Set<State> getNextStatesForSymbol(Character symbol) {
        Set<State> result = transitions.get(symbol);
        if (result == null) {
            return new HashSet();
        }
        
        return result;
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
     * States are equal if their ids are the same; the transition information is not considered
     * 
     * @param o
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
     * @return
     */
    @Override
    public int hashCode() {
        return ((Integer) id).hashCode();
    }

    /**
     *
     * @return String representation that shows the state's id, and on their own lines symbols and the states that the symbol can result in. 
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

            Set<State> reachable_states = transitions.get(symbol);
            int[] reachable_ids = reachable_states.stream().mapToInt(st -> st.getId()).toArray();

            result += symbol + " --> " + Arrays.toString(reachable_ids) + "\n";
        }
        return result;
    }

}
