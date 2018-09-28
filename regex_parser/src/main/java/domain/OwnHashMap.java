package domain;

public class OwnHashMap<K, V> {

    private OwnLinkedList<K, V>[] table;
    private int capacity;
    private int numOfElements;

    public OwnHashMap() {
        this(100);
    }

    public OwnHashMap(int initialCapacity) {
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

        // if enforcing only one key is enabled in linked list, this will need modification
        table[hashCode].insert(node);
        numOfElements++;
    }

    public V get(K key) {
        int hashCode = scaledHashCode(key);
        OwnLinkedList<K, V> list = table[hashCode];
        if (list == null) {
            return null;
        }
        PairNode<K, V> result = list.search(key);
        if (result == null) {
            return null;
        }
        return result.getValue();
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
                put(current);
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
    
    
    public void putAll(OwnHashMap<K,V> mapToBeJoined){
        //TODO
    }
    
    public boolean containsKey(K key){
        int hashCode = scaledHashCode(key);
        OwnLinkedList<K,V> list = table[hashCode];
        if (list == null){
            return false; 
        }
        
        return list.search(key) != null; 
    }

}
