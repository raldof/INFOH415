package redis;

import redis.object.Message;
import redis.object.User;
import redis.*;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;

public class RedisQuery {

    public CountDownLatch latch;
    ArrayList<Message> listMessage = new ArrayList<Message>();
    public Message[] receiveMesage(JedisPool connection, User user){
        latch = new CountDownLatch(1);
        listMessage.clear();
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        Pipeline pipeline = jedis.pipelined();
        int  tempMes= (int)jedis.keys("message:*").stream().count();
        Map<String,String> message = new HashMap<String,String>();
        List<Response<Map<String,String>>> responses = new ArrayList<>();
        double startTime = System.currentTimeMillis();
        if(tempMes > 0){
            for(int i=1;i<=tempMes; i++){
                responses.add(pipeline.hgetAll("message:"+i));
            }
            latch.countDown();
        }
        pipeline.sync();

        try{
            latch.await();
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
        double finalEndTime = System.currentTimeMillis() - startTime;
        System.out.println(finalEndTime);
        int d = 1;
        for(Response r : responses){
            Map<String,String> tmp= (Map<String,String>)r.get();
            User u = new User(tmp.get("user"),"dsqfqd");
            Message m = new Message(tmp.get("message"),u,LocalDateTime.parse(tmp.get("date")),d);
            listMessage.add(m);
            d++;
        }

        Collections.sort(listMessage,new Comparator<Message>(){
            public int compare(Message m1,Message m2){
                int id1=m1.getId();
                int id2=m2.getId();
                return id1-id2;
            }
        });

        Message[] arr= new Message[listMessage.size()];
        latch.countDown();
        return listMessage.toArray(arr);
    }


    //redis test une nouvelle connexion
    public void setMessasgeDB(JedisPool connection, String message, User user, LocalDateTime date){
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        Map<String,String> hash = new HashMap<>();
        hash.put("user",user.getName());
        hash.put("message",message);
        hash.put("date",date.toString());
        jedis.hset("message:"+(jedis.keys("message:*").stream().count()+1), hash);
        jedis.close();
    }

    public void setAllMessages(JedisPool connection, User user, LocalDateTime date, int numMessages){
        RedisConnection redisConnection =  new RedisConnection();
        Jedis jedis = redisConnection.getResources(connection);
        long d = jedis.keys("message:*").stream().count()+1;
        Pipeline pipeline = jedis.pipelined();
        Map<String,String> hash = new HashMap<>();
        for(int j = 0; j < numMessages;j++){
            String message =createMessage();
            String userTmp = createUserForInsert();
            hash.put("user",userTmp);
            hash.put("message",message);
            hash.put("date",date.toString());
            pipeline.hset("message:"+d, hash);
            if(numMessages != j){
                d++;
            }
        }
        pipeline.sync();
        jedis.close();
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

    public String createMessage(){
        byte[] array = new byte[150]; // length is bounded by 150
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }


    public String createUserForInsert() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }



}
