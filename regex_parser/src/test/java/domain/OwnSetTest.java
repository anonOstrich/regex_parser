
package domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jesper
 */
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
    
    

    
}
