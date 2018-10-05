package domain;

/**
 *  Abstract class that defines the basic operations for 
 *  classes that use hashing to store values in a table. 
 * 
 * <p>Inherited by OwnHashMap and HashSet</p>
 * 
 * @param <K> Type parameter for the keys stored in the table. 
 * @param <V> Type parameter for the values stored in the table. 
 */
public abstract class HashTable<K, V> {

    /**
     * Table that stores key-value pairs at the index that is determined by 
     * the hash code of the key. 
     * 
     * Collisions may occur, since different keys can have the same hash code. 
     * In such a case both values will be stored in a doubly linked list that is 
     * stored in the table. 
     * 
     * For simplicity even a single node is stored in a list. 
     * 
     */
    protected OwnLinkedList<K, V>[] table;
    
    /**
     * The size of the table. 
     * 
     * Determines the scope of different possible index values that keys may 
     * receive. 
     */
    protected int capacity;
    
    /**
     * The total number of all the elements stored in the table. 
     * 
     * Will be at most 70% of capacity to keep the expected length of
     * collision lists short enough. 
     */
    protected int numOfElements;

    /**
     * Default constructor that sets the capacity to 100. 
     */
    public HashTable() {
        this(100);
    }

    /**
     * Constructor for setting an initial capacity. 
     * 
     * If it is known that a great number of elements will be stored, 
     * the table can be constructed to be large from the beginning. 
     * 
     * @param initialCapacity 
     */
    public HashTable(int initialCapacity) {
        capacity = initialCapacity;
        this.table = new OwnLinkedList[capacity];
        numOfElements = 0;
    }

    /**
     * Creates a PairNode from the given parameters
     * 
     * @param key Key to be stored.
     * @param value Value to be attached to the key.
     */
    public void put(K key, V value) {
        PairNode<K, V> node = new PairNode(key, value);
        put(node);
    }

    /**
     * 
     * Stores the key-value pair as a PairNode.
     * 
     * <p> Before storing the method checks if there is need to expand the
     * size of the storing table. If there is need, the method will be
     * significantly slower: for all the stored elements, new hash codes
     * are calculated.</p>
     * <p>Suitable hash code is calculated for the key. If that cell
     * of the table doesn't contain a list, a new list is inserted. 
     * If there already exists a PairNode on that list, its value is changed
     * to the value of the parameter node. Otherwise the node is inserted
     * into the collision list and the number of elements in this table
     * is incremented by one.</p>
     * 
     * 
     * @param node The PairNode to be stored. 
     */
    public void put(PairNode<K, V> node) {
        if (needsRehashing()) {
            rehash();
        }
        int hashCode = scaledHashCode(node.getKey());        
        if (table[hashCode] == null) {
            table[hashCode] = new OwnLinkedList();
        }

        
        // would be prettier to implement in the list, but updating numOfElements
        // is easier with this quick solution
        PairNode<K, V> existing = table[hashCode].search(node.getKey());
        if (existing != null) {
            existing.setValue(node.getValue());
        } else {

            table[hashCode].insert(node);
            numOfElements++;
        }
    }

    /**
     * Removes the key-value paired with the given key. 
     * 
     * <p>First the method calculates in which list the pair to be removed
     * is located if it is stored in the table. Then the node is removed
     * from the list, and the total number of elements is decremented by one.</p>
     * 
     * @param key Key of the key-value pair to be removed.
     */
    public void remove(K key) {
        int hashCode = scaledHashCode(key);
        OwnLinkedList<K, V> list = table[hashCode];
        if (list != null) {
            if (list.delete(key)) {
                numOfElements--;
            }
        }
    }

    /**
     * Doubles the size of the table and stores all the pairs with possibly
     * new indexes. 
     * 
     */
    public void rehash() {
        // load factor 0.7 -> 0.35
        OwnLinkedList<K, V>[] oldTable = this.table;
        this.capacity *= 2;
        OwnLinkedList<K, V>[] newTable = new OwnLinkedList[capacity];
        this.table = newTable;

        //since each of the elements will be put with the new hash, each incrementing by one
        numOfElements = 0;

        for (int i = 0; i < oldTable.length; i++) {

            OwnLinkedList<K, V> list = oldTable[i];
            if (list == null) {
                continue;

            }

            PairNode<K, V> current = list.getFirstNode();
            while (current != null) {
                PairNode<K, V> copy = new PairNode(current.getKey(), current.getValue());
                put(copy);
                current = current.getNext();
            }
        }
    }

