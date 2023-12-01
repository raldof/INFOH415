package redis;

import redis.Panel;
import redis.object.Message;
import redis.object.PlaceHolder;
import redis.object.User;
import redis.clients.jedis.JedisPool;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;

public class Frame extends JFrame {
    private Panel panel;
    private ReceiverThread t = new ReceiverThread(this);
    private JedisPool connection;
    private RedisQuery redisQuery;
    private User user;
    public Frame(JedisPool connection, User user){
        this.setVisible(false);
        this.setSize(1280,800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.panel = new Panel(user,connection);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.connection = connection;
        this.redisQuery = new RedisQuery();
        this.user = user;
    }
    public void startGui(){
        this.setVisible(true);
        receiveMessage();
        Thread th = new Thread(t);
        th.start();

    }
    public void receiveMessage(){
        System.out.println("received");
        Message[] messages = redisQuery.receiveMesage(connection, user);
        if(messages.length>31){
            messages= Arrays.copyOfRange(messages,messages.length-32,messages.length);

        }
        Collections.reverse(Arrays.asList(messages));
        panel.newMessagesToPrint(messages);
        repaint();
    }

}
class ReceiverThread implements Runnable{
    private boolean running = true;
    private Frame listener;
    public ReceiverThread(Frame frame){
        listener=frame;
    }
    @Override
    public void run() {
        while(running){
            listener.receiveMessage();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
