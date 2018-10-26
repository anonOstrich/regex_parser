package utils.structures;

/**
 * Nodes that contain a link to both their predecessor and successor. The data
 * stored consists of two parts: key and value. This is a helper class for my
 * doubly linked list class when used as collision lists in my hash map
 * implementation.
 *
 */
public class PairNode<K, V> {

    /**
     * Variable that is used to store and retrieve this node from a hash map.
     */
    private K key;

    /**
     * Value linked to the key attribute
     */
    private V value;
    /**
     * Reference to the previous node in linked list.
     */
    private PairNode<K, V> prev;
    /**
     * Reference to the following node in linked list.
     */
    private PairNode<K, V> next;

    /**
     * By default both references are to null, and they will have to be set with
     * setter to link this node to a list.
     *
     * @param k Initial key
     * @param v Initial value
     */
    public PairNode(K k, V v) {
        this.key = k;
        this.value = v;
        prev = null;
        next = null;
    }

    /**
     *
     * @return The key attribute of this node.
     */
    public K getKey() {
        return key;
    }

    /**
     *
     * @return The value attribute of this node.
     */
    public V getValue() {
        return value;
    }

    /**
     * Changes the value that is attributed to the key of this node.
     *
     * @param v New value
     */
    public void setValue(V v) {
        this.value = v;
    }

    /**
     * Adds given node to be the node before this one.
     *
     * @param node Node to be linked.
     */
    public void setPrev(PairNode<K, V> node) {
        this.prev = node;
    }

    /**
     * Adds given node to be the node after this one.
     *
     * @param node Node to be linked.
     */
    public void setNext(PairNode<K, V> node) {
        this.next = node;
    }

    /**
     *
     * @return The node after this one.
     */
    public PairNode<K, V> getNext() {
        return next;
    }

    /**
     *
     * @return The node before this one.
     */
    public PairNode<K, V> getPrev() {
        return prev;
    }

    /**
     * Links to other nodes are not considered; only the equality of the
     * key-value pair.
     * 
     * Should be rewritten. 
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        PairNode<K, V> comp = (PairNode<K, V>) o;
        if ((this.getKey() == null && comp.getKey() != null) || (this.getValue() == null && comp.getValue() != null)) {
            return false;
        }

        if ((this.getKey() != null && comp.getKey() == null) || (this.getValue() != null && comp.getValue() == null)) {
            return false;
        }
        if(this.getValue() == null && comp.getValue() == null && this.getKey().equals(comp.getKey())){
            return true; 
        }

        return this.getKey().equals(comp.getKey()) && this.getValue().equals(comp.getValue());
    }

    @Override
    public int hashCode() {
        int code = 7;
        code = 31 * code + (key == null ? 0 : key.hashCode());
        code = 31 * code + (value == null ? 0 : value.hashCode());

        return code;
    }

}
