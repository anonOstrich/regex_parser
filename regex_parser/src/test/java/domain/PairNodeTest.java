package domain;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PairNodeTest {

    private PairNode<Integer, String> node;

    public PairNodeTest() {
    }

    @Before
    public void setUp() {
        node = new PairNode(1, "Testi");

    }

    @Test
    public void constructorSetsKeyCorrectly() {
        assertTrue(1 == node.getKey());
    }

    @Test
    public void constructorSetsValueCorrectly() {
        assertEquals("Testi", node.getValue());
    }

    @Test
    public void nextNodeIsNullByDefault() {
        assertEquals(null, node.getNext());
    }

    @Test
    public void prevNodeIsNullByDefault() {
        assertEquals(null, node.getPrev());
    }

    @Test
    public void setNextChangesNextNode() {
        PairNode<Integer, String> nextNode = new PairNode(2, "Pori");
        node.setNext(nextNode);
        assertEquals(nextNode, node.getNext());
    }

    @Test
    public void setPrevChangesPreviousNode() {
        PairNode<Integer, String> prevNode = new PairNode(0, "Tampere");
        node.setPrev(prevNode);
        assertEquals(prevNode, node.getPrev());
    }

}
