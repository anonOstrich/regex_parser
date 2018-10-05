package domain;

import java.util.Iterator;
import utils.SetIterator;

/**
 * A set based on HashTable.
 *
 * @param <T> Type of elements to be stored.
 */
public class OwnSet<T> extends HashTable<T, T> implements Iterable<T>{
    
    /**
     * By default the initial capacity is 100. 
     */
    public OwnSet(){
        super(); 
    }
    
    /**
     * Sets the initial capacity according to the parameter.
     * 
     * @param capacity 
     */
    public OwnSet(int capacity){
        super(capacity);
    }

    /**
     * Forms a pair node whose key is the element.
     * 
     * <p>Simpler and more intuitive method than having to call put 
     * directly</p>
     * 
     * @param element 
     */
    public void add(T element) {
        super.put(element, null);
    }
    
    /**
     * Adds all the elements in the parameter set to this set.
     * 
     * <p>Does not modify the parameter set. Works exactly as putAll of 
     * HashTable, only the name is different</p>
     * 
     * @param addSet Set whose elements are to be added.
     */
    public void addAll(OwnSet<T> addSet){
        super.putAll(addSet);
    }


    /**
     * 
     * @param element What is searched for.
     * @return True if the set contains the parameter, false otherwise.
     */
    public boolean contains(T element){
        int hash = super.scaledHashCode(element);
        OwnLinkedList<T, T> list = table[hash];
        if(list == null){
            return false; 
        }
        return list.search(element) != null; 
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        OwnSet<T> comp = (OwnSet<T>) o;
        if (this.getCapacity() != comp.getCapacity() || this.getNumOfElements() != comp.getNumOfElements()) {

        }

        for (int i = 0; i < table.length; i++) {
            
            if(this.table[i] == null){
                if(comp.getTable()[i] == null || comp.getTable()[i].isEmpty()){
                    continue; 
                } else {
                    return false; 
                }
            }
            
            if (comp.getTable()[i] == null){
                if(table[i] == null || table[i].isEmpty()){
                    continue; 
                } else {
                    return false; 
                }
            }
            
            
            if (!table[i].equals(comp.getTable()[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an iterator to go through the elements of the set.
     * 
     * <p>The iterator is called implicitly in for each loops, which
     * is the main reason behind implementing this method.</p>
     * 
     * @return Iterator based on the elements of this set.
     */
    @Override
    public Iterator<T> iterator() {
        SetIterator<T> it = new SetIterator(table); 
        return it;
    }

    public OwnSet<T> copy() {
        OwnSet<T> result = new OwnSet(capacity);
        result.setNumOfElements(numOfElements);
        OwnLinkedList<T,T>[] copyTable = new OwnLinkedList[capacity];
        for(int i = 0; i < capacity; i++){
            copyTable[i] = table[i];
        }      
        result.setTable(copyTable);
        return result; 
    }
    


}
