package domain;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class TransitionTable {

    private Map<State, Map<Character, Set<State>>> transitions;

    public TransitionTable() {
        transitions = new HashMap();
    }

    public Map<Character, Set<State>> getTransitionInfo(State state) {
        if (transitions.containsKey(state)) {
            return transitions.get(state);
        }
        return null;
    }

    public Set<State> getPossibleNextStates(State s, Character c) {
        Set<State> states = transitions.get(s).get(c);
        if (states == null) {
            return new HashSet();
        }
        return states; 
    }
    
    public Set<State> getPossibleNextStates(Set<State> states, Character c){
        System.out.println(states);
        Set<State> result = new HashSet(); 
        
        //is iterating over the whole set slower with HashSet than TreeSet?        
        for (State s: states){
            result.addAll(this.getPossibleNextStates(s, c));
        }      
        
        return result; 
    }

    public void addTransitions(State from, Character symbol, Set<State> to) {
        
        if (!transitions.containsKey(from)) {
            transitions.put(from, new HashMap());
        }

        transitions.get(from).put(symbol, to);
    }
    
    public void addTransitions(State from, Character symbol, State... states){
        Set<State> to = new HashSet(); 
        Collections.addAll(to, states);
    }

    public void addTransition(State from, Character symbol, State to) {
        Set<State> targets = new HashSet();
        targets.add(to);
        addTransitions(from, symbol, targets);
    }
    


    public Set<State> getStates() {
        return transitions.keySet();
    }

    @Override
    public String toString() {
        String result = "";
        for (State from : transitions.keySet()) {
            result += "Tila " + from + ":";

            for (Character symbol : transitions.get(from).keySet()) {
                result += "\t" + symbol + ": " + transitions.get(from).get(symbol) + "\n";
            }
        }
        return result;
    }

}
