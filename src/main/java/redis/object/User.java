package redis.object;

import java.awt.*;
import java.util.Random;

public class User {

    private String name;
    private String password;
    private int r;
    private int g;
    private int b;

    //private Color color;
    public User(String name,String password) {
        this.name = name;
        this.password = password;
        Random rn = new Random();
        this.r = rn.nextInt(100,200);
        this.g = rn.nextInt(100,200);
        this.b = rn.nextInt(100,200);
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

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    /*public Color getColor(){
        return this.color;
    }*/






}
