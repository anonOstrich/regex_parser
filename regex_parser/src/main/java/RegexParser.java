import domain.NFA;

public class RegexParser {
    
    public static void main(String[] args) {
        NFA nfa = new NFA(); 
        String test = "101";
        System.out.println("NFA accepts the string 101:\n" + nfa.accepts(test));
    }
    
}
