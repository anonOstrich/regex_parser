package utils.generators;

import utils.structures.OwnSet;
import utils.structures.OwnMap;
import domain.NFA;
import domain.State;
import utils.structures.OwnStack;
import utils.PatternProcessor;
import utils.Utilities;

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
    private OwnMap<String, NFA> cache;

    /**
     * Tells whether cache is used to potentially speed up generation.
     */
    private boolean cacheEnabled;

    /**
     *
     * The allowed symbols in the input that are not control characters.
     *
     */
    private OwnSet<Character> alphabet;

    /**
     * Set containing supported operations symbols.
     */
    private OwnSet<Character> operations;
    /**
     * Lowest positive integer that is not the id of any created state.
     */
    private int lowestAvailableId;
    /**
     * Utility class for preprocessing input patterns
     */
    private PatternProcessor patternProcessor;

    /**
     * A generator that offers tools for creating automata for the complement
     * languages of other automata.
     */
    private DFAGenerator dfaGenerator;

    /**
     * Sets cache on by as the default.
     */
    public NFAGenerator() {
        this(true);
    }

    /**
     *
     * Initializes all the necessary constructs.
     *
     * @param cache_enabled Whether cache should be used or not.
     */
    public NFAGenerator(boolean cache_enabled) {

        this.cache = new OwnMap();
        this.cacheEnabled = cache_enabled;

        this.alphabet = Utilities.defaultAlphabet();

        this.operations = Utilities.defaultBasicOperations();
        this.patternProcessor = new PatternProcessor();
        dfaGenerator = new DFAGenerator(-1);
    }

    /**
     *
     * Constructs an automaton from a regular expression expressed as string
     * 
     * <p>First preprocesses the pattern to contain only a few operations and
     * alphabet symbols. Then the string is scanned once. Each alphabet symbol
     * puts a simple NFA onto the automaton stack, and each operational symbol 
     * puts an operation symbol to the operation stack. The contents of the
     * operation stack are evaluated in the order of their priority and recency.</p>
     * 
     * <p>Each operation unites or otherwise modifies simpler automata in the
     * automaton stack into more complex ones, and places them back on the top of the stack.
     * After the whole string has been scanned, the rest of the operations
     * in the operation stack are evaluated. Only the automaton equal to the
     * whole regular expression lies in the automaton stack afterwards.</p>
     * 
     * @param pattern Pattern that guides the construction of the automaton.
     * @return NFA that recognizes the correct language.
     */
    public NFA generateNFA(String pattern) {
        pattern = patternProcessor.elongateRegularExpression(pattern);   
        if (cacheEnabled && cache.containsKey(pattern)) {
            return cache.get(pattern);
        }

        lowestAvailableId = 0;
        OwnStack<Character> operationStack = new OwnStack();
        OwnStack<NFA> NFAStack = new OwnStack();

        for (int i = 0; i < pattern.length(); i++) {
            char currentSymbol = pattern.charAt(i);

            if (currentSymbol == '/') {
                NFAStack.push(generateNFAFromOneSymbol(pattern.charAt(i + 1)));
                i++;
                continue;
            }

            if (currentSymbol == '#') {
                NFAStack.push(generateNFAFromEmptySymbol());
            }

            if (currentSymbol == '.') {
                NFAStack.push(generateNFAFromAnySingleSymbol());
            }

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

        while (!operationStack.isEmpty()) {
            evaluate(operationStack, NFAStack);
        }
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
            result = dfaGenerator.generateComplementDFA(automatonStack.pop());
        }
        automatonStack.push(result);
        return true;
    }

    /**
     * Evaluates everything in the operation stack before opening parenthesis.
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
        OwnSet<State> newAcceptingStates = new OwnSet();
        newAcceptingStates.add(newFinish);
        newStart.addStatesReachableWithoutSymbols(result.getStartingState());
        newStart.addStatesReachableWithoutSymbols(newFinish);
        
        for (State fState : result.getAcceptingStates()) {
            fState.addStatesReachableWithoutSymbols(result.getStartingState());
            fState.addStatesReachableWithoutSymbols(newFinish);
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

        start.addStatesReachableWithoutSymbols(first.getStartingState());
        start.addStatesReachableWithoutSymbols(second.getStartingState());

        for (State s : first.getAcceptingStates()) {
            s.addStatesReachableWithoutSymbols(finish);
        }

        for (State s : second.getAcceptingStates()) {
            s.addStatesReachableWithoutSymbols(finish);
        }

        OwnSet<State> acceptingStates = new OwnSet();
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
        NFA second = automatonStack.pop();
        NFA first = automatonStack.pop();

        State start = first.getStartingState();
        OwnSet<State> accepting = second.getAcceptingStates();

        for (State s : first.getAcceptingStates()) {
            s.addStatesReachableWithoutSymbols(second.getStartingState());
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
        OwnSet<State> finishingStates = new OwnSet();
        finishingStates.add(s1);
        s0.addNextStateForSymbol(symbol, s1);
        NFA result = new NFA(s0, finishingStates);
        return result;
    }
    
    
    /**
     * Creates a simple NFA that recognizes only empty input
     * 
     * @return Simple NFA
     */
    private NFA generateNFAFromEmptySymbol(){
        State s0 = new State(lowestAvailableId);
        lowestAvailableId++;
        State s1 = new State(lowestAvailableId);
        lowestAvailableId++;
        OwnSet<State> finishingStates = new OwnSet();
        finishingStates.add(s1);
        s0.addStatesReachableWithoutSymbols(s1);
        NFA result = new NFA(s0, finishingStates);
        return result;
    }

    /**
     * Creates a simple NFA that recognizes any single character
     * 
     * @return Simple NFA
     */
    private NFA generateNFAFromAnySingleSymbol() {
        State s0 = new State(lowestAvailableId);
        lowestAvailableId++;
        State s1 = new State(lowestAvailableId);
        lowestAvailableId++;
        OwnSet<State> finishingStates = new OwnSet();
        finishingStates.add(s1);
        s0.addStatesReachableWithAnyCharacter(s1);
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
        OwnSet<State> acceptingStates = new OwnSet();
        acceptingStates.add(s);
        NFA result = new NFA(s, acceptingStates);
        return result;
    }

    /**
     *
     * @return Set containing allowed non-operational symbols.
     */
    public OwnSet<Character> getAlphabet() {
        return alphabet;
    }

    /**
     * Change the alphabet
     * 
     * @param alphabet 
     */
    public void setAlphabet(OwnSet<Character> alphabet) {
        this.alphabet = alphabet;
    }

    /**
     *
     * @return Set containing supported operation symbols.
     */
    public OwnSet<Character> getOperations() {
        return operations;
    }

    /**
     *
     * @return Cache - all the automata that the generator has produced, if
     * cacheEnabled has been true.
     */
    public OwnMap<String, NFA> getCache() {
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
