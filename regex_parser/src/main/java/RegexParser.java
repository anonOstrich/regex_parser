
import domain.NFA;
import utils.AutomatonTools; 

public class RegexParser {

    public static void main(String[] args) {
        AutomatonTools creator = new AutomatonTools(); 
        String pattern = "Y";
        String test = "Y";
        NFA nfa = creator.transformIntoNFA(pattern);
        System.out.println("Automaatin rakenne:\n" + nfa);
        
        System.out.println("NFA accepts the string " + test  +":\n" + nfa.accepts(test));
    }

}
