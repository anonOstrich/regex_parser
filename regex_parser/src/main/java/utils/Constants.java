
package utils;

import java.util.Set; 
import java.util.HashSet; 

public class Constants {
    private static Set<Character> ready_alphabet; 
    
    
    // for now numbers are not supported, but they shouldn't be too difficult 
    public static Set<Character> alphabet(){
        
        
        if(ready_alphabet != null){
            return ready_alphabet;
        }
        
        Set<Character> result = new HashSet(); 
        for(int i = ((int) 'A'); i <= ((int) 'z') ; i++ ){
            result.add((char) i);
        }
        ready_alphabet = result; 
        
        return result; 
    }
    
}
