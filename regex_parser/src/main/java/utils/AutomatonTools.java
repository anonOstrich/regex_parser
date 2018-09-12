package utils;

import java.util.Set;
import java.util.HashSet;

import domain.NFA;
import domain.State;

public class AutomatonTools {

    private Set<Character> alphabet;
    private int lowestAvailable;

    public AutomatonTools() {
        alphabet = new HashSet();
        lowestAvailable = 0;

        for (int i = (int) 'A'; i <= (int) 'z'; i++) {
            alphabet.add((char) i);
        }
        for (int i = (int) '0'; i <= (int) '9'; i++) {
            alphabet.add((char) i);
        }
    }

    public NFA transformIntoDFA(String pattern) {

        NFA dfa = new NFA();

        if (pattern.length() == 1 && (pattern.charAt(0) == '#' || alphabet().contains(pattern.charAt(0)))) {
            State s1 = new State(lowestAvailable);
            State s2 = new State(lowestAvailable + 1);
            lowestAvailable += 2;
            nfa.addTransition(s1, pattern.charAt(0), s2);

        }

        return dfa;
    }

    public static NFA createNegationFor(String pattern) {

        // first create a corresponding NFA
        NFA nfa = new NFA(pattern);

        // then transorm it into a DFA
        // then swap accepting and non-accepting states
        return nfa;
    }

}
