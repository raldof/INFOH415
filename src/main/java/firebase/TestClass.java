package firebase;

import firebase.client.Display;
import firebase.object.Message;
import firebase.object.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.Locale;

public class TestClass {

    class ReceiverThread implements Runnable{
        String[] arrayMess = new String[1000];
        Message[] messageList = new Message[1000];
        public boolean running = true;

        @Override
        public void run() {
            int index2 = 0;
            while(running){




                String newTime =  LocalDateTime.now().toString();
                Message[] messages = Display.firebaseConnection.receiveMessage();

                for (Message message : messages){

                    int index = Integer.valueOf(message.getMessage());
                    if (arrayMess[index] == null){
                        arrayMess[index] = newTime;
                        messageList[index] = message;
                    }
                }
                if (messages.length >= 1000){
                    running = false;
                    System.out.println("End of Receive");
                }
                index2 ++;
            }
            System.out.println(index2);


            int index = 0;
            int total = 0;
            for (String messStr : arrayMess){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS][.SSSSSS][.SSS][.SS]");



                LocalDateTime localDateTime = LocalDateTime.parse(messStr, formatter);
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS][.SSSSSS][.SSS][.SS]");

                long tempComparaison = localDateTime.until((LocalDateTime.parse(messageList[index].getDate(), formatter2)), ChronoUnit.MILLIS);

                if (tempComparaison < -100){
                    System.out.println("Bizzare : " + messStr + " : " + messageList[index].getDate());
                    System.out.println("Tempcomp = " + tempComparaison);

                }



                total -= tempComparaison;
                index ++;
            }
            index = 0;
            System.out.println(index + " : " + arrayMess[0] + " message : " + messageList[index]);
            System.out.println((float) (total)/1000 + "   average");
            System.out.println(total+ "   total");
            Display.firebaseConnection.closeChatRoom();

        }
    }

    class SenderThread implements Runnable{
        public boolean running = true;


        @Override
        public void run() {
            for(int i = 0; i < 1000; i++){
                String ld =  LocalDateTime.now().toString();
                Display.firebaseConnection.sendMessage(new Message(String.valueOf(i), new User("user" + String.valueOf(i), "passmerde"), ld));
                System.out.println(i);
            }


        }
    }


    public void sendTimeComparaisonTest(){
        Display.firebaseConnection.deleteChatRoom();
        Thread sendT = new Thread(new SenderThread());
        Thread receiveT = new Thread(new ReceiverThread());

        receiveT.start();
        sendT.start();

        Display.firebaseConnection.deleteChatRoom();


    }

    public void createUsers(int numUsers){
        Display.firebaseConnection.deleteUsers();

        double[] xData = new double[numUsers];
        double[] yData = new double[numUsers];

        for (int i = 0; i < numUsers; i++){
            xData[i] = i;
            String a = String.valueOf(i);
            Display display  = new Display();

            double startTime = System.currentTimeMillis();


            display.auth("User"+a, "Password"+a);

            double endTime = System.currentTimeMillis() - startTime;
            yData[i] = endTime;
        }
        yData[0] = yData[1];
        XYChart chart = QuickChart.getChart("Time taken with the insertion", "Number of Users inserted", "Time (ms)", "Data", xData, yData);
        new SwingWrapper<>(chart).displayChart();

    }

    public void insertUserLatex(){
        Display.firebaseConnection.insertUserLatex();

    }

}
