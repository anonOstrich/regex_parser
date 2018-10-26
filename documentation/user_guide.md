User Guide
----------

Running the program
===================


### .jar
Clone the project or download just the [JAR FILE]. Run the program with the command "java -jar TIEDOSTONIMI". The program should start in the text terminal.


### in an IDE
Clone the project. At least with NetBeans you can select 'Open Project' and open the regex_parser in the IDE. Running in the IDE is probably best done by clicking a suitable button in the IDE, 'Run' in the case of NetBeans.


On the supported structure of regular expressions
=================================================

### alphabet
The alphabet is fairly restricted, since negation creates transitions for all the possible symbols from all states it creates, and it is performance-heavy enough as it currently is. 

* Upper-case letters A-Ö
* Lower-case letters a-ö
* Digits 0-9
* Miscellaneous symbols: `' ', '^', ';', '_', '@', '%', '{', '}', '=', '<', '>'`

### operations

`'&', '|', '*', '!', '('')'`

### shorthands
`'['']', '-', '+', '?'`



### literal and any character

There are two more special symbols: '.' and '/'. 

'/' is used as an escape character.  The symbol following this character is treated as part of the alphabet. So '/&' creates a regular expression that recognises only the string '&'. Notice that this escapes only the following character! You cannot escape a longer character sequence with one / even if it directly precedes parentheses. So '/(aba)' would not be a valid pattern for a regular expression, since it contains one closing parenthesis and zero opening parentheses. To achieve the wanted effect the pattern should be '/(aba/)'. If you don't need negation in the regular expression, you can include any symbol in the alphabet by escaping it first. '/€' might work. To avoid any chance of error I suggest remaining within the symbols listed in this section, however. 

'.' means that any single character will be accepted in this position. 'a.a' matches, among others, 'aya', 'a(a', a9a'. This is especially useful to use regular expressions to search for an occurrence of a pattern in a longer text: if we want to find out whether a text contains an exclamation ending with ' a dog!', we could match the whole text with the regular expression '.* a dog/!.*'

Constructing regular expressions
================================


### Order of evaluation

Different operations have different priorities in the following order: 
* `/` has most priority in one sense, since it will always target the very next character.
* `()`
* `!`
* `-`
* `*`, `[]`, `+`, `?`
* `&` or implicit concatenation 
* `|`

If you are unsure of how your expression will be evaluated, use parentheses! There is no fault in using too many, when unneeded they will be disregarded. 

### Detailed construction guide
Regular expressions are patterns that match possibly many strings, as long as they fit certain structural criteria. The simplest regular expressions contain only one alphabet character, and these regular expressions match only a string that contains that one character, and nothing else. More complex regular expressions can be formed by operations and shorthands symbols. Here follows a short description of each: 

