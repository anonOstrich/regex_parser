Testing document
================

Unit tests
----------
Most classes and methods are tested with JUnit unit tests. The UI will be tested, but I have not begun working on those tests yet. A report of test coverage and mutation testing: 
* [Coverage and mutation tests with PIT](https://htmlpreview.github.io/?https://github.com/anonOstrich/regex_parser/blob/master/documentation/pitreport/index.html)


Performance tests
-----------------
### Comparisons with Java's default regex tools

Both my solution and the default Java tools have the same task: to match `(a?){n}a{n}` with `aa...a` (n times). The same query is made twice, so that second time the tools take advantage of cached results. Below are the average results of three comparison runs. The times are in milliseconds. 

| N           | Own (1st time)  | Own (2nd time) | Default(1st time)  |  
| :---------: |:-------------:|:----------------:| :----------------: |
| 10 | 14 | 3 | 1  |
| 20  | 29  | 6   | 1 |
| 30 |   16   |  9  | 0 |
| 40 | 21 | 7 | 0 |
| 50  | 24  | 15  | 0 |
| 60 | 85   |  12  | 0 |
| 70 |  79 |  25  | 0 |
| 80 | 78   | 18   | 0 |
| 90 |  207 | 111 | 0 |
| 100 | 303  | 10 | 0 |


[Also as a graph](performance/performance_chart.pdf)


A few things are clear: my implementation gives no competition to the defaults, and caching improves performance significantly. There are some weird details, for instance when n = 20 takes more time thatn n = 50 or the variation in how much caching helps. 






* + Regular expressions without and with negation (negation necessitates DFA generation which will probably affect performance very adversely)
* Finding short and devious expressions, like `!((a-z)[10,20])` compared to easy ones.



