package jordan.com.mirrorcodetest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jordan on 2/25/2018.
 */

public class User {
    private String username;
    private String password;
    //age is in milis and displayed in years
    private long age;
    //height is in cm but displayed as feet, inches
    private int height;
    private boolean likes_javascript = true;
    //changes periodically but not consistently
    @SerializedName("magic_number")
    private int magic_number = -1;
    //Changes periodically but not consistently
    @SerializedName("magic_hash")
    private String magic_hash = "";
    private int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isLikes_javascript() {
        return likes_javascript;
    }

    public void setLikes_javascript(boolean likes_javascript) {
        this.likes_javascript = likes_javascript;
    }

    public int getMagic_number() {
        return magic_number;
    }

    public void setMagic_number(int magic_number) {
        this.magic_number = magic_number;
    }

    public String getMagic_hash() {
        return magic_hash;
    }

    public void setMagic_hash(String magic_hash) {
        this.magic_hash = magic_hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User() {}

    public User(User other){
        this.setUsername(other.getUsername());
        this.setPassword(other.getPassword());
        this.setAge(other.getAge());
        this.setHeight(other.getHeight());
        this.setId(other.getId());
        this.setLikes_javascript(other.isLikes_javascript());
        this.setMagic_hash(other.getMagic_hash());
        this.setMagic_number(other.getMagic_number());
    }
}
