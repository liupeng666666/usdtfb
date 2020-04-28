package com.whp.usdtfb.socket.utils;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 1:17
 * @descrpition :
 */
public class WebRedisUtils {

    public static Map<String, String> get() {
        Map<String, String> map = RedisUtils.hgetall("socket", 6);
        return map;
    }

    public static JSONObject hget(String pid) {
        String value = RedisUtils.hget("socket", 6, pid);

        if (value == null || value == "") {
            return new JSONObject();
        } else {
            return JSONObject.parseObject(value);
        }
    }

    public static void hset(String pid, String value) {
        RedisUtils.hset("socket", value, 6, pid);
    }
}
