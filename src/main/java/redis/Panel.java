package redis;

import redis.clients.jedis.JedisPool;
import redis.object.Message;
import redis.object.PlaceHolder;
import redis.object.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class Panel extends JPanel {
    private Message[] messagesToPrint={};
    public User user;
    private JTextField textField;
    private JButton sendButton;
    public JedisPool connection;
    public RedisQuery query;
    public Panel(User user, JedisPool connection){
        this.user = user;
        this.textField=new JTextField();
        this.setLayout(null);
        this.textField.setLocation(200,700);
        this.textField.setSize(300,50);
        this.textField.setFont(new Font("Arial",Font.PLAIN,40));
        this.add(textField);
        this.connection = connection;
        this.sendButton=new JButton();
        this.sendButton.setText("Send");
        this.sendButton.setSize(100,50);
        this.sendButton.setLocation(600,700);
        this.sendButton.setFont(new Font("Arial",Font.PLAIN,10));
        this.query = new RedisQuery();

        this.sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("click");
                if(textField.getText().length()>1) {
                    query.setMessasgeDB(connection,textField.getText(),user,LocalDateTime.now());
                    System.out.println("adding done");
                    textField.setText("");
                }
            }
        });

        this.add(sendButton);

    }
    public void paintComponent(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(100,50,1080,650);
        g.setFont(new Font("Arial",Font.PLAIN,20));
        if(messagesToPrint.length>0){
            Message[] tmpMessage=messagesToPrint;
            for(int i = tmpMessage.length-1;i>=0;i--){
                g.setColor(new Color(tmpMessage[i].getUser().getR(),tmpMessage[i].getUser().getG(),tmpMessage[i].getUser().getB()));
                g.drawString(tmpMessage[i].getUser().getName(),200,690-(i*20));
                g.setColor(Color.WHITE);
                g.drawString(" : "+ tmpMessage[i].getMessage(),300,690-(i*20));
            }
        }

    }
    public void newMessagesToPrint(Message[] newMessages) {
        messagesToPrint=newMessages;
    }
}
