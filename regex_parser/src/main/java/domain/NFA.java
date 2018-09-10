package domain;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class NFA {

    private State startingState;
    private Set<State> acceptingStates;
    private TransitionTable transitions;

    public NFA() {
        this("1010");
    }

    public NFA(String pattern) {
        acceptingStates = new HashSet();
        transitions = new TransitionTable(); 
        constructFromRegEx(pattern);
    }
    
    
    
    public boolean accepts(String test){
        return false; 
    }
    
    
    public void constructFromRegEx(String pattern){
        State start = new State(0);
        State state1 = new State(1);
        State state2 = new State(2);
        State state3 = new State(3);
        State state4 = new State(4);
        
        
        
        
    }
    
    
    @Override
    public String toString(){
        String result = ""; 
        result += "Starting state:\n" + startingState + "\n"; 
        result += "Accepting states:\n";
        for (State s: acceptingStates){
            result += s + "\n";
        }
        
        for(State s: transitions.getStates()){
            result += s + "\n";
        }
        
        return result; 
    }
    

}
