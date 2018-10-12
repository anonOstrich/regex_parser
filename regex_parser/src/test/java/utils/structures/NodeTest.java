package utils.structures;


import utils.structures.Node;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;


public class NodeTest {
    private  Node<String> n; 
    
    public NodeTest() {
        
    }
    
    @Before
    public void setUp(){
       n = new Node("test");

    }

    @Test
    public void constructorSetsDataCorrectly(){
        assertEquals("test", n.getData()); 
    }
    
    @Test
    public void getDataReturnsStoredData(){
        assertEquals("test", n.getData());
    }
    
    @Test
    public void setDataChangesStoredData(){
        n.setData("monkey");
        assertEquals("monkey", n.getData());
    }
    
    @Test
    public void previousNodeIsNullByDefault(){
        assertEquals(null, n.getPrev()); 
    }
    
    @Test
    public void setPrevSetsPreviousCorrectly(){
        n.setPrev(new Node("another")); 
        assertEquals("another", n.getPrev().getData()); 
    }
    
    
}
