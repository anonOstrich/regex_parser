/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import domain.OwnSet; 
import domain.OwnStack; 

/**
 * Class containing methods for preprocessing patterns for regular expressions.
 *
 *
 * @author jesper
 */
public class PatternProcessor {

    /**
     *
     * Set of all allowed input characters that are not operations, shorthands
     * or #.
     *
     */
    private OwnSet<Character> alphabet;

    private OwnSet<Character> shorthandSymbols;

    public PatternProcessor(OwnSet<Character> alphabet, OwnSet<Character> shorthandSymbols) {
        this.alphabet = alphabet;
        this.shorthandSymbols = shorthandSymbols;
    }

    /**
     * Transforms the input string so that it is easy for NFAGenerator to
     * process
     *
     * <p>
     * If the pattern is empty, the empty symbol ('#') is returned. Otherwise
     * the shorthand symbols are replace with a longer string that only contains
     * the basic operations. Then concatenation symbols are explicitly inserted
     * to where that operation takes place. The modified parameter string is
     * then returned.
     * </p>
     *
     *
     * @param pattern Human-written pattern that is easy to read
     * @return The input string in a processed form based on which it is easy to
     * construct an NFA
     */
    public String elongateRegularExpression(String pattern) {
        if (pattern.isEmpty()) {
            pattern = "#";
            return pattern;
        }
        pattern = replaceShorthands(pattern);
      //  pattern = removeUnnecessaryNegations(pattern);
        pattern = addConcatenationSymbols(pattern);
        return pattern;
    }

    /**
     *
     * Replaces useful shorthands with the basic operations that NFAGenerator
     * supports
     *
     * @param pattern String that possibly contains shorthand symbols
     * @return The input pattern with all shorthand symbols replaced with basic
     * operations
     */
    public String replaceShorthands(String pattern) {
        StringBuilder sb = new StringBuilder(pattern);

        for (int i = 0; i < sb.length(); i++) {
            char currentSymbol = sb.charAt(i);
            if (shorthandSymbols.contains(currentSymbol)) {
                String affectedPart = determineAffectedPart(sb.toString(), i - 1);

                if (currentSymbol == '+') {
                    i = replacePlus(sb, i, affectedPart);
                }
                if (currentSymbol == '?') {
                    i = replaceQuestionmark(sb, i, affectedPart);
                }
                if (currentSymbol == '-') {
                    i = replaceBetween(sb, i);
                }
                if (currentSymbol == '[') {
                    i = replaceRepetitions(sb, i, affectedPart);
                }

            }
        }

        pattern = sb.toString();
        return pattern;
    }

    /**
     * 
     *  Replaces '-' with all the possible values in between (inclusive) separated by union
     * 
     * 
     * @param sb Contains the string that is being simplified
     * @param i index that tells what character of the sb is considered
     * @return index that points to the next character in the sb after the inserted unions and closing parenthesis
     */
    private int replaceBetween(StringBuilder sb, int i) {
        char first = sb.charAt(i - 1);
        char last = sb.charAt(i + 1);
        sb.delete(i - 1, i + 2);
        i--;
        //this could be done by setting all these to be transition symbold in a single NFA...
        sb.insert(i, "()");
        i++;
        for (int j = (int) first; j <= (int) last; j++) {
            sb.insert(i, "|" + (char) j);
        }
        sb.deleteCharAt(i);
        return i;
    }

    
    /**
     * Replaces '?' with union of the affected part with the empty symbol, surrounded by parentheses.
     * 
     * 
     * @param sb Contains the string that is being simplified
     * @param i index that tells what character of the sb is considered
     * @affectedPart The part of sb that ? operates on
     * @return index that points to the next character in the sb after the inserted unions and closing parenthesis
     */
    private int replaceQuestionmark(StringBuilder sb, int i, String affectedPart) {
        sb.deleteCharAt(i);
        sb.insert(i, "|#)");
        sb.insert(i - affectedPart.length(), "(");
        i++;
        return i;
    }

    
    /**
     * 
     * Replaces [,] with all the numbers of repetitions that is specified with minimum and maximum
     * 
     * <p>
     * If both min and max are the same, only that number of repetitions is inserted. If both are otherwise specified, 
     * the different allowed numbers are separated with union and the created string is parenthesized .
     * </p>
     * <p>
     * If min is missing, min is interpreted to be 0. In that case empty string will also match this part of the string. 
     * If max is missing, there is an infinite number of possibilities. This is solved by repeating the affected part min times, and then
     * concatenating to its right side the affected part with Kleene star.
     * </p>
     * <p>
     * Don't give values such that min > max or otherwise differing from the format - there is very little 
     * preparation for invalid inputs. 
     * </p>
     * 
     * @param sb Contains the string that is being simplified
     * @param i index that tells what character of the sb is considered
     * @affectedPart The part of sb that ? operates on
     * @return index that points to the next character in the sb after the inserted unions and closing parenthesis
     */
    private int replaceRepetitions(StringBuilder sb, int i, String affectedPart) {
        int closing_idx = sb.indexOf("]", i);
        String[] valueStrings = sb.substring(i + 1, closing_idx).split(",", -1);
        int[] values = new int[2];

        for (int j = 0; j < 2; j++) {
            if (valueStrings[j].isEmpty()) {
                //min -> 0, max -> -1
                values[j] = -1*j;
            } else {
                values[j] = Integer.parseInt(valueStrings[j].trim());
            }
        }


        int min = values[0];
        int max = values[1];


        sb.delete(i, closing_idx + 1);
        sb.delete(i - affectedPart.length(), i);
        i -= affectedPart.length();

        if (max == -1) {
            sb.insert(i, "()");
            i++;
            sb.insert(i, "*");

            sb.insert(i, affectedPart);

            for (int j = 0; j < min; j++) {
                sb.insert(i, affectedPart);
            }

            return i;
        }

        sb.insert(i, "()");
        i++;

        for (int len = min; len <= max; len++) {
            if (len == 0) {
                sb.insert(i, "|#");
                continue;
            }

            String option = "";
            for (int j = 0; j < len; j++) {
                option += affectedPart;
            }
            sb.insert(i, "|" + option);
        }

        sb.deleteCharAt(i);

        return i;
    }

    
    /**
     * 
     * Replaces + with the affected part and Kleene star
     * 
     * @param sb Contains the string that is being simplified
     * @param i index that tells what character of the sb is considered
     * @affectedPart The part of sb that ? operates on
     * @return index that points to the next character in the sb after the inserted unions and closing parenthesis
     */
    private int replacePlus(StringBuilder sb, int i, String affectedPart) {
        sb.deleteCharAt(i);
        sb.insert(i - affectedPart.length(), "(");
        i++;
        sb.insert(i, affectedPart + "*)");
        return i;
    }

