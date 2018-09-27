/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 * For 
 * @author jesper
 */
public class PairNode<K,V> {
    private K key; 
    private V value; 
    private PairNode<K, V> prev; 
    private PairNode<K, V> next; 
    
    public PairNode(K k, V v){
        this.key = k;
        this.value = v; 
        prev = null; 
        next = null; 
    }
    
    
    public K getKey(){
        return key; 
    }
    
    public V getValue(){
        return value; 
    }
    
    public void setValue(V v){
        this.value = v; 
    }
    
    public void setPrev(PairNode<K, V> node){
        this.prev = node ; 
    }
    
    public void setNext(PairNode<K, V> node){
        this.next = node; 
    }

    public PairNode<K, V> getNext() {
        return next;
    }

    public PairNode<K, V> getPrev() {
        return prev;
    }
    
      
}
