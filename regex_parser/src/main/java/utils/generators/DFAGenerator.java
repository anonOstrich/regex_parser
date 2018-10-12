package utils.generators;

import utils.structures.OwnSet;
import utils.structures.OwnMap;
import domain.NFA;
import domain.State;
import utils.Utilities;

/**
 *
 * Tools for converting an NFA into A DFA with the power set method. Use should
 * be avoided in all but necessary cases - exponential time complexity! Only
 * required when negating an expression for the first time.
 *
 */
public class DFAGenerator {
    
    /**
     * Any possible symbols that might be encountered in 
     */
    private OwnSet<Character> allPossibleSymbols;

    /**
     * Stores negations based on the NFA key.
     *
     */
    private OwnMap<NFA, NFA> cache;

    /**
     * Boolean that indicates whether cache is used to boost performance.
     */
    private boolean cacheEnabled;

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
        this(highestAvailable, true);
    }

    public DFAGenerator(int highestAvailable, boolean cacheEnabled) {
        this.highestAvailable = highestAvailable;
        this.cacheEnabled = cacheEnabled;
        if (cacheEnabled) {
            cache = new OwnMap();
        }
        allPossibleSymbols = Utilities.defaultAlphabet(); 
        allPossibleSymbols.addAll(Utilities.defaultShorthands());
        allPossibleSymbols.addAll(Utilities.defaultBasicOperations());
        allPossibleSymbols.add('/');
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
    public NFA generateComplementDFA(NFA nfa) {
        //messy and overly long, to be cleaned up at some point... 

        if (cacheEnabled && cache.containsKey(nfa)) {
            return cache.get(nfa);
        }

        if (nfa.isDFA()) {
            nfa.invert();
            return nfa;
        }

        NFA dfa = new NFA();

        OwnMap<OwnSet<State>, State> subsetStatesBySetsOfStates = new OwnMap();
        OwnMap<State, OwnSet<State>> setsOfStatesBySubsetStates = new OwnMap();

        State startingSubsetState = new State(highestAvailable);
        highestAvailable--;
        dfa.setStartingState(startingSubsetState);

        OwnSet<State> NFAStartingStates = new OwnSet();
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

        OwnSet<State> subsetStatesToBeInvestigated = new OwnSet();
        subsetStatesToBeInvestigated.add(startingSubsetState);
        OwnSet<State> investigatedSubsetStates = new OwnSet();

        while (!subsetStatesToBeInvestigated.isEmpty()) {
            // could replace with random/first/etc method in OwnSet
            State currentSubsetState = subsetStatesToBeInvestigated.iterator().next();
            investigatedSubsetStates.add(currentSubsetState);
            subsetStatesToBeInvestigated.remove(currentSubsetState);
            OwnSet<State> NFAStates = setsOfStatesBySubsetStates.get(currentSubsetState);

            for (Character symbol : allPossibleSymbols) {
                
                OwnSet<State> reachableFromAny = new OwnSet();
                
                
                for (State NFAState : NFAStates) {
                    OwnSet<State> reachableFromState = NFAState.getNextStatesForSymbol(symbol);
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

        if (cacheEnabled) {
            cache.put(nfa, dfa);
        }
        
        dfa.setIsDFA(true);
        return dfa;
    }

    public void enableCaching() {
        cacheEnabled = true;
        if(cache == null){
            cache = new OwnMap(); 
        }
        
    }

    public void disableCaching() {
        cacheEnabled = false; 
    }

}
