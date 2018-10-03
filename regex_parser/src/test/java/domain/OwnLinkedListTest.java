
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
    public void sizeIsZeroAtBeginning(){
        assertTrue(0 == doublyLL.size());
    }
    
    @Test
    public void sizeIsOneAfterOneElement(){
        doublyLL.insert(1, "monkey");
        assertTrue(1 ==  doublyLL.size()); 
    }
    
    @Test
    public void sizeMatchesNumberOfElementsAfter100Elements(){
        for(int i = 1; i <= 100; i++){
            doublyLL.insert(i, "Whatever value");
        }
        assertTrue(100 == doublyLL.size());
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
    
    @Test
    public void deletingByNodeReferenceDecreasesSizeByOne(){
        doublyLL.insert(1, "nax");
        doublyLL.insert(2, "rux");
        doublyLL.insert(3, "pox");
        PairNode<Integer, String> node = doublyLL.search(2);
        doublyLL.delete(node);
        assertTrue(2 == doublyLL.size()); 
    }
    
    @Test
    public void withMultipleKeysPreventedAddingSameKeyTwiceResultsInOneElementOnList(){
        OwnLinkedList<Integer, String> lst = new OwnLinkedList(true);
        lst.insert(1, "nub");
        lst.insert(1, "flower");
        assertTrue(1 == lst.size());
    }
    
    @Test
    public void withMultipleKeysPreventedAddingSameKeyTwiceResultsInLatterValueStaying(){
        OwnLinkedList<Integer, String> lst = new OwnLinkedList(true);
        lst.insert(1, "first");
        lst.insert(1, "second");
        assertTrue("second".equals(lst.search(1).getValue()));
    }
    
    
    @Test
    public void equalsReturnsTrueWithSameObject(){
        assertTrue(doublyLL.equals(doublyLL));
    }
    
    @Test
    public void equalReturnsTrueWithDifferentObjectsWithSameState(){
        doublyLL.insert(3, "ocelot");
        OwnLinkedList<Integer, String> list = new OwnLinkedList(); 
        list.insert(3, "ocelot");
    }
    
    @Test
    public void equalReturnsFalseWithDifferentPreventMultipleKeys(){
        OwnLinkedList<Integer, String> list = new OwnLinkedList(); 
        list.setPreventMultipleKeys(true);
        assertFalse(doublyLL.equals(list));
    }
    
    @Test
    public void equalReturnsFalseWithDifferentLengthLists(){
        doublyLL.insert(1, "koira");
        doublyLL.insert(2, "koira");
        OwnLinkedList<Integer, String> list = new OwnLinkedList(); 
        list.insert(1, "koira");
        list.insert(2, "koira");
        list.insert(3, "koira");
        assertFalse(doublyLL.equals(list));
    }
    
    @Test
    public void equalReturnsFalseWithDifferentFirstNode(){
        doublyLL.insert(1, "one");
        doublyLL.insert(6, "six");
        OwnLinkedList<Integer, String> list = new OwnLinkedList(); 
        list.insert(3, "three");
        list.insert(6, "six");
        assertFalse(doublyLL.equals(list));
    }
    
    @Test
    public void equalReturnsTrueWithLongerListsWithSameElements(){
        OwnLinkedList<Integer, String> list = new OwnLinkedList(); 
        
        for(int i = 0; i < 50; i++){
            doublyLL.insert(i, "" + i);
            list.insert(i, "" + i);
        }
        
        assertEquals(doublyLL, list);
    }
    
    
    
    
    
    
}
