package com.hik.seckill.utils;

import java.util.UUID;

/**
 * @author SYSTEM
 */
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }

}
