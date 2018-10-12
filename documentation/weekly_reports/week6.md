Week 6
======

Accomplished this week
----------------------
Improved usability hugely and began writing the final testing and implementation documents. The performance results are only the first ideas. 

 Gave more options to choose from in the UI, since now I implemented a wildcard operation. The added alternative is searching Frankenstein the book by a regular expression. 

Advancement of program
----------------------
* Escape character `/`. Now the test string may contain any operational or shorthand symbols, and their presence can be tested in writing regular expressions. E.g. to match only the string "!" you could define a regular expression with "/!". 

* Changed the implementation of empty transitions. 

* Added transitions with any single symbol to states. The states that can be reached this way are stored as their own attribute. This is to allow for the wildcard `.` without needing to build an NFA from a large union of symbols.  

* Added wildcard '.'. The wildcard makes it practical to search for a phrase anywhere in the test string: ".*ca.*" matches any string that contains "ca" at any position.

* Refactoring tests and some other classes. Performance increase by adjusting the size method of HashTable from O(n) to O(1). 

* Modified the UI to allow for searching an entire book with a regular expression. The book in question is the public domain Frankenstein. 


Sources of difficulty/frustration
---------------------------------
Trying to come up with performance comparison/testing ideas. Of course it's safe to record and present as many metrics as possible, but this part is definitely not my favorite part of the project. 

I am also in doubt about how good a choice this topic was. It feels pretty small compared to the game projects that I saw when doing peer reviews. By itself the regex parser is not very useful; it would need to return all the indexes where the test string matches the regular expression to be more functional. But at this point I aim to finalize what I've planned to do from the beginning... 


To do next
----------
* Continue with more performance tests
* Update and expand unit tests. Begin writing unit tests for the UI class. 
* Prepare for the presentation by tweaking and expanding the UI. 
* Try to find performance improvements. 
* Replace StringBuilder (which had somehow eluded replacing thus far)
* Begin writing the user guide. 


Time spent on the project
-------------------------

* 10.10.18: 3h, refactoring and beginning with escape characters
* 11.10.18: 3h, fully implementing escape characters, cleaning up code, extending the input alphabet
* 12.10.18: 6h, refactored how empty transitions are stored in a state. Added support for '.', corresponding to any single character when used as an operator. Wrote documentation, conducted performance tests. 

Total time: 12h
