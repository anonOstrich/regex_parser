package utils;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import domain.NFA;
import domain.State;

/**
 *
 * Tools for converting an NFA into A DFA with the power set method. Use should
 * be avoided in all but necessary cases - exponential time complexity! Only
 * required when negating an expression for the first time.
 *
 */
public class DFAGenerator {

    /**
     * Stores negations based on the NFA key.
     *
     */
    private Map<NFA, NFA> cache;
    /**
     * Highest available negative integer for creating unique states.
     */
    private int highestAvailable;

    /**
     *
     * @param highestAvailable Highest (negative) integer that is used in ids of
     * new states.
     */
    public DFAGenerator(int highestAvailable) {
        cache = new HashMap();
        this.highestAvailable = highestAvailable;
    }

    /**
     *
     * Generates a DFA that recognizes the complement language of the input NFA.
     *
     * <p>
     * If the answer is already in the cache, it is returned from there.
     * Otherwise the method begins constructing a new DFA. The states of this
     * DFA each represent one subset of the states of the parameter NFA; these
     * states are referred to as subset states in the method. There are two maps
     * to help with conversions from a subset state to the corresponding set of
     * NFA states and vice versa.
     * </p>
     * <p>
     * The NFA is simulated and new subset states are created only when needed.
     * These results of reachable states are stored also in subset states in the
     * DFA under construction. Once all possible sets of NFA states have been
     * considered with every letter of the alphabet, the DFA has been created.
     * </p>
     * <p>
     * Once all the reachable NFA states have been determined from one subset,
     * the method checks if any of them is included in the NFA's accepting
     * states. If that is the case, the creted subset state is NOT added to the
     * set of accepting subset states of the DFA; and if any states is not an
     * acceptin state, the subset state is added to the accepting states of the
     * DFA. In this way the DFA will accept exactly those input strings that
     * will not end the operation of the input NFA in an accepting state.
     * </p>
     *
     * @param nfa Automaton that is to be negated.
     * @param alphabet Allowed symbols in state transitions (excluding #).
     * @return Deterministic (also non-deterministic) finite automaton that
     * recognizes the complement language of the parameter nfa.
     */
    public NFA generateComplementDFA(NFA nfa, Set<Character> alphabet) {
        if (cache.containsKey(nfa)) {
            return cache.get(nfa);
        }

        if (nfa.isDFA()) {
            nfa.invert();
            return nfa;
        }

        NFA dfa = new NFA();

        Map<Set<State>, State> subsetStatesBySetsOfStates = new HashMap();
        Map<State, Set<State>> setsOfStatesBySubsetStates = new HashMap();

        State startingSubsetState = new State(highestAvailable);
        highestAvailable--;
        dfa.setStartingState(startingSubsetState);

        Set<State> NFAStartingStates = new HashSet();
        NFAStartingStates.add(nfa.getStartingState());
        nfa.addEpsilonTransitionsOfStates(NFAStartingStates);
        subsetStatesBySetsOfStates.put(NFAStartingStates, startingSubsetState);
        setsOfStatesBySubsetStates.put(startingSubsetState, NFAStartingStates);

        boolean accepts = true;
        for (State s : NFAStartingStates) {
            if (nfa.getAcceptingStates().contains(s)) {
                accepts = false;
                break;
            }
        }

        if (accepts) {
            dfa.getAcceptingStates().add(startingSubsetState);
        }

        Set<State> subsetStatesToBeInvestigated = new HashSet();
        subsetStatesToBeInvestigated.add(startingSubsetState);
        Set<State> investigatedSubsetStates = new HashSet();

        while (!subsetStatesToBeInvestigated.isEmpty()) {
            State currentSubsetState = subsetStatesToBeInvestigated.iterator().next();
            investigatedSubsetStates.add(currentSubsetState);
            subsetStatesToBeInvestigated.remove(currentSubsetState);
            Set<State> NFAStates = setsOfStatesBySubsetStates.get(currentSubsetState);

            for (char symbol : alphabet) {
                Set<State> reachableFromAny = new HashSet();
                for (State NFAState : NFAStates) {
                    Set<State> reachableFromState = NFAState.getNextStatesForSymbol(symbol);
                    dfa.addEpsilonTransitionsOfStates(reachableFromState);
                    reachableFromAny.addAll(reachableFromState);
                }

                //turn into one subset state
                State nextSubsetState;
                if (subsetStatesBySetsOfStates.containsKey(reachableFromAny)) {
                    nextSubsetState = subsetStatesBySetsOfStates.get(reachableFromAny);
                } else {
                    nextSubsetState = new State(highestAvailable);
                    highestAvailable--;
                    subsetStatesBySetsOfStates.put(reachableFromAny, nextSubsetState);
                    setsOfStatesBySubsetStates.put(nextSubsetState, reachableFromAny);
                }
                currentSubsetState.addNextStateForSymbol(symbol, nextSubsetState);

                boolean acceptingState = true;
                for (State s : reachableFromAny) {
                    if (nfa.getAcceptingStates().contains(s)) {
                        acceptingState = false;
                        break;
                    }
                }
                if (acceptingState) {
                    dfa.getAcceptingStates().add(nextSubsetState);
                }
                if (!investigatedSubsetStates.contains(nextSubsetState)) {
                    subsetStatesToBeInvestigated.add(nextSubsetState);
                }
            }
        }

        cache.put(nfa, dfa);
        dfa.setIsDFA(true);
        return dfa;
    }

}