    /**
     *
     * Returns the substring that is affected by shorthand, last character of
     * whic is at index idx.
     *
     * <p>
     * If the symbol at the specified index is not closing bracket, only that
     * character is returned (should be from the alphabet in well-formed regular
     * expression). However, if the character at the index is closing bracket,
     * the affected part is extended to the left until the matching opening
     * bracket is encountered. The contents of the brackets, and the brackets
     * themselves, are then returned.
     * </p>
     * <p>
     * Finding the matching opening bracket is done with help a stack: each
     * closing bracket adds a symbol and each opening bracket pops one. When the
     * stack is empty, the closing bracket has been found. Without some such
     * solution nested parentheses would pose problems.
     * </p>
     *
     *
     *
     * @param pattern String that affected part is part of.
     * @param idx Index of the last character of the affected part.
     * @return The substring that is the affected part.
     */
    public String determineAffectedPart(String pattern, int idx) {
        if (pattern.charAt(idx) != ')') {
            return "" + pattern.charAt(idx);
        }

        String result = ")";
        OwnStack<Character> parStack = new OwnStack();
        parStack.push(')');
        idx--;

        while (!parStack.isEmpty()) {
            if (pattern.charAt(idx) == ')') {
                parStack.push(')');
            }
            if (pattern.charAt(idx) == '(') {
                parStack.pop();
            }
            result = "" + pattern.charAt(idx) + result;
            idx--;
        }

        return result;
    }

    /**
     * Adds an explicit concatenation symbol ('&') to every place in the string
     * where applicable.
     *
     * <p>
     * Goes through every pair of consecutive characters and adds the
     * concatenation symbol where concatenation operation would take place.
     * </p>
     *
     * @param pattern Pattern without concatenation symbols
     * @return pattern Same pattern with the concatenation symbols inserted
     */
    public String addConcatenationSymbols(String pattern) {
        StringBuilder sb = new StringBuilder(pattern);
        char c1;
        char c2;

        for (int i = 0; i < sb.length() - 1; i++) {
            c1 = sb.charAt(i);
            c2 = sb.charAt(i + 1);

            if (alphabet.contains(c1) && alphabet.contains(c2)
                    || alphabet.contains(c1) && c2 == '('
                    || c1 == '*' && alphabet.contains(c2)
                    || c1 == '*' && c2 == '('
                    || c1 == ')' && c2 == '('
                    || c1 == ')' && alphabet.contains(c2)
                    || c1 == ')' && c2 == '!'
                    || c1 == '*' && c2 == '!'
                    || alphabet.contains(c1) && c2 == '!') {

                sb.insert(i + 1, '&');
            }

        }
        pattern = sb.toString();
        return pattern;

    }
    
    /**
     * 
     * Processes pattern string for multiple consequent negation symbols. Since negation of negation is 
     * the same as no negation at all, two following ! symbols are replaced with empty string. 
     * 
     * @param pattern
     * @return same pattern, with every following two ! symbols removed
     */
    public String removeUnnecessaryNegations(String pattern){
        String result = ""; 
        
        char c1, c2; 
        for(int i = 0; i < pattern.length(); i++){
            c1 = pattern.charAt(i);
            if(i + 1 == pattern.length()){
                result += "" + c1; 
                break; 
            }
            c2 = pattern.charAt(i+1); 
            if(c1 == '!' && c2 == '!'){
                i++; 
                continue; 
            }
            result += "" + c1; 
        }
        

        pattern = result; 
        return pattern; 
    }

    /**
     *
     *
     * @return Alphabet in use
     */
    public OwnSet<Character> getAlphabet() {
        return alphabet;
    }

    /**
     *
     * @return Shorthand symbols in use
     */
    public OwnSet<Character> getShorthandSymbols() {
        return shorthandSymbols;
    }

}
