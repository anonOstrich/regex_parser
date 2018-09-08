Regular Expression Parser
=========================

Description
-----------
A program capable of deciding whether a string matches a given regular expression. Made for the data structures and algorithms project course at the University of Helsinki.

Supports the following symbols as the alphabet for the regular expression: 
* Lowercase letters
* Uppercase letters
* Numbers

Some symbols have special meanings in detailing the structure of the regular expression 
* `|` - union/or
* `*` - Kleene star (repeats however many times, 0 included)
* `()` - changing the priority, grouping different parts together
* `{}` - repeat specified number of times. E.g. 'a{1, 4}' means that a repeats 1-4 times. 
* `,` - used to separate arguments (see the previous example)
* `+` - repeats at least once
* `?` - repeats zero or one times
* `-` - one of the alphabet symbols in between. E.g. 'A-F' matches any of the following: 'A', 'B', 'C', 'D', 'E', 'F'
* `^` - complement/anything but  


Links
-----

* [Design Document](documentation/design_document.md)
* Implementation Document
* Testing Document
* User Guide

### Weekly reports 

* [Week 1](documentation/weekly_reports/week1.md)