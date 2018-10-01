package xyz.warringtons.daterandevu.database;

/**
 * Created by Warrington on 2/17/18.
 */

public class Category {

    String name;
    int value;
    String key;

    public Category(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Category() {
    }

    public Category(String name, int value, String key) {
        this.name = name;
        this.value = value;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public boolean showSelected(){
        if(value==1){
            return true;
        }else{
            return false;
        }
    }

    public void setValue(int value) {
        this.value = value;
    }
}
