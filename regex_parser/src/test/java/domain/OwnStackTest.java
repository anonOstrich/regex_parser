
package domain;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class OwnStackTest {
    OwnStack<String> stack; 
    
    @Before
    public void setUp(){
        stack = new OwnStack();
    }
    
    @Test
    public void initiallyStackIsEmpty(){
        assertTrue(stack.isEmpty());
    }
    
    @Test
    public void poppingEmptyStackThrowsException(){
        try{
            stack.pop(); 
            assertFalse(true);
        } catch (NoSuchElementException e){
            assertTrue(true);
        }   
    }
    
    @Test
    public void peekingReturnsNullWhenNoDataPushed(){
        assertEquals(null, stack.peek());
    }
    
    @Test
    public void stackIsNotEmptyAfterPushing(){
        stack.push("test");
        assertFalse(stack.isEmpty()); 
    }
    
    @Test
    public void peekingAfterPushingReturnsPushedData(){
        stack.push("test");
        assertEquals("test", stack.peek());
    }
    
    @Test
    public void peekingAfterPushingDoesNotLeaveStackEmpty(){
        stack.push("test");
        stack.peek(); 
        assertFalse(stack.isEmpty());
    }
    
    @Test
    public void poppingAfterPushingReturnsPushedData(){
        stack.push("test");
        assertEquals("test", stack.pop());
    }
    
    @Test
    public void stackIsEmptyAfterPushingAndPopping(){
        stack.push("test");
        stack.pop(); 
        assertTrue(stack.isEmpty());
    }
    
    @Test
    public void poppingReturnsLatestPushWhenMultipleDataHaveBeenPushed(){
        stack.push("first");
        stack.push("second");
        assertEquals("second", stack.pop());
    }
    
    @Test
    public void poppingThriceReturnsThreeDataInOrder(){
        stack.push("third");
        stack.push("second");
        stack.push("first");
        assertTrue("first".equals(stack.pop()) && "second".equals(stack.pop()) && "third".equals(stack.pop()));
    }
    
    @Test
    public void poppingTwiceAfterPushingOnceThrowsException(){
        stack.push("test");
        stack.pop(); 
        try{
            stack.pop(); 
            assertFalse(true);
        } catch (NoSuchElementException e){
            assertTrue(true);
        }
    }
    
}
