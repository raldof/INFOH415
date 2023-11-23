package redis.object;

import java.awt.*;
import java.util.Random;

public class User {

    private String name;
    private String password;

    private Color color;
    public User(String name,String password) {
        this.name = name;
        Random rn = new Random();
        this.color=new Color(rn.nextInt(100,200),rn.nextInt(100,200),rn.nextInt(100,200));
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Color getColor(){
        return this.color;
    }






}
