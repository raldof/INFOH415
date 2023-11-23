package common.object;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class PlaceHolder {
    static ArrayList<Message> messages=new ArrayList<>();
    public static Message[] receiveMessage(){
        /*if (messages.size()==0){
        Random rn= new Random();
        for(int i =0; i<rn.nextInt(20,40);i++){
            messages.add(new Message(String.valueOf(i),new User("Mathieu","test"), LocalDateTime.now()));
        }}*/
        Message[] arr=new Message[messages.size()];
        return messages.toArray(arr);
    }
    public static void sendMessage(Message message){
        messages.add(message);
    }
}
