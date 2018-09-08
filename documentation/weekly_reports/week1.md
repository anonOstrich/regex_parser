Weekly report 1
===============

Accomplished this week
----------------------
The most important accomplishment was deciding the subject of the project. I feel I also familiarized myself with the different strategies of regular expression parsing, so my choice of using NFAs is well motivated. I now have a firm understanding of what I want to accomplish with the program, even if some details about the implementation of NDAs are still open. 
I also created the github repository and initialized the Maven project. The first files of documentation have been written. The design document in particular helped focus the scope of the project. 

Advancement of the program
--------------------------
Created the Maven project and added testing dependencies. Other than that, I'm yet to write the first line of Java code... 


What have I learned
-------------------
I learned that the term 'regular expression' can mean multiple different things. In theoritecial computer science regular expression produce all the regular language, whereas in many programming languages regular expressions can match strings with features that are not possible in regular languages. In the latter case regular expressions means roughly 'a template for some language/set of strings'. 

More importantly I feel I gained an appreciation for planning your project before beginning to code. Earlier I've usually began to work on the problem very fast and paused to think at the first problemati part. Since the requirements for the design document cover time complexity and used algorithms, I was forced to consider the problems in more detail than I would have done without the strict but benevolent guidance of the course. At this point I feel more confident, having spent considerable time considering the path before me. 

Sources of difficulty
---------------------
Some difficulty with estimating the time complexity. For most algorithms I was able to confirm my suspicions from other sources, but for Thompson's algorithm that was more difficult. I am not confident in my estimation, maybe in part because it's already been two years since I took the algorithms course. 

Choice of subject was also difficult, since it was not clear what types of subject require more comparisons other side stuff. 


Questions / confusion
---------------------
I will probably have to implement strings by myself?
Does the subject seem broad enough to not need many comparisons? I will compare my solution to the default Java regex-tools in any case, but I could also compare the performance to fully DFA-based solution if that is not enough.
Just making sure: text interface is enough? 


To do next
----------
The aim is to provide full functionality with existing data structure solutions, and only begin to swap in my own versions once the program functions as desired. The full functionality will definitely not be completed durign the next week, but I will be pleased if I have a roughly working NDA implementation that can process input strings of simple alphabet by the end of the week. 
I will focus on implementing the necessary classes and methods for that functionality. I should also write tests constantly alongside/before writing the code to be tested. For that purpose I'll have to do some rereading on testing, since I have usually not paid it the respect it deserves... 


Time spent on the project
-------------------------
~8h 