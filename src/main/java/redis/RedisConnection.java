package redis;

import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.Map;

public class RedisConnection {

    public JedisPool getConnectionToDb(){
        JedisPool pool =  new JedisPool("localhost",6379);
        return pool;
    }

    public Jedis getResources(JedisPool pool){
        Jedis jedis;
        return jedis = pool.getResource();
    }


}
