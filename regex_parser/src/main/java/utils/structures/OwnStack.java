package utils.structures;

import java.util.NoSuchElementException;

/**
 * Own implementation of stack. Based on the idea of a linked list, which gives
 * time complexity O(1) for all operations.
 *
 */
public class OwnStack<T> {

    /**
     * The pointer to the top of the stack. 
     */
    private Node<T> top;

    /**
     * Initializes the stack
     */
    public OwnStack() {
        top = null; 
    }

    /** 
     * Creates a new node that holds the given data and then 
     * places it to the top of the stack. 
     * 
     * @param data What is to be stored in the stack.
     */
    public void push(T data) {
        Node<T> n = new Node(data);
        n.setPrev(top);
        top = n;
    }

    
    /**
     * 
     * @return True if there is nothing in the stack, false otherwise. 
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Returns the data that was last pushed into the stack. Also removes that node from the stack.  If the stack is empty, 
     * throws an exception. 
     * 
     * @return The data stored in the topmost node. 
     */
    public T pop() {
        try {
            T result = top.getData();
            top = top.getPrev();
            return result;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }

    }

    /**
     * Returns the data stored in the topmost node but doesn't modify the stack in any way. 
     * 
     * @return The data stored in the topmost node. 
     */
    public T peek() {
        if (top == null){
            return null; 
        }
        return top.getData();
    }

}
