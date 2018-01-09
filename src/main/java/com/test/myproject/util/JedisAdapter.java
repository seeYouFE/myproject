package com.test.myproject.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Component
public class JedisAdapter implements InitializingBean{

    private JedisPool pool=null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);
    }

    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);

        }catch(Exception e){
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long srem(String key,String value){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch(Exception e){
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long sadd(String key,String value){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch(Exception e){
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);

        }catch(Exception e){
            return false;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long lpush(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.lpush(key,value);
        }catch(Exception e){
            return 0;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.brpop(timeout,key);
        }catch(Exception e){
            return null;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
