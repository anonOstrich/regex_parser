Weekly report 3
===============

Accomplished this week
----------------------
Hit a major milestone: the necessary functions for parsing regular expressions is in place. JavaDoc covers everything but the UI class. Program works at a basic level. 



Advancement of the program
--------------------------
NFAGenerator class is good enough for a functioning regex parser. I wrote tests alongside coding, and they should cover most of the written code. Updated JavaDoc to better reflect the current state of the program. The code could use some refactoring and especially improved error handling, since I have not prepared for encountering syntactically incorrect expressions. The UI has basic funtionality for quering a regular expression and a test string, and informing the user whether they match. 


What I learned
--------------
It is very pleasant to do changes when the tests are already in place - you can quickly tell if the change breaks things. 
Learned more concretely about converting any NFA to an equivalent DFA after implementing such a method by myself. The trick is storing enough states to keep track of any combination of states that the NFA can be in at one time. 



Sources of difficulty
---------------------

Mainly the DFAGenerators process for converting an NFA into a complement DFA. It would be more efficient to determine if the given NFA is already a DFA, since in that case the only changes would be swapping the accepting and non-accepting states with each other. However, at the moment there is no easy way to access all the states of an NFA to get a set of non-accepting states. For the moment I postponed the problem since the general method works with DFAs too, even if it could be considerably faster. 

New introduced shorthand symbols enable more ambiguous patterns than earlier; the user might sometimes be surprised by matching. This is because if there is any way to interpret the test string to match the regex, it will be matched. For instance, I wondered for a good while what was wrong with the program when test string `carWnafta` matched the regular expression `(car)+W?!(nafta|#)`: clearly the W is present and the following part matches `nafta|#`, so it should not be accepted. However, the program interprets that empty string matches `W?`, and the following `Wnafta` matches `!(nafta|#)`, which it of course does match. One must be very carefully think of all possible interpretations. 


Questions / confusion
---------------------



To do next
----------
Begin creating own data structures and replacing readymade ones. Some of the first ones are going to be Map and Set classes, since they are so often used. 

Also refactoring code somewhat. There is duplicate code in places and functionality from long methods could be further divided into multiple smaller ones. 

Text UI for entering regexes by hand or for giving the path of a file.  



Time spent on the project
-------------------------
18.9.2018: 5h 

19.9.2018: 7h, rewrote some parts of DFAGenerator class multiple times

20.9.2018: 5h

21.9.2018 1h

Total: 18h


Time spent during the second week (also in the 2nd week report)
---------------------------------------------------------------
16h