Weekly report 2
===============


Accomplished this week
----------------------
Decided on the structure of the program, saw advancements in key areas. Didn't get stuck on any problem for a frustratingly long time.



Advancement of the program
--------------------------
From nothing to offering most of the core functionality. Created all the basic classes representing a state and its transitions, a nondeterministic finite automaton, and an NFA generator that transforms a regular expression string into an automaton. 

The state and NFA classes should be finished if no surprises come up. The algorithm for simulating the processing of an NFA on a given input is implemented and it works. So once the NFA has been constructed, the checking operation (whether the automaton accepts the input) works. Having said that, there might be opportunities to improve performance if the results discovered in the process of traversing states could be cached somehow... 

The construction from a regular expression into an NFA is still under construction. If the regular expression only contains symbols of the alphabet, Keene star, and union, the NFA generator returns an equivalent NFA - parentheses, useful shorthand notation, or negation is not supported yet. 

There is no user interface as of yet. 

What have I learned
-------------------

Writing extensive tests can point out issues fast. There was a null pointer exception issue that I caught because of a test I wrote, and I'm happy it didn't take longer to manifest. Similarly it wasn't until testing that I realized I had to consider states that might be reachable from a state by more than one empty transition in the method that adds to a set of states all the states that are reachable from them without symbols. 

Writing tests can be dull. But at least you don't have to concentrate as focusedly as when writing 'real code'. Trying writing more tests has still been more positive than negative experience. Before creating the NFAGenerator class I decided to write tests well ahead of time so I can quickly see after writing code whether the changes work as they should. 


Sources of difficulty
---------------------

I redid some parts of the program, having first decided to offer very little functionality in the State class, and then reversing the decision. I was not very far when I made the change, so luckily we're not talking about many hours. 

My own memory, since I forgot by which week day the week reports should be done... 

Questions / confusion
---------------------
-

To do next
----------
Modify Thompson's algorithm to allow for parentheses, after which the program will be pretty useful already. Then add to the NFAGenerator class a method to preprocess the regular expression to replace shorthands with longer equivalent strings: for instance, "a[1,3]" will be turned into "a|aa|aaa", and only after that will the construction of an equivalent NFA begin (it might be faster to not preprocess and take the issue into account in the creation process, but I will consider improvements once I have a fully functional program).

To implement negation I will have to create a method to transform an NFA into an equivalent DFA. After that accounting for negation is easy. 

My main aim for the next week is to achieve all the core functionality - support all the features that I wanted in regular expressions. 


Time spent on the project (as instructed, will be included in week report 3 as well)
------------------------------------------------------------------------------------

Around 16h, and thinking about the implementation of Thompson's algorithm in the back of my mind for a few hours more. 