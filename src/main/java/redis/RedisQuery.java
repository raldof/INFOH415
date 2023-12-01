package redis;

import redis.object.Message;
import redis.object.User;
import redis.*;
import redis.clients.jedis.*;

import java.time.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;

public class RedisQuery {

    public CountDownLatch latch;
    ArrayList<Message> listMessage = new ArrayList<Message>();
    public Message[] receiveMesage(JedisPool connection){
        latch = new CountDownLatch(1);
        listMessage.clear();
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        int  tempMes= (int)jedis.keys("message:*").stream().count();
        Map<String,String> message = new HashMap<String,String>();
        if(tempMes > 0){
            for(int i=1;i<=tempMes; i++){
                message = jedis.hgetAll("message:"+i);
                Map<String,String> temp = new HashMap<String,String>();
                temp = jedis.hgetAll("user:"+message.get("user"));
                User user = new User(temp.get("name"),temp.get("password"));
                Message tempMessage = new Message(message.get("message"),user,LocalDateTime.parse(message.get("date")),i);
                listMessage.add(tempMessage);
                latch.countDown();
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Collections.sort(listMessage,new Comparator<Message>(){
            public int compare(Message m1,Message m2){
                int id1=m1.getId();
                int id2=m2.getId();
                return id1-id2;
            }
        });

        Message[] arr= new Message[listMessage.size()];

        return listMessage.toArray(arr);
    }


    //redis test une nouvelle connexion
    public void setMessasgeDB(JedisPool connection, String message, User user, LocalDateTime date){
        RedisConnection redisConnection =  new RedisConnection();
        JedisPool tmpPool = redisConnection.getConnectionToDb();
        Jedis jedis = redisConnection.getResources(tmpPool);
        Map<String,String> hash = new HashMap<>();
        hash.put("user",user.getName());
        hash.put("message",message);
        hash.put("date",date.toString());
        jedis.hset("message:"+(jedis.keys("message:*").stream().count()+1), hash);
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
        hash.put("r", String.valueOf(user.getR()));
        hash.put("g", String.valueOf(user.getG()));
        hash.put("b", String.valueOf(user.getB()));
        jedis.hset("user:"+user.getName(),hash);

    }




}
