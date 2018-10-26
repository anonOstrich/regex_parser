package utils.structures;

/**
 * A map implementation based on HashTable.
 *
 *
 * @param <K> Type for stored keys.
 * @param <V> Type for stored values.
 */
public class OwnMap<K, V> extends HashTable<K, V> {

    /**
     * Set of all the keys in the map.
     */
    private OwnSet<K> keySet;

    /**
     * Sets capacity to 100 and initializes the key set.
     */
    public OwnMap() {
        super();
        keySet = new OwnSet();
    }

    /**
     * Sets initial capacity and initializes the key set.
     *
     * @param capacity Initial capacity.
     */
    public OwnMap(int capacity) {
        super(capacity);
        keySet = new OwnSet();
    }

    /**
     * Returns the value associated with the key or null if there is none.
     *
     * <p>
     * Calculates in which cell of the array the key would be stored. If there
     * is no list there, returns null. Otherwise searches the list for the right
     * PairNode and returns its value.
     * </p>
     *
     * @param key
     * @return
     */
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

    /**
     * In addition to storing in the table, inserts the key to the key set.
     *
     * @param node PairNode to be stored.
     */
    @Override
    public void put(PairNode<K, V> node) {
        super.put(node);
        keySet.add(node.getKey());
    }

    /**
     *
     * In addition to removing, also removes the key from the key set.
     *
     * @param key Key of the PairNode to be removed.
     */
    @Override
    public void remove(K key) {
        super.remove(key);
        keySet.remove(key);
    }

    /**
     *
     * @param key The key whose presence is tested.
     * @return True if the map stores a PairNode with this key. False otherwise.
     */
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    /**
     *
     * @return The set containing all keys of this map.
     */
    public OwnSet<K> keySet() {
        return this.keySet;
    }

    
    /**
     * Only needed with copy
     * 
     * @param st 
     */
    private void setKeySet(OwnSet<K> st){
        this.keySet = st; 
    }
  
    
    /**
     * 
     * @return a map with identical contents but different address
     */
    public OwnMap<K, V> copy() {
        OwnMap<K,V> result = new OwnMap(capacity);
        result.setNumOfElements(numOfElements);
        OwnLinkedList<K, V>[] copyTable = new OwnLinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            copyTable[i] = table[i];
        }
        result.setTable(copyTable);
        result.setKeySet(keySet.copy());
        return result;
    }

}
