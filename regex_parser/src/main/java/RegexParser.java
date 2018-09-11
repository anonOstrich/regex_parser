
import domain.NFA;

public class RegexParser {

    public static void main(String[] args) {
        String pattern = "Y";
        String test = "Y";
        NFA nfa = new NFA(pattern);
        System.out.println("Automaatin rakenne:\n" + nfa);
        
        System.out.println("NFA accepts the string " + test  +":\n" + nfa.accepts(test));
    }

}
