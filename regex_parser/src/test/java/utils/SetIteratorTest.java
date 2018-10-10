package utils;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import domain.OwnSet; 
import java.util.Iterator;


public class SetIteratorTest {
    private OwnSet<String> set; 
    private OwnSet<String> emptySet;
    private Iterator<String> iter; 
    
    public SetIteratorTest() {
    }

    
    @Before
    public void setUp() {
        emptySet = new OwnSet(); 
        set = new OwnSet(); 
        for(int i = 1; i <= 20; i++){
            set.add(""+i);
        }
        iter = set.iterator(); 
    }
    
    @Test
    public void emptySetsIteratorDoesNotHaveNext(){
        Iterator<String> ir = emptySet.iterator(); 
        assertFalse(ir.hasNext());
    }
    
    @Test
    public void callingNextOnEmptyIteratorCausesException(){
        
        try{
            Iterator<String> ir = emptySet.iterator(); 
            ir.next(); 
            assertFalse(true);
        } catch(Exception e){
            assertTrue(true);
        }
    }
    
    @Test
    public void nonemptySetsIteratorHasNext(){
        assertTrue(iter.hasNext()); 
    }
    
    @Test
    public void callingNextOnNonEmptyIteratorDoesNotCauseException(){
        try{
            iter.next(); 
            assertTrue(true);
        } catch (Exception e){
            assertFalse(true);
        }
    }
    
    @Test
    public void nextReturnsEveryElementInTurn(){
        Integer[] t = new Integer[20];
        
        for(String s : set){
            t[Integer.parseInt(s) - 1] = 1;
        }
        
        boolean allIsFine = true; 
        for(int i = 0; i < t.length; i++){
            if(t[i] != 1){
                allIsFine = false; 
                break; 
            }
        }
        
        assertTrue(allIsFine);
    }
    
    


    
}