    /**
     * Calculates hash code for the key scaled between [0, capacity - 1]
     * 
     * <p>First calls the key for the hash code. Then uses modulus to 
     * ensure that the returned value is between the wanted limits. 
     * Since in Java modulus may be negative, some trickery is involved in 
     * returning the absolute value of the modulus.</p>
     * 
     * 
     * @param key Key that determines where new element will be stored
     * @return Value between 0 and capacity - 1 that is determined by the key's 
     * hash code.
     */
    public int scaledHashCode(K key) {
        int absoluteHashCode = key.hashCode();
        int modulus = absoluteHashCode % capacity;
        // since modulus might be negative (quite unlike in mathematics...)
        modulus = (modulus + capacity) % capacity;
        return modulus;
    }

    /**
     * Evaluates whether the table should be expanded. 
     * 
     * <p>Load factor (elements / capacity) should not be too big, since
     * collision lists become longer and thus slower to go through. 
     * The choice of maximum load factor is the same as in the default Java
     * implementations. </p>
     * 
     * @return True is load factor is too large, false otherwise. 
     */
    public boolean needsRehashing() {
        double loadFactor = (1.0 * numOfElements) / capacity;
        return loadFactor > 0.7;
    }

    
    /**
     * 
     * @return Size of the hash table. 
     */
    public int getCapacity() {
        return capacity;
    }

   /**
    * 
    * @return Total number of stored elements.
    */
    public int getNumOfElements() {
        return numOfElements;
    }

    /**
     * 
     * @return The array in which all elements are stored. 
     */
    public OwnLinkedList<K, V>[] getTable() {
        return table;
    }

    /**
     * 
     * Without modifying the parameter, adds all its elements to this table. 
     * 
     * <p>
     * Goes through all table cells looking for stored elements. Sure, it
     * needlessly checks even empty cells, but since load factor hovers between
     * .35 and .7 (unless very few elements have been put to the map), the
     * number of indexes is directly proportional to the number of elements in
     * the map (O(1)).</p>
     * 
     * <p>If there is a list in the current index, the method examines each 
     * PairNode on it. Each of them is copied and the copy is added to this 
     * hash table. If at some point all the elements of the parameter 
     * table have been added, the rest of the indexes are not considered
     * and the program breaks out of the for loop.</p>
     *
     * @param toBeJoined HashTable whose elements are to be added to this table. 
     */
    public void putAll(HashTable<K, V> toBeJoined) {
        int numOfAdded = 0;

        for (int i = 0; i < toBeJoined.getCapacity(); i++) {
            OwnLinkedList<K, V> collisionList = toBeJoined.getTable()[i];
            if (collisionList == null) {
                continue;
            }

            PairNode<K, V> node = collisionList.getFirstNode();
            while (node != null) {
                PairNode<K, V> nextNode = node.getNext();
                this.put(node);
                numOfAdded++;
                node = nextNode;
            }

            if (numOfAdded == toBeJoined.getNumOfElements()) {
                break;
            }
        }
    }

    /**
     * 
     * @return True if there are no elements, false otherwise.
     */
    public boolean isEmpty() {
        return numOfElements == 0;
    }

    /**
     * 
     * @return The number of stored elements.
     */
    public int size() {
        return numOfElements;
    }

    /**
     * Restores the default initial capacity and replaces the table with an
     * empty one. 
     */
    public void clear() {
        numOfElements = 0;
        table = new OwnLinkedList[100];
        capacity = 100; 
    }

    @Override
    public boolean equals(Object o) {
        
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        HashTable<K, V> v = (HashTable<K, V>) o;
        if (this.getCapacity() != v.getCapacity() || this.getNumOfElements() != v.getNumOfElements()) {
            return false;
        }

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                if (v.getTable()[i] != null && !v.getTable()[i].isEmpty()) {
                    return false;
                }
                continue;
            }

            if (v.getTable()[i] == null) {
                if (table[i] != null && !table[i].isEmpty()) {
                    return false;
                }
                continue;
            }

            if (!table[i].equals(v.getTable()[i])) {
                return false;
            }
        }

        return true;
    }

    // to speed up, we will never look at beyond the first 150 cells
    // still fairly slow, so perhapss sets should not be used as keys...
    @Override
    public int hashCode() {
        int max = capacity;
        if (max > 200) {
            max = 150;
        }
        int code = 7;
        for (int i = 0; i < max; i++) {
            if (table[i] != null) {
                code = 31 * code + table[i].hashCode();
            }
        }
        return code;
    }

}
