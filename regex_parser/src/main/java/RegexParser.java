
import domain.NFA;
import java.util.Set;
import java.util.HashSet;
import domain.State;
import utils.NFAGenerator;
import utils.DFAGenerator; 

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
        String pattern = "a"; 
        String test = ""; 
        NFAGenerator generator = new NFAGenerator(default_alphabet());
        NFA nfa = generator.generateNFA(pattern);
        DFAGenerator dGenerator = new DFAGenerator(-1); 
        NFA dfa = dGenerator.generateComplementDFA(nfa, default_alphabet());
        
        boolean happens = dfa.accepts(test);
        System.out.println("Pattern: " + pattern + ";  test: " + test + "; Result: " + happens);
       // generator.diagnosticMethod();
        

    }

    public static Set<Character> default_alphabet() {
        Set<Character> result = new HashSet();

        for (int i = (int) 'A'; i <= (int) 'z'; i++) {
            result.add((char) i);
        }

        for (int i = (int) '0'; i <= (int) '9'; i++) {
            result.add((char) i);
        }

        return result;
    }

}
