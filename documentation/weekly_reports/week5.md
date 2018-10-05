Week 5
======

Accomplished this week
----------------------
Replaced Sets with own implementations. All the major data structures have now been replaced, and the program still passes the test! I added links to the PIT reports and JavaDoc to the documentation. Also began adding performance comparisons to the program, so the user may choose from the text UI if they want to compare preplanned tests, or match regexes of their own choice. 

Advancement of program
----------------------
Replaced the remaining data structures with my own ones. Fixed some error, wrote plenty of tests to cover my newly created data structure classes. Also tried to improve performance, in which I succeeded; I implemented caching in the NFA simulation, so if one NFA is used to match several strings, it may sometimes find ready in the cache all the following states from the current states with the current symbol. 

What I learned
--------------
Catching bugs and mistakes can be frustrating. My own HashSet implementation ended up creating (nearly) infite loops, because in transforming a nondeterministic automaton into deterministic, the program put multiple values for one key into HashMap; I did not remember that such a scenario happens, so it took quite a while to solve the issue. Of course, if I had implemented some sort of check that one key cannot have multiple values into the map itself, the issue could have been avoided...

Also: it's hard to beat default implementations. The difference in performance is vast, even though I admit my solutions are not optimised that well. 

Sources of difficulty/frustration
---------------------------------
Writing tests should be done gradually, not in a few big pushes - otherwise it can become boring.
Managing memory and choosing the optimal data structures. I am not sure if I need to make so many copies in caching or if there is any advantage to doubly linked lists. 
Trying to understand a long and complicated method is no fun, so I should have refactored it earlier on. The whole DFAGenerator is pretty ugly to look at, so I treat it more or less like a black box for now.  



To do next
----------
* Lots of performance testing
* Write good bases for testing and implementation documents
* Refactor code: especially the tests and DFAGenerator
* Begin working on graphical test presentation


Time spent on the project
-------------------------
2.10. 5h 
3.10. 3,5h
5.10. 3h

Total: 11,5h