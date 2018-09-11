package domain;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import utils.Constants;

public class NFA {

    private State startingState;
    private Set<State> acceptingStates;
    private TransitionTable transitions;
    private int lowestAvailable;

    public NFA() {
        this("1010");
    }

    public NFA(String pattern) {
        acceptingStates = new HashSet();
        transitions = new TransitionTable();
        lowestAvailable = 0;
        if (pattern.isEmpty()) {
            pattern = "#";
        }
        constructFromRegEx(pattern);
    }

    public boolean accepts(String test) {
        if (test.isEmpty()){
            test = "#";
        }

        // here we should simulate the functioning of the automaton, then... 
        Set<State> currentStates = new HashSet();
        currentStates.add(startingState);
        Set<State> nextStates = new HashSet();

        boolean remains = test.length() > 0;

        for (int i = 0; i < test.length(); i++) {
            // see all that can be reached 
            nextStates = transitions.getPossibleNextStates(currentStates, test.charAt(i));
            currentStates = nextStates;

            if (currentStates.isEmpty()) {
                return false;
            }
        }

        if (test.isEmpty()) {
            // I hereby do declare '#' to represent epsilon. Remember to write down in documentation!
            currentStates.addAll(transitions.getPossibleNextStates(startingState, '#'));
        }

        for (State s : acceptingStates) {
            if (currentStates.contains(s)) {
                return true;
            }
        }

        return false;
    }

    public void constructFromRegEx(String pattern) {

        // while the implementation is lacking for pattern -> NFA construction, 
        // we'll build an easy example
        // support for: 
        // 1) epsilon [DONE]
        // 2) A single symbol [DONE]
        // 3) concatenation [NOT DONE]
        // 4) union [NOT DONE]
        // 5) Keene star [NOT DONE]
        // 6) negation [NOT DONE]
        // 7) repetition arbitrary number of times (maybe) [NOT DONE]
        // 8) one symbol among many (maybe) [NOT DONE]
        // handy in naming the states    
        
        if (pattern.length() == 1 && (pattern.charAt(0) == '#' || Constants.alphabet().contains(pattern.charAt(0)))){
            State s1 = new State(lowestAvailable);
            State s2 = new State(lowestAvailable + 1);
            lowestAvailable += 2;
            transitions.addTransition(s1, pattern.charAt(0), s2);
            startingState = s1;
            acceptingStates.add(s2);
        }
        

        if (pattern.charAt(0) == '('){
            int endingParIdx = pattern.indexOf(")");
            
        }

        //constructPredefined();
    }

    private void constructPredefined() {
        State start = new State(0);
        State state1 = new State(1);
        State state2 = new State(2);
        State state3 = new State(3);
        State state4 = new State(4);
        State state5 = new State(5);

        transitions.addTransition(start, '1', state1);
        transitions.addTransition(state1, '0', state2);
        transitions.addTransition(state2, '1', state3);
        transitions.addTransition(state3, '0', state4);
        transitions.addTransition(state3, '1', state5);

        startingState = start;
        acceptingStates.add(state4);
        acceptingStates.add(state5);
    }

    @Override
    public String toString() {
        String result = "";
        result += "Starting state:\n" + startingState + "\n";
        result += "Accepting states:\n";
        for (State s : acceptingStates) {
            result += s + "\n";
        }


        return result;
    }

}
