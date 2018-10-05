Testing document
================

Unit tests
----------
* JUnit tests for all classes except main class
* [Coverage and mutation tests with PIT](https://htmlpreview.github.io/?https://github.com/anonOstrich/regex_parser/blob/master/documentation/pitreport/index.html)


Performance tests
-----------------
* Aiming to compare performance with the default Java regular expressions 
* Also with and without caching
* + Regular expressions without and with negation (negation necessitates DFA generation which will probably affect performance very adversely)

Presentation of test results
----------------------------
Some sort of graphical presentation is the aim. Will consider more once I have some preliminary performance results.
At the moment there is a comparison of my implementation and the default implementation with pattern: "(a?){n}a{n}", matched with aa...a (n times). This shows how poorly my solution scales in comparison to the default, and also with biggern numbers of n the differences with/without different types of caching are easy to appreciate. I might have to devise more difficult cases and then draw a graph, with n increasing on the horizontal axis. 

