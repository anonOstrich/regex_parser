
import domain.NFA;
import java.util.Set;
import java.util.HashSet;
import domain.State;
import utils.NFAGenerator;

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
        NFAGenerator generator = new NFAGenerator(default_alphabet());
        NFA testNFA = generator.generateNFA("aa|b|ca");
        System.out.println(generator.insertConcatenationSymbols("aa|b|ca"));
        
        
        System.out.println(testNFA.accepts("aa"));
        System.out.println(testNFA.accepts("b"));
        System.out.println(testNFA.accepts("a"));
        System.out.println(testNFA.accepts("bb"));
        System.out.println(testNFA.accepts("ca"));
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
