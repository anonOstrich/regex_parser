package domain;

import java.util.Set;
import java.util.HashSet;



public class NFA {
    private State startingState; 
    private Set<State> acceptingStates; 
    // We might manage without a set of all states. 
    // tho might be that negation/ powerset construction needs, if
    // all states are created at the same time at the beginning
    
    
    
    public NFA(){
        
    }
    
    public NFA(State startingState, Set<State> acceptingStates){
        this.startingState = startingState;
        this.acceptingStates = acceptingStates; 
    }
    

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
