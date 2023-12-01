package redis.client;

import redis.*;
import redis.object.User;
import redis.RedisConnection;
import redis.RedisQuery;
import redis.clients.jedis.JedisPool;

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

    public void accessToApp(User user, Scanner userInput){
        System.out.println("Access to the app ? [Y/N]");
        String answer = userInput.next();

        while (!(answer.equals("Y") || answer.equals("N") || answer.equals("X"))){
            System.out.println("Access to the app ?[Y/N] or Testing?[X]");
            answer = userInput.next();
        }

        if (answer.equals("Y")){
            createSession(user, connection);
        }
        else if (answer.equals("N")){
            System.out.println("Exit");
        }
        else if (answer.equals("X")){
            RedisTest test = new RedisTest(connection, 10000);
            test.receinvingMessages();
            test.sendingMessages();
        }
    }

    public int auth(String username, String password){
        // Need to verify if the username already exist or not; if exist: 0, if wrong password: 1, if user does not exist: 2
        Map<String, String> result = redisQuery.getUserDB(connection, username);
        System.out.println(result);
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
