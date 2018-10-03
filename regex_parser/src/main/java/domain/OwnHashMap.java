package domain;

public class OwnHashMap<K,V> extends HashTable<K,V>{

    private OwnSet<K> keySet; 
    
    public OwnHashMap(){
        super();
        keySet = new OwnSet(); 
    }
    
    public OwnHashMap(int capacity){
        super(capacity); 
        keySet = new OwnSet(); 
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
    
    @Override
    public void put(PairNode<K,V> node){
        super.put(node); 
        keySet.add(node.getKey());
    }
    
    @Override
    public void remove(K key){
        super.remove(key); 
        keySet.remove(key);
    }

    
    public boolean containsKey(K key){       
        return keySet.contains(key); 
    }
      
    public OwnSet<K> keySet(){
        return this.keySet; 
    }
    
  
    
    

}
