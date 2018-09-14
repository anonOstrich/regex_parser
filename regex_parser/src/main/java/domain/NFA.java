package domain;

import java.util.Set;
import java.util.HashSet;

/**
 * A representation of a nondeterministic finite automaton.
 *
 * <p>
 * Used to simulate the processing of an NFA that is recognizes the same
 * language that a specified regular expression generates. The information about
 * all the states is not easily accessible, but it should not be needed in the
 * process of parsing regular expressions.
 * </p>
 *
 * @author jesper
 */
public class NFA {

    /**
     * The state in which the automaton is prior to reading any input. The
     * following states are determined from the transition information contained
     * in the startingState
     */
    private State startingState;

    /**
     *
     * The set of states that is used to determine whether the automaton accepts
     * or rejects an input string. Any of these being a possible final state
     * results in acceptance.
     *
     */
    private Set<State> acceptingStates;

    /**
     * Creates an empty NFA. Has no use in itself.
     */
    public NFA() {
        startingState = new State(-1);
        acceptingStates = new HashSet(); 
    }

    /**
     *
     * @param startingState The initial state of the NFA
     * @param acceptingStates These states result in acceptance
     */
    public NFA(State startingState, Set<State> acceptingStates) {
        this.startingState = startingState;
        this.acceptingStates = acceptingStates;
    }

    /**
     *
     * Changes the starting state of the automaton to the given state.
     *
     * @param state
     */
    public void setStartingState(State state) {
        this.startingState = state;
    }

    /**
     *
     *
     * @return State at which NFA begins.
     */
    public State getStartingState() {
        return this.startingState;
    }

    /**
     *
     * Changes the accepting states of the automaton to the parameter.
     *
     * @param states
     */
    public void setAcceptingStates(Set<State> states) {
        this.acceptingStates = states;
    }

    /**
     *
     * @return
     */
    public Set<State> getAcceptingStates() {
        return this.acceptingStates;
    }

    /**
     *
     * Returns whether the NFA accepts or rejects the input string.
     *
     *
     * <p>
     * Simulates the operation of the NFA step by step when given the test
     * string as input. The method keeps track of all the possible states that
     * the NFA could be in at any given step. The input is processed by
     * concatenating the empty symbol, '#', to its end. This way we will in the
     * end consider also the states that can be accessed without symbols.
     * </p>
     * <p>
     * For every character, the method first adds to the current states all the
     * states that can be reached with empty symbol from current states. Then
     * the method queries each current states for the set of states that can be
     * accessed with the input symbol. These sets of states from each current
     * state are combined to form the set of all the states that the automaton
     * can be in after it has processed the next symbol character. Then the
     * current states are replaced with the next states, and the next symbol of
     * the test string is processed similarly until they end.
     * </p>
     * <p>
     * After exhausting all the characters, the method adds to the set of current states
     * all the states that are reachable from them with empty symbols - these are
     * also possible states at the end of the processing.
     * </p>
     * <p>
     * If at any point the set of current states is empty, it is certain that
     * the automaton cannot finish in an accepted state. Hence the method
     * immediately results false. 
     * </p>
     *
     * @param test Input string whose operation on the NFA is of interest. If
     * the string is empty, it is replaced with character '#', which represents
     * epsilon (empty symbol).
     *
     * @return True if the final state of the NFA may be in the set of accepting
     * states. Otherwise false.
     */
    public boolean accepts(String test) {

        Set<State> currentStates = new HashSet();
        currentStates.add(startingState);
        Set<State> nextStates = new HashSet();
        //Used to momentarily store the pointer to the current set, so that current set and next set point to different sets
        //at the end of each cycle
        Set<State> empty; 

        test += "#";

        for (int i = 0; i < test.length(); i++) {

            char symbol = test.charAt(i);
            
            if (symbol == '#'){
                break; 
            }
            
            currentStates = addEpsilonTransitionsOfStates(currentStates);

            for (State currentState : currentStates) {
                nextStates.addAll(currentState.getNextStatesForSymbol(symbol));
            }
         
            empty = currentStates; 
            currentStates = nextStates;
            nextStates = empty; 
            nextStates.clear(); 
            if (currentStates.isEmpty()) {
                return false;
            }

        }

        currentStates = addEpsilonTransitionsOfStates(currentStates);

        for (State s : acceptingStates) {
            if (currentStates.contains(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * The method expands the set of possible states by seeing which states can
     * be accessed from the set of states by using the empty/epsilon/# symbol.
     *
     * @param states
     * @return Same set, but the states reachable from its member by
     * epsilon-symbols are also included.
     */
    private Set<State> addEpsilonTransitionsOfStates(Set<State> states) {
        for (State s : states) {
            states.addAll(s.getNextStatesForSymbol('#'));
        }
        return states;
    }

}
