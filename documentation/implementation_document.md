Implementation document
=======================

General structure
-----------------
### Text UI


### Preprocessing regular expression string

Replaces all shorthands that help writing more complex regular expressions so that only '|', '*', '&' and '!' remain. The NFAGenerator needs to construct a fewer number of different NFA this way. Consists of three phases: replacing the shorthands, removing some of the unncesessary negations, and adding the concatenation symbol '&' wherever different parts are meant to be concatenated. 

#### Replacing shorthands

Inspects the input string one character at a time. If the character is the escape character '/', the character following it is skipped. If the character is one of the shorthand characters ('-', '[', ']', '+', '?'), the first thing that's done is determining the part of the pattern that is influence by the shorthand operation. That is either the character preceding the current character, the two preceding characters ('/+' for instance) or everything contained in parentheses that end just before the current index. Then replacemet depends on the shorthand: 


##### ?

##### +

##### -

##### [ 


#### Removing unnecessary negations

The processor scans the pattern string and if two '!' symbols follow each other (except when the first is escaped with '/') they are both removed. The method is not sophisticated enough to deal with parentheses, so it cannot remove every unnecessary negation. Since negation requires constructing a very costly DFA, it is well worth it to try to avoid negation when possible.

The pattern string is scanned once, so the time complexity is O(n).

#### Adding concatenation symbols

The string is inspected two characters at a time, and a lengthy condition checks whether they are meant to be concatenated. To ease the process, if '/' is encountered, it and the following character are enclosed in parentheses. 

Clearly O(n) time complexity. 


### Constructing a nondeterministic finite automaton

### Constructing a negation deterministic finite automaton (when needed)

#### Caching and NFA that have been negated before

#### Powerset algorithm

#### Negating the regular expression

### Simulating the automaton on test input



Space complexity of data structures
-----------------------------------

### Stack 

### Linked List

### HashTable

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