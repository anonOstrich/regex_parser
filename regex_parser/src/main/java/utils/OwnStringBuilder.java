package utils;


/**
 * 
 * Stores a string and modifies it in more ways that the string class offers.
 * 
 */
public class OwnStringBuilder {

    /**
     * The contents to be stored and modified.
     * 
     */
    private String string;

    /**
     * 
     * @param string Initial string that is stored in OwnStringBuilder.
     */
    public OwnStringBuilder(String string) {
        this.string = string;
    }

    /**
     * 
     * Deletes the character at the index in the stored string.
     * 
     * @param idx Index of the character to be deleted.
     * @return Reference to this object.
     */
    public OwnStringBuilder deleteCharAt(int idx) {
        string = string.substring(0, idx) + string.substring(idx+1);
        return this;
    }

    /**
     * Deletes all the characters in the indexes i1, ... , i2 - 1.
     * 
     * @param i1 Index of first character to be deleted.
     * @param i2 Index of the first character after i2 not to be deleted.
     * @return Reference to this object.
     * 
     */
    public OwnStringBuilder delete(int i1, int i2) {
        string =  string.substring(0, i1) +  string.substring(i2);
        return this;
    }

    /** 
     * 
     * Stored string is extended by inserting the parameter string. 
     * 
     * 
     * @param idx The index where the inserted part is placed. The character
     * originally in this position moves to a greater index.
     * @param inserted String to be inserted into the stored string.
     * @return Reference to this object.
     */
    public OwnStringBuilder insert(int idx, String inserted) {
        if (idx == 0) {
            string = inserted + string;
        } else {
            string = string.substring(0, idx) + inserted + string.substring(idx);
        }
        return this;
    }
    
    /**
     * Used to determine the length of the stored string.
     * 
     * @return The number of characters in the stored string.
     */
    public int length(){
        return string.length(); 
    }
    
    /**
     * Used to inspect which character is at the index.
     * 
     * @param i The index of interest. 
     * @return The character at the parameter index of the stored string.
     */
    public char charAt(int i){
        return string.charAt(i);
    }
    
    /**
     * 
     * Searches for the parameter string in the indicated part of the stored string. 
     * 
     * Considers only the part of the stored string beginning and after i. 
     * Returns the first index where the str is found - the returned index is
     * the index of the first character of the first occurrence of str in 
     * the specified part. 
     * 
     * @param str String to be searched for.
     * @param i Index before which the characters are not considered.
     * @return 
     */
    public int indexOf(String str, int i){
        return string.indexOf(str, i);
    }
    
    /**
     * 
     * Returns a part of the stored string without modifying it. 
     * 
     * @param i Index of the first character to be included.
     * @param j Index of the first character to be excluded after i.
     * @return The string consisting of the characters between i and j.
     */
    public String substring(int i, int j){
        return string.substring(i, j);
    }

    /**
     * 
     * @return The stored string.
     */
    @Override
    public String toString() {
        return string;
    }
}
