Testing document
================

Unit tests
----------
* JUnit tests for all classes except UI and the main class
* [Coverage and mutation tests with PIT](regex_parser/pitreport/index.html)


Performance tests
-----------------
* Aiming to compare performance with the default Java regular expressions
* Also with and without caching
* + Regular expressions without and with negation (negation necessitates DFA generation which will probably affect performance very adversely)

Presentation of test results
----------------------------
Some sort of graphical presentation is the aim. Will consider more once I have some preliminary performance results.
