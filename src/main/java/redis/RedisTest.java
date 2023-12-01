package redis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.object.Message;
import redis.object.User;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

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

    public void sendingMessages(){
        int i = 0;
        while (i < messagesNumber){
            String message =createMessage();
            this.query.setMessasgeDB(this.connection, message, this.user, LocalDateTime.now(),this.jedis);
            i++;
        }
    }

    public Message[] receinvingMessages(){
        return this.query.receiveMesage(this.connection, this.user);
    }
}
