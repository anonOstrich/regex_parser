/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class NodeTest {
    private Node<String> n; 
    
    public NodeTest() {
        n = new Node("test");
    }
    
    @Before
    public void setUp(){
    
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
