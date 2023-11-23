package common;

import common.object.Message;
import common.object.PlaceHolder;
import common.object.User;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Frame extends JFrame {
    private Panel panel;

    private Thread t = new Thread(new ReceiverThread(this));
    public Frame(User user){
        this.setVisible(false);
        this.setSize(1280,800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.panel = new Panel(user);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void startGui(){
        this.setVisible(true);
        receiveMessage();
        t.start();


    }
    public void receiveMessage(){
        Message[] messages=PlaceHolder.receiveMessage();
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
