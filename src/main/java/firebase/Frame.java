package firebase;

import firebase.client.Display;
import firebase.object.Message;
import firebase.object.PlaceHolder;
import firebase.object.User;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Frame extends JFrame {
    private Panel panel;

    private ReceiverThread t = new ReceiverThread(this);
    public Frame(User user){
        this.setVisible(false);
        this.setSize(1280,800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.panel = new Panel(user, this);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void startGui(){
        this.setVisible(true);
        receiveMessage();
        Thread th = new Thread(t);
        th.start();


    }
    public void receiveMessage(){
        Message[] messages= Display.firebaseConnection.receiveMessage();
        if(messages.length>31){
            messages= Arrays.copyOfRange(messages,messages.length-32,messages.length);

        }
        Collections.reverse(Arrays.asList(messages));
        panel.newMessagesToPrint(messages);
        repaint();
    }

    public void endGui(){
        t.running = false;
        dispose();
    }

}
class ReceiverThread implements Runnable{
    public boolean running = true;
    private Frame listener;
    public ReceiverThread(Frame frame){
        listener=frame;
    }
    @Override
    public void run() {
        while(running){
            if (!Display.firebaseConnection.isChatRoomOpen()){
                listener.endGui();
            }

            listener.receiveMessage();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
