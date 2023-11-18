package firebase.object;

//import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {

    private String name;
    private String password;

    private List<Integer> color2 = new ArrayList<>();

    //private Color;
    public User(String name,String password) {
        this.name = name;
        Random rn = new Random();
        //this.color=new Color(rn.nextInt(100,200),rn.nextInt(100,200),rn.nextInt(100,200));
        this.color2.add(rn.nextInt(100,200));
        this.color2.add(rn.nextInt(100,200));
        this.color2.add(rn.nextInt(100,200));

    }
    public User(String name,int r, int g, int b) {
        this.name = name;
        Random rn = new Random();
        //this.color=new Color(rn.nextInt(100,200),rn.nextInt(100,200),rn.nextInt(100,200));
        this.color2.add(r);
        this.color2.add(g);
        this.color2.add(b);

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


    public List<Integer> getColor(){
        return this.color2;
    }







}
