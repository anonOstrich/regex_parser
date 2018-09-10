package domain;


public class State {
    private int id; 
    
    public State(int id){
        this.id = id;
    }
    
    public int getId(){
        return id; 
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    
    @Override
    public boolean equals(Object o){
        if (o == null || o.getClass() != this.getClass()){
            return false; 
        }
        State compareState = (State) o;
        return this.getId() == compareState.getId(); 
    }
    
    
    @Override
    public int hashCode(){
        return ((Integer) id).hashCode();
    }
    
    @Override
    public String toString(){
        return "" + id;
    }
    
}
