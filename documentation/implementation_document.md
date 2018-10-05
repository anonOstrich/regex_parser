Implementation document
=======================

General structure
-----------------
* Read string describing a regular expression
* Preprocess the string to only contain basic operations that are easy to translate to equivalent automata.
* Construct a nondeterministic finite automaton that recognizes the exact language that the regular expression produces. 
* Simulate how the NFA processes a string to be matched with the regular expression. If the finishing state is accepting, they match. 
* Output the result.

Achieved space complexity
-------------------------
Worst case scenario is unfortunately O(2^n), since I handle negation by first constructing a deterministic finite automaton that might have O(2^m) states, where m is the number of states in the NFA created on the basis of the regular expression. 


Achieved time complexity
------------------------



Suggestions for improvement
---------------------------
* Implement also by backtracking method and offer new funcionality afforded that way.
* Allow more characters than alphabets and letters: also an escape character, so that operation symbols can be used also as alphabet symbols. 

Sources
-------
* [https://swtch.com/~rsc/regexp/regexp1.html](https://swtch.com/~rsc/regexp/regexp1.html)
* [https://en.wikipedia.org/wiki/Nondeterministic_finite_automaton#Implementation](https://en.wikipedia.org/wiki/Nondeterministic_finite_automaton#Implementation)