
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
        State s = new State(0);
        Set<State> a_states = new HashSet(); 
        a_states.add(new State(1));
        a_states.add(new State(2)); 
        s.addNextStatesForSymbol('a', a_states);
        s.addNextStatesForSymbol('b', new HashSet());
        s.addNextStateForSymbol('b', new State(3));
        
        
        System.out.println(s);
    }
    
    
    

}
