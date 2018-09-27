
package domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class OwnLinkedListTest {
    OwnLinkedList<Integer, String> doublyLL; 
    
    public OwnLinkedListTest() {
    }
    
    @Before
    public void setUp(){
        doublyLL = new OwnLinkedList(); 
    }
    
    @Test
    public void listIsEmptyAfterInitialization(){
        assertTrue(doublyLL.isEmpty());
    }
    
    @Test
    public void searchReturnsNullWithEmptyList(){
        assertEquals(null, doublyLL.search(3));
    }
    
    @Test
    public void afterInsertionListIsNotEmpty(){
        doublyLL.insert(1, "donkey");
    }
    
    @Test
    public void searchReturnsCorrectValueWhenKeyPresentsOnce(){
        doublyLL.insert(1, "donkey"); 
        assertEquals("donkey", doublyLL.search(1).getValue());
    }
    
    @Test
    public void searchReturnsNullWhenKeyIsNotPresentInList(){
        doublyLL.insert(1, "donkey"); 
        assertEquals(null, doublyLL.search(0)); 
    }
    
    @Test
    public void searchReturnsLatestValueWhenSameKeyIsInsertedTwice(){
        doublyLL.insert(1, "donkey"); 
        doublyLL.insert(1, "newest");
        assertEquals("newest", doublyLL.search(1).getValue()); 
    }
    
    @Test
    public void deletingKeyThatIsNotPresentDoesNotModifyList(){
        doublyLL.insert(1, "donkee"); 
        doublyLL.delete(2);
        assertFalse(doublyLL.isEmpty());
    }
    
    @Test
    public void deletingOnlyKeyThatIsPresentResultsInEmptyList(){
        doublyLL.insert(1, "donkey");
        doublyLL.delete(1);
        assertTrue(doublyLL.isEmpty());
    }
    
    @Test
    public void allowingOnlySingleInstancesOfKeysDoesNotAddMultipleNodes(){
        doublyLL.setPreventMultipleKeys(true);
        doublyLL.insert(1, "a");
        doublyLL.insert(1, "b");
        PairNode<Integer, String> node = doublyLL.insert(1, "c"); 
        doublyLL.delete(node);
        assertTrue(doublyLL.isEmpty());
    }
    

   
    

    
}
