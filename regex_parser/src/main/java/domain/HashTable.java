package domain;

public abstract class HashTable<K, V> {

    protected OwnLinkedList<K, V>[] table;
    protected int capacity;
    protected int numOfElements;

    public HashTable() {
        this(100);
    }

    public HashTable(int initialCapacity) {
        capacity = initialCapacity;
        this.table = new OwnLinkedList[capacity];
        numOfElements = 0;
    }

    public void put(K key, V value) {
        PairNode<K, V> node = new PairNode(key, value);
        put(node);
    }

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

    public void remove(K key) {
        int hashCode = scaledHashCode(key);
        OwnLinkedList<K, V> list = table[hashCode];
        if (list != null) {
            if (list.delete(key)) {
                numOfElements--;
            }
        }
    }

    public void rehash() {
        // load factor 0.7 -> 0.35
        OwnLinkedList<K, V>[] oldTable = this.table;
        capacity *= 2;
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

    public int scaledHashCode(K key) {
        int absoluteHashCode = key.hashCode();
        int modulus = absoluteHashCode % capacity;
        // since modulus might be negative (quite unlike in mathematics...)
        modulus = (modulus + capacity) % capacity;
        return modulus;
    }

    public boolean needsRehashing() {
        double loadFactor = (1.0 * numOfElements) / capacity;
        return loadFactor > 0.7;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumOfElements() {
        return numOfElements;
    }

    public OwnLinkedList<K, V>[] getTable() {
        return table;
    }

    /**
     * Goes through all table cells looking for stored elements. Sure, it
     * needlessly checks even empty cells, but since load factor hovers between
     * .35 and .7 (unless very few elements have been put to the map), the
     * number of indexes is directly proportional to the number of elements in
     * the map (O(1)).
     *
     * If a list had been used to store elements of map to fastly go through in
     * putAll, the performance of delete would have decresed O(1) -> O(n), at
     * least with my list implementation.
     *
     * @param toBeJoined
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

    public boolean isEmpty() {
        return numOfElements == 0;
    }

    public int size() {
        return numOfElements;
    }

    public void clear() {
        numOfElements = 0;
        table = new OwnLinkedList[capacity];
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
