/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Set;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Class containing methods for preprocessing patterns for regular expressions.
 *
 * Shorthands are converted into simpler operations in order to keep the core of
 * NFAGenerator as simple as possible
 *
 * @author jesper
 */
public class PatternProcessor {

    private Set<Character> alphabet;
    private Set<Character> operations;
    private Set<Character> shorthandSymbols;

    public PatternProcessor(Set<Character> alphabet, Set<Character> operations, Set<Character> shorthandSymbols) {
        this.alphabet = alphabet;
        this.operations = operations;
        this.shorthandSymbols = shorthandSymbols;
    }
    
   

    public String elongateRegularExpression(String pattern){
        if(pattern.isEmpty()){
            pattern = "#";
            return pattern; 
        }
        pattern = replaceShorthands(pattern);
        pattern = addConcatenationSymbols(pattern);       
        return pattern;
    }
    
    public String replaceShorthands(String pattern) {
        StringBuilder sb = new StringBuilder(pattern);

        for (int i = 0; i < sb.length(); i++) {
            char currentSymbol = sb.charAt(i);
            if (shorthandSymbols.contains(currentSymbol)) {
                String affectedPart = determineAffectedPart(sb.toString(), i - 1);

                if (currentSymbol == '+') {
                    sb.deleteCharAt(i);
                    sb.insert(i-affectedPart.length(), "("); 
                    i++;
                    sb.insert(i, affectedPart + "*)");
                }

                if (currentSymbol == '?') {
                    sb.deleteCharAt(i);
                    sb.insert(i, "|#)");
                    sb.insert(i - affectedPart.length(), "(");
                    i++;
                }

                // remember to add brackets if they don't exist!
                // first we only allow one character on either side of the shorthand (numbers of more than one digit: not yet supported)
                if (currentSymbol == '-') {
                    char first = sb.charAt(i - 1);
                    char last = sb.charAt(i + 1);
                    
                    sb.delete(i-1, i + 2);
                    i--; 
                    
                    //this could be done by setting all these to be transition symbold in a single NFA... 
                    sb.insert(i, "()");
                    i++; 
                    
                    for(int j = (int) first; j <= (int) last; j++){
                        sb.insert(i, "|" + (char) j);
                    }
                    sb.deleteCharAt(i);
                }

 
                if (currentSymbol == '[') {

                    int closing_idx = sb.indexOf("]", i);
                    String[] valueStrings = sb.substring(i + 1, closing_idx).split(",", -1);
                    int[] values = new int[2];

                    if (valueStrings[0].isEmpty()) {
                        values[0] = 0;
                    } else {
                        values[0] = Integer.parseInt(valueStrings[0]);
                    }

                    if (valueStrings[1].isEmpty()) {
                        values[1] = -1;
                    } else {
                        values[1] = Integer.parseInt(valueStrings[1]);

                    }

                    int min = values[0];
                    int max = values[1];

                    System.out.println(min + ", " + max);
                    sb.delete(i, closing_idx + 1);
                    sb.delete(i - affectedPart.length(), i);
                    i -= affectedPart.length();

                    if (max == -1) {
                        sb.insert(i, "()");
                        i++; 
                        sb.insert(i, "*");
                       
                        sb.insert(i, affectedPart);
                       
                        for(int j = 0; j < min; j++){
                            sb.insert(i, affectedPart);
                        }
                        
                        continue;
                    }

                    sb.insert(i, "()");
                    i++;

                    for (int len = min; len <= max; len++) {
                        if(len == 0){
                            sb.insert(i, "|#");
                            continue;
                        }

                        String option = "";
                        for (int j = 0; j < len; j++) {
                            option += affectedPart;
                        }
                        sb.insert(i, "|" + option);
                        System.out.println(sb);
                    }

                    sb.deleteCharAt(i);

                }

            }
        }

        pattern = sb.toString();

        return pattern;
    }

    public  String determineAffectedPart(String pattern, int idx) {
        if (pattern.charAt(idx) != ')') {
            return "" + pattern.charAt(idx);
        }

        String result = ")";
        Deque<Character> parStack = new LinkedList();
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

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public Set<Character> getOperations() {
        return operations;
    }

    public Set<Character> getShorthandSymbols() {
        return shorthandSymbols;
    }

    
}
