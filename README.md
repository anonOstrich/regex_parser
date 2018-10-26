Regular Expression Parser
=========================

Description
-----------
A program capable of deciding whether a string matches a given regular expression. Made for the data structures and algorithms project course at the University of Helsinki. More of a learning experience than a serious alternative for existing solutions. The method of evaluation is based on simulating finite automata that recognize the same regular language than the regular expression.

Supports lowercase and uppercase letters, numbers and most commonly used symbols. 

Some symbols have special meanings in detailing the structure of the regular expression 

* `|` - union / or.
* `*` - Kleene star (repeats however many times, 0 included)
* `()` - changing the priority, grouping different parts together
* `[min, max]` - repeat between min and max times
* `,` - used to separate arguments (see the previous example)
* `+` - repeats at least once
* `?` - repeats at most once (0 or 1 times)
* `-` - one of the symbols in between
* `!` - complement / anything but 
* `#` - empty string
* `.` - any single symbol
* `/` - escape character

Installation
------------
Required: 
* Java 8
* Maven, if you want the build the project yourself

### Run the program
Copy the github repository. Inside it run 'java -jar regex_parser.jar' to open the program in terminal. 

### Modify and build the program
Copy the github repository. When you want to build the project, run 'mvn clean install' in the terminal. The .jar files and documentation will be created in the target folder. 

Links
-----

* [Design Document](documentation/design_document.md)
* [Implementation Document](documentation/implementation_document.md)
* [Testing Document](documentation/testing_document.md)
* [User Guide](documentation/user_guide.md) - contains a much more detailed guide on how to build a valid regular expression!
* [JavaDoc](https://htmlpreview.github.io/?https://github.com/anonOstrich/regex_parser/blob/master/regex_parser/apidocs/overview-summary.html) 

### Weekly reports 

* [Week 1](documentation/weekly_reports/week1.md)
* [Week 2](documentation/weekly_reports/week2.md)
* [Week 3](documentation/weekly_reports/week3.md)
* [Week 4](documentation/weekly_reports/week4.md)
* [Week 5](documentation/weekly_reports/week5.md)
* [Week 6](documentation/weekly_reports/week6.md)
