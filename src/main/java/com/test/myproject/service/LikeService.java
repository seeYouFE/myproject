package com.test.myproject.service;

import com.test.myproject.util.JedisAdapter;
import com.test.myproject.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    public int getLikeStatus(int userId,int entityType,int entityId){

        String likeKey= RedisKeyUtil.getLikeKey(entityType, entityId);

        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }

        String disLikeKey=RedisKeyUtil.getDislikeKey(entityType, entityId);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId))?-1:0;
    }

    public long like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        String disLikeKey=RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);

    }

    public long disLike(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));

        String disLikeKey=RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(disLikeKey);

    }

}
