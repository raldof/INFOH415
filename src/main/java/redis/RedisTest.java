package redis;
import redis.clients.jedis.JedisPool;
import redis.object.User;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RedisTest {
    public JedisPool connection;
    public Integer messagesNumber = 0;
    public RedisQuery query;

    public RedisTest(JedisPool connection, Integer messagesNumber){
        this.connection = connection;
        this.messagesNumber = messagesNumber;
        this.query = new RedisQuery();
        System.out.println(createMessage());
    }

    public String createMessage(){
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public User createUser(){
        User user = new User("TEST", "admin");
        return user;
    }

    public void sendingMessages(){
        Integer i = 0;
        while (i < messagesNumber){
            //this.query.setMessasgeDB(this.connection);
        }
    }

    public void receinvingMessages(){

    }
}