* concatenation - writing two characters one after the other creates a regular expression that recognizes those two characters in the same order. Pattern 'ab' will match string 'ab' only. 
* `|` - union / or. Either of the regular expressions beside this symbol will match the regular expression. 'a|b' matches strings 'a' and 'b'.
* `*` - Kleene star. The preceding regular expression can repeat however many times, zero included. Pattern 'a*' matches '', 'a', 'aa', ... . 
* `()` - changing the priority, grouping different parts together. Pattern `(a|b)*` matches any string that consists of only a and b, so the union is evaluated before the Kleene star - without parenthesis this would be the union of `a` and `b*`
* `[min, max]` - repeat at lowest min times, and at most max times. Both limits are inclusive. You can leave one or even both limits as empty, in which case min is interpreted to be 0 and max infinity. Notice: this has higher priority than most operations, so the part to be repeated should be surrounded by parentheses if it is more than one character! Some examples: pattern `a[2,3]` will match `aa` and `aaa`. Pattern `a[,2]` will match empty string, `a`, and `aa`. Pattern `a[2,]` will match `aa`, `aaa`, `aaaa`, ... . 
* `,` - used to separate arguments (see the previous example).
* `+` - repeats at least once. Pattern `a+` matches `a`, `aa`, `aaa`, ... 
* `?` - repeats at most once (0 or 1 times). Pattern `a?` matches empty string and `a`.
* `-` - one of the symbols in between. Pattern `a-c` matches `a`, `b`, and `c`; pattern `4-7` matches strings `4`, `5`, `6`, and `7`. 
IMPORTANT! Both the first and second character must be from the same collection, and there are three possible collections: lowercase letters a-z, uppercase letters A-Z, digits 0-9. Other choices might not cause an error, but I cannot assure that the regular expression matcher works correctly. 
* `!` - negation/complements. Will match any strings that do NOT match the negated regular expression. Pattern `!a` will match all strings except `a`, and pattern `!(aa|b)` will match all strings excpect `aa` or `b`. 
* `#` - empty string. Pattern `a|#` will match `a` or the empty strnig.
* `.` - any single character will be accepted in this position. `a.a` matches, among others, `aya`, `a(a`, `a9a`. This is especially useful to use regular expressions to search for an occurrence of a pattern in a longer text: if we want to find out whether a text contains an exclamation ending with ` a dog!`, we could match the whole text with the regular expression `.* a dog/!.*`
* `/` - escape character.  The symbol following this character is treated as part of the alphabet. So `/&` creates a regular expression that recognises only the string `&`. Notice that this escapes only the following character! You cannot escape a longer character sequence with one / even if it directly precedes parentheses. So `/(aba)` would not be a valid pattern for a regular expression, since it contains one closing parenthesis and zero opening parentheses. To achieve the wanted effect the pattern should be `/(aba/)`. If you don't need negation in the regular expression, you can include any symbol in the alphabet by escaping it first. `/€` might work. To avoid any chance of error I suggest remaining within the symbols listed in this section, however. Notice: pattern processor does not adequately test if a character is escaped if there are multiple escape characters directly before it. If you wish to escape /, try (//)  parentheses might help. 

Different uses of the program
=============================
On opening, the program asks what you wish to do. There are three different basic operations at the moment, and also exiting the program.

### Performance comparisons

Choosing the first option leads to performance comparisons between my regular expression parser and the default one in Java. The idea is to match regular expression `a?[n,n]a[n,n]` ( = `a?` repeating n times followed by `a` repeating n time) with the test string `a....a` (a repeated n times). They do match, but only if all the 'a?' parts are empty strings. You are asked to provide a positive intege, which is the maximum for n. N is given all the values between 1 and the number of your choosing, and for every value the performance results are displayed. The same matching is done twice, so that the second time can make some use of caching. Both my own regular expression parser and the Java default one are tested, and the time it took to match is displayed in microseconds.

At 1..100 the comparisons will not take more than a few seconds, whereas with 1..200 they will take several minutes. 


### Free-form matching

The second option is to input your own regular expressions and test strings and see how they match. First you are asked to provide a pattern for creating a regular expression. There is no check in place to ensure that the input is valid, so you must make sure that it follows the structure of a valid regular expression. Otherwise the results might be wrong or there might occur an error which will restart the program.

Once you have given the first input string, you are now asked to provide a test string with which to match the regular expression. The program displayes performance informatino and the answer to whether the test string and the regular expression match. You are asked for more test inputs for the same regular expression for as long as you want to give them. Once you decide not to, hit enter twice. Now the program will ask if you want to give a new regular expression or exit, and you can choose by giving `y` or `n` as an answer. With `y` you can form a new regular expression, and `n` will take you back to the main menu where you can choose what to do with the program. 

### File search 

The third option is to search a text file for parts that match a given regular expression. First you are prompted to give a name of the file. If you press enter, the file will be the whole Frankenstein novel. See "Supported file formats" for how to add your own files. Once a correct filename has been given, the program will read its contents to its memory. Then you need to write a regular expression pattern that is searched - it will be surrounded by `.*` at the beginning and end, so if any section of the text matches, the program concludes the result of the search positive. At this point the program only informs the user about whether there is at least one matching section - it does not give the index of the first appearance or the surrounding context for the match. After seeing the results, you may choose if you want to search for a new regular expression in the same text. If you choose no, you will be returned to the main selection of the program. 




Supported file formats
======================

I have not tried too many, but .txt is guaranteed to work. Anything textual that only contains the supported symbols and line changes has a chance of working. Just make sure they are located in the src/main/resources/ folder before running the program (if you run the .jar file, you cannot add files of your own - a few examples are included in the package by default).