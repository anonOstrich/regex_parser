
package utils;


import java.util.Map; 
import java.util.HashMap; 
import domain.NFA;
import domain.State; 

/**
 *  
 * Tools for converting an NFA into A DFA with the power set method
 * Use should be avoided in all but necessary cases - exponential time complexity!
 * Only required when negating an expression for the first time
 * 
 * @author jesper
 */
public class DFAGenerator {
    private Map<String, NFA> cache; 
    
    
    public DFAGenerator(){
        cache = new HashMap(); 
    }
    
}
