package firebase.object;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class PlaceHolder {
    static ArrayList<Message> messages=new ArrayList<>();
    public static Message[] receiveMessage(){
        Message[] arr=new Message[messages.size()];
        return messages.toArray(arr);
    }
    public static void sendMessage(Message message){
        messages.add(message);
    }
}
