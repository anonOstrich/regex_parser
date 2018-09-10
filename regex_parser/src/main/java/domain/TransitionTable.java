
package domain;

import java.util.Map; 
import java.util.HashMap; 
import java.util.HashSet; 
import java.util.Set; 


public class TransitionTable {
    private Map<State, Map<Character, Set<State>>> transitions; 
    
    
    public TransitionTable(){
        transitions = new HashMap(); 
    }
    
    
    public Map<Character, Set<State>> getTransitionInfo(State state){
        if (transitions.containsKey(state)){
            return transitions.get(state);
        }
        return null; 
    }
    
    
    public void addTransitions(State from, Character symbol, Set<State> to){
        if (!transitions.containsKey(from)){
            transitions.put(from, new HashMap());
        }
        
        transitions.get(from).put(symbol, to);            
    }
    
    public void addTransition(State from, Character symbol, State to){
        Set<State> targets = new HashSet(); 
        targets.add(to);
        addTransitions(from, symbol, targets);
    }
    
    
    public Set<State> getStates(){
        return transitions.keySet();
    }
    
    
    @Override
    public String toString(){
        String result = ""; 
        for (State from: transitions.keySet()){
            result += "Tila " + from + ":";
            
            for (Character symbol: transitions.get(from).keySet()){
                result += "\t" + symbol + ": " + transitions.get(from).get(symbol) + "\n";
            }
        }
        return result; 
    }
    
    
    
    
}
