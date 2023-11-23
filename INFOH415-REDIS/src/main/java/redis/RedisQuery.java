package redis;

import redis.object.User;
import redis.*;
import redis.clients.jedis.*;

import java.time.*;
import java.util.*;

public class RedisQuery {

    int count;

    public Map<String, String> getMessasgeDB(JedisPool connection, String message){
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        return jedis.hgetAll(message);
    }

    public void setMessasgeDB(JedisPool connection, String message, LocalDateTime date, String user){
        System.out.println("Adding message");
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        Map<String,String> hash = new HashMap<>();
        hash.put("user",user);
        hash.put("message",message);
        hash.put("date",date.toString());
        count++;
        jedis.hset("message" + count, hash);
        System.out.println("Adding done");
    }

    public Map<String, String> getUserDB(JedisPool connection, String user){
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        return jedis.hgetAll("user:"+user);
    }

    public void setUserDB(JedisPool connection, User user){
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        Map<String,String> hash = new HashMap<>();
        hash.put("name",user.getName());
        hash.put("password",user.getPassword());
        hash.put("Color",user.getColor().toString());
        jedis.hset("user:"+user.getName(),hash);

    }




}
