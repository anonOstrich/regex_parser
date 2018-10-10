package domain;

/**
 * A representation of a nondeterministic finite automaton.
 *
 * <p>
 * Used to simulate the processing of an NFA that recognizes the same language
 * that a specified regular expression generates. The information about all the
 * states is not easily accessible, but it should not be needed in the process
 * of parsing regular expressions.
 * </p>
 * <p>
 * Since all deterministic finite automata can be thought of as restricted NFA,
 * the class also represents DFA. Whether it is also a DFA is indicated in the
 * isDFA attribute
 * </p>
 *
 * @author jesper
 */
public class NFA {

    /**
     * An attempt to cache the implicit DFA that is travelled when simulating
     * the NFA Could decrease the performance requirements of simulation closer
     * to that of DFA: O(mn) -> O(n) where m is the number of states in the
     * automaton.
     *
     */
    private OwnMap<OwnSet<State>, OwnMap<Character, OwnSet<State>>> cache;

    /**
     * Whether simulated parts of the implicit DFA are stored and retrieved when
     * suitable.
     */
    private boolean cacheEnabled;

    /**
     * The state in which the automaton is prior to reading any input. The
     * following states are determined from the transition information contained
     * in the startingState
     */
    private State startingState;

    /**
     *
     * The set of states that is used to determine whether the automaton accepts
     * or rejects an input string. If any of the possible states at the end of
     * processing belongs to acceptingStates, the NFA accepts the input.
     *
     */
    private OwnSet<State> acceptingStates;

    /**
     * Special attribute that is false by default and only needed with some
     * regexes that contain negation. If true, accepting states actually
     * indicates all the states that are NOT accepting: every other state is
     * accepting, in such a case.
     */
    private boolean inverted;

    /**
     * Indicates whether the NFA meets the stricter criteria of DFA. The NFA can
     * theoretically be DFA even with this value being false; the important part
     * is that true means it absolutely certainly is.
     */
    private boolean isDFA;

    /**
     * Creates an empty NFA.
     */
    public NFA() {
        this(new State(0), new OwnSet());
    }

    /**
     *
     * Creates the NFA specified by the state information in parameters.
     *
     * @param startingState The initial state of the NFA
     * @param acceptingStates These states result in acceptance
     */
    public NFA(State startingState, OwnSet<State> acceptingStates) {
        this(startingState, acceptingStates, false);
    }

    /**
     *
     *
     * @param startingState The initial state
     * @param acceptingStates All states that lead to acceptance
     * @param isDFA If created object is certain to be DFA, this should be true.
     * False by default.
     */
    public NFA(State startingState, OwnSet<State> acceptingStates, boolean isDFA) {
        this(startingState, acceptingStates, isDFA, true);
    }

    public NFA(State startingState, OwnSet<State> acceptingStates, boolean isDFA, boolean cacheEnabled) {
        this.startingState = startingState;
        this.acceptingStates = acceptingStates;
        this.isDFA = isDFA;
        this.cacheEnabled = cacheEnabled;
        cache = new OwnMap();
        inverted = false; 
    }

    /**
     *
     * Changes the starting state the given state.
     *
     * @param state New starting state
     */
    public void setStartingState(State state) {
        this.startingState = state;
    }

    /**
     *
     *
     * @return State at which NFA begins.
     */
    public State getStartingState() {
        return this.startingState;
    }
    
    public void enableCaching(){
        this.cacheEnabled = true; 
    }
    
    public void disableCaching(){
        this.cacheEnabled = false; 
    
    }
    
    public OwnMap<OwnSet<State>, OwnMap<Character, OwnSet<State>>> getCache(){
        return this.cache; 
    }

    /**
     *
     * Changes the accepting states to the given set.
     *
     * @param states Set of new accepting states.
     */
    public void setAcceptingStates(OwnSet<State> states) {
        this.acceptingStates = states;
    }

    /**
     *
     * @return The set of accepting states.
     */
    public OwnSet<State> getAcceptingStates() {
        return this.acceptingStates;
    }

