package com.test.myproject.asyn;

import com.alibaba.fastjson.JSONObject;
import com.test.myproject.util.JedisAdapter;
import com.test.myproject.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;

        }catch(Exception e){
            return false;
        }


    }

}
