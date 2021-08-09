package com.hik.seckill.access;

import com.hik.seckill.redis.BasePrefix;

/**
 * @author fanbinhai
 */
public class AccessKey extends BasePrefix {

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expire) {
        return new AccessKey(expire, "access");
    }
}