    /**
     *
     * Returns whether the NFA accepts or rejects the input string.
     *
     *
     * <p>
     * Simulates the operation of the NFA step by step when given the test
     * string as input. The method keeps track of all the possible states that
     * the NFA could be in at any given step. At first the method initializes
     * the set of currents states to include only the starting state of the NFA.
     * </p>
     * <p>
     * For every character, the method first adds to the current states all the
     * states that can be reached with empty symbol from current states. Then
     * the method queries each current state for the set of states that can be
     * accessed with the input symbol. These sets of states from each current
     * state are combined to form the set of all the states that the automaton
     * can be in after it has processed the next symbol character. Then the
     * current states are replaced with the next states, and the next symbol of
     * the test string is processed similarly until they end.
     * </p>
     * <p>
     * After exhausting all the characters, the method adds to the set of
     * current states all the states that are reachable from them with empty
     * symbols - these are also possible states at the end of the processing.
     * </p>
     * <p>
     * If at any point the set of current states is empty, it is certain that
     * the automaton cannot finish in an accepted state. Hence the method
     * immediately returns false.
     * </p>
     *
     * @param test Input string which is to be processed. If the string is
     * empty, it is replaced with character '#', which represents epsilon (empty
     * symbol).
     *
     * @return True if any of the final states is in the set of accepting
     * states. Otherwise false.
     */
    public boolean accepts(String test) {
        OwnSet<State> currentStates = new OwnSet();
        currentStates.add(startingState);
        addEpsilonTransitionsOfStates(currentStates);
        OwnSet<State> nextStates = new OwnSet();
        //Used to momentarily store the pointer to the current set, so that current set and next set point to different sets
        //at the end of each cycle
        OwnSet<State> empty;

        for (int i = 0; i < test.length(); i++) {
            char symbol = test.charAt(i);
            
           
            if (cacheEnabled) {
                if (cache.containsKey(currentStates) && cache.get(currentStates).containsKey(symbol)) {
                    currentStates = cache.get(currentStates).get(symbol).copy();
                    continue;
                }
            }

            for (State currentState : currentStates) {
                nextStates.addAll(currentState.getNextStatesForSymbol(symbol));
            }

            addEpsilonTransitionsOfStates(nextStates);

            if (cacheEnabled) {
                if (!cache.containsKey(currentStates)) {
                    cache.put(currentStates.copy(), new OwnMap());
                }
                cache.get(currentStates).put(symbol, nextStates.copy());
            }
            empty = currentStates;
            currentStates = nextStates;
            nextStates = empty;
            nextStates.clear();

            if (currentStates.isEmpty()) {
                return false;
            }
        }

        return containsAcceptingState(currentStates);
    }

    /**
     *
     * Expands the parameter set with all states that can be reached from its
     * states without reading any input.
     *
     * <p>
     * If a state can be reached by a number of epsilon/# transitions from any
     * of the states of the set, is is added to the same set. Uses a helper
     * method to discover transitions of different lengths.
     * </p>
     *
     *
     * @param states Set of states to be possible expanded
     */
    public void addEpsilonTransitionsOfStates(OwnSet<State> states) {
        addEpsilonTransitionsOfStates(states, new OwnSet());
    }

    /**
     * Adds to the given states the unvisited states that are reachable with one
     * epsilon transition
     *
     * <p>
     * Forms a new empty set. By going through the given states, the method then
     * adds to this set of new states all the states that are reachable with one
     * epsilon transition and that are not in the visitedStates set. This
     * decreases some extra work and prevents infitinte loops in cases where two
     * states have an epsilon transition from and to each other.
     * </p>
     * <p>
     * If new states are found, the method calls itself recursively with the set
     * of new states and the same set of visited states. This continues for as
     * long as new states are discovered. Each call tries to discover longer
     * sequences of empty transitions.
     * </p>
     *
     * @param states Set of states that caller wants to expand.
     * @param visitedStates States that have already been considered.
     */
    public void addEpsilonTransitionsOfStates(OwnSet<State> states, OwnSet<State> visitedStates) {
        OwnSet<State> newStates = new OwnSet();
        for (State s : states) {
            if (!visitedStates.contains(s)) {
                newStates.addAll(s.getNextStatesForSymbol('#'));
                visitedStates.add(s);
            }
        }
        if (newStates.size() > 0) {
            addEpsilonTransitionsOfStates(newStates, visitedStates);
        }
        states.addAll(newStates);
    }

    /**
     *
     * @param isDFA Change the indicator of whether the NFA is also DFA
     */
    public void setIsDFA(boolean isDFA) {
        this.isDFA = isDFA;
        if(isDFA){
            this.disableCaching();
        } else {
            this.enableCaching(); 
        }
    }

    /**
     *
     * @return Whether the NFA is certain to be DFA
     */
    public boolean isDFA() {
        return isDFA;
    }
    
    public boolean usesCaching(){
        return this.cacheEnabled; 
    }

    /**
     * The accepting states changes in the following manner, depending on
     * whether inverted is true or false: accepting -> non-accepting (false)
     * non-accepting -> accepting (true)
     */
    public void invert() {
        this.inverted = !this.inverted;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    /**
     * Depending on the inverted bit, correctly returns whether the given set of
     * states contains an accepting state. By default inverted is false, so
     * accepting states indicates actual accepting states; method returns true
     * only if that set contains any state of the input state. Vice versa when
     * inverted is true.
     *
     * @param states
     * @return
     */
    public boolean containsAcceptingState(OwnSet<State> states) {
        for (State s : states) {
            if (inverted != acceptingStates.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        NFA comp = (NFA) o;

        if (inverted != comp.isInverted()) {
            return false;
        }
        if (isDFA != comp.isDFA()) {
            return false;
        }

        if (!startingState.equals(comp.getStartingState())) {
            return false;
        }

        if (!acceptingStates.equals(comp.getAcceptingStates())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int code = 7;
        code = 31 * code + startingState.hashCode();
        code = 31 * code + acceptingStates.hashCode();
        code = 31 * code + 7 * (isDFA ? 1 : 0);
        code = 31 * code + 7 * (inverted ? 1 : 0);
        return code;
    }
}
