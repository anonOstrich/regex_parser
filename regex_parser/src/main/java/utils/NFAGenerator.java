package utils;

import domain.NFA;
import domain.State;
import domain.OwnStack; 
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * Offers the tools to create nondeterministic finite automata from regular
 * expressions.
 *
 */
public class NFAGenerator {

    /**
     *
     * Store for already constructed automata. If the same regular expression is
     * used more than once, the resulting NFA is quickly accessible.
     *
     */
    private Map<String, NFA> cache;

    /**
     * Tells whether cache is used to potentially speed up generation.
     *
     */
    private boolean cacheEnabled;

    /**
     *
     * The allowed symbols in the input that are not control characters.
     *
     */
    private Set<Character> alphabet;

    /**
     * Set containing supported operations symbols.
     */
    private Set<Character> operations;
    /**
     * Lowest positive integer that is not the id of any created state.
     */
    private int lowestAvailableId;
    /**
     * Utility class for preprocessing input patterns to such a form that they
     * only contain operations, alphabet symbols, and #.
     */
    private PatternProcessor patternProcessor;

    /**
     * A generator that offers tools for creating automata for the complement
     * languages of other automata.
     */
    private DFAGenerator dfaGenerator;

    /**
     * Sets cache on by as the default.
     *
     * @param alphabet Set of supported non-operational symbols.
     */
    public NFAGenerator(Set<Character> alphabet) {
        this(alphabet, true);
    }

    public NFAGenerator() {
        this(null);
    }

    /**
     *
     * Initializes all the necessary constructs. Supported operations and
     * shorthands are hardcoded, since there is no need to change them in the
     * scope of this project.
     *
     * @param alphabet Set of supported non-operational symbols.
     * @param cache_enabled Whether cache should be used or not.
     */
    public NFAGenerator(Set<Character> alphabet, boolean cache_enabled) {
        if (alphabet == null) {
            alphabet = new HashSet();
            for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
                alphabet.add((char) i);
            }

            for (int i = (int) 'a'; i <= (int) 'z'; i++) {
                alphabet.add((char) i);
            }

            for (int i = (int) '0'; i <= (int) '9'; i++) {
                alphabet.add((char) i);
            }

        }

        this.cache = new HashMap();
        this.alphabet = alphabet;
        this.cacheEnabled = cache_enabled;
        Character[] supported_operations = {'*', '|', '&', '(', ')', '!'};
        this.operations = new HashSet();
        this.operations.addAll(Arrays.asList(supported_operations));
        Character[] supported_shorthands = {'+', '?', '[', '-'};
        Set<Character> shorthands = new HashSet();
        shorthands.addAll(Arrays.asList(supported_shorthands));
        this.patternProcessor = new PatternProcessor(alphabet, shorthands);
        dfaGenerator = new DFAGenerator(-1);
    }

    /**
     *
     * @param pattern
     * @return
     */
    public NFA generateNFA(String pattern) {
        //add explicit concatenation symbols and see if the pattern has been encountered before
        pattern = patternProcessor.elongateRegularExpression(pattern);
        if (cache.containsKey(pattern) && cacheEnabled) {
            return cache.get(pattern);
        }

        //initialization of tools
        lowestAvailableId = 0;
        OwnStack<Character> operationStack = new OwnStack();
        OwnStack<NFA> NFAStack = new OwnStack();

        for (int i = 0; i < pattern.length(); i++) {
            char currentSymbol = pattern.charAt(i);

            if (alphabet.contains(currentSymbol) || currentSymbol == '#') {
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
        while (!operationStack.isEmpty()) {
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
     * Depending on the operation one or more operands are popped off the stack.
     * From them a new NFA is created according to the rules of the popped
     * operation symbol. The created NFA is then pushed onto the NFA stack.
     * </p>
     *
     * @param operationStack stack of operations
     * @param automatonStack stack of operands for the operations
     * @return true if the operation symbol is valid and there are enough
     * operands to be popped. False if a failure is encountered.
     */
    public boolean evaluate(OwnStack<Character> operationStack, OwnStack<NFA> automatonStack) {
        if (operationStack.peek() == null) {
            return false;
        }
        char operation = operationStack.pop();
        NFA result = new NFA();

        if (operation == '&') {
            evaluateConcatenation(automatonStack, result);
        }
        if (operation == '|') {
            evaluateUnion(automatonStack, result);
        }
        if (operation == '*') {
            result = evaluateKleeneStar(result, automatonStack);
        }
        if (operation == ')') {
            evaluateParentheses(operationStack, automatonStack);
            return true;
        }
        if (operation == '!') {
            result = dfaGenerator.generateComplementDFA(automatonStack.pop(), alphabet);
        }
        automatonStack.push(result);
        return true;
    }

    /**
     * Evaluates everything in the operationstack before opening parenthesis.
     * The NFA created from the last operation will be on top of the automaton
     * stack at the end.
     *
     * @param operationStack
     * @param automatonStack
     */
    private void evaluateParentheses(OwnStack<Character> operationStack, OwnStack<NFA> automatonStack) {
        while (operationStack.peek() != '(') {
            evaluate(operationStack, automatonStack);
        }
        operationStack.pop();
    }

    /**
     * Evaluates Kleene star and returns the resulting NFA.
     *
     * @param result
     * @param automatonStack
     * @return
     */
    private NFA evaluateKleeneStar(NFA result, OwnStack<NFA> automatonStack) {
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
        return result;
    }

    /**
     * Evaluates the union operation by popping the two automata on top of the
     * automaton stack.
     *
     * @param automatonStack
     * @param result
     */
    private void evaluateUnion(OwnStack<NFA> automatonStack, NFA result) {
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

    /**
     * Evaluates concatenation operation. Since stacks are LIFO, the second
     * operand is the topmost.
     *
     * @param automatonStack
     * @param result
     */
    private void evaluateConcatenation(OwnStack<NFA> automatonStack, NFA result) {
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

    /**
     *
     * Helper method for evaluating the operations in the proper order.
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

        if (operation2 == '(') {
            return false;
        }
        if (operation1 == ')') {
            return true;
        }

        if (operation1 == '!') {
            return true;
        }

        return false;
    }

    /**
     * Creates a simple NFA that recognizes only the input symbol.
     *
     * @param symbol Character that the NFA must accept
     * @return NFA that has two states: starting state, and accepting state,
     * which can be reached only by the input symbol.
     */
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

    /**
     * Creates the simplest NFA that accepts only empty string.
     *
     * @return NFA that has its accepting state as starting state. There is only
     * one state in total.
     */
    public NFA generateNFAFromEmptyString() {
        State s = new State(lowestAvailableId);
        lowestAvailableId++;
        Set<State> acceptingStates = new HashSet();
        acceptingStates.add(s);
        NFA result = new NFA(s, acceptingStates);
        return result;
    }

    /**
     *
     * @return Set containing allowed non-operational symbols.
     */
    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<Character> alphabet) {
        this.alphabet = alphabet;
    }

    /**
     *
     * @return Set containing supported operation symbols.
     */
    public Set<Character> getOperations() {
        return operations;
    }

    /**
     *
     * @return Cache - all the automata that the generator has produced, if
     * cacheEnabled has been true.
     */
    public Map<String, NFA> getCache() {
        return cache;
    }

    /**
     *
     * @return True if the generator uses cache, false otherwise.
     */
    public boolean getCacheEnabled() {
        return cacheEnabled;
    }

}
