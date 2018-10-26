package utils;

import utils.structures.OwnSet;

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

        alphabet.add('Å');
        alphabet.add('Ä');
        alphabet.add('Ö');
        
        for (int i = (int) 'a'; i <= (int) 'z'; i++) {
            alphabet.add((char) i);
        }
        alphabet.add('å');
        alphabet.add('ä');
        alphabet.add('ö');

        for (int i = (int) '0'; i <= (int) '9'; i++) {
            alphabet.add((char) i);
        }
        char[] miscAllowedChars = {' ', '^', ';', '_', '@', '%', '{', '}', '=', '<', '>'};
        for (int i = 0; i < miscAllowedChars.length; i++){
            alphabet.add(miscAllowedChars[i]);
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
    
    /**
     * 
     * @return Set of supported operations that NFAGenerator can handle
     */
    public static OwnSet<Character> defaultBasicOperations(){
        OwnSet<Character> result = new OwnSet();
        Character[] supportedOperations = {'*', '|', '&', '(', ')', '!'};
        for (int i = 0; i < supportedOperations.length; i++) {
            result.add(supportedOperations[i]);
        }
        return result;
    }
}
