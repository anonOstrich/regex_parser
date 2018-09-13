
import domain.NFA;
import java.util.Set; 
import java.util.HashSet; 
import domain.State; 

/**
 * Main class
 * 
 * @author jesper
 */
public class RegexParser {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        s0.addNextStateForSymbol('#', s1);
        s0.addNextStateForSymbol('#', s3);
        s1.addNextStateForSymbol('a', s2);
        s2.addNextStateForSymbol('#', s1);
        s2.addNextStateForSymbol('#', s3);
        Set<State> accepting = new HashSet(); 
        accepting.add(s3);
        NFA keene_nfa = new NFA(s0, accepting);
        boolean empty = keene_nfa.accepts(""); 
        boolean singleton = keene_nfa.accepts("a"); 
        boolean multiple = keene_nfa.accepts("aa");
        System.out.println(empty + "\n" + singleton + "\n" + multiple);
    }
    
    
    

}
