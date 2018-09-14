package utils;

import domain.NFA;
import domain.State;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * Used to create a corresponding NFA to a regular expression.
 *
 * @author jesper
 */
public class NFAGenerator {

    private Map<String, NFA> cache;
    private boolean cacheEnabled;
    private Set<Character> alphabet;
    private Set<Character> operations;
    private int lowestAvailableId;

    public NFAGenerator(Set<Character> alphabet) {
        this(alphabet, true);
    }

    public NFAGenerator(Set<Character> alphabet, boolean cache_enabled) {
        this.cache = new HashMap();
        this.alphabet = alphabet;
        this.cacheEnabled = cache_enabled;
        Character[] supported_operations = {'*', '|', '(', ')', '&'};
        this.operations = new HashSet();
        this.operations.addAll(Arrays.asList(supported_operations));
    }

    public NFA generateNFA(String pattern) {
        if (cache.containsKey(pattern)) {
            return cache.get(pattern);
        }

        lowestAvailableId = 0;
        pattern = insertConcatenationSymbols(pattern);

        NFA result = new NFA();
        
        //magic stuff at the hear of the problem shall follow...
        
        
        return result;
    }

    public String insertConcatenationSymbols(String pattern) {

        StringBuilder sb = new StringBuilder(pattern); 
        char c1;
        char c2;

        for (int i = 0; i < sb.length() - 1; i++) {
            c1 = sb.charAt(i);
            c2 = sb.charAt(i + 1);

            if (alphabet.contains(c1) && alphabet.contains(c2)
                    || alphabet.contains(c1) && c2 == '('
                    || c1 == '*' && alphabet.contains(c2)
                    || c1 == '*' && c2 == '('
                    || c1 == ')' && c2 == '('
                    || c1 == ')' && alphabet.contains(c2)) {

                sb.insert(i + 1, '&');
            }

            
        }
        pattern = sb.toString(); 
        return pattern;
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public Set<Character> getOperations() {
        return operations;
    }

    public Map<String, NFA> getCache() {
        return cache;
    }

    public boolean getCacheEnabled() {
        return cacheEnabled;
    }
}
