Implementation document
=======================

General structure
-----------------
### Text UI


### Preprocessing regular expression string

Replaces all shorthands that help writing more complex regular expressions so that only '|', '*', '&' and '!' remain. The NFAGenerator needs to construct a fewer number of different NFA this way. Consists of three phases: replacing the shorthands, removing some of the unncesessary negations, and adding the concatenation symbol '&' wherever different parts are meant to be concatenated. Worst case time complexity is probably O(n), but some shorthands will result in significantly longer strings than others. For example, "(a-z)[0, 999]" will form 1000 different concatenations separated by union; each element of these concatenations is a union of 26 symbols. 

#### Replacing shorthands

Inspects the input string one character at a time. If the character is the escape character '/', the character following it is skipped. If the character is one of the shorthand characters ('-', '[', ']', '+', '?'), the first thing that's done is determining the part of the pattern that is influence by the shorthand operation. That is either the character preceding the current character, the two preceding characters ('/+' for instance) or everything contained in parentheses that end just before the current index. Then replacemet depends on the shorthand: 


##### ?
The affected part may appear at most once. The string "(ab)?" would be replaced with "((ab)|#)".


##### +
The affected part must appear once or more. The string "(ab)+" would be replaced with "((ab)(ab)*)".


##### -
Any of the characters 'in between' will do. Both characters surrounding this symbol must belong to the same category of the following three: small letters, capitalized letters, digits. The replacements is done by union, so it's possible to construct a large replacement if the two symbols are far apart. The string "a-g" would be replaced by "(g|f|e|d|c|b|a)".


##### [min, max]
The affected part may be repeated at least min times and at most max times. Min or max may be left empty, in which case min is interpreted as zero and max is infinity (may repeat however many times). If max is present, the replacement occurs in this manner: "a[2,4]" -> "(aaaa|aaa|aa)"; if minimum is absent or zero, the empty string is allowed as well: "a[,2]" -> "(aa|a|#)". If max is absent, the Kleene star may be used and the construction is very easy: "a[10,]" -> "(aaaaaaaaaaa*)". 



#### Removing unnecessary negations

The processor scans the pattern string and if two '!' symbols follow each other (except when the first is escaped with '/') they are both removed. The method is not sophisticated enough to deal with parentheses, so it cannot remove every unnecessary negation. Since negation requires constructing a very costly DFA, it is well worth it to try to avoid negation when possible.

The pattern string is scanned once, so the time complexity is O(n).

#### Adding concatenation symbols

The string is inspected two characters at a time, and a lengthy condition checks whether they are meant to be concatenated. To ease the process, if '/' is encountered, it and the following character are enclosed in parentheses. 

Clearly O(n) time complexity. 


### Constructing a nondeterministic finite automaton

After the pattern string has been preprocessed, an NFA recognizing that language must be constructed. If NFAGenerator uses caching (default option) and the preprocessed pattern string has been used as a blueprint for an NFA before, the automaton stored in the cache is returned without further consideration. Otherwise NFAGenerator initializes the tools for constructing an NFA. The construction algorithm constructs increasingly complex NFA by using two stacks: an operational stack (stores characters symbolizing operations, e.g. '&') and an automaton stack (storing NFA). The algorithm scans the pattern string character by character and manipulates the contents of the stacks in a manner that depends on the symbol.

#### Constructing simplest automata

If the inspected character belongs to the supported alphabet of the program, a simple NFA that corresponds to that symbol is constructed. If the symbol is a normal alphabet symbol, e.g. 'Y' or '4', this means an NFA that has a starting state and finishing state and only one transition that can be traversed with the symbol in question. If the symbol is '#' or '.' (empty symbol or any single symbol, respectively) an NFA that can move from starting state to the accepting state with empty symbol / any single symbol is created; these are their own methods, since states store this information slightly differently. In addition, if the read symbol is '/', that symbol is disregarded and the next symbol is used to create an NFA as if it were a normal alphabet symbol. For example, if the next two symbols are '/&', an NFA is created that can only change from starting state to accepting state by reading the symbol '&'. Having created any of these NFA, the algorithm then pushes the created automaton to the automaton stack. 

#### Combining simple automata

A very different sequence of events occures if the algorithm reads an operational symbol: '&', '|', '*', '(', ')', or '!'. Once an operational symbol is read, the operations indicated by the operation symbols on top of the stack are evaluated as long as they have higher priority than the symbol that was read. For example, '&' has higher priority than '|' because 'a&b|c&c' is meant as '(a&b)|(c&c)'. Once all the operations with higher priority have been evaluated, the current symbol is pushed to the operation stack for later evaluation.

