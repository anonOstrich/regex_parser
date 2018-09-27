package domain;

/**
 *  Represents a single node in OwnStack
 * 
 * 
 */
public class Node<T> {
    /**
     * Pointer to whatever data is in this place/note of the node.
     */
    private T data; 
    
    /**
     * Pointer to the previous node in the stack. Points to null if this node
     * is the last one in the stack.
     */
    private Node<T> prev; 
    
    /**
     * Creates the node and stores the data in it. 
     * @param data Object to be stored
     */
    public Node(T data){
        this.data = data; 
    }

    /**
     * Used to access the node below/before this node.
     * @return Pointer to the previous node
     */
    public Node<T> getPrev() {
        return prev;
    }
    
    /**
     * Sets the link to the parameter node.
     * @param n The node that is below this node.
     */
    public void setPrev(Node<T> n){
        this.prev = n; 
    }

    /**
     * 
     * @return The object stored in this node.
     */
    public T getData() {
        return data;
    }

    /**
     * 
     * @param data The object to be stored in this node. 
     */
    public void setData(T data) {
        this.data = data;
    }
    
}
