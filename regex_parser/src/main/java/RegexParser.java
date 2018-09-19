
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
        String pattern = "(a|cd*)!(a*)"; 
        String test = ""; 
        NFAGenerator generator = new NFAGenerator(default_alphabet());
        NFA nfa = generator.generateNFA(pattern);
 
        
        boolean happens = !nfa.accepts("aa") && nfa.accepts("aba") && nfa.accepts("cruntti");
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
