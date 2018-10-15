package utils;

public class OwnStringBuilder {

    private String string;

    public OwnStringBuilder(String string) {
        this.string = string;
    }

    public OwnStringBuilder deleteCharAt(int idx) {
        string = string.substring(0, idx) + string.substring(idx+1);
        return this;
    }

    public OwnStringBuilder delete(int i1, int i2) {
        string =  string.substring(0, i1) +  string.substring(i2);
        return this;
    }

    public OwnStringBuilder insert(int idx, String inserted) {
        if (idx == 0) {
            string = inserted + string;
        } else {
            string = string.substring(0, idx) + inserted + string.substring(idx);
        }
        return this;
    }
    
    public int length(){
        return string.length(); 
    }
    
    public char charAt(int i){
        return string.charAt(i);
    }
    
    public int indexOf(String str, int i){
        return string.indexOf(str, i);
    }
    
    public String substring(int i, int j){
        return string.substring(i, j);
    }

    @Override
    public String toString() {
        return string;
    }
}
