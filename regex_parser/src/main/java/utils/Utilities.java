package utils;

import domain.OwnSet;

/**
 * Common tools and methods used in multiple classes 
 * 
 */
public class Utilities {

    /** 
     * 
     * @return Set of allowed symbols. Letters and numbers. 
     */
    public static OwnSet<Character> defaultAlphabet() {
        OwnSet<Character> alphabet = new OwnSet();
        for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
            alphabet.add((char) i);
        }

        for (int i = (int) 'a'; i <= (int) 'z'; i++) {
            alphabet.add((char) i);
        }

        for (int i = (int) '0'; i <= (int) '9'; i++) {
            alphabet.add((char) i);
        }
        return alphabet;
    }

    /** 
     * 
     * @return Set of supported shorthand symbols. 
     */
    public static OwnSet<Character> defaultShorthands() {
        OwnSet<Character> shorthands = new OwnSet();
        Character[] supportedShorthands = {'?', '+', '[', ']', '-', ','};
        for (int i = 0; i < supportedShorthands.length; i++) {
            shorthands.add(supportedShorthands[i]);
        }
        return shorthands;
    }
    
    public static OwnSet<Character> defaultBasicOperations(){
        OwnSet<Character> result = new OwnSet();
        Character[] supportedShorthands = {'*', '|', '&', '(', ')', '!'};
        for (int i = 0; i < supportedShorthands.length; i++) {
            result.add(supportedShorthands[i]);
        }
        return result;
    }
}
