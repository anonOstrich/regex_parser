package utils;

import domain.NFA;
import domain.State;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
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
        //add explicit concatenation symbols and see if the pattern has been encountered before
        pattern = insertConcatenationSymbols(pattern);
        if (cache.containsKey(pattern) && cacheEnabled) {
            return cache.get(pattern);
        }

        //initialization of tools
        lowestAvailableId = 0;
        Deque<Character> operationStack = new LinkedList();
        Deque<NFA> NFAStack = new LinkedList();

        //magic stuff at the hear of the problem shall follow...
        for (int i = 0; i < pattern.length(); i++) {
            char currentSymbol = pattern.charAt(i);

            if (alphabet.contains(currentSymbol)) {
                NFAStack.push(generateNFAFromOneSymbol(currentSymbol));
            }

            if (operations.contains(currentSymbol)) {

                while (operationStack.peek() != null && hasPrecedence(operationStack.peek(), currentSymbol)) {
                    evaluate(operationStack, NFAStack);
                }

                operationStack.push(currentSymbol);

            }

        }

        //evaluate remaining operations
        while (operationStack.peek() != null) {
            evaluate(operationStack, NFAStack);
        }

        // returning result
        NFA result = NFAStack.pop();
        if (cacheEnabled) {
            cache.put(pattern, result);
        }
        return result;
    }

    /**
     *
     * Processes the topmost operation of the stack
     *
     * <p>
     * Depending on the operation one or more operands are popped of the stack.
     * From them a new NFA is created according to the rules of the popped
     * operation symbol. The created NFA is then pushed onto the NFA stack.
     * </p>
     *
     * @param operationStack stack of operations
     * @param automatonStack stack of operands for the operations
     * @return true if the operation symbol is valid and there are enough
     * operands to be popped. False if a failure is encountered.
     */
    public boolean evaluate(Deque<Character> operationStack, Deque<NFA> automatonStack) {
        if (operationStack.peek() == null) {
            return false;
        }

        char operation = operationStack.pop();
        // OR: I could just modify an operand NFA and avoid creating a new object. 
        // Should save resources, implement once functionality is in place. 
        NFA result = new NFA();

        if (operation == '&') {
            // first pop -> was added second to the stack!
            NFA second = automatonStack.pop();
            NFA first = automatonStack.pop();

            State start = first.getStartingState();
            Set<State> accepting = second.getAcceptingStates();

            for (State s : first.getAcceptingStates()) {
                s.addNextStateForSymbol('#', second.getStartingState());
            }
            result.setStartingState(start);
            result.setAcceptingStates(accepting);

        }

        if (operation == '|') {
            NFA second = automatonStack.pop();
            NFA first = automatonStack.pop();

            State start = new State(lowestAvailableId);
            lowestAvailableId++;
            State finish = new State(lowestAvailableId);
            lowestAvailableId++;

            start.addNextStateForSymbol('#', first.getStartingState());
            start.addNextStateForSymbol('#', second.getStartingState());

            for (State s : first.getAcceptingStates()) {
                s.addNextStateForSymbol('#', finish);
            }

            for (State s : second.getAcceptingStates()) {
                s.addNextStateForSymbol('#', finish);
            }

            Set<State> acceptingStates = new HashSet();
            acceptingStates.add(finish);
            result.setStartingState(start);
            result.setAcceptingStates(acceptingStates);
        }

        if (operation == '*') {
            result = automatonStack.pop();
            State newStart = new State(lowestAvailableId);
            lowestAvailableId++;
            State newFinish = new State(lowestAvailableId);
            lowestAvailableId++;
            Set<State> newAcceptingStates = new HashSet();
            newAcceptingStates.add(newFinish);
            newStart.addNextStateForSymbol('#', result.getStartingState());
            newStart.addNextStateForSymbol('#', newFinish);

            for (State fState : result.getAcceptingStates()) {
                fState.addNextStateForSymbol('#', result.getStartingState());
                fState.addNextStateForSymbol('#', newFinish);
            }

            result.setStartingState(newStart);
            result.setAcceptingStates(newAcceptingStates);
        }

        automatonStack.push(result);
        return true;
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

    /**
     *
     * Helper method for evaluating the operations in generateNFA in the proper
     * order.
     *
     * @param operation1 symbol for operation
     * @param operation2 symbol for compared operation
     * @return true if operation1 has precedence over operation2. Otherwise
     * false.
     */
    public boolean hasPrecedence(char operation1, char operation2) {

        if (operation1 == '*' && operation2 != '*') {
            return true;
        }

        if (operation1 == '&' && operation2 == '|') {
            return true;
        }

        return false;
    }

    public NFA generateNFAFromOneSymbol(char symbol) {
        State s0 = new State(lowestAvailableId);
        lowestAvailableId++;
        State s1 = new State(lowestAvailableId);
        lowestAvailableId++;
        Set<State> finishingStates = new HashSet();
        finishingStates.add(s1);
        s0.addNextStateForSymbol(symbol, s1);
        NFA result = new NFA(s0, finishingStates);
        return result;
    }

    public NFA generateNFAFromEmptyString() {
        return generateNFAFromOneSymbol('#');
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
