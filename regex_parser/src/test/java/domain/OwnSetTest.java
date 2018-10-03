
package domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OwnSetTest {
    private OwnSet<String> set; 
    
    public OwnSetTest() {
    }
    
    @Before
    public void setUp(){
        set = new OwnSet(); 
    }
    
    @Test
    public void defaultCapacityIs100(){
        assertTrue(100 == set.getCapacity());
    }
    
    @Test
    public void initialCapacityCanBeChangedWithConstructor(){
        set = new OwnSet(150);
        assertTrue(150 == set.getCapacity());
    }
    
    @Test
    public void initiallyThereAre0Elements(){
        assertTrue(0 ==  set.getNumOfElements());
    }
    
    @Test
    public void addAllModifiesNothingWithEmptySet(){
        set.add("koira");
        set.addAll(new OwnSet());
        assertTrue(set.contains("koira") && 1 == set.size());
    }
    
    @Test
    public void elementOfAddedSetCanBeFoundAfterAddAll(){
        set.add("koira");
        OwnSet<String> set2 = new OwnSet(); 
        set2.add("kissa");
        set.addAll(set2);
        assertTrue(set2.contains("kissa"));
    }
    
    @Test
    public void elementOfOriginalSetCanBeFoundAfterAddAll(){
        set.add("koira");
        OwnSet<String> set2 = new OwnSet(); 
        set2.add("kissa");
        set.addAll(set2);
        assertTrue(set.contains("koira"));
    }
    
    @Test
    public void addAllIncreasesCapacityIfNeeded(){
        OwnSet<String> set2 = new OwnSet(); 
        for(int i = 0; i < 80; i++){
            set2.add(""+ i);
        }
        set.addAll(set2);
        assertTrue(200 == set.getCapacity());
    }
    
    
    @Test
    public void sizeIsZeroInitially(){
        assertTrue(0 == set.size());
    }
    
    @Test
    public void addingElementIncreasesSizeByOne(){
        set.add("jotain");
        assertTrue(1 == set.size());
    }
    
    @Test
    public void addedElementCanBeFound(){
        set.add("element");
        assertTrue(set.contains("element"));
    }
    
    @Test
    public void emptySetDoesNotContainRandomElement(){
        assertFalse(set.contains("whatevs"));
    }
    
    @Test
    public void containsReturnsFalseWhenElementHasNotBeenAdded(){
        set.add("one");
        assertFalse(set.contains("two"));
    }
    
    @Test
    public void equalsReturnsTrueForSameSet(){
        set.add("ilement");
        assertEquals(set, set);
    }
    
    @Test
    public void equalsReturnsTrueForEmptySets(){
        OwnSet<String> set2 = new OwnSet(); 
        assertEquals(set, set2);
    }
    
    
    @Test
    public void equalsReturnsFalseForDifferentNumbersOfElements(){
        set.add("yksi");
        set.add("kaksi");
        set.add("kolme");
        
        OwnSet<String> set2 = new OwnSet(); 
        set2.add("yksi");
        set2.add("kaksi");
        assertFalse(set.equals(set2));
    }
    
    @Test
    public void equalsReturnsTrueForSetsWithSameStates(){
        OwnSet<String> set2 = new OwnSet(); 
        for(int i = 0; i < 50; i++){
            set.add("" + i);
            set2.add("" + i);
        }
        assertEquals(set, set2);
    }
      
    
}
