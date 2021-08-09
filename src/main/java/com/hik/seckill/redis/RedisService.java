package com.hik.seckill.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.concurrent.TimeUnit;

/**
 * @author SYSTEM
 */
@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置key 的有效期，单位是秒
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            log.error("expire key:{} error", key, e);
            return false;
        }
    }

    /**
     * 普通缓存获取
     * @param key
     * @return
     */
    public <T> Object get(KeyPrefix prefix, String key, Class<T> tClass) {
//        return key == null ? null : redisTemplate.opsForValue().get(key);
        String realKey = prefix.getPrefix() + key;
        Object obj = redisTemplate.opsForValue().get(realKey);
        return obj;
    }

    /**
     * 设置对象
     * @param prefix
     * @param key
     * @param obj
     * @return
     */
    public boolean set(KeyPrefix prefix, String key, Object obj) {
        try {
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                redisTemplate.opsForValue().set(realKey, obj);
            }else {
                redisTemplate.opsForValue().set(realKey, obj, seconds, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e){
            log.error("set key: {} error", key, e);
            return false;
        }
    }

    /**
     * 删除
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key) {
        try {
            String realKey = prefix.getPrefix() + key;
            boolean ans = redisTemplate.delete(realKey);
            return ans;
        } catch (Exception e){
            log.error(e.toString());
        }
        return true;
    }

    /**
     * 增加值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.opsForValue().increment(realKey);
    }

    /**
     * 减少值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.opsForValue().decrement(realKey);
    }

    public <T> boolean exists(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.hasKey(realKey);
    }
}
