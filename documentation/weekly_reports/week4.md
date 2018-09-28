Week 4
====== 

Accomplished this week
----------------------
Began work on the most important data structures. Tried to decrease the performance requirements of negations slightly by two different ways. If there are multiple negations following one another, they can be removed in pairs. If an automaton that has been negated is to be negated again, the method responsible for creating negation automata takes a significant shorcut: it simply swaps accepting and non-accepting states. 

Advancement of the program
--------------------------
Added some performance enhancements, entirely removed Java's default stack classes with ones that I created myself, and implemented the majority of hash map class. Tried to keep up with the tests while creating new code. Not everything has appropriate JavaCode at the moment. 

What I learned
--------------
Reacquainted myself with how hash tables work. I had somehow not considered that I would have to handle collission with some method. I opted for collision lists since they are more intuitive to me. 


Sources of difficulty/frustration
---------------------------------
This week's lack of time to spend on the project. Also, perhaps I didn't have clear plans for what to begin implementing first, so at this point I feel more disorganized than I did at the beginning. As an example of the lack of organisation I implemented doubly-linked linked list for collision lists of hash map, although at least for now they don't offer me any advantages over singly linked lists - they would, if I deleted an element whose address I already know.

Questions
---------
* Do strings need to be replaced with own implementation?
* How about classes like Integer, Character, etc.? 

To do next
----------
* Implement the rest of the methods for own HashSet and replace the default implementation with my own
* Implement own set structure and replace HashSet with it
* Try to cache something useful from the process of simulating NFA (implicit construction of part of DFA -> storing explicitly that part for reuse)
* Add functionality for reading files with test data

Time spent of the project
-------------------------
25.9. 1h
26.9. 1,5h
27.9. 6h (some time spent on reading about caching implicit DFA, although the implementation of that has not began at all)
28.9. 1,5h
Total: 10h