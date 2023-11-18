package firebase;

import firebase.client.Display;
import firebase.object.Message;
import firebase.object.PlaceHolder;
import firebase.object.User;

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

    private JButton quitButton;
    public Panel(User user, Frame frame){
        this.user = user;
        this.textField=new JTextField();
        this.setLayout(null);
        this.textField.setLocation(200,700);
        this.textField.setSize(300,50);
        this.textField.setFont(new Font("Arial",Font.PLAIN,40));
        this.add(textField);

        this.sendButton=new JButton();
        this.sendButton.setText("Send");
        this.sendButton.setSize(100,50);
        this.sendButton.setLocation(600,700);
        this.sendButton.setFont(new Font("Arial",Font.PLAIN,10));

        this.sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textField.getText().length()>0) {
                    Display.firebaseConnection.sendMessage(new Message(textField.getText(), user, LocalDateTime.now().toString()));
                    System.out.println("text sent");
                    textField.setText("");
                }
            }
        });

        this.add(sendButton);
        this.quitButton=new JButton();
        this.quitButton.setText("Quit");
        this.quitButton.setSize(100,50);
        this.quitButton.setLocation(800,700);
        this.quitButton.setFont(new Font("Arial",Font.PLAIN,10));

        this.quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Display.firebaseConnection.chatRoomCreator ){
                    Display.firebaseConnection.closeChatRoom();

                }
                frame.endGui();

            }
        });

        this.add(quitButton);
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
                g.setColor(new Color(tmpMessage[i].getUser().getColor().get(0), tmpMessage[i].getUser().getColor().get(1), tmpMessage[i].getUser().getColor().get(2)));
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
