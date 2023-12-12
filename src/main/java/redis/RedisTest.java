package redis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.object.Message;
import redis.object.User;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

public class RedisTest {
    private final JedisPool connection;
    private Integer messagesNumber = 0;
    private final RedisQuery query = new RedisQuery();
    private final User user = createUser();
    private Jedis jedis;

    public RedisTest(JedisPool connection, Integer messagesNumber, Jedis jedis){
        this.connection = connection;
        this.messagesNumber = messagesNumber;
        this.jedis = jedis;
    }

    public String createMessage(){
        byte[] array = new byte[150]; // length is bounded by 150
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public User createUser(){
        return new User("TEST", "admin");
    }

    /**
     *
     *
     * Inserts messages into Redis
     * Commented code is for chart purposes, uncomment and adapt if you wish to see the graph
     *
     *
     * */
    public void sendingMessages(){
        if(this.messagesNumber == 1000){
            sendThousandMessages();
        }else if(this.messagesNumber ==10000){
            sendTenThousandMessages();
        }else if(this.messagesNumber ==100000){
            sendOneHundredThousandMessages();
        }else if(this.messagesNumber ==1000000){
            sendOneMillionMessages();
        }
    }

    public void sendThousandMessages(){
        //double[] xData = new double[];
        //double[] yData = new double[];
        int i = 0;
        Scanner userInput = new Scanner(System.in);
        System.out.println("Do you wish to use pipeline [Y/N] ?");
        String answer = userInput.next();
        if(answer.equals("N")){
            double startTime = System.currentTimeMillis();
            while (i < this.messagesNumber){
                String message =createMessage();
                this.query.setMessasgeDB(this.connection, message ,this.user, LocalDateTime.now());
                //xData[i] = i;
                //double endTime = System.currentTimeMillis() - startTime;
                //yData[i] = endTime;
                i++;
            }
            //XYChart chart = QuickChart.getChart("Time taken with the insertion", "Number of Messages inserted", "Time (ms)", "Data", xData, yData);
            //new SwingWrapper<>(chart).displayChart();
            double finalEndTime = System.currentTimeMillis() - startTime;
            System.out.println(finalEndTime);
        }else{
            double startTime = System.currentTimeMillis();
            while (i < this.messagesNumber/100){
                this.query.setAllMessages(this.connection,  this.user, LocalDateTime.now(),this.messagesNumber/10);
                //xData[i] = i;
                //double endTime = System.currentTimeMillis() - startTime;
                //yData[i] = endTime;
                i++;
            }
            //XYChart chart = QuickChart.getChart("Time taken with the insertion", "Number of Messages inserted", "Time (ms)", "Data", xData, yData);
            //new SwingWrapper<>(chart).displayChart();
            double finalEndTime = System.currentTimeMillis() - startTime;
            System.out.println(finalEndTime);
        }
    }

    public void sendTenThousandMessages(){
        //double[] xData = new double[];
        //double[] yData = new double[];
        int i = 0;
        Scanner userInput = new Scanner(System.in);
        System.out.println("Do you wish to use pipeline [Y/N] ?");
        String answer = userInput.next();
        if(answer.equals("N")){
            double startTime = System.currentTimeMillis();
            while (i < this.messagesNumber){
                String message =createMessage();
                this.query.setMessasgeDB(this.connection, message ,this.user, LocalDateTime.now());
                //xData[i] = i;
                //double endTime = System.currentTimeMillis() - startTime;
                //yData[i] = endTime;
                i++;
            }
            double finalEndTime = System.currentTimeMillis() - startTime;
            System.out.println(finalEndTime);
        }else{
            double startTime = System.currentTimeMillis();
            while (i < this.messagesNumber/100){
                this.query.setAllMessages(this.connection,  this.user, LocalDateTime.now(),this.messagesNumber/100);
                //xData[i] = i;
                //double endTime = System.currentTimeMillis() - startTime;
                //yData[i] = endTime;
                i++;
            }
            //XYChart chart = QuickChart.getChart("Time taken with the insertion", "Number of Messages inserted", "Time (ms)", "Data", xData, yData);
            //new SwingWrapper<>(chart).displayChart();
            double finalEndTime = System.currentTimeMillis() - startTime;
            System.out.println(finalEndTime);
        }
    }

    public void sendOneHundredThousandMessages(){
        //double[] xData = new double[];
        //double[] yData = new double[];
        int i = 0;
        double startTime = System.currentTimeMillis();
        while (i < this.messagesNumber/1000){
            this.query.setAllMessages(this.connection,  this.user, LocalDateTime.now(),this.messagesNumber/100);
            //xData[i] = i;
            //double endTime = System.currentTimeMillis() - startTime;
            //yData[i] = endTime;
            i++;
        }
        //XYChart chart = QuickChart.getChart("Time taken with the insertion", "Number of Messages inserted", "Time (ms)", "Data", xData, yData);
        //new SwingWrapper<>(chart).displayChart();
        double finalEndTime = System.currentTimeMillis() - startTime;
        System.out.println(finalEndTime);
    }

    public void sendOneMillionMessages(){
        //double[] xData = new double[];
        //double[] yData = new double[];
        int i = 0;
        double startTime = System.currentTimeMillis();
        while (i < this.messagesNumber/10000){
            this.query.setAllMessages(this.connection,  this.user, LocalDateTime.now(),this.messagesNumber/100);
            double endTime = System.currentTimeMillis() - startTime;
            //xData[i] = i;
            //double endTime = System.currentTimeMillis() - startTime;
            //yData[i] = endTime;
            i++;
        }
        //XYChart chart = QuickChart.getChart("Time taken with the insertion", "Number of Messages inserted", "Time (ms)", "Data", xData, yData);
        //new SwingWrapper<>(chart).displayChart();
        double finalEndTime = System.currentTimeMillis() - startTime;
        System.out.println(finalEndTime);
    }


    public Message[] receinvingMessages()
    {
        Message[] messagesArray = this.query.receiveMesage(this.connection, this.user);
        return messagesArray;
    }


}
