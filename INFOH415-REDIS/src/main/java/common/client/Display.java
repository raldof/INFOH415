package common.client;
import common.Frame;
import common.object.User;
import redis.RedisConnection;
import redis.RedisQuery;
import redis.clients.jedis.JedisPool;

import java.util.*;
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
            //redisQuery.setUserDB(connection,user);
            accessToApp(user, userInput);
        }
    }

    public void accessToApp(User user, Scanner userInput){
        System.out.println("Access to the app ? [Y/N]");
        String answer = userInput.next();

        while (!(answer.equals("Y") || answer.equals("N"))){
            System.out.println("Access to the app ?[Y/N]");
            answer = userInput.next();
        }

        if (answer.equals("Y")){
            createSession(user);
        }
        else if (answer.equals("N")){
            System.out.println("Exit");
        }
    }

    public int auth(String username, String password){
        // Need to verify if the username already exist or not; if exist: 0, if wrong password: 1, if user does not exist: 2
        Map<String, String> result = redisQuery.getUserDB(connection, username);
        if(!result.equals(null)){
            if(result.get(password).equals(password)){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 2;
        }
    }

    public void createSession(User user){
        Frame frame= new Frame(user);
        frame.startGui();
    }

}
