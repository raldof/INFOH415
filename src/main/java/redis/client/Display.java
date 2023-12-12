package redis.client;

import postgres.PsqlConnection;
import postgres.PsqlQuery;
import redis.*;
import redis.clients.jedis.Jedis;
import redis.object.User;
import redis.object.Message;
import redis.RedisConnection;
import redis.RedisQuery;
import redis.clients.jedis.JedisPool;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

public class Display {
    RedisQuery redisQuery = new RedisQuery();
    RedisConnection redisConnection = new RedisConnection();
    JedisPool connection = redisConnection.getConnectionToDb();

    public void connexion(){
        System.out.println("Connexion");
        Scanner userInput = new Scanner(System.in);
        System.out.println("Username:");
        String username = userInput.next();
        System.out.println("Password:");
        String password = userInput.next();

        if (auth(username, password) == 2){ // case where user does not exist
            User user = new User(username, password);
            redisQuery.setUserDB(connection,user);
            accessToApp(user, userInput);
        }
    }

    public void accessToApp(User user, Scanner userInput) {

        System.out.println("Access to the app ?[Y/N] , Testing insertion(Redis)?[X], Testing Selections(Redis)?[S], Testing insertions(postgres) [P] or Testing selections(postgres) [A]");
        String answer = userInput.next();

        while (!(answer.equals("Y") || answer.equals("N") || answer.equals("X") || answer.equals("A") || answer.equals("S") || answer.equals("P"))){
            System.out.println("Access to the app ?[Y/N] , Testing insertion(Redis)?[X], Testing Selections(Redis)?[S], Testing insertions(postgres) [P] or Testing selections(postgres) [A]");
            answer = userInput.next();
        }

        if (answer.equals("Y")){
            createSession(user, connection);
        }
        else if (answer.equals("N")){
            System.out.println("Exit");
        }
        else if (answer.equals("X")){
            System.out.println("How many messages to select 1:1000 2:10 000 3:100 000 4:1 000 000?");
            int messageCount = userInput.nextInt();
            System.out.println(messageCount);
            while(!(messageCount == 1 || messageCount == 2 || messageCount == 3 || messageCount == 4)){
                System.out.println("Wrong number, How many messages to select 1:1000 2:10 000 3:100 000 4:1 000 000?");
                messageCount = userInput.nextInt();
            }
            int count = 0;
            switch(messageCount){
                case 1:
                    count = 1000;
                    break;
                case 2:
                    count = 10000;
                    break;
                case 3:
                    count = 100000;
                    break;
                case 4:
                    count = 1000000;
                    break;
            }
            Jedis jedis = connection.getResource();
            RedisTest test = new RedisTest(connection, count,jedis);
            System.out.println("Starting sending");
            test.sendingMessages();
            System.out.println("Ending sending");
        }else if (answer.equals("P")){
            PsqlConnection psql = new PsqlConnection();
            Connection connexionSQL = psql.connection();
            PsqlQuery psqlQuery = new PsqlQuery();
            System.out.println("Do you want to clean the database [Y/N]?");
            answer = userInput.next();
            if(answer.equals("Y")){
                psqlQuery.deleteMessages(connexionSQL);
            }
            System.out.println("How many messages to insert ?");
            int messageCount = userInput.nextInt();
            double startTime = System.currentTimeMillis();
            //
            //double[] xData = new double[messageCount];
            //double[] yData = new double[messageCount];
            for(int i =0;i<messageCount;i++){
                psqlQuery.insertMessage(connexionSQL, "jimmmy", LocalDateTime.now());
                /**
                 *
                 * The Code below is only for charts, uncomment for a chart
                 *
                 * */
                //xData[i] = i;
                //double endTime = System.currentTimeMillis() - startTime;
                //yData[i] = endTime;
            }
            //yData[0] = yData[1];
            double finalEndTime = System.currentTimeMillis() - startTime;
            System.out.println(finalEndTime);
            //XYChart chart = QuickChart.getChart("Time taken with the insertion", "Number of Messages inserted", "Time (ms)", "Data", xData, yData);
            //new SwingWrapper<>(chart).displayChart();
            try{
                connexionSQL.close();
            }catch(SQLException ex){

            }
        }else if (answer.equals("S")){
            Jedis jedis = connection.getResource();
            RedisTest test = new RedisTest(connection, 1000,jedis);
            Message[] messages = {};
            messages = test.receinvingMessages();
            System.out.println("Do you wish to to print the messages ?[Y/N]?");
            answer = userInput.next();
            if(answer.equals("Y")){
                for(Message m : messages){
                    System.out.println(m.getMessage());
                }
            }

        }else if (answer.equals("A")){
            PsqlConnection psql = new PsqlConnection();
            Connection connexionSQL = psql.connection();
            PsqlQuery psqlQuery = new PsqlQuery();
            System.out.println("How many messages to select ?");
            int messageCount = userInput.nextInt();
            double startTime = System.currentTimeMillis();
            psqlQuery.selectXMessages(connexionSQL,messageCount);
            double finalEndTime = System.currentTimeMillis() - startTime;

        }
    }

    public int auth(String username, String password){
        // Need to verify if the username already exist or not; if exist: 0, if wrong password: 1, if user does not exist: 2
        Map<String, String> result = redisQuery.getUserDB(connection, username);
        if(result.size() > 0){
            if(result.get("password").equals(password)){
                User u = new User(username,password);
                Scanner userInput = new Scanner(System.in);
                accessToApp(u, userInput);
                return 0;
            }

            else{
                return 1;
            }
        }
        else{
            // User doesn't Exist:
            createUser();
            return 2;
        }
    }

    public void createUser(){
        System.out.println("Register, please enter a Username and Password:");
        Scanner userInput = new Scanner(System.in);
        System.out.println("Username:");
        String username = userInput.next();
        System.out.println("Password:");
        String password = userInput.next();
        System.out.println(password);
        User user = new User(username, password);
        redisQuery.setUserDB(connection, user);

    }

    public void createSession(User user,  JedisPool connection){

        Frame frame= new Frame(connection, user);
        frame.startGui();
    }

}
