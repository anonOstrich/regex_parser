Weekly report 3
===============

Accomplished this week
----------------------
Hit a major milestone: the necessary functions for parsing regular expressions is in place.



Advancement of the program
--------------------------
NFAGenerator class is good enough for a functioning regex parser. I wrote tests alongside coding, and they should cover most of the written code. Updated JavaDoc to better reflect the current state of the program. The code could use some refactoring and especially improved error handling, since I have not prepared for encountering syntactically incorrect expressions. 


What have I learned
-------------------




Sources of difficulty
---------------------

Mainly the DFAGenerators process for converting an NFA into a complement DFA. It would be more efficient to determine if the given NFA is already a DFA, since in that case the only changes would be swapping the accepting and non-accepting states with each other. However, at the moment there is no easy way to access all the states of an NFA to get a set of non-accepting states. For the moment I postponed the problem since the general method works with DFAs too, even if it could be considerably faster. 


Questions / confusion
---------------------



To do next
----------
Begin creating own data structures and replacing readymade ones. Some of the first ones are going to be Map and Set classes, since they are so often used. 

Also refactoring code somewhat. There is duplicate code in places and functionality from long methods could be divided into multiple smaller ones. 

Text UI for entering regexes by hand or for giving the path of a file.  



Time spent on the project
-------------------------
18.9.2018: 5h 
19.9.2018: 7h, rewrote some parts of DFAGenerator class multiple times
20.9.2018: 


Time spent during the second week (also in the 2nd week report)
---------------------------------------------------------------
16h