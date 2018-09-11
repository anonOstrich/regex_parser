package utils;

import domain.NFA; 

public class AutomatonTools {
    
    public static NFA createNegationFor(String pattern){
        
        // first create a corresponding NFA
        NFA nfa = new NFA(pattern);
        

        // then transorm it into a DFA
        
        
        // then swap accepting and non-accepting states
        
        return nfa; 
    }
    
    private static NFA transformIntoDFA(NFA nfa){
        NFA dfa = new NFA(); 
        
        return dfa; 
    }
    
}


