package domain;

import java.util.Map; 
import java.util.HashMap; 
import java.util.Set; 
import java.util.HashSet; 

public class State {
    private int id; 
    private Map<Character, Set<State>> transitions; 
    
    public State(int id, Map<Character, Set<State>> transitions){
        this.id = id;
        this.transitions = transitions; 
    }
    
    public State(int id){
        this(id, new HashMap());
    }
    
    public Map<Character, Set<State>> getAllTransitions(){
        return this.transitions; 
    }
    
    public void setTransitions(Map<Character, Set<State>> transitions){
        this.transitions = transitions; 
    }
    
    public void setNextStateForSymbol(Character symbol, State next){
        transitions.put(symbol, new HashSet());
        transitions.get(symbol).add(next);
    }
    
    public void setNextStatesForSymbol(Character symbol, Set<State> next_states){
        transitions.put(symbol, next_states);
    }
    
    
    public void addTransitions(Map<Character, Set<State>> transitions){
        this.transitions.putAll(transitions);        
    }
    
    public void addNextStateForSymbol(Character symbol, State next){
        if (!transitions.containsKey(symbol)){
            transitions.put(symbol, new HashSet());
        }
        transitions.get(symbol).add(next);
    }
    
    public void addNextStatesForSymbol(Character symbol, Set<State> next_states){
        if (!transitions.containsKey(symbol)){
            transitions.put(symbol, new HashSet());
        }
        
        transitions.get(symbol).addAll(next_states);
        
    }
    
    public Set<State> getNextStatesForSymbol(Character symbol){
        return transitions.get(symbol); 
    }
    
    
    public int getId(){
        return id; 
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    
    
    //will I care about the transitions? Or will I ensure that id is a good enough identifier? 
    @Override
    public boolean equals(Object o){
        if (o == null || o.getClass() != this.getClass()){
            return false; 
        }
        State compareState = (State) o;
        return this.getId() == compareState.getId(); 
    }
    
    
    @Override
    public int hashCode(){
        return ((Integer) id).hashCode();
    }
    
    @Override
    public String toString(){
        return "" + id;
    }
    
}
