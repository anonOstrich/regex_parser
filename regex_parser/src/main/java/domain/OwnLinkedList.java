/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 * Doubly linked, for use with HashMap
 *
 */
public class OwnLinkedList<K, V> {
    private boolean preventMultipleKeys; 

    private PairNode<K, V> first;

    public OwnLinkedList() {
        first = null;
        preventMultipleKeys = false; 
    }
    
    public OwnLinkedList(boolean enforceSingleKey){
        first = null; 
        preventMultipleKeys = enforceSingleKey; 
    }

    public PairNode<K,V> insert(K key, V value) {
        PairNode<K, V> node = new PairNode(key, value);
        return insert(node);
    }

    public PairNode<K,V> insert(PairNode<K, V> node) {
        
        if(preventMultipleKeys){
            PairNode<K,V> existing = search(node.getKey());
            if(existing != null){
                existing.setValue(node.getValue());
                return existing; 
            }
        }
        
        node.setNext(first);
        if (first != null) {
            first.setPrev(node);
        }
        first = node;
        return node; 
    }

    //returns the value that was last added to the key, if singularity of keys is not enforced. 
    public PairNode<K, V> search(K key) {
        PairNode<K, V> current = first;
        while (current != null && !current.getKey().equals(key)) {
            current = current.getNext();
        }
        return current;
    }

    //not faster than singly linked list
    public boolean delete(K key) {
        PairNode<K, V> node = search(key);
        if (node != null) {
            delete(node);
            return true; 
        }
        return false; 
    }

    //faster than singly linked list
    // I guess we can assume parameter is not null?
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
    }

    public boolean isEmpty() {
        return null == first;
    }
    
    public void setPreventMultipleKeys(boolean allow){
        this.preventMultipleKeys = allow; 
    }
    
    public PairNode<K,V> getFirstNode(){
        return first; 
    }
}
