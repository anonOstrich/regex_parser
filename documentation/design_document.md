Design Document
===============

Defining the problem
--------------------
The core of the program's purpose is to determine whether a regular expression matches a string. Regular expression has sligthly different meanings in theoretical computer science and in practical programs - in the latter even some non-regular languages can be matched with features such as backreferencing, which are lacking in the theoretical model. My implementation follows the more restricted definition and is thus limited to regular languages. The alphabet (set of possible characters) available for input string is also limited.
At first the program should support regular expressions with lowercase or uppercase letters or numbers. At least symbols '(', ')', '|', '*', '{', '}', '+', '?', '-', and '^' are reserved for construction of  regular expressions. 


Inputs and their uses
---------------------
Two string inputs: a regular expression string and a  test string against which to match it. There may be however many such pairs, and they can be given either as a text file or directly via the text interface of the program. 

The regular expression string is first modified so that it only contains concatenation, union (|), star(*) and complement(^). The alphabet for the automata is the set of symbols in the regular expression input string that are not reserved symbols. 

The simplified regular expression string is then used to build a nondeterministic finite automaton (NFA) whose accepted language is the same that the regular expression produces.

Then the program simulates the operation of the NFA on the test string. If the automaton accepts the string (at least one of the finishing states is an accepting one), the program returns true. In the case of the NFA not accepting the string, the program returns false.  

Chosen algorithms and data structures
-------------------------------------

### Algorithms

#### General strategy
I chose to implement regular expression parsing with finite automata instead of backtracking, since the worst case performance is clearly superior and they interest me more. As a result my regular expressions lack some commonly used features. The most optimal algorithm would probably first determine if the regular expression contains features that cannot be realized with finite automata, and only in such cases use backtracking algorithms. Such a program would never perform worse than mine, and would enable more extensive patterns. 

#### Detailed algorithm choices
The slight modifications in the regular expression string are fairly simple string manipulation. E.g. replacing "a{1,3}" with "a|aa|aa". Even if the string data structure has to be implemented by hand the existing array features are sufficient. 

For creating the NFA from the simplified regular expression string I shall use [Thompson's construction algorithm](https://en.wikipedia.org/wiki/Thompson%27s_construction). Even though [Glushkov's construction algorithm](https://en.wikipedia.org/wiki/Glushkov%27s_construction_algorithm) results in a somewhat smaller NFA, I chose Thompson since I'm familiar with all the concepts and the algorithms itself (it was studied and used by pen and paper in the course Models of Computation). There is a slight modification to the algorithm: since I allow negations of regular expressions, they need their own processing. In the case of a negation, say '^A', the program will first generate an NFA based on the regular expression A. This will then be turned into an FDA with the [powerset construction algorithm](https://en.wikipedia.org/wiki/Powerset_construction), after which the accepting and non-accepting states are swapped with one another. 

There are two possibilities at this point: keep the NFA and simulate its operation, or convert the NFA into a DFA first. With DFA the simulation of the automaton is possible in linear time, since each character causes exactly one transition. However, if the NFA has n states, the resulting DFA could have up to 2^n states - the worst case scenario of the conversion is very demanding indeed. The simulation of the NFA is slower, since we need to keep track of all the possible states that the NFA is in. At any single moment there cannot be more states than the total number of states in the NFA, however. In doing the simulation, we implicitly create the DFA, but with lower memory requirements. In both cases it is possible to cache the parsed automata for later use (if the same regular expression is reused). With explicitly constructed DFA the caching is simpler: use a map, and store the regular expression string as key, and the constructed DFA as value. 

This program uses nondeterministic finite automata when possible. In this way the memory requirements stay lower and the worst-case scenario is often faster.  

In the simulation, we move from one state to the next as indicated by the transitions of the automaton. Since the automaton is nondeterministic, we have to keep track of all the states that it could currently be in.

### Data structures

I will probably implement nondeterministic finite automata and their states as their own classes. In doing so I will need at least maps and sets. Since the order of the elements or keys will be irrelevant, the implementations HashMaps and HashSets. HashMaps will also be a practical way of implementing a cache for regular expression string and the corresponding automata. 

A stack will be handy in Thompson's algorithm. 



Target time complexity
----------------------
Let m = length of the regular expression string and n = length of the test string.  

The worst case is that the regular expression is of the form '^A', where A is a regular expression. In that case the program will create a deterministic finite automaton that can have over 2^m states, and this single most demanding step has the time complexity of O(2^m). Matching the string in this case takes only O(n), however. The overall time worst case time complexity remains an unfortunate O(max{2^m, n})


There are situations where the program performs better. If we exlude negations and also all repetition shorthands except for the star (since 'a{1, 100⁹}' would cause a vast number of states in the NFA), the time complexity drops. 
The creation of the NFA from the regular expression string should take O(m), since the length of the regular expression string determines how many times the NFA stack is used (O(1)) and  NFA are modified/joined (O(1)). As [Xing](http://people.wku.edu/guangming.xing/xing-thesis.pdf) states: "... Thompson gave a linear time and space construction to convert a regular expression to an equivalent NFA with epsilon-transitions, ...." My target is an implementation that reaches this. 

Simulating the NFA on test strings has the worst case time complexity of O(mn). Since this is the most demanding single step, it is also the time complexity of the whole program. 

Target space complexity
-----------------------
Let n and m retain their meanings.

Again, in the worst case there are 2^m states in the resulting NFA. The space complexity of the algorithm is therefore O(2^m).

With the same restrictions as applied with the time complexity is O(m): during the simulation, the maximum number of states that are in memory is all of them, which is O(m).

Sources
-------
* https://en.wikipedia.org/wiki/Glushkov%27s_construction_algorithm
* https://en.wikipedia.org/wiki/Thompson%27s_construction
* https://en.wikipedia.org/wiki/Powerset_construction
* https://swtch.com/~rsc/regexp/regexp1.html
* https://en.wikipedia.org/wiki/Regular_expression
* http://people.wku.edu/guangming.xing/xing-thesis.pdf 