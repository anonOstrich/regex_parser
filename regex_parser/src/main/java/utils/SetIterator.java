package utils;

import utils.structures.OwnLinkedList;
import utils.structures.PairNode;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * Iterator for going through the values of a HashSet.
 * 
 * @param <T> Type of element that the iterator produces.
 */
public class SetIterator<T> implements Iterator{

    /**
     * List containing all the elements that the iterator can produce.
     */
    private OwnLinkedList<T, T> elements;
    
    /**
     * The element that the iterator will return next.
     */
    private PairNode<T, T> current;
    
    
    private int currentIdx; 

    /**
     * 
     * Creates a single list from all the PairNodes in the parameter array.
     * 
     * <p>
     * Goes through every cell of the table. If there is a linked list 
     * in there, the method goes through its PairNodes one by one. 
     * A copy with the same key is made, and that is inserted into the 
     * elements list of this iterator.
     * </p>
     * 
     * <p>
     * The operation can take some time, but the following hasNext and next
     * methods will be very fast. If next is called until there is no next 
     * element to return, there is no hit to performance. On the other hand, 
     * if there is a possibility that all the values of the set are not inspected, 
     * it is wasted effort to go through all the elements of the table in 
     * the constructor...  C
     * </p>
     * <p>Changing implementation would boost performance.</p>
     * 
     * @param table Array containing all the elements of a set.
     */
    public SetIterator(OwnLinkedList<T, T>[] table) {
        currentIdx = 0; 
        elements = new OwnLinkedList();
        

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            }

            PairNode<T, T> node = table[i].getFirstNode();
            while (node != null) {
                PairNode<T, T> copy = new PairNode(node.getKey(), null);
                elements.insert(copy);
                node = node.getNext();
            }
        }

        current = elements.getFirstNode();

    }

    /**
     * 
     * @return True if there is an element to return. False otherwise.
     */
    @Override
    public boolean hasNext() {
        return current != null; 
    }

    /**
     * Returns the next element after the previous return value of next. 
     * 
     * <p>Returns the current node and updates the current to reference
     * the next PairNode of previous current. If there are no more elements to 
     * return throws an exception.</p>
     * 
     * @return Current element.
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T result = current.getKey(); 
        current = current.getNext(); 
        return result;
    }

}
