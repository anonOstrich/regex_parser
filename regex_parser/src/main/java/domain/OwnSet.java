/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Iterator;
import utils.SetIterator;

public class OwnSet<T> extends HashTable<T, T> implements Iterable<T>{
    
    public OwnSet(){
        super(); 
    }
    
    public OwnSet(int capacity){
        super(capacity);
    }

    public void add(T element) {
        super.put(element, null);
    }
    
    public void addAll(OwnSet<T> addSet){
        super.putAll(addSet);
    }


    
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
            if (table[i] == null && (comp.getTable()[i] == null || comp.getTable()[i].isEmpty())) {
                continue;
            }
            if (table[i].isEmpty() && (comp.getTable()[i] == null)) {
                continue;
            }
            if (!table[i].equals(comp.getTable()[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        SetIterator<T> it = new SetIterator(table); 
        return it;
    }
    


}