Evaluation depends on the operation. In the case of '&', '|' or '*' [Thompson's algorithm](https://en.wikipedia.org/wiki/Thompson%27s_construction) is applied in a straightforward manner. First the correct number of operands are popped from the automaton stack in the right order, and by using their starting states, accepting states, and a few new states and transitions indicated by Thompson's algorithm, a new NFA is constructed. This slightly more complex NFA is then pushed to the automaton stack.

Symbol '(' has the lowest priority and is not considered until ')' is encountered. Then the operations in the operation stack are evaluated until '(' is encountered and discarded. Now the contents between these parentheses has been evaluated and the corresponding NFA is pushed to the top of the automaton stack. 

Negation, symbolized with '!', is a special case that has the potential to exponentially increase time and space requirements. The evaluation is explained in detail in the next subsection.

Once all the symbols of the pattern string have been read, the operations left in the operation stack are evaluated in order. Once it is empty, the automaton stack contains only one NFA, which corresponds to the whole pattern string (if the regular expression pattern is correctly constructed). This is then stored in cache and returned.


### Constructing a negation deterministic finite automaton (when needed)

Sometimes it is easy to replace a regular expression with negation. If there is only one negation that negates the rest of the expression, the regular expression can be matched with a test string, and if the negation is deemed to match when the rest of the expression does not match. However, always working strategies are not as easy, and I found no other solution for my needs than to convert the nondeterministic finite automaton into a deterministic one and then swapping accepting and non-accepting states with one another.


#### Caching and NFA that have been negated before

When possible, construction of negation automata should be avoided, so the resulting automata are cached. Each NFA has information about whether they are also DFA. If they are, the negation process is very fast, since accepting states become non-accepting and vice versa. This is done by swapping the value of a boolean attribute in the NFA to indicate that the set of states that earlier indicated accepting states now means the non-accepting states. 


#### Powerset algorithm

Fairly standard [powerset construction algorithm](https://en.wikipedia.org/wiki/Powerset_construction) for creating a deterministic finite automaton that recognizes exactly the same language as some nondetermistic finite automaton. Only sets of states that the nondeterministic automaton can ever be in are considered, so the number of states in the resulting DFA may be lower than 2^|number of states in the NFA|. If some set of states has already been inspected, it won't be inspected again: no more information can be obtained. 



#### Negating the regular expression

If an NFA has to be converted with Thompson's algorithm, each subset of states is made an accepting state of the DFA being made if NONE of those states is accepting in the NFA. Similarly, a subset of states is NOT an accepting state in the DFA if any of those states is accepting in the NFA. 



### Simulating the automaton on test input

The useful part of the program is using the constructed automata to match regular expressions and strings; to discover if a certain string matches the pattern of some regular expression. The problem is equal to giving the test string as input to a finite automaton that has been constructed using the way described above.

#### Basics

To simulate a nondeterministic automaton the simulation algorithm keeps track of all the possible states that the automaton may be in at any given moment. Before reading any input the automaton is in the starting state. The cycle of simulation begins by expanding the current set of states: in it are added all the states that can be reached by following only empty transitions, so without the need for any input symbols. Then the next character in the test string is read. A new set of states is formed by examining each of the current possible states: the states that are reachable with the input symbol from all of the current states are joined together to form the next set of states that the automaton can be in. 

Once all the input characters have been exhausted, the final set of possible states is extended with empty transitions. Then the method returns true if this set contains any of the accepting states (since there is at least one path of choices that leads to an accepting state), and false otherwise. In addition, if at any point the current set of states is empty, there is no possibility of reaching other states so the simulation can be halted. 


#### DFA vs NFA

The construction of a DFA can take significantly longer than that of NFA: however, the worst-case time complexity for simulating the workings of automata favors DFA, since in that case reading one symbol leads to exactly one transition from the current state to the state where the appropriate transitions guides operation. The time complexity is always O(n) where n is the length of the test string. In the nondeterministic version the machine may be at most in all of its states at one point, so the worst-case scenario is O(n*|number of states|) without further techniques. The worst-case scenario holds for a newly-created NFA in my implementation. 

#### Caching parts of the implicitly constructed DFA

With the preceding description every simulation of the same test string can take significantly longer with an NFA than DFA - with enough match cheking, the initial requirements to generate a DFA might be offsetted by the speedy simulation process. However, when an NFA calculates the set of states that can be reached from another set of states by a given symbol, this corresponds to discovering a transition between two states in a DFA that could be formed by Thompson's algorithm. Many such implicit fragments of the deterministic automaton may be encountered, and my program attempts to store them to speed up the simulation of similar events in the future. 

The NFA instances have a HashMap cache whose keys are sets of states, and values HashMaps. These value maps use characters as keys and sets of states as values. When simulating the workings of the automaton with some input string, the method first checks if the cache contains the current set of states as a key, and the current symbol as the key in the value map. If this is the case, the NFA  has calculated the state that it should end up in, and it can be immediately retrieved from the value map. Otherwise the method must resort to discovering all the possible states from the current states with transitions that match the current symbol. In that case the end result, after being extended with empty transitions, is stored in the cache. There might be room for better performance here: currently the sets used as keys in the cache are copies, since the original sets usually undergo changes, which makes for really poor keys. Copying takes its time, especially with large automata. 




Space complexity of data structures
-----------------------------------

### Stack 

If we only count the stack, it always has the same number of pointers. In using stack there need to be n nodes that contain the contents of the stack, however, so the space complexity is O(n).

### Linked List

Very similarly O(n)

### HashTable

The size of the array is proportional to the number of elements that HashTable contains. Every index of that table contains a Linked List whose length is only as long as needed to store all its contents. The size complexity is O(n). 


Time complexity of data structures
----------------------------------

### Stack

Very standard implementation where the stack has a pointer to its topmost node. It never has to worry about the data in other nodes. All operations O(1). 

### Linked List

* search: if the key is not found, the whole list is inspected element by element. Clearly O(n). 
* insert: Depends on whether multiple similar keys are guarded against. If they are not, the time requirement is O(1) since every added element is placed at the beginning of the list, and how many elements there are beyond the first doesn't affect the method at all. If multiple keys are prevented, the method first searches the list for existing nodes with the given key. This takes O(n) in the worst case, so the time requirement of the whole method is O(n).
* delete(node): the method only changes the pointers in the previous and following nodes, so the time is constant. O(1). 
* delete(key): Otherwise the same, but first the method calls search for locating the correct node. Therefore O(n). 
* size: O(1)

### HashTable

* put: If rehashing is not needed, O(m) where m is the length of the table in the index determined by the hash code of the key. Since the maximum expected value for m is 0.7 (the maximum load factor), the method is O(1). However, if rehashing is needed the time complexity is O(n), where n is the total number of elements. 
* rehash: The method initiates a new table whose size is proportional to the number of elements in the current table. Each index of the old table is inspected, and when there is a collision list, all its contents are put to the new table. The size of the old table is also proportional to the number of elements n. Putting will not necessitate new rehashing, so its time complexity is O(1). All in all the time complexity is O(n).
* remove: calculating the correct index takes O(1), and removing the element takes O(m) where m is the length of the list. Assuming evenly distributed hash codes, the expected value for m is the load factor, which will never exceed 0.7. So in theory the total time requirement is O(1).
* scaledHashCode: After retrieving the hash code of the key, the method only performs two divisions. So the time complexity of the hash code method of the parameter class will determine the time complexity of scaledHashCode. Mainly the code should be fast to calculate, so probably most of the time O(1). 
* needsRehashing: O(1) since only basic arithmetic. 
* putAll: The parameter table is inspected element by element, so the total time requirement is usually O(m) where n is the number of elements in the parameter table. If rehashing is needed when putting elements to the table whose method is called, the time complexity is O(max{n, m}) where n is the number of elements in the table that new elements are added to. 
* clear: O(1)

#### HashMap

* get: O(1), since the method is very similar to remove of HashTable.
* containsKey: O(1), the same as HashSet's contains method. 
* copy: O(n) since each index is inspected and their number is directly proportional to the total number of elements. 


#### HashSet

* contains: O(1), since calculating the index takes constant time and the expected length of the collision list is under 1. 
* copy: O(n) as with HashMap
* iterator: O(n), since in constructing the iterator each element is inspected. 
* smarter uses of hashmaps. Now when HashTables are used as keys, perheps some needless copies are created to avoid modifying a key. 

Suggestions for improvement
---------------------------

* Implement also by backtracking method and offer new funcionality that way.
* In itself the parser is not very useful. Maybe it would be wise to construct a class that uses the parser to find all instances in a text and return their indexes, or something similar. 
* There is clearly room for performance improvements, although I don't have anything easy and impressive in mind... A good first step would be to study existing regex parsers in detail. 
* Let the user choose any file they want to search for matches


Sources
-------
* [https://swtch.com/~rsc/regexp/regexp1.html](https://swtch.com/~rsc/regexp/regexp1.html)
* [https://en.wikipedia.org/wiki/Nondeterministic_finite_automaton#Implementation](https://en.wikipedia.org/wiki/Nondeterministic_finite_automaton#Implementation)
* [https://en.wikipedia.org/wiki/Thompson%27s_construction](https://en.wikipedia.org/wiki/Thompson%27s_construction)
* [https://en.wikipedia.org/wiki/Powerset_construction](https://en.wikipedia.org/wiki/Powerset_construction)