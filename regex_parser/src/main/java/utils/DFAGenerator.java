package utils;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import domain.NFA;
import domain.State;

/**
 *
 * Tools for converting an NFA into A DFA with the power set method Use should
 * be avoided in all but necessary cases - exponential time complexity! Only
 * required when negating an expression for the first time
 *
 * @author jesper
 */
public class DFAGenerator {

    private Map<NFA, NFA> cache;
    private int highestAvailable;

    public DFAGenerator(int highestAvailable) {
        cache = new HashMap();
        this.highestAvailable = highestAvailable;
    }

    // if nfa is already dfa, it would be simply a matter of swappings states - for which there is no method available
    public NFA generateComplementDFA(NFA nfa, Set<Character> alphabet) {
        if (cache.containsKey(nfa)) {
            return cache.get(nfa);
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
        for(State s: NFAStartingStates){
            if (nfa.getAcceptingStates().contains(s)){
                accepts = false; 
                break; 
            }
        }
        
        if (accepts){
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

        return dfa;
    }

}
