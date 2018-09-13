package domain;

import java.util.Set;
import java.util.HashSet;

/**
 * A representation of a nondeterministic finite automaton.
 * 
 * <p>
 * Used to simulate the processing of an NFA that is recognizes the same language
 * that a specified regular expression generates. The information about all the states is not 
 * easily accessible, but it should not be needed in the process of parsing regular expressions. 
 * </p>
 * 
 * @author jesper
 */
public class NFA {
    
    /**
     * The state in which the automaton is prior to reading any input. 
     * The following states are determined from the transition information contained in the startingState
     */
    private State startingState; 
    
    /**
     * 
     * The set of states that is used to determine whether the automaton accepts or rejects an input string.
     * Any of these being a possible final state results in acceptance. 
     * 
     */
    private Set<State> acceptingStates; 

    
    /**
     * Creates an empty NFA. Has no use in itself. 
     */
    public NFA(){
        
    }
    
    /**
     *
     * @param startingState The initial state of the NFA
     * @param acceptingStates These states result in acceptance
     */
    public NFA(State startingState, Set<State> acceptingStates){
        this.startingState = startingState;
        this.acceptingStates = acceptingStates; 
    }
    
    /**
     *
     * Returns whether the NFA accepts or rejects the input string. 
     * 
     * 
     * <p>
     * Simulates the operation of the NFA step by step when given the test string as input. The method
     * keeps track of all the possible states that the NFA could be in at any given step. Then each of these states is queried
     * for the states that can result from the next character symbol of the test string. These sets of states from each current state
     * are combined to form the set of all the states that the automaton can be in after it has processed the next symbol character. 
     * Then the current states is replaced with the next states, and the next symbol of the test string is processed similarly
     * until they end. 
     * </p>
     * <p>
     * If at any point the set of current states is empty, it is certain that the automaton cannot finish in an accepted state. Hence
     * the method immediately results false. 
     * </p>
     * 
     * @param test Input string whose operation on the NFA is of interest. If the string is empty, it is replaced with 
     * character '#', which represents epsilon (empty symbol).
     * 
     * @return True if the final state of the NFA  may be in the set of accepting states. Otherwise false.
     */
    public boolean accepts(String test){
        
        Set<State> currentStates = new HashSet();
        currentStates.add(startingState);
        Set<State> nextStates = new HashSet(); 
        
        if (test.isEmpty()){
            test = "#";
        }
        
        for(int i = 0; i < test.length(); i++){
            char symbol = test.charAt(i);
            
            for(State currentState: currentStates){
                nextStates.addAll(currentState.getNextStatesForSymbol(symbol));
            }
            currentStates = nextStates;
            if(currentStates.isEmpty()){
                return false; 
            }
            
        }
        
        for (State s: acceptingStates){
            if (currentStates.contains(s)){
                return true;
            }
        }
        
        return false; 
    }
  
}
