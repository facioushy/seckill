package com.hik.seckill.redis;

/**
 * @author SYSTEM
 */
public interface KeyPrefix {

    /**
     * 缓存过期时间
     * @return
     */
    public int expireSeconds();

    /**
     * 获取前缀
     * @return
     */
    public String getPrefix();
}
