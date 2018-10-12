/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.structures;

/**
 * Doubly linked, to be used as collision list in hash map.
 *
 */
public class OwnLinkedList<K, V> {

    /**
     * Whether multiple keys are prevented. The program should not attempt
     * adding the same key twice, so in theory this is unnecessary. If this is
     * false, insertion takes O(1) instead of O(n).
     */
    private boolean preventMultipleKeys;

    /**
     * Reference to the first element of the list.
     */
    private PairNode<K, V> first;

    private int size;

    /**
     * Initializes the empty list, by default not demanding guarding against key
     * repetition.
     */
    public OwnLinkedList() {
        first = null;
        preventMultipleKeys = false;
        size = 0;
    }

    /**
     * Initializes empty list and assigns the given value to the boolean
     * controlling multiple key check.
     *
     * @param enforceSingleKey
     */
    public OwnLinkedList(boolean enforceSingleKey) {
        first = null;
        preventMultipleKeys = enforceSingleKey;
        size = 0;
    }

    /**
     * Useful for other methods to call without need to form a pair node.
     * Creates a pair node out of the given key and value, and inserts it into
     * this list.
     *
     * @param key
     * @param value
     * @return Reference to the added pair node.
     */
    public PairNode<K, V> insert(K key, V value) {
        PairNode<K, V> node = new PairNode(key, value);
        return insert(node);
    }

    /**
     *
     * Adds the parameter node to the beginning of this list.
     *
     * <p>
     * If multiple keys are explicitly prohibited, first the list is searched
     * for the parameter key. If a node containing the key is found, the value
     * attribute of that node is changed to the value attribute of the parameter
     * node. So in this case no new pair node is created. </p>
     * <p>
     * Otherwise the first node of the list is set to be the next node of the
     * parameter node and the parameter node is set as the previous node for the
     * first node of this list. Finally the first attribute of this list is
     * updated to reference the node given as a parameter</p>
     * <p>
     * O(n) if preventMultipleKeys is true, O(1) otherwise
     * </p>
     *
     * @param node
     * @return
     */
    public PairNode<K, V> insert(PairNode<K, V> node) {

        if (preventMultipleKeys) {
            PairNode<K, V> existing = search(node.getKey());
            if (existing != null) {
                existing.setValue(node.getValue());
                return existing;
            }
        }

        node.setNext(first);
        if (first != null) {
            first.setPrev(node);
        }
        first = node;
        size++;
        return node;
    }

    /**
     *
     * Searches one by one for a node that has the parameter as its key value.
     * If there are for some reason many such nodes, the search returns the node
     * that it encounters first; that is, the node that was added most recently.
     * O(n).
     *
     * @param key
     * @return
     */
    public PairNode<K, V> search(K key) {
        PairNode<K, V> current = first;
        while (current != null && !current.getKey().equals(key)) {
            current = current.getNext();
        }
        return current;
    }

    /**
     * Removes the node with the corresponding key from the list by removing all
     * references to it. O(n).
     *
     * @param key
     * @return
     */
    public boolean delete(K key) {
        PairNode<K, V> node = search(key);
        if (node != null) {
            delete(node);
            return true;
        }
        return false;
    }

    /**
     * Deletes the given node from the list by removing all references to it.
     * O(1).
     *
     * @param node Node to be deleted.
     */
    public void delete(PairNode<K, V> node) {
        PairNode<K, V> previous = node.getPrev();
        PairNode<K, V> next = node.getNext();
        if (previous != null) {
            previous.setNext(next);
        } else {
            first = next;
        }
        if (next != null) {
            next.setPrev(previous);
        }
        size--;
    }

    /**
     *
     * @return True if the list contains no nodes, false otherwise.
     */
    public boolean isEmpty() {
        return null == first;
    }

    /**
     *
     * @param allow
     */
    public void setPreventMultipleKeys(boolean allow) {
        this.preventMultipleKeys = allow;
    }

    /**
     *
     * @return Reference to the first node of the list.
     */
    public PairNode<K, V> getFirstNode() {
        return first;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        OwnLinkedList<K, V> comp = (OwnLinkedList<K, V>) o;

        if (preventMultipleKeys != comp.getPreventMultipleKeys()) {
            return false;
        }

        PairNode<K, V> thisNode = this.getFirstNode();
        PairNode<K, V> compNode = comp.getFirstNode();

        while (thisNode != null && compNode != null) {
            if (!thisNode.equals(compNode)) {
                return false;
            }
            thisNode = thisNode.getNext();
            compNode = compNode.getNext();
        }

        if (thisNode != null || compNode != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int code = 7;

        PairNode<K, V> current = first;

        while (current != null) {
            code = 31 * code + current.hashCode();
            current = current.getNext();
        }

        return code;
    }

    public boolean getPreventMultipleKeys() {
        return preventMultipleKeys;
    }

}
