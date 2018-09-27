package domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class OwnHashMapTest {
    private OwnHashMap<String, Integer> map; 
    
    public OwnHashMapTest() {
        
    }
    
    @Before
    public void setUp(){
        map = new OwnHashMap(); 
    }
    
    @Test
    public void capacityIsHundredInitiallyByDefault(){
        assertEquals(100, map.getCapacity());
    }
    
    @Test
    public void capacityCanBeSetWithConstructor(){
        OwnHashMap<String, Integer> map2 = new OwnHashMap(20); 
        assertEquals(20, map2.getCapacity()); 
    }
    
    @Test
    public void numberOfElementsIsInitiallyZero(){
        assertEquals(0, map.getNumOfElements());
    }
    
    @Test
    public void putIncrementsNumberOfElements(){
        map.put("kameli", 8);
        assertEquals(1, map.getNumOfElements());
    }
    
    @Test
    public void elementCanBeFoundAfterItHasBeenPutIntoMap(){
        map.put("kameli", 8);
        assertTrue(8 == map.get("kameli")); 
    }
    
    @Test
    public void elementCannotBeFoundAfterItHasBeenDeleted(){
        map.put("kameli", 8);
        map.remove("kameli");
        assertEquals(null, map.get("kameli")); 
    }
    
    @Test
    public void elementCannotBeFoundFromEmptymap(){
        assertEquals(null, map.get("whatever")); 
    }
    
    @Test
    public void removingKeyThatIsPresentDecreasesNumberOfElements(){
        map.put("kameli", 8); 
        map.remove("kameli");
        assertEquals(0, map.getNumOfElements());
    }
    
    @Test
    public void removingKeyThatIsNotPresentDoesNotChangeNumberOfElements(){
        map.put("kameli", 8); 
        map.remove("dromedaari");
        assertEquals(1, map.getNumOfElements()); 
    }
    
    @Test
    public void removingKeyFromEmptyMapDoesNotChangeNumberOfElements(){
        map.remove("dromedaari");
        assertEquals(0, map.getNumOfElements());
    }
        
    @Test
    public void rehashingEmptyMapDoublesCapacity(){
        map.rehash(); 
        assertEquals(200, map.getCapacity());
    }
    
    @Test
    public void puttingHundredElementsCausesRehash(){
        addHundredElements(); 
        assertEquals(200, map.getCapacity());
    
    }
    
    @Test
    public void firstElementCanbeFoundAfterAddingHundredElements(){
        addHundredElements();
        System.out.println(map.get("key0"));
        assertTrue(0 == map.get("key0"));
    }
    
    @Test
    public void mapDoesNotNeedRehashingWhenEmpty(){
        assertFalse(map.needsRehashing());
    }
    
    @Test
    public void indexesAbove100AreUsedWhen100ElementsHaveBeenAdded(){
        addHundredElements(); 
        OwnLinkedList<String, Integer>[] table = map.getTable(); 
        boolean used = false; 
        for(int i = 101; i < 200; i++){
            if (table[i] != null){
                used = true; 
                break; 
            }
        }
        assertTrue(used);
    }
    
    private void addHundredElements(){
        for(int i = 0; i < 100; i++){
            map.put("key" + i, i);
        }
    }
    
}
